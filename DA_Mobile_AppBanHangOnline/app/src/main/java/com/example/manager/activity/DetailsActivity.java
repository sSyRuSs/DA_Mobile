package com.example.manager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appbanhangonline.R;
import com.example.manager.utils.DatabaseHandler;
import com.example.manager.model.Contact;

public class DetailsActivity extends AppCompatActivity {

    TextView txttitle,txtsingle,txtPhone,txtDiaChi;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        Contact songs = (Contact) getIntent().getSerializableExtra("aaa");
        txttitle = findViewById(R.id.txtTitle);
        txtsingle =findViewById(R.id.txtSingle);

        txtsingle.setText(songs.getDetails().toString().trim());
        txttitle.setText(songs.getName().toString().trim());
        //txtPhone.setText(songs.getPhone());
        initView();
        initControl();

    }
    private void initView() {
        toolbar = findViewById(R.id.toobar);
    }
    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LienHeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}