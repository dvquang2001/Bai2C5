package com.example.bai2c5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

import com.example.adapter.PhongBanAdapter;
import com.example.model.NhanVien;
import com.example.model.PhongBan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String DATABASE_NAME ="PhongBan.sqlite";
    public static String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;
    public static PhongBan selectedPB = null;

    ArrayList<NhanVien> list= new ArrayList<>();
    ListView lvPhongBan;
    EditText edtMaPB, edtTenPB;
    Button btnLuuPB;
    PhongBanAdapter phongBanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processCopy();
        addControls();
        addEvents();
    }

    private void addEvents() {
        lvPhongBan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPB = phongBanAdapter.getItem(position);
            }
        });
        lvPhongBan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPB = phongBanAdapter.getItem(position);
                return false;
            }
        });
        btnLuuPB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuLyLuuPB();
            }
        });
    }

    private void XuLyLuuPB() {
        String newMaPB = edtMaPB.getText().toString();
        String newTenPB = edtTenPB.getText().toString();

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.them_pb);
        dialog.setCanceledOnTouchOutside(false);

        TextView txtThemPB = dialog.findViewById(R.id.txtThemPB);
        Button btnDongY = dialog.findViewById(R.id.btnDongy);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        txtThemPB.setText("Ban muon tao them phong ban "+newTenPB+"?");

        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("MaPB",newMaPB);
                values.put("TenPB",newTenPB);
                database.insert("PhongBan",null,values);
                hienthiDanhsach();
                dialog.dismiss();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void addControls() {
       edtMaPB = findViewById(R.id.edtMaPB);
       edtTenPB = findViewById(R.id.edtTenPB);
       btnLuuPB = findViewById(R.id.btnLuuPB);
       lvPhongBan = findViewById(R.id.lvPhongBan);
        phongBanAdapter = new PhongBanAdapter(MainActivity.this,R.layout.item_pb);
        lvPhongBan.setAdapter(phongBanAdapter);
        registerForContextMenu(lvPhongBan);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_for_main,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuThemNV:
                XuLyThemNhanVien();
                break;
            case R.id.menuDanhsach:
                Intent intent = new Intent(MainActivity.this,XemDanhSachNhanVien.class);
                intent.putExtra("maphongban",selectedPB.getMaPB());
                intent.putExtra("tenphongban",selectedPB.getTenPB());
                startActivity(intent);
                break;
            case R.id.menuBauCu:
                XuLyBauCu();
                break;
            case R.id.menuXoa:
                XuLyXoaPhongBan();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void XuLyXoaPhongBan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("!!!!");
        builder.setIcon(android.R.color.holo_red_dark);
        builder.setMessage("Ban co chac chan muon xoa "+selectedPB.getTenPB()+"?");
        builder.setPositiveButton("Dong y", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int result = database.delete("PhongBan","MaPB=?",
                        new String[]{selectedPB.getMaPB()});
                if(result>0)
                    Toast.makeText(MainActivity.this,"Xoa thanh cong",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this,"Xoa that bai",Toast.LENGTH_LONG).show();
                hienthiDanhsach();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Huy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void XuLyBauCu() {
        Intent intent = new Intent(MainActivity.this,BauCu.class);
        intent.putExtra("maphongban",selectedPB.getMaPB());
        intent.putExtra("tenphongban",selectedPB.getTenPB());
        startActivity(intent);
    }

    private void XuLyThemNhanVien() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.them_nv);
        dialog.setTitle("Them nhan vien");
        dialog.setCanceledOnTouchOutside(false);

        EditText edtMaNV = dialog.findViewById(R.id.edtMaNV);
        EditText edtTenNV = dialog.findViewById(R.id.edtTenNV);
        RadioButton radNam = dialog.findViewById(R.id.radNam);
        RadioButton radNu = dialog.findViewById(R.id.radNu);
        Button btnLuuNV = dialog.findViewById(R.id.btnLuuNV);
        Button btnXoaTrangNV = dialog.findViewById(R.id.btnXoaTrangNV);

        btnXoaTrangNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtMaNV.setText("");
                edtTenNV.setText("");
                radNam.setChecked(false);
                radNu.setChecked(false);
                edtMaNV.requestFocus();
            }
        });
        btnLuuNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newMaNV = Integer.parseInt(edtMaNV.getText().toString());
                String newTenNV = edtTenNV.getText().toString();
                String defaultNV = "Nhan vien";
                String gioitinh = "";
                if(radNam.isChecked())
                    gioitinh+="Nam";
                else if(radNu.isChecked())
                    gioitinh+="Nu";
                String maPB_NV = selectedPB.getMaPB();

                ContentValues values = new ContentValues();
                values.put("MaNV",newMaNV);
                values.put("TenNV",newTenNV);
                values.put("GioiTinh",gioitinh);
                values.put("ChucVu",defaultNV);
                values.put("MaPB",maPB_NV);

                long result = database.insert("NhanVien",null,values);
                if(result>0)
                {
                    Toast.makeText(MainActivity.this,"Them thanh cong",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Them that bai",Toast.LENGTH_LONG).show();
                }
                phongBanAdapter.clear();
                hienthiDanhsach();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void hienthiDanhsach() {
        database = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("select * from PhongBan",null);
        Cursor cursor1 = database.rawQuery("select * from NhanVien",null);
        list.clear();
        phongBanAdapter.clear();
        while (cursor1.moveToNext())
        {
            int MaNV = cursor1.getInt(0);
            String TenNV = cursor1.getString(1);
            String GioiTinh = cursor1.getString(2);
            String ChucVu = cursor1.getString(3);
            String MaPB_NV = cursor1.getString(4);
            NhanVien nhanVien = new NhanVien(MaNV,TenNV,GioiTinh,ChucVu,MaPB_NV);
            list.add(nhanVien);
        }
        while (cursor.moveToNext())
        {
            String MaPB = cursor.getString(0);
            String TenPB = cursor.getString(1);
            PhongBan phongBan = new PhongBan(MaPB,TenPB);
            for(int i=0;i<list.size();i++)
            {
                if(list.get(i).getMaPB().equals(phongBan.getMaPB()))
                {
                    phongBan.getNhanViens().add(list.get(i));
                }
            }
            phongBanAdapter.add(phongBan);
        }
        cursor1.close();
        cursor.close();
    }
    private void processCopy()
    {
        try {
            File dbFile = getDatabasePath(DATABASE_NAME);
            if (!dbFile.exists()) {
                CopyDataBaseFromAsset();
                Toast.makeText(MainActivity.this,"Sao chep thanh cong",Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this,ex.toString(),Toast.LENGTH_LONG).show();
            Log.e("ERROR: ",ex.toString());
        }
    }
    private String getDataBasePath()
    {

        return getApplicationInfo().dataDir+DB_PATH_SUFFIX+DATABASE_NAME;
    }


    private void CopyDataBaseFromAsset() {
        try {
            InputStream myInput = getAssets().open(DATABASE_NAME);
            String outFileName = getDataBasePath();
            File f = new File(getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            if(!f.exists())
                f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte [] buffer = new byte[1024];
            int length;
            while((length = myInput.read(buffer))>0)
            {
                myOutput.write(buffer,0,length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception ex)
        {
            Log.e("EROOR: ",ex.toString());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        hienthiDanhsach();
    }
}