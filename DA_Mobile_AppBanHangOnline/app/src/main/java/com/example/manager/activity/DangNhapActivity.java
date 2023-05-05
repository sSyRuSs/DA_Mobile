package com.example.manager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangki,txtResetPass;
    EditText email,pass;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    AppCompatButton btndangnhap;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        initView();
        initControl();
    }

    private void initControl() {
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DangKiActivity.class);
                startActivity(intent);
            }
        });
        txtResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ResetPassActivity.class);
                startActivity(intent);

            }
        });
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = email.getText().toString().trim();
                String str_pass = pass.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                }else {
                    Paper.book().write("Email", str_email);
                    Paper.book().write("Pass", str_pass);
                    if (user != null){
                        // user đã có tren firebase chua sigout
                        dangNhap(str_email,str_pass);
                    }else{
                        // user đã sigout
                        firebaseAuth.signInWithEmailAndPassword(str_email,str_pass)
                                .addOnCompleteListener(DangNhapActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if ( task.isSuccessful()){
                                            dangNhap(str_email,str_pass);
                                        }
                                    }
                                });
                    }

                }
            }
        });
    }

    private void initView() {
        Paper.init(this);
        apiBanHang = RetrofitClinet.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangki = findViewById(R.id.txtdangki);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        btndangnhap = findViewById(R.id.btndangnhap);
        txtResetPass = findViewById(R.id.txtResetPass);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //read data
        if (Paper.book().read("Email") != null && Paper.book().read("Pass")!= null){
            email.setText(Paper.book().read("Email"));
            pass.setText(Paper.book().read("Pass"));
            if(Paper.book().read("isLogin")!= null){
                boolean flag = Paper.book().read("isLogin");
                if ( flag ){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //dangNhap(Paper.book().read("Email"),Paper.book().read("Pass"));
                        }
                    },1000);
                }
            }

        }
    }

    private void dangNhap(String email,String pass) {
        compositeDisposable.add(apiBanHang.dangnhap(email,pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            //userModel.isSuccess: Lấy giá trị success trong php
                            if (userModel.isSuccess()) {
                                //Ghi nhớ trạng thái đăng nhập
                                isLogin = true;
                                Paper.book().write("isLogin",isLogin);
                                Utils.user_current = userModel.getResult().get(0);

                                //Luu lai thong tin user
                                Paper.book().write("muser", userModel.getResult().get(0));
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail() != null && Utils.user_current.getPass() != null){
            email.setText(Utils.user_current.getEmail() );
            pass.setText(Utils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}