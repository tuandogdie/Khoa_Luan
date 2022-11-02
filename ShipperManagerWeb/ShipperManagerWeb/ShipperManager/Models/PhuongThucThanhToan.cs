using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ShipperManager.Models
{
    public class PhuongThucThanhToan
    {
        public string Id { get; set; }
        [Display(Name ="Tên")]
        public string Ten { get; set; }
        [Display(Name = "Mô tả")]
        public string MoTa { get; set; }
        public PhuongThucThanhToan()
        {

        }
    }
}