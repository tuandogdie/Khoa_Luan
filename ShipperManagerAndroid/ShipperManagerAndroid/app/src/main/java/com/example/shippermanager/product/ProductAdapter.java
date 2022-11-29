package com.example.shippermanager.product;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shippermanager.Model.ChiTietDonHang;
import com.example.shippermanager.Model.DonHang;
import com.example.shippermanager.Model.HelperUtils;
import com.example.shippermanager.Model.SanPham;
import com.example.shippermanager.Order.DonHangAdapter;
import com.example.shippermanager.Order.ShowOrderActivity;
import com.example.shippermanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ChiTietDonHang> list;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public ProductAdapter(Context context, ArrayList<ChiTietDonHang> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Set layout cho adapter để hiển thị lên list
        View view = LayoutInflater.from(context).inflate(R.layout.view_product, parent, false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        //money format
        NumberFormat format = HelperUtils.GetNumberFormat();
        //Trả về 1 item tại vị trí position(vị trí hiện tại theo list)
        ChiTietDonHang sp = list.get(position);
        holder.txtProductName.setText(sp.SanPham.Ten);
        holder.txtProductPrice.setText("Giá: " + format.format(sp.SanPham.Gia));
        holder.txtProductQuantity.setText("Số Lượng: " + String.valueOf(sp.SoLuong));
        holder.txtSumOrderDetail.setText("Tổng Tiền: " + format.format(sp.GetMoneySum()));
        StorageReference photoReference= FirebaseStorage.getInstance().getReferenceFromUrl(sp.SanPham.ImagePath);
        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.productImage.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });

//        holder.view.setOnClickListener(v -> {
//            Intent intent = new Intent(context, ShowOrderActivity.class);
//            intent.putExtra("MaDonHang", dh.Id);
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductPrice, txtProductQuantity,txtSumOrderDetail;
        View view;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Khai báo id theo itemView
            txtProductName = itemView.findViewById(R.id.productName);
            txtProductPrice = itemView.findViewById(R.id.productPrice);
            txtProductQuantity = itemView.findViewById(R.id.productQuantity);
            txtSumOrderDetail = itemView.findViewById(R.id.sumOrderDetail);
            productImage = itemView.findViewById(R.id.productImage);
            view = itemView;
        }
    }

}
