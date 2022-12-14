package com.example.shippermanager.Order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shippermanager.Model.TrangThaiDonHang;
import com.example.shippermanager.map.MapsActivity;
import com.example.shippermanager.Model.DonHang;
import com.example.shippermanager.Model.HelperUtils;
import com.example.shippermanager.R;
import com.example.shippermanager.product.ProductListActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class ShowOrderActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DonHang DonHang;

    private TextView txtTongTien;
    private TextView txtPhiShip;
    private TextView txtThuHo;
    private TextView txtLoTrinh;
    private TextView txtDiaChi;
    private TextView txtTenKh;
    private TextView txtSdt;
    private TextView txtSanPhamCount;

    private Button btnHuyDon;
    private Button btnXemMap;
    private Button btnDaGiao;
    private Button btnViewProduct;
    private ImageButton btnCall;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("TH??NG TIN ????N H??NG");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("DonHang");
        initView();
        registerListener();
    }

    private void registerListener(){
        btnHuyDon.setOnClickListener(this);
        btnXemMap.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        btnDaGiao.setOnClickListener(this);
        btnViewProduct.setOnClickListener(this);
    }

    private void initView()
    {
        txtTongTien = findViewById(R.id.txt_show_order_tong);
        txtPhiShip = findViewById(R.id.txt_show_order_phi_ship);
        txtThuHo = findViewById(R.id.txt_show_order_thu_ho);
        txtLoTrinh = findViewById(R.id.txt_show_order_lo_trinh);
        txtDiaChi = findViewById(R.id.txt_show_order_dia_chi);
        txtTenKh = findViewById(R.id.txt_show_order_ten_kh);
        txtSdt = findViewById(R.id.txt_show_order_sdt);
        txtSanPhamCount = findViewById(R.id.txt_show_order_san_pham_count);

        btnHuyDon = findViewById(R.id.btn_show_order_huy_don);
        btnXemMap = findViewById(R.id.btn_show_order_xem_map);
        btnCall = findViewById(R.id.btn_show_order_call);
        btnDaGiao = findViewById(R.id.btn_show_order_da_giao);
        btnViewProduct = findViewById(R.id.btn_show_order_xem_san_Pham);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String ma = extras.getString("MaDonHang");
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference ref = database.child("DonHang");
            //money format
            NumberFormat format = HelperUtils.GetNumberFormat();

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child: snapshot.getChildren()) {
                        DonHang = child.getValue(DonHang.class);
                        DonHang.Id = child.getKey();
                        if(DonHang.Id.equals(ma))
                        {
                            if(DonHang.PhuongThucThanhToan.Ten.equals("Thanh To??n Tr???c Ti???p")) {
                                txtThuHo.setText(format.format(DonHang.TongTien));
                                double tong = DonHang.TongTien + DonHang.PhiGiaoHang;
                                txtTongTien.setText(format.format(tong));
                            }
                            else
                            {
                                txtThuHo.setText(format.format(0));
                                double tong = DonHang.PhiGiaoHang;
                                txtTongTien.setText(format.format(tong));
                            }
                            txtPhiShip.setText(format.format(DonHang.PhiGiaoHang));
                            txtLoTrinh.setText("L??? Tr??nh: "+String.valueOf(DonHang.KhoanCach)+"km");
                            txtDiaChi.setText(DonHang.KhachHang.DiaChi);
                            txtTenKh.setText(DonHang.KhachHang.Ten);
                            txtSdt.setText(DonHang.KhachHang.SoDienThoai);
                            txtSanPhamCount.setText("S???n Ph???m: "+String.valueOf(DonHang.DanhSachCTTT.size()));
                            if(DonHang.TrangThaiGiao == TrangThaiDonHang.DaGiao.ordinal())
                            {
                                btnDaGiao.setVisibility(View.GONE);
                                btnHuyDon.setVisibility(View.GONE);
                            }
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("error", "Failed to read value.", error.toException());
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_show_order_huy_don:
                CancelOrder();
                break;
            case R.id.btn_show_order_xem_map:
                ViewMap();
                break;
            case R.id.btn_show_order_call:
                CallCustomer();
                break;
            case R.id.btn_show_order_da_giao:
                SuccessDelivery();
                break;
            case R.id.btn_show_order_xem_san_Pham:
                ViewProduct();
                break;
            default:
                break;
        }
    }

    private void ViewProduct()
    {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.putExtra("MaDonHang",DonHang.Id);
        startActivity(intent);
    }

    private void CallCustomer()
    {
        Intent i = new Intent(Intent.ACTION_DIAL);
        String p = "tel:" + DonHang.KhachHang.SoDienThoai;
        i.setData(Uri.parse(p));
        startActivity(i);
    }

    private void SuccessDelivery()
    {
        DonHang.TrangThaiGiao = TrangThaiDonHang.DaGiao.ordinal();
        DonHang.NgayGiao =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        databaseReference.child(DonHang.Id).setValue(DonHang);
        Toast.makeText(getBaseContext(),"????n h??ng ???? ???????c chuy???n v??o l???ch s??? giao",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, OrderListActivity.class));
    }


    private void ViewMap()
    {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("lat",DonHang.KhachHang.KinhDo);
        intent.putExtra("lng",DonHang.KhachHang.ViDo);
        startActivity(intent);
    }

    private void CancelOrder()
    {
        DonHang.Shipper = null;
        DonHang.TrangThaiGiao = 0;
        databaseReference.child(DonHang.Id).child("Shipper").setValue(null);
        databaseReference.child(DonHang.Id).child("TrangThaiGiao").setValue(0);
        startActivity(new Intent(this, OrderListActivity.class));
    }
}
