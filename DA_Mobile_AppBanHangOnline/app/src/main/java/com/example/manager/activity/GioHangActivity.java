package com.example.manager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhangonline.R;
import com.example.manager.adapter.GioHangAdapter;
import com.example.manager.model.EventBus.SumMonnyEvent;
import com.example.manager.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong,tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    AppCompatButton btnmuahang;
    GioHangAdapter adapter;
    long tongtiensp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        if ( Utils.mangmuahang != null){
            Utils.mangmuahang.clear();
        }
        initSumMonny();
    }

    private void initSumMonny() {
        tongtiensp = 0;
        for (int i = 0; i < Utils.mangmuahang.size(); i ++){
            tongtiensp = tongtiensp + (Utils.mangmuahang.get(i).getGiasp() * Utils.mangmuahang.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tongtiensp)+" đ");
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (Utils.manggiohang.size() ==0){
            giohangtrong.setVisibility(View.VISIBLE);
        }else{
            adapter = new GioHangAdapter(getApplicationContext(),Utils.manggiohang);
            recyclerView.setAdapter(adapter);
        }

        btnmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.manggiohang.size() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Bạn chưa thêm sản phẩm nào. Không thể thanh toán", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                    intent.putExtra("tongtien", tongtiensp);
                    Utils.manggiohang.clear();

                    startActivity(intent);
                }

            }
        });

    }

    private void initView() {
        giohangtrong = findViewById(R.id.txtgiohangtrong);
        tongtien = findViewById(R.id.txttongtien);
        toolbar = findViewById(R.id.toobar);
        recyclerView = findViewById(R.id.recyclerView_giohang);
        btnmuahang = findViewById(R.id.btnmuahang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void EventSumMonny(SumMonnyEvent event){
        if (event != null){
            initSumMonny();
        }
    }
}