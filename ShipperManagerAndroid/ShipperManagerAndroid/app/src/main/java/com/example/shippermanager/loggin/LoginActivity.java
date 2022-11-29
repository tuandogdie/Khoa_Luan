package com.example.shippermanager.loggin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shippermanager.Model.HelperUtils;
import com.example.shippermanager.Model.Shipper;
import com.example.shippermanager.Order.OrderListActivity;
import com.example.shippermanager.R;
import com.example.shippermanager.menu.MenuActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText textUsername;
    private TextInputEditText textPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private DatabaseReference Database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Database = FirebaseDatabase.getInstance().getReference();
        initView();
        registerListener();

    }

    private void initView()
    {
        textPassword = findViewById(R.id.text_password);

        textUsername = findViewById(R.id.text_username);

        buttonLogin = findViewById(R.id.btnDangNhap);
        buttonRegister = findViewById(R.id.btnDangKy);
    }

    private void registerListener(){

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnDangNhap:
                login();
                break;
            case R.id.btnDangKy:
                Register();
                break;
            default:
                break;


        }
    }

    private void Register()
    {

        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    private void login()
    {
        String username = textUsername.getText().toString();
        String password = textPassword.getText().toString();

        if(username.isEmpty())
        {
            textUsername.setError("Tên tài khoản không được trống");
            textUsername.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            textPassword.setError("Mật khẩu không được trống");
            textPassword.requestFocus();
            return;
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Shipper");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    Shipper s = child.getValue(Shipper.class);
                    if(username.equals(s.TaiKhoan)&&password.equals(s.MatKhau))
                    {
                        if(s.TrangThai)
                        {
                            loginInSuccess(s);
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(),"tài khoảng của bạn không khả dụng, xin liên hệ với người quản lý hoặc chờ xác thực",Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                }
                //login fail
                Toast.makeText(getBaseContext(),"đăng nhập thất bại, tên tài khoảng hoặc mật khẩu không đúng",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("error", "Failed to read value.", error.toException());
            }
        });


    }

    public void loginInSuccess(Shipper s) {
        Gson gson = new Gson();
        String json = gson.toJson(s);
        HelperUtils.AddSharedObject(this,json,"Shipper");
        startActivity(new Intent(this, MenuActivity.class));
    }

}
