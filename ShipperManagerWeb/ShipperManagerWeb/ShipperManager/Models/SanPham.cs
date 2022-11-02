using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ShipperManager.Models
{
    public class SanPham
    {
        public string Id { get; set; }
        [Required(ErrorMessage = "Tên không được trống")]
        [Display(Name = "Tên")]
        public string Ten { get; set; }

        [RegularExpression("^[1-9][0-9]{3,8}$", ErrorMessage = "Giá phải là số, lớn hơn 1.000đ và nhỏ hơn 999.999.999đ")]
        [DataType(DataType.Currency)]
        [Required(ErrorMessage = "Giá không được trống")]
        [DisplayFormat(DataFormatString = "{0:N0}đ", ApplyFormatInEditMode = true)]
        [Display(Name = "Giá")]
        public decimal Gia { get; set; }

        [Required(ErrorMessage = "Mô Tả không được trống")]
        [Display(Name = "Mô Tả")]
        public string MoTa { get; set;}

        [Required(ErrorMessage = "Danh Mục không được trống")]
        [Display(Name = "Danh Mục")]
        public string DanhMuc { get; set; }

        [Display(Name = "Ảnh")]
        public string ImagePath { get; set; }

        public SanPham()
        {

        }

        public SanPham(string id, string ten, decimal gia, string moTa, string danhMuc, string imagePath)
        {
            Id = id;
            Ten = ten;
            Gia = gia;
            MoTa = moTa;
            DanhMuc = danhMuc;
            ImagePath = imagePath;
        }

        public SanPham(string ten, decimal gia, string moTa, string danhMuc, string imagePath)
        {
            Ten = ten;
            Gia = gia;
            MoTa = moTa;
            DanhMuc = danhMuc;
            ImagePath = imagePath;
        }


    }
}