using ShipperManager.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ShipperManager.ViewModel
{
    public class KhoViewModel
    {
        public Kho Kho;
        public List<DonHang> ListDonHang { get; set; } = new List<DonHang>();
        public List<SelectListItem> TrangThaiListItem { get; set; } = new List<SelectListItem>();
        public KhoViewModel(Kho kho,List<DonHang> listDonHang)
        {
            Kho = kho;
            ListDonHang = listDonHang;
            TrangThaiListItem.Add(new SelectListItem { Text = "Tất Cả", Value = "0" });
            TrangThaiListItem.Add(new SelectListItem { Text = "Đang Tìm", Value = "1" });
            TrangThaiListItem.Add(new SelectListItem { Text = "Đang Giao", Value = "2" });
            TrangThaiListItem.Add(new SelectListItem { Text = "Đã Giao", Value = "3" });

        }
    }
}