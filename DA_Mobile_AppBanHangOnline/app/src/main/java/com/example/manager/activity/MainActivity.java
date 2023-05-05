package com.example.manager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbanhangonline.R;
import com.example.manager.adapter.LoaiSpAdapter;
import com.example.manager.adapter.SanPhamMoiAdapter;
import com.example.manager.model.LoaiSp;
import com.example.manager.model.SanPhamMoi;
import com.example.manager.model.User;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClinet;
import com.example.manager.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewHome;
    NavigationView navigationView;
    ListView listViewHome;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imagesearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClinet.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        if (Paper.book().read("muser") != null){
            User user =Paper.book().read("muser");
            Utils.user_current = user;
        }
        getToken();
        Anhxa();
        ActionBar();
        ActionViewLipper();
        if (isConnect(this))
        {
            ActionViewLipper();
            getLoaiSanPham();
            getSpMoi();
            getEventClick();
        }else
        {
            Toast.makeText(this, "Không Có Internet, Vui lòng kết nối", Toast.LENGTH_SHORT).show();
        }

    }
    private void  getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)){
                            compositeDisposable.add(apiBanHang.updatetoken(Utils.user_current.getId(),s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            masageModel -> {

                                            },
                                            throwable -> {
                                                Log.d("log",throwable.getMessage());
                                            }
                                    ));
                        }
                    }
                });
    }

    private void getEventClick() {
        listViewHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(),DienThoaiActivity2.class);
                        startActivity(dienthoai);
                        dienthoai.putExtra("loai",1);
                        break;
                    case 2:
                        Intent phukien = new Intent(getApplicationContext(),DienThoaiActivity2.class);
                        phukien.putExtra("loai",3);
                        startActivity(phukien);
                        break;
                    case 3:
                        Intent dongho = new Intent(getApplicationContext(),DienThoaiActivity2.class);
                        dongho.putExtra("loai",4);
                        startActivity(dongho);
                        break;
                    case 4:
                        Intent laptop = new Intent(getApplicationContext(),DienThoaiActivity2.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 5:
                        Intent lienhe = new Intent(getApplicationContext(),LienHeActivity.class);
                        startActivity(lienhe);
                        break;
                    case 6:
                        Intent lichsu = new Intent(getApplicationContext(),LichSuActivity.class);
                        startActivity(lichsu);
                        break;
                    case 7:
                        Intent quanli = new Intent(getApplicationContext(),QuanLiActivity.class);
                        startActivity(quanli);
                        finish();
                        break;
                    case 8:
                        // xoa user
                        Paper.book().delete("muser");
                        FirebaseAuth.getInstance().signOut();
                        Intent dangnhap = new Intent(getApplicationContext(),DangNhapActivity.class);
                        startActivity(dangnhap);
                        finish();
                        break;
                    case 9:
                        Intent chat = new Intent(getApplicationContext(),UserActivity.class);
                        startActivity(chat);
                        finish();
                        break;
                    case 10:
                        Intent thongke = new Intent(getApplicationContext(),ThongKeSPActivity.class);
                        startActivity(thongke);
                        break;
                }
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
                                mangSpMoi = sanPhamMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                                recyclerViewHome.setAdapter(spAdapter);

                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối được với sever"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }

                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()) {
                                mangloaisp = loaiSpModel.getResult();
                                mangloaisp.add(new LoaiSp("Chat","https://static.vecteezy.com/system/resources/previews/000/512/952/original/message-board-glyph-black-icon-vector.jpg"));
                                mangloaisp.add(new LoaiSp("Thống Kê","https://static.vecteezy.com/system/resources/previews/000/349/407/original/vector-statistics-icon.jpg"));
                                loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                                listViewHome.setAdapter(loaiSpAdapter);
                            }
                        }
                ));
    }


    private void ActionViewLipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://i.ytimg.com/vi/uA9By--6dck/maxresdefault.jpg");
        mangquangcao.add("https://tintuc.dienthoaigiakho.vn/wp-content/uploads/2022/09/1200x800_Main-Banner.jpg");
        mangquangcao.add("https://img.global.news.samsung.com/in/wp-content/uploads/2022/02/S22_KV_Banner_728x410.jpg");
        mangquangcao.add("https://i.ytimg.com/vi/_KzQmifeek4/maxresdefault.jpg");
        mangquangcao.add("https://www.sanluis24.com.ar/wp-content/uploads/2021/08/Pixel-6-Pro-scaled-1-768x432.jpg");
        for ( int i = 0; i < mangquangcao.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);


        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.siler_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

    }

    private void Anhxa() {
        imagesearch = findViewById(R.id.imagesreach);
        toolbar = findViewById(R.id.toolbarHome);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerViewHome = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setHasFixedSize(true);
        listViewHome = findViewById(R.id.ListViewHome);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        //khoi tao list
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        if (Utils.manggiohang == null){

            Utils.manggiohang = new ArrayList<>();
        }else {
            onResume();
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intent);
            }
        });
        imagesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for ( int i = 0; i < Utils.manggiohang.size(); i ++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            // Cứ 1 lần chạy qua mảng thì nó sẽ lấy số lượng cộng với total
        }
        badge.setText(String.valueOf(totalItem));
    }

    public  boolean isConnect(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected()) ) {
            return  true;
        }
        else{
            return false;
        }
    }
    public  void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}