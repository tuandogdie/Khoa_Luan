using Firebase.Auth;
using Firebase.Storage;
using ShipperManager.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace ShipperManager.Controllers
{
    public class SanPhamController : Controller
    {
        private static string ApiKey = "AIzaSyB7fWSqTSoYECXz3CFd8qRg4y25Anc3aFA";
        private static string Bucket = "shippermanager-606da.appspot.com";
        private static string AuthEmail = "admin@gmail.com";
        private static string AuthPassword = "admin123";
        // GET: Product
        public async Task<ActionResult> Index()
        {
            List<SanPham> lst = new List<SanPham>();
            var products = await DatabaseUtils.GetAllElement<SanPham>("SanPham");
            foreach(var item in products)
            {
                var sp = item.Object;
                sp.Id = item.Key;
                lst.Add(sp);
            }

            return View(lst);
        }

        // GET: Product/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: Product/Create
        [HttpPost]
        public async Task<ActionResult> Create(SanPham product,HttpPostedFileBase file)
        {
            try
            {
                if (!ModelState.IsValid) return View(product);

                FileStream stream;
                if(file.ContentLength > 0 && product != null)
                {
                    string fileName = Guid.NewGuid() + "-" + file.FileName;
                    //upload product image
                    string path = Path.Combine(Server.MapPath("~/Content/images/"), fileName);
                    file.SaveAs(path);
                    stream = new FileStream(Path.Combine(path), FileMode.Open);
                    
                    string link = await Upload(stream, fileName);
                    product.ImagePath = link;
                    //update product object
                    var obj = await DatabaseUtils.AddElement("SanPham", product);
                    obj.Object.Id = obj.Key;
                    await DatabaseUtils.UpdateElementByKey(TableCategory.SanPham, obj.Object, obj.Key);
                }

                return RedirectToAction("Index");
            }
            catch(Exception)
            {
                return View();
            }
        }

        private async Task<string> Upload(FileStream stream,string fileName)
        {
            // of course you can login using other method, not just email+password
            var auth = new FirebaseAuthProvider(new FirebaseConfig(ApiKey));
            var a = await auth.SignInWithEmailAndPasswordAsync(AuthEmail, AuthPassword);

            // you can use CancellationTokenSource to cancel the upload midway
            var cancellation = new CancellationTokenSource();

            var task = new FirebaseStorage(
                Bucket,
                new FirebaseStorageOptions
                {
                    AuthTokenAsyncFactory = () => Task.FromResult(a.FirebaseToken),
                    ThrowOnCancel = true // when you cancel the upload, exception is thrown. By default no exception is thrown
                })
                .Child("product-images")
                .Child(fileName)
                .PutAsync(stream, cancellation.Token);

            try
            {
                // error during upload will be thrown when you await the task
                return await task;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception was thrown: {0}", ex);
            }
            finally
            {
                stream.Close();
            }
            return null;
        }

        // GET: Product/Edit/5
        public async Task<ActionResult> Edit(string ma)
        {
            var sp = await DatabaseUtils.GetElementByKey<SanPham>(TableCategory.SanPham, ma);
            return View(sp.Object);
        }

        // POST: Product/Edit/5
        [HttpPost]
        public async Task<ActionResult> Edit(string id, FormCollection collection)
        {
            try
            {
                SanPham sp = new SanPham();
                sp.Id = id;
                sp.Ten = collection["Ten"];
                if (Decimal.TryParse(collection["Gia"], out decimal gia))
                    sp.Gia = gia;
                sp.MoTa = collection["MoTa"];
                sp.ImagePath = collection["ImagePath"];
                sp.DanhMuc = collection["DanhMuc"];
                if (!ModelState.IsValid) return View(sp);
                await DatabaseUtils.UpdateElementByKey(TableCategory.SanPham, sp, id);
                // TODO: Add update logic here
                return RedirectToAction("Index");
            }
            catch
            {
                return RedirectToAction("Index");
            }
        }

        // GET: Product/Delete/5
        public async Task<ActionResult> Delete(string ma)
        {
            await DatabaseUtils.DeleteElement(TableCategory.SanPham, ma);
            return RedirectToAction("Index");
        }

    }
}
