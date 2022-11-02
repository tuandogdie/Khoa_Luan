using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ShipperManager.Models
{
    public class KhachHang
    {
        public string Id { get; set; }

        [Required(ErrorMessage = "Tên không được trống")]
        [StringLength(50,ErrorMessage = "Tên không hợp lệ")]
        public string Ten { get; set; }

        [Required(ErrorMessage = "Địa chỉ không được trống")]
        public string DiaChi { get; set; }

        [Required(ErrorMessage = "Số điện thoại không được trống")]
        [Phone(ErrorMessage = "Số điện thoại không hợp lệ")]
        public string SoDienThoai { get; set; }

        [Required(ErrorMessage = "Kinh độ không được trống")]
        public double KinhDo { get; set; }

        [Required(ErrorMessage = "Vĩ độ không được trống")]
        public double ViDo { get; set; }

        public KhachHang()
        {

        }
    }
}