package com.example.manager.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appbanhangonline.R;
import com.example.appbanhangonline.databinding.ActivityThemSpactivityBinding;
import com.example.manager.model.MasageModel;
import com.example.manager.model.SanPhamMoi;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClinet;
import com.example.manager.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemSPActivity extends AppCompatActivity {
    Spinner spinner;
    int loai = 0;
    ActivityThemSpactivityBinding binding;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String mediaPath;
    SanPhamMoi sanPhamSua;
    Toolbar toolbar;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_spactivity);
        binding = ActivityThemSpactivityBinding.inflate(getLayoutInflater());
        apiBanHang = RetrofitClinet.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setContentView(binding.getRoot());
        initView();
        initData();
        Intent intent = getIntent();
        sanPhamSua = (SanPhamMoi) intent.getSerializableExtra("Sửa");
        if ( sanPhamSua == null){
            //Thêm mới sản phẩm
            flag = false;
        }else {
            //Sửa sản phẩm
            flag = true;
            binding.btnThem.setText("Sửa sản phẩm");
            //Show data
            binding.mota.setText(sanPhamSua.getMota());
            binding.gia.setText(sanPhamSua.getGiaSp());
            binding.tensp.setText(sanPhamSua.getTenSp());
            binding.hinhanh.setText(sanPhamSua.getHinhAnh());
            binding.spinnerLoai.setSelection(sanPhamSua.getLoai());
        }

    }

    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Vui lòng chọn loại sản phẩm");
        stringList.add("Loại: 1");
        stringList.add("Loai: 2");
        stringList.add("Loai: 3");
        stringList.add("Loai: 4");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loai = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == false){
                    themsanpham();
                }else {
                    suaSanPham();
                }

            }
        });
        binding.imagecamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ThemSPActivity.this)
                        .crop()
                        .compress(1024)			//Final image size will be less than 1 MB(Option)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),QuanLiActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void suaSanPham() {
        String str_ten = binding.tensp.getText().toString().trim();
        String str_gia = binding.gia.getText().toString().trim();
        String str_mota = binding.mota.getText().toString().trim();
        String str_hinhanh = binding.hinhanh.getText().toString().trim();
        if (TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) ||  loai == 0){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin sản phẩm", Toast.LENGTH_SHORT).show();
        }else {
            compositeDisposable.add(apiBanHang.suasp(str_ten,str_gia,str_hinhanh,str_mota,loai, sanPhamSua.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            masageModel -> {
                                if (masageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(),"Sửa Thành công", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                                }


                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadFile();
        Log.d("log","onActivityResult"+mediaPath);
    }

    private void themsanpham(){
        String str_ten = binding.tensp.getText().toString().trim();
        String str_gia = binding.gia.getText().toString().trim();
        String str_mota = binding.mota.getText().toString().trim();
        String str_hinhanh = binding.hinhanh.getText().toString().trim();
        if (TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) ||  loai == 0){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin sản phẩm", Toast.LENGTH_SHORT).show();
        }else {
            compositeDisposable.add(apiBanHang.themsp(str_ten,str_gia,str_hinhanh,str_mota,(loai)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            masageModel -> {
                                if (masageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(),"Thêm Thành công", Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                                }


                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));

        }
    }
    private String getPath(Uri uri){
        String result;
        Cursor cursor = getContentResolver().query(uri,null,null,null);
        if (cursor == null){
            result = uri.getPath();

        }else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return  result;
    }
    private void uploadFile() {



        Uri uri = Uri.parse(mediaPath);

        File file = new File(getPath(uri));
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        Call<MasageModel> call = apiBanHang.uploadFile(fileToUpload);
        call.enqueue(new Callback<MasageModel>() {
            @Override
            public void onResponse(Call < MasageModel > call, Response <MasageModel> response) {
                MasageModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        binding.hinhanh.setText(serverResponse.getName());
                    } else {
                        Toast.makeText(getApplicationContext(), "Không thành công",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.v("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("log",t.getMessage());
            }
        });
    }
    private void initView() {
        spinner = findViewById(R.id.spinner_loai);
        toolbar = findViewById(R.id.toobar);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}