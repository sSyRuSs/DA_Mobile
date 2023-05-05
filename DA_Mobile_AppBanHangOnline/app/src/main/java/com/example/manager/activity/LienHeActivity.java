package com.example.manager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.appbanhangonline.R;
import com.example.manager.model.Contact;
import com.example.manager.adapter.MusicListAdapter;
import com.example.manager.utils.DatabaseHandler;

import java.util.List;

public class LienHeActivity extends AppCompatActivity {

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lien_he);

        DatabaseHandler dbHandle = new DatabaseHandler(this);
        List<Contact> songList = dbHandle.getAllContacts();

        MusicListAdapter adapter=new MusicListAdapter(this, songList);

        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LienHeActivity.this, DetailsActivity.class);
                String idd = String.valueOf(list.getId());
                Contact contact = (Contact) parent.getItemAtPosition(position);
                intent.putExtra("aaa",contact);
                startActivity(intent);
            }
        });

    }

}