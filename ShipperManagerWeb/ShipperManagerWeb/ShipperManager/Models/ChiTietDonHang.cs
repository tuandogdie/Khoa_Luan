using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace ShipperManager.Models
{
    public class ChiTietDonHang
    {

        public int SoLuong { get; set; }
        public SanPham SanPham { get; set; }

        public ChiTietDonHang(SanPham sp)
        {

            SoLuong = 1;
            SanPham = sp;
        }

        public ChiTietDonHang()
        {
        }

        public decimal GetTongTien()
        {
            return SanPham.Gia * SoLuong;
        }

    }
}