package com.example.manager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.appbanhangonline.R;
import com.example.manager.utils.DatabaseHandler;
import com.example.manager.model.Contact;


import java.util.List;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        DatabaseHandler db = new DatabaseHandler(this);

        // Inserting Contacts
        db.addContact(new Contact("Tên Trường", "Trường đại học CNTP TP.HCM (HUFI) ","",""));
        db.addContact(new Contact("Tên Đồ Án", "Ứng dụng bán hàng online","",""));
        db.addContact(new Contact("Tên Nhóm", "Nhóm X","",""));
        db.addContact(new Contact("GV.Hướng Dẫn", "GV.Nguyễn Ngọc Lâm","",""));
        db.addContact(new Contact("Face Book", "Nguyễn Thành Đạt","",""));
        db.addContact(new Contact("Zalo", "0971881583","",""));
        db.addContact(new Contact("Email", "ThanhDat29012002@gmail.com","",""));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            String log = "Name: " + cn.getName() + " ,Details: " + cn.getDetails() + " ,Phone: " + cn.getPhone() + " ,DiaChi: " + cn.getDiaChi();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }




}