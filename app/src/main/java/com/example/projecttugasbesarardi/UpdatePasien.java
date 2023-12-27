package com.example.projecttugasbesarardi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class UpdatePasien extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    protected Cursor cursor;
    DataHelper dbHelper;
    Button ton1, ton2;
    EditText text1, text2, text3, text4, text5;

    String[] gender = { "Laki-Laki", "Perempuan"};

    int spinner_position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pasien);

        dbHelper = new DataHelper(this);
        text1 = (EditText) findViewById(R.id.idnikUpdate);
        text2 = (EditText) findViewById(R.id.idnamaUpdate);
//        text3 = (EditText) findViewById(R.id.idjkUpdate);
        Spinner spin = (Spinner) findViewById(R.id.idjkUpdate);
        spin.setOnItemSelectedListener(this);
        text4 = (EditText) findViewById(R.id.idttlUpdate);
        text5 = (EditText) findViewById(R.id.idalamatUpdate);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM biodata WHERE no = '" +
                Integer.parseInt(getIntent().getStringExtra("no_id")) + "'",null);
        cursor.moveToFirst();

        String myString = "Laki-Laki";

        if (cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            text1.setText(cursor.getString(1).toString());
            text2.setText(cursor.getString(2).toString());
//            text3.setText(cursor.getString(3).toString());
            myString = cursor.getString(4).toString();
            text4.setText(cursor.getString(3).toString());
            text5.setText(cursor.getString(5).toString());
        }

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,gender);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        spin.setSelection(Arrays.asList(gender).indexOf(myString));

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
                        "jk='" + gender[spinner_position].toString() +"', " +
                        "tgl_lahir='" + text4.getText().toString() +"', " +
                        "alamat='" + text5.getText().toString() + "' " +
                        "where no='" + Integer.parseInt(getIntent().getStringExtra("no_id")) +"'");
                Toast.makeText(getApplicationContext(), "Biodata berhasil di perbarui", Toast.LENGTH_LONG).show();
                Dashboard.ma.RefreshList();
                finish();
            }
        });
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        spinner_position = position;
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
