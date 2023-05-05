package com.example.manager.model;

import java.io.Serializable;

public class SanPhamMoi implements Serializable {
    int id;
    String TenSp;
    String HinhAnh;
    String Mota;

    public String getGiaSp() {
        return GiaSp;
    }

    public void setGiasp(String giasp) {
        GiaSp = giasp;
    }

    String GiaSp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSp() {
        return TenSp;
    }

    public void setTenSp(String tenSp) {
        TenSp = tenSp;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public String getMota() {
        return Mota;
    }

    public void setMota(String mota) {
        Mota = mota;
    }


    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
    }

    int loai;
}
