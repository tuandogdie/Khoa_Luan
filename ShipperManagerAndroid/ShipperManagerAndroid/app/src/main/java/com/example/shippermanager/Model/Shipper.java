package com.example.shippermanager.Model;

public class Shipper {

    public String Id;
    public String Ten;
    public String NgaySinh;
    public String QueQuan;
    public String TaiKhoan;
    public String MatKhau;
    public boolean TrangThai;
    public double KinhDo;
    public double ViDo;
    public String ImagePath;
    public Kho Kho;

    public Shipper() {

    }

    public Shipper(String id, String ten, String ngaySinh, String queQuan, String taiKhoan, String matKhau, boolean trangThai, double kinhDo, double viDo,String imagePath) {
        Id = id;
        Ten = ten;
        NgaySinh = ngaySinh;
        QueQuan = queQuan;
        TaiKhoan = taiKhoan;
        MatKhau = matKhau;
        TrangThai = trangThai;
        KinhDo = kinhDo;
        ViDo = viDo;
        ImagePath = imagePath;
    }
    public Shipper(String id, String ten, String ngaySinh, String queQuan, String taiKhoan, String matKhau, boolean trangThai, double kinhDo, double viDo,String imagePath,Kho kho) {
        Id = id;
        Ten = ten;
        NgaySinh = ngaySinh;
        QueQuan = queQuan;
        TaiKhoan = taiKhoan;
        MatKhau = matKhau;
        TrangThai = trangThai;
        KinhDo = kinhDo;
        ViDo = viDo;
        ImagePath = imagePath;
        Kho =kho;
    }
}
