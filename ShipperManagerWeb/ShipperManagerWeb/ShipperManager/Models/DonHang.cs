using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ShipperManager.Models
{
    public enum TrangThaiDonHang
    {
        [Display(Name = "Đang Tìm")]
        DangTim,
        [Display(Name = "Đang Giao")]
        DangGiao,
        [Display(Name = "Đã Giao")]
        DaGiao
    }
    public class DonHang
    {
        public string Id { get; set; }
        public NhanVien NhanVien { get; set; }
        public KhachHang KhachHang { get; set; }
        public string NgayTao { get; set; }
        public TrangThaiDonHang TrangThaiGiao { get; set; }
        public PhuongThucThanhToan PhuongThucThanhToan { get; set; }
        [DisplayFormat(DataFormatString = "{0:N0}đ")]
        public decimal TongTien { get; set; } = 0;
        public List<ChiTietDonHang> DanhSachCTTT { get; set; } = new List<ChiTietDonHang>();
        [DisplayFormat(DataFormatString = "{0:N2}km")]
        public double KhoanCach { get; set; }
        public string NgayGiao { get; set; }
        [DisplayFormat(DataFormatString = "{0:N0}đ")]
        public decimal PhiGiaoHang { get; set; }
        public Shipper Shipper { get; set; }

        public DonHang(NhanVien nhanVien, KhachHang khachHang, string ngayTao, PhuongThucThanhToan phuongThucThanhToan,List<ChiTietDonHang> cttt,double khoanCanh,decimal phiGiaoHang)
        {
            NhanVien = nhanVien;
            KhachHang = khachHang;
            NgayTao = ngayTao;
            TrangThaiGiao = TrangThaiDonHang.DangTim;
            PhuongThucThanhToan = phuongThucThanhToan;
            DanhSachCTTT = cttt;
            foreach (var item in DanhSachCTTT) TongTien += item.GetTongTien();
            Shipper = null;
            NgayGiao = string.Empty;
            KhoanCach = khoanCanh;
            PhiGiaoHang = phiGiaoHang;
            
        }

        public DonHang() { }

        
    }
}