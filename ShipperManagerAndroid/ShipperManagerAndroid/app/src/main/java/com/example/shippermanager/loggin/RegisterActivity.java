package com.example.shippermanager.loggin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shippermanager.Model.Shipper;
import com.example.shippermanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, IPickResult {

    private TextInputEditText txtName;
    private TextInputEditText txtQueQuan;
    private TextInputEditText txtNgaySinh;
    private TextInputEditText txtUsername;
    private TextInputEditText txtPassword;
    private TextInputEditText txtValidatePassword;
    private Button btnRegister;
    private DatabaseReference Database;
    private Button btnPickImg;
    private ImageView imgShipper;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Đăng ký");
        Database = FirebaseDatabase.getInstance().getReference();
        initView();
        registerListener();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        txtName = findViewById(R.id.txt_resigter_name);
        txtQueQuan = findViewById(R.id.txt_register_quequan);
        txtNgaySinh = findViewById(R.id.txt_register_ngaysinh);
        txtUsername = findViewById(R.id.txt_register_username);
        txtPassword = findViewById(R.id.txt_register_password);
        txtValidatePassword = findViewById(R.id.txt_register_validate_password);
        btnRegister = findViewById(R.id.btn_register_dangky);
        btnPickImg = findViewById(R.id.btn_retgister_pickimg);
        imgShipper = findViewById(R.id.img_shipper);

    }

    private void registerListener() {
        btnRegister.setOnClickListener(this);
        btnPickImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_dangky:
                register();
                break;
            case R.id.btn_retgister_pickimg:
                pickImage();
                break;
            default:
                break;


        }
    }

    private void pickImage() {
        PickImageDialog.build(new PickSetup()).show(this);

    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            imgShipper.setImageURI(r.getUri());

            //If you want the Bitmap.
            //getImageView().setImageBitmap(r.getBitmap());

            //Image path
            filePath = r.getUri();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void register() {
//                Calendar car =  Calendar.getInstance();
//        car.set(Calendar.YEAR,1999);
//        car.set(Calendar.MONTH,10);
//        car.set(Calendar.DAY_OF_MONTH,20);
        String key = Database.child("Shipper").push().getKey();
        String name = txtName.getText().toString();
        String quequan = txtQueQuan.getText().toString();
        String ngaySinh = txtNgaySinh.getText().toString();
        String userName = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String validatePassword = txtValidatePassword.getText().toString();

        if (name.isEmpty()) {
            txtName.setError("Tên không được trống");
            txtName.requestFocus();
            return;
        }
        if (quequan.isEmpty()) {
            txtQueQuan.setError("quê quán không được trống");
            txtQueQuan.requestFocus();
            return;
        }
        if (ngaySinh.isEmpty()) {
            txtNgaySinh.setError("ngày sinh không được trống");
            txtNgaySinh.requestFocus();
            return;
        }
        if (userName.isEmpty()) {
            txtUsername.setError("tên tài khoản không được trống");
            txtUsername.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            txtPassword.setError("mật khẩu không được trống");
            txtPassword.requestFocus();
            return;
        }
        if (validatePassword.isEmpty()) {
            txtValidatePassword.setError("xác nhận mật khẩu không được trống");
            txtValidatePassword.requestFocus();
            return;
        }

        if (!password.equals(validatePassword)) {
            txtValidatePassword.setError("xác nhận mật khẩu không đúng");
            txtValidatePassword.requestFocus();
            return;
        }


        // Defining the child of storageReference
        StorageReference ref = storageReference
                .child("shipper-images/" + UUID.randomUUID().toString());
        // adding listeners on upload
        // or failure of image
        ref.putFile(filePath)
                .addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Download file From Firebase Storage
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadPhotoUrl) {
                                        Shipper shipper = new Shipper(key, name, ngaySinh, quequan, userName, password, false, 0, 0, downloadPhotoUrl.toString());
                                        Database.child("Shipper").child(key).setValue(shipper);
                                    }
                                });
                                Log.i("tag", "update success");
                            }
                        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("tag", "update failure");
                    }
                });


        startActivity(new Intent(this, LoginActivity.class));


    }
}
