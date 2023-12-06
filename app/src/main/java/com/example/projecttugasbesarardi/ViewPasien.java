package com.example.projecttugasbesarardi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewPasien extends AppCompatActivity {
    protected Cursor cursor;
    DataHelper dbHelper;
    TextView text1, text2, text3, text4, text5, text6;
    public static ViewPasien vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lihat_pasien);

        Button btnBack = findViewById(R.id.buttonBack2);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        Button btnDiagnose = findViewById(R.id.diagnoseButton);
        btnDiagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(ViewPasien.this,diagnosa.class);
                move.putExtra("no_id", getIntent().getStringExtra("no_id"));
                startActivity(move);
            }
        });
        vp = this;
        dbHelper = new DataHelper(this);
        Refresh();
    }

    public void Refresh(){
        text1 = (TextView) findViewById(R.id.nama_pasien);
        text2 = (TextView) findViewById(R.id.nik_pasien);
        text3 = (TextView) findViewById(R.id.tgl_lahir_pasien);
        text4 = (TextView) findViewById(R.id.alamat_pasien);
        text5 = (TextView) findViewById(R.id.jk_pasien);
        text6 = (TextView) findViewById(R.id.diagnosa_pasien);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM biodata WHERE no = '" +
                Integer.parseInt(getIntent().getStringExtra("no_id")) + "'",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            text1.setText(cursor.getString(2).toString());
            text2.setText(cursor.getString(1).toString());
            text3.setText(cursor.getString(3).toString());
            text4.setText(cursor.getString(4).toString());
            text5.setText(cursor.getString(5).toString());
            text6.setText(cursor.getString(6).toString());
        }
    }

}