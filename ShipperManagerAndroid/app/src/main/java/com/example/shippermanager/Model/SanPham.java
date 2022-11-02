package com.example.shippermanager.Model;

import java.io.Serializable;

public class SanPham implements Serializable {
    public String Id;
    public String Ten;
    public double Gia;
    public String MoTa;
    public String DanhMuc;
    public String ImagePath;

    public SanPham(String id, String ten, double gia, String moTa, String danhMuc, String imagePath) {
        Id = id;
        Ten = ten;
        Gia = gia;
        MoTa = moTa;
        DanhMuc = danhMuc;
        ImagePath = imagePath;
    }

    public SanPham() {
    }

}
