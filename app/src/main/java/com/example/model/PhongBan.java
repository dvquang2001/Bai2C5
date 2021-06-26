package com.example.model;

import java.util.ArrayList;

public class PhongBan {
    private String MaPB;
    private String TenPB;
    private ArrayList<NhanVien> nhanViens = new ArrayList<>();

    public PhongBan() {
    }

    public PhongBan(String maPB, String tenPB) {
        MaPB = maPB;
        TenPB = tenPB;
    }

    public String getMaPB() {
        return MaPB;
    }

    public void setMaPB(String maPB) {
        MaPB = maPB;
    }

    public String getTenPB() {
        return TenPB;
    }

    public void setTenPB(String tenPB) {
        TenPB = tenPB;
    }

    public ArrayList<NhanVien> getNhanViens() {
        return nhanViens;
    }

    public void setNhanViens(ArrayList<NhanVien> nhanViens) {
        this.nhanViens = nhanViens;
    }

    @Override
    public String toString() {
        return this.MaPB+" - "+this.TenPB;
    }
}
