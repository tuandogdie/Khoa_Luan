using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ShipperManager.Models
{
    public class NhanVien
    {
        public string ID { get; set; }
        public string Ten { get; set; }
        public string SoDienThoai { get; set; }
        public string TaiKhoan { get; set; }
        public string MatKhau { get; set; }

        public NhanVien(string ten, string soDienThoai, string taiKhoan, string matKhau)
        {
            Ten = ten;
            SoDienThoai = soDienThoai;
            TaiKhoan = taiKhoan;
            MatKhau = matKhau;
        }

       

        
    }
}