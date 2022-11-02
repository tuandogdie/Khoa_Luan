using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ShipperManager.Models
{
    public class Shipper
    {
        public string Id { get; set; }
        [Display(Name = "Tên")]
        public string Ten { get; set; }
        [Display(Name = "Quê Quán")]
        public string QueQuan { get; set; }
        [Display(Name = "Tài Khoản")]
        public string TaiKhoan { get; set; }
        [Display(Name = "Mật Khẩu")]
        public string MatKhau { get; set; }
        [Display(Name = "Ngày Sinh")]
        public string NgaySinh { get; set; }
        [Display(Name = "Trạng Thái")]
        public bool TrangThai { get; set; }
        [Display(Name = "Ảnh")]
        public string ImagePath { get; set; }
    }
}