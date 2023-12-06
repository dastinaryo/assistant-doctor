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

public class UpdatePasien extends AppCompatActivity {
    protected Cursor cursor;
    DataHelper dbHelper;
    Button ton1, ton2;
    EditText text1, text2, text3, text4, text5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pasien);

        dbHelper = new DataHelper(this);
        text1 = (EditText) findViewById(R.id.idnikUpdate);
        text2 = (EditText) findViewById(R.id.idnamaUpdate);
        text3 = (EditText) findViewById(R.id.idjkUpdate);
        text4 = (EditText) findViewById(R.id.idttlUpdate);
        text5 = (EditText) findViewById(R.id.idalamatUpdate);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM biodata WHERE no = '" +
                Integer.parseInt(getIntent().getStringExtra("no_id")) + "'",null);
        cursor.moveToFirst();

        if (cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            text1.setText(cursor.getString(1).toString());
            text2.setText(cursor.getString(2).toString());
            text3.setText(cursor.getString(3).toString());
            text4.setText(cursor.getString(4).toString());
            text5.setText(cursor.getString(5).toString());
        }

        Button btnBackUpdate = findViewById(R.id.buttonBackUpdate);
        btnBackUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        Button btnUpdate = findViewById(R.id.buttonUpdatePasien);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // TODO Auto-generated method stub
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("update biodata set " +
                        "nik='"+ text1.getText().toString() +"', " +
                        "nama='"+ text2.getText().toString() +"', " +
                        "jk='" + text3.getText().toString()+"', " +
                        "tgl_lahir='" + text4.getText().toString() +"', " +
                        "alamat='" + text5.getText().toString() + "' " +
                        "where no='" + Integer.parseInt(getIntent().getStringExtra("no_id")) +"'");
                Toast.makeText(getApplicationContext(), "Biodata berhasil di perbarui", Toast.LENGTH_LONG).show();
                Dashboard.ma.RefreshList();
                finish();
            }
        });
    }
}
