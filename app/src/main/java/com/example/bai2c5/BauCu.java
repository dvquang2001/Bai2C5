package com.example.bai2c5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.model.NhanVien;
import com.example.model.PhongBan;

import java.util.ArrayList;
import java.util.List;

public class BauCu extends AppCompatActivity {
    ListView lvTruongPhong,lvPhoPhong;
    ArrayAdapter<NhanVien> ChonTPadapter;
    NhanVien selectedTruongPhong = null;
    ArrayAdapter<NhanVien> ChonPPadapter;
    Button btnXacNhan;
    ArrayList<NhanVien> listChonPP = new ArrayList<>();
    ArrayList<NhanVien> listTamThoi = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bau_cu);
        addControls();
        addEvents();
    }

    private void addEvents() {
        lvTruongPhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTruongPhong = ChonTPadapter.getItem(position);
                listChonPP.clear();
                lvPhoPhong.setEnabled(true);
            }
        });
        lvTruongPhong.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTruongPhong = ChonTPadapter.getItem(position);
                lvPhoPhong.setEnabled(true);
                listChonPP.clear();
                return false;
            }
        });
        lvPhoPhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NhanVien nhanVien = ChonPPadapter.getItem(position);
                if(nhanVien.getMaNV() == selectedTruongPhong.getMaNV())
                {
                    view.setEnabled(false);
                }
                else {
                    view.setEnabled(true);
                }
                if(!listChonPP.contains(nhanVien))
                    listChonPP.add(nhanVien);
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuLyXacNhan();
            }
        });
    }

    private void XuLyXacNhan() {
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();
        values.put("ChucVu","Nhan vien");
        values1.put("ChucVu","Pho phong");
        values2.put("ChucVu","Truong phong");
        int kq =0;
        int kqTP = 0;
        int kqPP = 0;
        for(int i=0;i<listChonPP.size();i++)
        {
            listTamThoi.remove(listChonPP.get(i));
             kqPP = MainActivity.database.update("NhanVien",values1,
                    "MaNV=?",new String[]{String.valueOf(listChonPP.get(i).getMaNV())});
        }
        for(int i=0;i<listTamThoi.size();i++)
        {
            if(listTamThoi.get(i).getMaNV() == selectedTruongPhong.getMaNV())
            {
                kqTP =   MainActivity.database.update("NhanVien",values2,
                        "MaNV=?",new String[]{String.valueOf(listTamThoi.get(i).getMaNV())});
            }
            else
            {
                kq = MainActivity.database.update("NhanVien",values,
                        "MaNV=?",new String[]{String.valueOf(listTamThoi.get(i).getMaNV())});
            }
        }
        if(kq>0 && kqPP>0 || kqTP>0)
            Toast.makeText(BauCu.this,"Cap nhat thanh cong",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(BauCu.this,"Cap nhat that bai",Toast.LENGTH_LONG).show();

    }

    private void addControls() {
        lvTruongPhong = findViewById(R.id.lvTruongPhong);
        ChonTPadapter= new ArrayAdapter<>(BauCu.this, android.R.layout.simple_list_item_single_choice);
        lvTruongPhong.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvTruongPhong.setAdapter(ChonTPadapter);
        lvPhoPhong = findViewById(R.id.lvPhoPhong);
        ChonPPadapter = new ArrayAdapter<>(BauCu.this, android.R.layout.simple_list_item_multiple_choice);
        lvPhoPhong.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvPhoPhong.setAdapter(ChonPPadapter);
        btnXacNhan = findViewById(R.id.btnXacNhan);
    }
    private void hienthiDanhsach() {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor = MainActivity.database.rawQuery("select * from NhanVien",null);
        ChonTPadapter.clear();
        ChonPPadapter.clear();
        listChonPP.clear();
        listTamThoi.clear();
        while (cursor.moveToNext())
        {
            int MaNV = cursor.getInt(0);
            String TenNV = cursor.getString(1);
            String GioiTinh = cursor.getString(2);
            String ChucVu = cursor.getString(3);
            String MaPB_NV = cursor.getString(4);
            NhanVien nhanVien = new NhanVien(MaNV,TenNV,GioiTinh,ChucVu,MaPB_NV);
            Intent intent = getIntent();
            String maPB = intent.getStringExtra("maphongban");
            String tenPB = intent.getStringExtra("tenphongban");
            if(nhanVien.getMaPB().equals(maPB))
            {
                listTamThoi.add(nhanVien);
                ChonPPadapter.add(nhanVien);
                ChonTPadapter.add(nhanVien);
            }
        }
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hienthiDanhsach();
        lvPhoPhong.setEnabled(false);
        listChonPP.clear();
    }
}