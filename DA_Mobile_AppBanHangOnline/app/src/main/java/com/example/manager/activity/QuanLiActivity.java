package com.example.manager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.appsearch.GetByDocumentIdRequest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.appbanhangonline.R;
import com.example.manager.adapter.SanPhamMoiAdapter;
import com.example.manager.model.EventBus.SuaXoaEvent;
import com.example.manager.model.SanPhamMoi;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClinet;
import com.example.manager.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphCardView;

public class QuanLiActivity extends AppCompatActivity {
    ImageView image_them;
    LinearLayout layoutBack;
    RecyclerView recyclerView_ql;
    Toolbar toolbar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> list;
    SanPhamMoiAdapter adapter;
    SanPhamMoi sanPhamSuaXoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li);
        apiBanHang = RetrofitClinet.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        initView();
        initControl();
        getSpMoi();

    }

    private void initControl() {
        image_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ThemSPActivity.class);
                startActivity(intent);

            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess())
                            {
                                list = sanPhamMoiModel.getResult();
                                adapter = new SanPhamMoiAdapter(getApplicationContext(),list);
                                recyclerView_ql.setAdapter(adapter);

                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối được với sever"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }

                ));
    }

    private void initView() {
        image_them = findViewById(R.id.image_them);
        recyclerView_ql = findViewById(R.id.recyclerView_ql);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_ql.setHasFixedSize(true);
        recyclerView_ql.setLayoutManager(layoutManager);
        toolbar = findViewById(R.id.toobar);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if ( item.getTitle().equals("Sửa")){
            suaSanPham();
        }else if (item.getTitle().equals("Xóa")){
            xoaSanPham();
        }
        return super.onContextItemSelected(item);
    }

    private void xoaSanPham() {
        compositeDisposable.add(apiBanHang.xoaSanPham(sanPhamSuaXoa.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        masageModel -> {
                            if ( masageModel.isSuccess()){
                                Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                                getSpMoi();
                            }else{
                                Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                            }

                        },
                        throwable -> {
                            Log.d("log",throwable.getMessage());
                        }
                ));

    }

    private void suaSanPham() {
        Intent intent = new Intent(getApplicationContext(),ThemSPActivity.class);
        intent.putExtra("Sửa",sanPhamSuaXoa);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public  void eventSuaXoa(SuaXoaEvent event){
        if (event != null){
            sanPhamSuaXoa = event.getSanPhamMoi();
        }
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