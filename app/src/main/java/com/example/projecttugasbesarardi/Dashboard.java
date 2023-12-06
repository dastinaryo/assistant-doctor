package com.example.projecttugasbesarardi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    String[] daftar;

    String[] no_id;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    ImageView logoutBtn;
    public static Dashboard ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        Button btn2 = findViewById(R.id.buttonAdd);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent move = new Intent(Dashboard.this,AddPasien.class);
                startActivity(move);
            }
        });

        logoutBtn = findViewById(R.id.logout);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                MainActivity.lg.clear();
                finish();
            }
        });

        ma = this;
        dbcenter = new DataHelper(this);
        RefreshList();
    }

    public void RefreshList(){
        SQLiteDatabase db = dbcenter.getReadableDatabase();
//        String sql = "INSERT INTO biodata (nik, nama, tgl_lahir, jk, alamat, diagnosa, verifikasi) VALUES ('3204230203040006', 'Dewi Permata', '55 September 2002', 'Laki-laki','Babakan Jati, Paseh', '-', 'Unverified');";
//        db.execSQL(sql);
        cursor = db.rawQuery("SELECT * FROM biodata ORDER BY nama ASC",null);
        daftar = new String[cursor.getCount()];
        no_id  = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(2).toString(); // Nama
            no_id[cc] = cursor.getString(0).toString();
        }
        ListView01 = (ListView)findViewById(R.id.listPasien);
        List<String> data = getData();
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, data);
        ListView01.setAdapter(adapter);
//        ListView01.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = no_id[arg2]; //.getItemAtPosition(arg2).toString(); //NIK
                final CharSequence[] dialogitem = {"Lihat Pasien", "Edit Biodata", "Hapus Pasien"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch(item){
                            case 0 :
                                Intent i = new Intent(getApplicationContext(), ViewPasien.class);
                                i.putExtra("no_id", selection);
                                startActivity(i);
                                break;
                            case 1 :
                                Intent in = new Intent(getApplicationContext(), UpdatePasien.class);
                                in.putExtra("no_id", selection);
                                startActivity(in);
                                break;
                            case 2 :
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("delete from biodata where no = '"+Integer.parseInt(selection)+"'");
                                RefreshList();
                                break;
                        }
                    }
                });
                builder.create().show();
            }});
        ((ArrayAdapter)ListView01.getAdapter()).notifyDataSetInvalidated();
    }

    private List<String> getData() {
        List<String> dataList = new ArrayList<>();
        for (int cc=0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            dataList.add(cursor.getString(2).toString());
        }
        return dataList;
    }

}
