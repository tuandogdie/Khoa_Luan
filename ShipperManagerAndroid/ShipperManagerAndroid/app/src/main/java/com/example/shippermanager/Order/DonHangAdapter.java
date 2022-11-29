package com.example.shippermanager.Order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shippermanager.Model.DonHang;
import com.example.shippermanager.Model.HelperUtils;
import com.example.shippermanager.Model.Shipper;
import com.example.shippermanager.Model.TrangThaiDonHang;
import com.example.shippermanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DonHang> list;
    private ArrayList<DonHang> listfiltered;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public DonHangAdapter(Context context, ArrayList<DonHang> list) {
        this.context = context;
        this.list = list;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("DonHang");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Set layout cho adapter để hiển thị lên list
        View view = LayoutInflater.from(context).inflate(R.layout.view_don_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //money format
        NumberFormat format = HelperUtils.GetNumberFormat();
        //Trả về 1 item tại vị trí position(vị trí hiện tại theo list)
        DonHang dh = list.get(position);
        if(dh.TrangThaiGiao == TrangThaiDonHang.DangTim.ordinal())
            holder.txtTrangThai.setText("đang tìm");
        else if(dh.TrangThaiGiao == TrangThaiDonHang.DangGiao.ordinal())
            holder.txtTrangThai.setText("đã nhận");
        else
            holder.txtTrangThai.setText("đã giao");
        holder.txtLoTrinh.setText(String.valueOf(dh.KhoanCach) + "km");
        holder.txtPhiShip.setText(format.format(dh.PhiGiaoHang));
        holder.txtDiaChi.setText(dh.KhachHang.DiaChi);
        holder.txtTongDon.setText(format.format(dh.TongTien));

        holder.view.setOnClickListener(v -> {
            if(dh.TrangThaiGiao == TrangThaiDonHang.DangTim.ordinal()){
                new AlertDialog.Builder(context)
                        .setTitle("Nhận Đơn Hàng")
                        .setMessage("Bạn có muốn nhận đơn hàng này?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            //get current shipper account
                            String json = HelperUtils.GetSharedObject(context,"Shipper");
                            Gson gson = new Gson();
                            Shipper obj = gson.fromJson(json, Shipper.class);
                            //add shipper into order
                            dh.Shipper = obj;
                            dh.TrangThaiGiao = 1;
                            addDataToFirebase(dh);
                            holder.txtTrangThai.setText("đã nhận");
                            Intent intent = new Intent(context, ShowOrderActivity.class);
                            intent.putExtra("MaDonHang",dh.Id);
                            context.startActivity(intent);
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
            else if(dh.TrangThaiGiao == TrangThaiDonHang.DangGiao.ordinal()) {
                Intent intent = new Intent(context, ShowOrderActivity.class);
                intent.putExtra("MaDonHang",dh.Id);
                context.startActivity(intent);
            }
            else {
                Intent intent = new Intent(context, ShowOrderActivity.class);
                intent.putExtra("MaDonHang", dh.Id);
                context.startActivity(intent);
            }


        });
    }

    private void addDataToFirebase(DonHang donHang) {
        databaseReference.child(donHang.Id).child("TrangThaiGiao").setValue(1);
        databaseReference.child(donHang.Id).child("Shipper").setValue(donHang.Shipper);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtLoTrinh, txtPhiShip, txtTongDon, txtDiaChi, txtTrangThai;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Khai báo id theo itemView
            txtLoTrinh = itemView.findViewById(R.id.txtLoTrinh);
            txtPhiShip = itemView.findViewById(R.id.txtPhiShip);
            txtTongDon = itemView.findViewById(R.id.txtTongDon);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChi);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);
            view = itemView;
        }
    }
    public void filterList(ArrayList<DonHang> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        list = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
}
