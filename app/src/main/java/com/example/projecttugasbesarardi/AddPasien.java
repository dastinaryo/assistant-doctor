package com.example.projecttugasbesarardi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPasien extends AppCompatActivity {
    protected Cursor cursor;
    DataHelper dbHelper;
    EditText text1, text2, text3, text4, text5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pasien);

        dbHelper = new DataHelper(this);
        text1 = (EditText) findViewById(R.id.idnik);
        text2 = (EditText) findViewById(R.id.idnama);
        text3 = (EditText) findViewById(R.id.idjk);
        text4 = (EditText) findViewById(R.id.idttl);
        text5 = (EditText) findViewById(R.id.idalamat);
        String diagnosa = "Belum ada";
        String verifikasi = "unverified";

        Button btn3 = findViewById(R.id.buttonBack);
        Button btn4 = findViewById(R.id.buttonAddPasien);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("insert into biodata(nik, nama, tgl_lahir, jk, alamat, diagnosa, verifikasi) values('" +
                        text1.getText().toString() + "','" +
                        text2.getText().toString() + "','" +
                        text3.getText().toString() + "','" +
                        text4.getText().toString() + "','" +
                        text5.getText().toString() + "','" +
                        diagnosa + "','" +
                        verifikasi + "')" );
                Toast.makeText(getApplicationContext(), "Berhasil Tambah Pasien", Toast.LENGTH_LONG).show();
                Dashboard.ma.RefreshList();
                finish();
            }
        });

    }
}
