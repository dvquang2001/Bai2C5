package com.example.model;

public class NhanVien {
    private int MaNV;
    private String TenNV;
    private String GioiTinh;
    private String ChucVu;
    private String MaPB;
    PhongBan phongBan;
    public NhanVien() {
    }

    public NhanVien(int maNV, String tenNV, String gioiTinh, String chucVu, String maPB) {
        MaNV = maNV;
        TenNV = tenNV;
        GioiTinh = gioiTinh;
        ChucVu = chucVu;
        MaPB = maPB;
    }

    public int getMaNV() {
        return MaNV;
    }

    public void setMaNV(int maNV) {
        MaNV = maNV;
    }

    public String getTenNV() {
        return TenNV;
    }

    public void setTenNV(String tenNV) {
        TenNV = tenNV;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public String getChucVu() {
        return ChucVu;
    }

    public void setChucVu(String chucVu) {
        ChucVu = chucVu;
    }

    public String getMaPB() {
        return MaPB;
    }

    public void setMaPB(String maPB) {
        MaPB = maPB;
    }

    public PhongBan getPhongBan() {
        return phongBan;
    }

    public void setPhongBan(PhongBan phongBan) {
        this.phongBan = phongBan;
    }

    @Override
    public String toString() {
        return this.MaNV+" - "+this.TenNV+"("+this.ChucVu+")"+"\n";
    }
}
