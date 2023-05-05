package com.example.manager.model;

import java.io.Serializable;

public class Contact implements Serializable {
    private String Name;
    private String Details;
    private  String Phone;
    private  String DiaChi;

    public Contact(String name, String details, String phone, String diaChi) {
        Name = name;
        Details = details;
        Phone = phone;
        DiaChi = diaChi;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }
}
