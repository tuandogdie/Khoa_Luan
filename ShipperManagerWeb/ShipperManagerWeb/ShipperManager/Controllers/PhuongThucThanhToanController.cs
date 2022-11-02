using ShipperManager.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace ShipperManager.Controllers
{
    public class PhuongThucThanhToanController : Controller
    {
        // GET: PhuongThucThanhToan
        public async Task<ActionResult> Index()
        {
            List<PhuongThucThanhToan> lst = new List<PhuongThucThanhToan>();
            var pttt = await DatabaseUtils.GetAllElement<PhuongThucThanhToan>("PhuongThucThanhToan");
            foreach (var item in pttt)
            {
                var obj = item.Object;
                obj.Id = item.Key;
                lst.Add(obj);
            }
            return View(lst);
        }

        // GET: PhuongThucThanhToan/Details/5
        public ActionResult Details(int id)
        {
            return View();
        }

        // GET: PhuongThucThanhToan/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: PhuongThucThanhToan/Create
        [HttpPost]
        public async Task<ActionResult> Create(PhuongThucThanhToan pttt)
        {
            try
            {
                var obj = await DatabaseUtils.AddElement("PhuongThucThanhToan", pttt);
                obj.Object.Id = obj.Key;
                await DatabaseUtils.UpdateElementByKey(TableCategory.PhuongThucThanhToan, obj.Object, obj.Key);
                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }

        // GET: PhuongThucThanhToan/Edit/5
        public ActionResult Edit(int id)
        {
            return View();
        }

        // POST: PhuongThucThanhToan/Edit/5
        [HttpPost]
        public ActionResult Edit(int id, FormCollection collection)
        {
            try
            {
                // TODO: Add update logic here

                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }

        // GET: PhuongThucThanhToan/Delete/5
        public async Task<ActionResult> Delete(string id)
        {
            await DatabaseUtils.DeleteElement(TableCategory.PhuongThucThanhToan, id);
            return RedirectToAction("Index");
        }

        // POST: PhuongThucThanhToan/Delete/5
        [HttpPost]
        public ActionResult Delete(int id, FormCollection collection)
        {
            try
            {
                // TODO: Add delete logic here

                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }
    }
}
