package com.example.manager.model;

public class LoaiSp {
    int id;
    String TenSP;

    public LoaiSp(String tenSP, String hinh) {
        TenSP = tenSP;
        Hinh = hinh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public String getHinh() {
        return Hinh;
    }

    public void setHinh(String hinh) {
        Hinh = hinh;
    }

    String Hinh;

}
