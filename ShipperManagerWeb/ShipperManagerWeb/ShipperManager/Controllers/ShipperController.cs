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
    public class ShipperController : Controller
    {
        // GET: Shipper
        public async Task<ActionResult> Index()
        {
            var lst = await DatabaseUtils.GetAllElement<Shipper>(TableCategory.Shipper);
            return View(lst.Select(x => x.Object));
        }

        // GET: Shipper/Details/5
        public async Task<ActionResult> Details(string id)
        {
            var shipper = await DatabaseUtils.GetElementByKey<Shipper>(TableCategory.Shipper, id);
            var listObj = await DatabaseUtils.GetAllElement<DonHang>(TableCategory.DonHang);
            var listDonHang = new List<DonHang>();
            foreach(var item in listObj)
            {
                if(item.Object.Shipper != null)
                {
                    if(item.Object.Shipper.Id.Equals(id) && item.Object.TrangThaiGiao == TrangThaiDonHang.DaGiao)
                    {
                        listDonHang.Add(item.Object);
                    }
                }
            }

            return View(new ShipperViewModel(shipper.Object,listDonHang));
        }

        // GET: Shipper/Edit/5
        public async Task<ActionResult> Edit(string id)
        {
            var obj = await DatabaseUtils.GetElementByKey<Shipper>(TableCategory.Shipper, id);
            return View(obj.Object);
        }

        // POST: Shipper/Edit/5
        [HttpPost]
        public async Task<ActionResult> Edit(string id, FormCollection collection)
        {
            var fireObj = await DatabaseUtils.GetElementByKey<Shipper>(TableCategory.Shipper, id);
            var obj = fireObj.Object;
            obj.Id = fireObj.Key;
            obj.Ten = collection["Ten"];
            obj.QueQuan = collection["QueQuan"];
            obj.NgaySinh = collection["NgaySinh"];
            obj.TaiKhoan = collection["TaiKhoan"];
            obj.MatKhau = collection["MatKhau"];
            obj.TrangThai = Convert.ToBoolean(collection["TrangThai"].Split(',').First());
            await DatabaseUtils.UpdateElementByKey(TableCategory.Shipper, obj, obj.Id);

            foreach (var item in await DatabaseUtils.GetAllElement<DonHang>(TableCategory.DonHang))
            {
                var dh = item.Object;
                dh.Id = item.Key;
                if(dh.Shipper != null)
                {
                    if (dh.Shipper.Id.Equals(id))
                    {
                        dh.Shipper = obj;
                        await DatabaseUtils.UpdateElementByKey(TableCategory.DonHang, dh, dh.Id);
                    }
                }
            }

            return RedirectToAction("Index");
        }

        // GET: Shipper/Delete/5
        public async Task<ActionResult> Delete(string id)
        {
            await DatabaseUtils.DeleteElement(TableCategory.Shipper, id);
            return RedirectToAction("Index");
        }

    }
}
