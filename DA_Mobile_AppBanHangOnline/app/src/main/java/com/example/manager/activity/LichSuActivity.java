package com.example.manager.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.appbanhangonline.R;
import com.example.manager.adapter.DonHangAdapter;
import com.example.manager.model.DonHang;
import com.example.manager.model.EventBus.DonHangEvent;
import com.example.manager.model.NotiSenddata;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.ApiPushNotification;
import com.example.manager.retrofit.RetrofitClinerNoti;
import com.example.manager.retrofit.RetrofitClinet;
import com.example.manager.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LichSuActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView redonhang;
    Toolbar toolbar;
    DonHang donHang;
    int tinhtrang;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su);
        initView();
        initToolbar();
        getOrder();
    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.xemDonHang(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {

                            DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(),donHangModel.getResult());
                            redonhang.setAdapter(adapter);
                            //Toast.makeText(getApplicationContext(), donHangModel.getResult().get(0).getItem().get(0).getTensp(), Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {

                        }
                ));

    }
    private void pushNotitoUser() {
        compositeDisposable.add(apiBanHang.getToken(0,donHang.getIduser())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if ( userModel.isSuccess()){
                                for (int i = 0; i < userModel.getResult().size(); i ++){
                                    Map<String,String> data = new HashMap<>();
                                    data.put("title","Thông báo từ ứng dụng bán hàng online");
                                    data.put("body",trangThaiDonHang(tinhtrang));
                                    NotiSenddata notiSenddata = new NotiSenddata(userModel.getResult().get(i).getToken(),data);
                                    ApiPushNotification apiPushNotification = RetrofitClinerNoti.getInstance().create(ApiPushNotification.class);
                                    compositeDisposable.add(apiPushNotification.sendNotification(notiSenddata)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(
                                                    notiResponse -> {

                                                    },
                                                    throwable -> {
                                                        Log.d("logg",throwable.getMessage());
                                                    }
                                            ));
                                }
                            }
                        },
                        throwable -> {
                            Log.d("log",throwable.getMessage());
                        }
                ));

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        apiBanHang = RetrofitClinet.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        redonhang = findViewById(R.id.recyclerView_donhang);
        toolbar = findViewById(R.id.toobar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        redonhang.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventDonHang(DonHangEvent event){
        if ( event != null){
            donHang = event.getDonHang();
            showCustomDialog();
        }
    }
    private String trangThaiDonHang(int status){
        String result = "";
        switch (status ){
            case 0:
                result = "Đơn hàng đang được sử lí";
                break;
            case 1:
                result = "Đơn hàng đã bị hủy";
                break;
            case 2:
                result = "Đơn hàng đang được đóng gói, chuẩn bị giao cho nhà vận chuyển";
                break;
            case 3:
                result = "Đơn hàng đã đến kho vận chuyển";
                break;
            case 4:
                result = "Shipper đã nhận hàng";
                break;
            case 5:
                result = "Đơn hàng đã được giao";
                break;
        }
        return result;
    }

    private void showCustomDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_donhang,null);
        Spinner spinner = view.findViewById(R.id.spinner_dialog);
        AppCompatButton btnDongY = view.findViewById(R.id.dongy_dialog);
        List<String> list = new ArrayList<>();
        list.add("Đơn hàng đang được sử lí");
        list.add("Đơn hàng đã bị hủy");
        list.add("Đơn hàng đang được đóng gói, chuẩn bị giao cho nhà vận chuyển");
        list.add("Đơn hàng đã đến kho vận chuyển");
        list.add("Shipper đã nhận hàng");
        list.add("Đơn hàng đã được giao");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,list);
        spinner.setAdapter(adapter);
        spinner.setSelection(donHang.getTrangthai());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tinhtrang = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updataDialog();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog= builder.create();
        dialog.show();

    }

    private void updataDialog() {
        compositeDisposable.add(apiBanHang.updateDialog(donHang.getId(),tinhtrang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        masageModel -> {
                            getOrder();
                            dialog.dismiss();
                            pushNotitoUser();
                        },
                        throwable -> {

                        }
                ));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}