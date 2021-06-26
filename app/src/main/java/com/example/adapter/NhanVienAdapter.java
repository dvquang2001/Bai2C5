package com.example.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bai2c5.R;
import com.example.model.NhanVien;

public class NhanVienAdapter extends ArrayAdapter<NhanVien> {
    Activity context;
    int resource;
    public NhanVienAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View customView = this.context.getLayoutInflater().inflate(this.resource,null);
        TextView txtTenMa = customView.findViewById(R.id.txtMaTen);
        TextView txtChucVu = customView.findViewById(R.id.txtChucVu);
        ImageView imgGioiTinh = customView.findViewById(R.id.imgGioiTinh);
        NhanVien nhanVien = getItem(position);
        txtTenMa.setText(nhanVien.getMaNV()+" - "+nhanVien.getTenNV());
        txtChucVu.setText("Chuc vu: "+nhanVien.getChucVu()+"\n"+"Gioi tinh: "+nhanVien.getGioiTinh());
        if(nhanVien.getGioiTinh().equals("Nam"))
        {
            imgGioiTinh.setImageResource(R.drawable.man);
        }
        else if(nhanVien.getGioiTinh().equals("Nu"))
        {
            imgGioiTinh.setImageResource(R.drawable.woman);
        }
        return customView;
    }
}
