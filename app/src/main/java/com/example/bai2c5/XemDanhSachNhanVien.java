package com.example.bai2c5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.NhanVienAdapter;
import com.example.model.NhanVien;
import com.example.model.PhongBan;

import java.util.ArrayList;

public class XemDanhSachNhanVien extends AppCompatActivity {
    TextView txtTieuDe;
    ListView lvNhanVien;
    NhanVienAdapter nhanVienAdapter;
    public  static NhanVien selectedNhanVien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_danh_sach_nhan_vien);
        addControls();
        addEvents();
    }

    private void addEvents() {
        lvNhanVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNhanVien = nhanVienAdapter.getItem(position);
            }
        });
        lvNhanVien.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNhanVien = nhanVienAdapter.getItem(position);
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_for_nv,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuSuaNV:
                SuaThongTinNhanVien();
                break;
            case R.id.menuChuyenPB:
                ChuyenPhongBanChoNhanVien();
                break;
            case R.id.menuXoaNV:
                XuLyXoaNV();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void XuLyXoaNV() {
        Dialog dialog = new Dialog(XemDanhSachNhanVien.this);
       dialog.setTitle("Xoa nhan vien");
       dialog.setContentView(R.layout.xoa_nv);

       TextView txtXoa = dialog.findViewById(R.id.txtXoa);
       Button btnXoaNV = dialog.findViewById(R.id.btnXoaNV);
       Button btnKhongXoaNV = dialog.findViewById(R.id.btnKhongXoaNV);

       txtXoa.setText("Ban co chac chan muon xoa "+selectedNhanVien.toString());
       btnKhongXoaNV.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });
       btnXoaNV.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               nhanVienAdapter.remove(selectedNhanVien);
               int result = MainActivity.database.delete("NhanVien","MaNV=?",
                       new String[]{String.valueOf(selectedNhanVien.getMaNV())});
               if(result>0)
                   Toast.makeText(XemDanhSachNhanVien.this,"Xoa thanh cong",Toast.LENGTH_LONG).show();
               else
                   Toast.makeText(XemDanhSachNhanVien.this,"Xoa that bai",Toast.LENGTH_LONG).show();
           }
       });
       dialog.show();

    }

    private void ChuyenPhongBanChoNhanVien() {
        Intent intent = getIntent();
        String maPB = intent.getStringExtra("maphongban");
        String tenPB = intent.getStringExtra("tenphongban");
        Intent intent1 = new Intent(XemDanhSachNhanVien.this,ChuyenPhongBanNV.class);
        intent1.putExtra("MaPB",maPB);
        intent1.putExtra("TenPB",tenPB);
        startActivity(intent1);
    }

    private void SuaThongTinNhanVien() {
        Dialog dialog = new Dialog(XemDanhSachNhanVien.this);
        dialog.setContentView(R.layout.them_nv);
        dialog.setTitle("Chinh sua thong tin nhan vien");
        dialog.setCanceledOnTouchOutside(false);

        EditText edtMaNV = dialog.findViewById(R.id.edtMaNV);
        EditText edtTenNV = dialog.findViewById(R.id.edtTenNV);
        RadioButton radNam = dialog.findViewById(R.id.radNam);
        RadioButton radNu = dialog.findViewById(R.id.radNu);
        Button btnLuuNV = dialog.findViewById(R.id.btnLuuNV);
        Button btnXoaTrangNV = dialog.findViewById(R.id.btnXoaTrangNV);

        edtMaNV.setText(String.valueOf(selectedNhanVien.getMaNV()));
        edtMaNV.setEnabled(false);
        edtTenNV.setText(selectedNhanVien.getTenNV());
        if(selectedNhanVien.getGioiTinh().equals("Nam"))
            radNam.setChecked(true);
        else if(selectedNhanVien.getGioiTinh().equals("Nu"))
            radNu.setChecked(true);

        btnXoaTrangNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radNam.setChecked(false);
                radNu.setChecked(false);
                edtTenNV.setText("");
                edtTenNV.requestFocus();
            }
        });
        btnLuuNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMaNV = edtMaNV.getText().toString();
                String newTenNV = edtTenNV.getText().toString();
                String defaultNV = "Nhan vien";
                String gioitinh = "";
                if(radNam.isChecked())
                    gioitinh+="Nam";
                else if(radNu.isChecked())
                    gioitinh+="Nu";
                Intent intent = getIntent();
                String maPB = intent.getStringExtra("maphongban");
                String tenPB = intent.getStringExtra("tenphongban");
                String maPB_NV = maPB;

                ContentValues values = new ContentValues();
                values.put("MaNV",newMaNV);
                values.put("TenNV",newTenNV);
                values.put("GioiTinh",gioitinh);
                values.put("ChucVu",defaultNV);
                values.put("MaPB",maPB_NV);

                int result = MainActivity.database.update("NhanVien",values,
                        "MaNV=?",new String[]{edtMaNV.getText().toString()});
                if(result>0)
                {
                    Toast.makeText(XemDanhSachNhanVien.this,"Cap nhap thanh cong",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(XemDanhSachNhanVien.this,"Cap nhat that bai",Toast.LENGTH_LONG).show();
                }
                hienthiDanhsach();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addControls() {
        txtTieuDe = findViewById(R.id.txtTieuDe);
        lvNhanVien = findViewById(R.id.lvNhanVien);
        nhanVienAdapter = new NhanVienAdapter(XemDanhSachNhanVien.this,R.layout.item_nv);
        lvNhanVien.setAdapter(nhanVienAdapter);
        registerForContextMenu(lvNhanVien);
    }
    private void hienthiDanhsach() {
        MainActivity.database = openOrCreateDatabase(MainActivity.DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor = MainActivity.database.rawQuery("select * from NhanVien",null);
        nhanVienAdapter.clear();
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
                nhanVienAdapter.add(nhanVien);
            }
            txtTieuDe.setText("Danh sach nhan vien phong "+tenPB);
        }
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hienthiDanhsach();
    }
}