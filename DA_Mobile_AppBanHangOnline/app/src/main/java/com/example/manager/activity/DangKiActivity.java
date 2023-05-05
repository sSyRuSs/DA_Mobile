package com.example.manager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbanhangonline.R;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClinet;
import com.example.manager.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {
    EditText email,pass,resetpass,phone,username;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    FirebaseAuth firebaseAuth;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangki();
            }
        });
    }

    private void dangki() {
        String str_email = email.getText().toString().trim();//cắt chuỗi 2 đầu
        String str_pass = pass.getText().toString().trim();
        String str_resetpass = resetpass.getText().toString().trim();
        String str_phone = phone.getText().toString().trim();
        String str_username = username.getText().toString().trim();
        if (TextUtils.isEmpty(str_username)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập tên người dùng", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(), "Email không được để trống", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(), "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(str_resetpass)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập lại mật khẩu", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(str_phone)){
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();

        }else {
            if (str_pass.equals(str_resetpass)){
               firebaseAuth = FirebaseAuth.getInstance();
               firebaseAuth.createUserWithEmailAndPassword(str_email,str_pass)
                       .addOnCompleteListener(DangKiActivity.this, new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if (task.isSuccessful()){
                                   FirebaseUser user = firebaseAuth.getCurrentUser();
                                   if ( user != null){
                                       postData( str_email, str_pass, str_username, str_phone,user.getUid());
                                   }
                               }else{
                                   Toast.makeText(getApplicationContext(), "Email đã được sử dụng hoặc mật khẩu bạn nhập chưa hơp lệ. Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
            }
            else {
                Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void postData(String str_email,String str_pass,String str_username, String str_phone, String uid) {
        compositeDisposable.add(apiBanHang.dangki(str_email,"onfirebase",str_username,str_phone,uid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                Utils.user_current.setEmail(str_email);
                                Utils.user_current.setPass("onfirebase");
                                Toast.makeText(getApplicationContext(), "Đăng kí tài khoản thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),userModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }
    private void initView() {
        apiBanHang = RetrofitClinet.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        resetpass = findViewById(R.id.resetpass);
        phone = findViewById(R.id.phone);
        username = findViewById(R.id.username);
        button = findViewById(R.id.btndangki);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}