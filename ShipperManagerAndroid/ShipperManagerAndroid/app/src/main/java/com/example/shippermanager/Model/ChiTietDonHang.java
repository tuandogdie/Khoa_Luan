package com.example.shippermanager.Model;

import java.io.Serializable;

public class ChiTietDonHang implements Serializable {

    public SanPham SanPham;
    public int SoLuong;

    public ChiTietDonHang(SanPham sanPham) {
        this.SanPham = sanPham;
        this.SoLuong = 1;
    }

    public ChiTietDonHang() {

    }

    public double GetMoneySum()
    {
        return SanPham.Gia * SoLuong;
    }

}
