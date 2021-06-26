package com.example.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bai2c5.R;
import com.example.model.NhanVien;
import com.example.model.PhongBan;

import java.util.ArrayList;

public class PhongBanAdapter extends ArrayAdapter<PhongBan> {
    ArrayList<NhanVien> nhanViens = new ArrayList<>();
    Activity context;
    int resource;
    public PhongBanAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View customview = this.context.getLayoutInflater().inflate(this.resource,null);
        TextView txtPhongBan = customview.findViewById(R.id.txtPhongBan);
        TextView txtNhanVien = customview.findViewById(R.id.txtNhanVien);
        PhongBan pb = getItem(position);
        txtPhongBan.setText(pb.toString()+"(Co "+pb.getNhanViens().size()+" nhan vien)");
        for(int i=0;i<pb.getNhanViens().size();i++)
        {
            NhanVien nhanVien = pb.getNhanViens().get(i);
            if(nhanVien.getChucVu().equals("Truong phong") || nhanVien.getChucVu().equals("Pho phong"))
            {
                nhanViens.add(nhanVien);
            }
        }
        String temp = "";
        for(int i=0;i<nhanViens.size();i++)
        {
            temp+= nhanViens.get(i).toString();
        }
        txtNhanVien.setText(temp);
        nhanViens.clear();
        return customview;
    }
}
