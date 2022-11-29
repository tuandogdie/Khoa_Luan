package com.example.shippermanager.Order;

import static com.example.shippermanager.R.menu.menu_search;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shippermanager.Model.DonHang;
import com.example.shippermanager.Model.HelperUtils;
import com.example.shippermanager.Model.LocationService;
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
import java.util.logging.Filter;

public class OrderListActivity extends AppCompatActivity {

    private ArrayList<DonHang> OrderList = new ArrayList<>();
    DonHangAdapter adapter;
    private RecyclerView recyclerView;
    private Shipper shipper;
    int LOCATION_REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        setTitle("TÌM ĐƠN HÀNG MỚI");
        init();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String json = HelperUtils.GetSharedObject(this,"Shipper");
        Gson gson = new Gson();
        shipper = gson.fromJson(json, Shipper.class);
//        new AlertDialog.Builder(this)
//                .setTitle("test")
//                .setMessage(
//                        "Đơn hàng thuộc kho "+shipper.Kho.Ten)
//
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Continue with delete operation
//                    }
//                })
//
//                // A null listener allows the button to dismiss the dialog and take no further action.
//                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView =(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Tìm địa chỉ");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<DonHang> filteredlist = new ArrayList<DonHang>();

        // running a for loop to compare elements.
        for (DonHang item : OrderList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.KhachHang.DiaChi.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.

        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
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

    private void GetOrderList()
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("DonHang");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    DonHang o = child.getValue(DonHang.class);
                    if(o.Kho.Id.equals(shipper.Kho.Id)) {
                        o.Id = child.getKey();
                        if (o.TrangThaiGiao == TrangThaiDonHang.DangTim.ordinal()) {
                            OrderList.add(o);
                            adapter.notifyItemInserted(OrderList.size() - 1);
                        } else if (o.TrangThaiGiao == TrangThaiDonHang.DangGiao.ordinal()) {
                            if (shipper.Id.equals(o.Shipper.Id)) {
                                OrderList.add(o);
                                adapter.notifyItemInserted(OrderList.size() - 1);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("error", "Failed to read value.", error.toException());
            }
        });
    }

    private void init() {

        //Set layout recycle view
        recyclerView = findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //add order list to adapter
        adapter = new DonHangAdapter(this, OrderList);
        recyclerView.setAdapter(adapter);

        //add data to list order
        GetOrderList();
    }

}

