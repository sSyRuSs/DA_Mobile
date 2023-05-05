package com.example.manager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.appbanhangonline.R;
import com.example.manager.model.NotiSenddata;
import com.example.manager.retrofit.ApiPushNotification;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClinerNoti;
import com.example.manager.retrofit.RetrofitClinet;
import com.example.manager.utils.Utils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien,txtemail,txtdienthoai;
    EditText editdiachi;
    AppCompatButton btnthanhtoan;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        initView();
        countItem();
        initControl();
    }

    private void countItem() {
         totalItem = 0;
        for (int i = 0; i < Utils.mangmuahang.size(); i ++){
            totalItem = totalItem + Utils.mangmuahang.get(i).getSoluong();
            // Cứ 1 lần chạy qua mảng thì nó sẽ lấy số lượng cộng với total
        }
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
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien",0);
        txttongtien.setText(decimalFormat.format(tongtien)+"đ");
        txtemail.setText("Email: "+Utils.user_current.getEmail());
        txtdienthoai.setText("Điện thoại: "+Utils.user_current.getPhone());

        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_diachi = editdiachi.getText().toString();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
                }else{
                    //post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getPhone();
                    int id = Utils.user_current.getId();
                    Log.d("test",new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.createOder(str_email,str_sdt,String.valueOf(tongtien),id,str_diachi,totalItem,new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(), "Mua hàng thành công", Toast.LENGTH_SHORT).show();
                                        Utils.mangmuahang.clear();
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }


    private void initView() {
        apiBanHang = RetrofitClinet.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toobar);
        txttongtien = findViewById(R.id.txttongtien);
        txtemail = findViewById(R.id.txtemail);
        txtdienthoai = findViewById(R.id.txtdienthoai);
        editdiachi = findViewById(R.id.editdiachi);
        btnthanhtoan = findViewById(R.id.btnthanhtoan);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}