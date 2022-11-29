package com.example.shippermanager.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DonHang {
    public String Id;
    public KhachHang KhachHang;
    public Kho Kho;
    public NhanVien NhanVien;
    public String NgayTao;
    public int TrangThaiGiao;
    public PhuongThucThanhToan PhuongThucThanhToan;
    public double TongTien;
    public List<ChiTietDonHang> DanhSachCTTT = new ArrayList<>();
    public double KhoanCach;
    public String NgayGiao;
    public double PhiGiaoHang;
    public Shipper Shipper;

    public DonHang() {

    }

}
