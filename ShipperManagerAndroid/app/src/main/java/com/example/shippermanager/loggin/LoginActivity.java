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
        textPassword.setText("admin");
        textUsername = findViewById(R.id.text_username);
        textUsername.setText("admin");
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
        Calendar car =  Calendar.getInstance();
        car.set(Calendar.YEAR,1999);
        car.set(Calendar.MONTH,10);
        car.set(Calendar.DAY_OF_MONTH,20);
        String key = Database.child("Shipper").push().getKey();
        Shipper shipper = new Shipper(key,"hy",car.getTimeInMillis(),"Kien Giang",true,"admin","admin");
        Database.child("Shipper").child(key).setValue(shipper);
    }

    private void login()
    {
        String username = textUsername.getText().toString();
        String password = textPassword.getText().toString();

        if(username.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this,"username or password is empty",Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Shipper");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    Shipper s = child.getValue(Shipper.class);
                    if(username.equals(s.getTaiKhoan())&&password.equals(s.getMatKhau()))
                    {
                        loginInSuccess(s);
                        return;
                    }
                }
                //login fail
                Toast.makeText(getBaseContext(),"login failure, username or password incorrect",Toast.LENGTH_SHORT).show();
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
