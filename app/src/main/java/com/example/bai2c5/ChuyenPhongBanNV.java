package com.example.bai2c5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.model.PhongBan;

import java.util.ArrayList;

public class ChuyenPhongBanNV extends AppCompatActivity {
    ListView lvPhongBanChuyen;
    ArrayAdapter<PhongBan> pbAdapter;
    Button btnChuyen;
    ArrayList<PhongBan> pbList = new ArrayList<>();
    PhongBan selectedPB = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuyen_phong_ban_n_v);
        addControls();
        addEvents();
    }

    private void addEvents() {
        lvPhongBanChuyen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPB = pbAdapter.getItem(position);
            }
        });
        lvPhongBanChuyen.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPB = pbAdapter.getItem(position);
                return false;
            }
        });
        btnChuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuLyChuyenNV();
            }
        });
    }

    private void XuLyChuyenNV() {
        ContentValues contentValues = new ContentValues();
        String newMaPB_NV = selectedPB.getMaPB();
        contentValues.put("MaPB",newMaPB_NV);
        int result = MainActivity.database.update("NhanVien",contentValues,
                "MaNV=?",new String[]{String.valueOf(XemDanhSachNhanVien.selectedNhanVien.getMaNV())});
        if(result>0)
        {
            Toast.makeText(ChuyenPhongBanNV.this,"Chuyen thanh cong",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(ChuyenPhongBanNV.this,"Chuyen that bai",Toast.LENGTH_LONG).show();
        };
        Intent intent = getIntent();
        String maPB = intent.getStringExtra("maphongban");
        String tenPB = intent.getStringExtra("tenphongban");
        Intent intent1 = new Intent(ChuyenPhongBanNV.this,MainActivity.class);
        intent1.putExtra("MaPB",maPB);
        intent1.putExtra("TenPB",tenPB);
        startActivity(intent1);
    }


    private void addControls() {
        lvPhongBanChuyen = findViewById(R.id.lvPhongBanChuyen);
        pbAdapter = new ArrayAdapter<>(ChuyenPhongBanNV.this, android.R.layout.simple_list_item_single_choice);
        lvPhongBanChuyen.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvPhongBanChuyen.setAdapter(pbAdapter);
        btnChuyen = findViewById(R.id.btnChuyen);

    }
    private void hienthiDanhSachPhongBan()
    {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor = MainActivity.database.rawQuery("select * from PhongBan",null);
        pbAdapter.clear();
        while (cursor.moveToNext())
        {
            String maPB = cursor.getString(0);
            String tenPB = cursor.getString(1);
            PhongBan phongBan = new PhongBan(maPB,tenPB);
            pbAdapter.add(phongBan);
        }
        cursor.close();
    }


    @Override
    protected void onResume() {
        super.onResume();
        hienthiDanhSachPhongBan();
    }
}