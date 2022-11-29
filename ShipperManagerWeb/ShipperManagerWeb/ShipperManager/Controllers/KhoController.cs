using Firebase.Database;
using Firebase.Database.Query;
using ShipperManager.Models;
using ShipperManager.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace ShipperManager.Controllers
{
    public class KhoController : Controller
    {
        // GET: Customer
        public async Task<ActionResult> Index()
        {
            List<Kho> lst = new List<Kho>();
            var customers = await DatabaseUtils.GetAllElement<Kho>("Kho");
            foreach (var item in customers)
            {
                var kh = item.Object;
                kh.Id = item.Key;
                lst.Add(kh);
            }

            return View(lst);
        }

        // GET: Customer/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: Customer/Create
        [HttpPost]
        public async Task<ActionResult> Create(Kho customer)
        {
            try
            {
                if (!ModelState.IsValid) return View(customer);
                var kh = await DatabaseUtils.AddElement(TableCategory.Kho, customer);
                kh.Object.Id = kh.Key;
                await DatabaseUtils.UpdateElementByKey(TableCategory.Kho, kh.Object, kh.Key);
                return RedirectToAction("Index");
            }
            catch (Exception)
            {
                return View();
            }
        }

        // GET: Customer/Edit/5
        public async Task<ActionResult> Edit(string id)
        {
            var kh = await DatabaseUtils.GetElementByKey<KhachHang>(TableCategory.Kho, id);
            return View(kh.Object);
        }

        // POST: Customer/Edit/5
        [HttpPost]
        public async Task<ActionResult> Edit(string id, FormCollection collection)
        {
            var fireObj = await DatabaseUtils.GetElementByKey<KhachHang>(TableCategory.Kho, id);
            var kh = fireObj.Object;
            kh.Id = fireObj.Key;
            kh.Ten = collection["Ten"];
            kh.DiaChi = collection["DiaChi"];
            
            kh.KinhDo = Double.Parse(collection["KinhDo"]);
            kh.ViDo = Double.Parse( collection["ViDo"]);
            await DatabaseUtils.UpdateElementByKey(TableCategory.Kho, kh, kh.Id);

            foreach (var item in await DatabaseUtils.GetAllElement<DonHang>(TableCategory.DonHang))
            {
                var dh = item.Object;
                dh.Id = item.Key;
                if (dh.KhachHang.Id.Equals(id))
                {
                    dh.KhachHang = kh;
                    await DatabaseUtils.UpdateElementByKey(TableCategory.DonHang, dh, dh.Id);
                }
            }

            return RedirectToAction("Index");
        }

        // GET: Customer/Delete/5
        public async Task<ActionResult> Delete(string id)
        {
            await DatabaseUtils.DeleteElement(TableCategory.Kho, id);
            return RedirectToAction("Index");
        }

        // GET: Shipper/Details/5
        public async Task<ActionResult> Details(string id)
        {
            var kh = await DatabaseUtils.GetElementByKey<Kho>(TableCategory.Kho, id);
            var listObj = await DatabaseUtils.GetAllElement<DonHang>(TableCategory.DonHang);
            var listDonHang = new List<DonHang>();
            foreach (var item in listObj)
            {
                if (item.Object.KhachHang != null)
                {
                    if (item.Object.KhachHang.Id.Equals(id))
                    {
                        listDonHang.Add(item.Object);
                    }
                }
            }

            return View(new KhoViewModel(kh.Object, listDonHang));
        }

        public async Task<PartialViewResult> ShowDonHang(string id,string trangThai)
        {
            var listObj = await DatabaseUtils.GetAllElement<DonHang>(TableCategory.DonHang);
            var listDonHang = new List<DonHang>();
            foreach (var item in listObj)
            {
                if (item.Object.Kho != null)
                {
                    if (item.Object.Kho.Id.Equals(id))
                    {
                        if(trangThai.Equals("0"))
                            listDonHang.Add(item.Object);
                        else if (trangThai.Equals("1"))
                        {
                            if(item.Object.TrangThaiGiao == TrangThaiDonHang.DangTim)
                                listDonHang.Add(item.Object);
                        }
                        else if (trangThai.Equals("2"))
                        {
                            if (item.Object.TrangThaiGiao == TrangThaiDonHang.DangGiao)
                                listDonHang.Add(item.Object);
                        }
                        else if (trangThai.Equals("3"))
                        {
                            if (item.Object.TrangThaiGiao == TrangThaiDonHang.DaGiao)
                                listDonHang.Add(item.Object);
                        }
                    }
                }
            }
            return PartialView("ShowDonhang", listDonHang);
        }

    }
}
