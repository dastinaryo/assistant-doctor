package com.example.projecttugasbesarardi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "biodata_pasien.db";
    private static final int DATABASE_VERSION = 1;
    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql = "create table biodata(no integer primary key autoincrement, nik text null, nama text null, tgl_lahir text null, jk text null, alamat text null, diagnosa text null, verifikasi text null);";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);
        sql = "INSERT INTO biodata (nik, nama, tgl_lahir, jk, alamat, diagnosa, verifikasi) VALUES ('3204230203040006', 'Dastin Aryo Atamanto', '28 September 2002', 'Laki-laki','Babakan Jati, Paseh', '-', 'Unverified');";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }


}
