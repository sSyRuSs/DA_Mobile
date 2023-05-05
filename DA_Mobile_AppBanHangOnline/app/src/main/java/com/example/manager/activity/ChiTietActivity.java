package com.example.manager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.appbanhangonline.R;
import com.example.manager.model.GioHangModel;
import com.example.manager.model.SanPhamMoi;
import com.example.manager.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp,giasp,mota;
    Button btnthemvaogio;
    ImageView imghinhanh;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnthemvaogio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if (Utils.manggiohang.size() > 0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Utils.manggiohang.size(); i ++){
                if (Utils.manggiohang.get(i).getIdsp() == sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());
                    long gia = Long.parseLong(sanPhamMoi.getGiaSp()) * Utils.manggiohang.get(i).getSoluong();
                    Utils.manggiohang.get(i) .setGiasp(gia);
                    flag = true;
                }
            }
            if ( flag == false){
                long gia = Long.parseLong(sanPhamMoi.getGiaSp())* soluong;
                GioHangModel gioHangModel = new GioHangModel();
                gioHangModel.setGiasp(gia);
                gioHangModel.setSoluong(soluong);
                gioHangModel.setIdsp(sanPhamMoi.getId());
                gioHangModel.setTensp(sanPhamMoi.getTenSp());
                gioHangModel.setHinhsp(sanPhamMoi.getHinhAnh());
                Utils.manggiohang.add(gioHangModel);

            }

        }else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiaSp())* soluong;
            GioHangModel gioHangModel = new GioHangModel();
            gioHangModel.setGiasp(gia);
            gioHangModel.setSoluong(soluong);
            gioHangModel.setIdsp(sanPhamMoi.getId());
            gioHangModel.setTensp(sanPhamMoi.getTenSp());
            gioHangModel.setHinhsp(sanPhamMoi.getHinhAnh());
            Utils.manggiohang.add(gioHangModel);

        }
        if (Utils.manggiohang != null)
        {
            int totalItem = 0;
            for ( int i = 0; i < Utils.manggiohang.size(); i ++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
                // Cứ 1 lần chạy qua mảng thì nó sẽ lấy số lượng cộng với total
            }
            badge.setText(String.valueOf(totalItem));
        }
    }

    private void initData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTenSp());
        mota.setText(sanPhamMoi.getMota());
        if (sanPhamMoi.getHinhAnh().contains("http")){
            Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhAnh()).into(imghinhanh);
        }else{
            String hinhanh = Utils.BASE_URL+"images/"+sanPhamMoi.getHinhAnh();
            Glide.with(getApplicationContext()).load(hinhanh).into(imghinhanh);
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiaSp()))+" đ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);
    }

    private void initView() {
        tensp = findViewById(R.id.txttensp);
        giasp = findViewById(R.id.txtgiasp);
        mota = findViewById(R.id.txtmotachitiet);
        btnthemvaogio = findViewById(R.id.btnthemvaogio);
        spinner = findViewById(R.id.spinner);
        imghinhanh = findViewById(R.id.imgchitiet);
        toolbar = findViewById(R.id.toobar);
        badge = findViewById(R.id.menu_sl);
        FrameLayout frameLayoutGiohang = findViewById(R.id.framegiohang);
        frameLayoutGiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if (Utils.manggiohang != null)
        {
            int totalItem = 0;
            for ( int i = 0; i < Utils.manggiohang.size(); i ++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
                // Cứ 1 lần chạy qua mảng thì nó sẽ lấy số lượng cộng với total
            }
            badge.setText(String.valueOf(Utils.manggiohang.size()));
        }
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.manggiohang != null)
        {
            int totalItem = 0;
            for ( int i = 0; i < Utils.manggiohang.size(); i ++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
                // Cứ 1 lần chạy qua mảng thì nó sẽ lấy số lượng cộng với total
            }
            badge.setText(String.valueOf(Utils.manggiohang.size()));
        }
    }
}