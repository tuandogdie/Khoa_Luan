using ShipperManager.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ShipperManager.ViewModel
{
    public class ShipperViewModel
    {
        public Shipper Shipper { get; set; }
        public List<DonHang> ListDonHang { get; set; } = new List<DonHang>();

        public ShipperViewModel(Shipper shipper,List<DonHang> listDonHang)
        {
            Shipper = shipper;
            ListDonHang = listDonHang;
        }


    }
}