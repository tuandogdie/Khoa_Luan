package com.example.shippermanager.product;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shippermanager.Model.ChiTietDonHang;
import com.example.shippermanager.Model.DonHang;
import com.example.shippermanager.Model.Shipper;
import com.example.shippermanager.Model.TrangThaiDonHang;
import com.example.shippermanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private ArrayList<ChiTietDonHang> ProductList = new ArrayList<>();
    ProductAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setTitle("SẢN PHÂM CỦA ĐƠN HÀNG");
        init();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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

    private void init() {

        //Set layout recycle view
        recyclerView = findViewById(R.id.product_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //add order list to adapter
        adapter = new ProductAdapter(this, ProductList);
        recyclerView.setAdapter(adapter);
        //add data to list order
        Intent i = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String ma = extras.getString("MaDonHang");
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference ref = database.child("DonHang");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child: snapshot.getChildren()) {
                        DonHang dh = child.getValue(DonHang.class);
                        dh.Id = child.getKey();
                        if(dh.Id.equals(ma))
                        {
                            ProductList.addAll(dh.DanhSachCTTT);
                            adapter.notifyItemInserted(ProductList.size() -1);
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
}
