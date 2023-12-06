package com.example.projecttugasbesarardi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.Manifest;
import java.io.IOException;

public class diagnosa extends AppCompatActivity {
    protected Cursor cursor;
    DataHelper dbHelper;
    Button pilihFile, camera, verifyButton, btnBack;

    ImageView imageView;

    TextView outputDiagnose;

    public static final int PICK_IMAGE = 1;

    int imageSize = 224;

    boolean pred = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosa);

        pilihFile = findViewById(R.id.pilihFile);
        camera = findViewById(R.id.kamera);
        imageView = findViewById(R.id.imageResult);
        outputDiagnose = findViewById(R.id.resultDiagnose);
        verifyButton = findViewById(R.id.verifikasiButton);
        btnBack = findViewById(R.id.buttonBack3);

//        String verifikasi = "Verified";

        dbHelper = new DataHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        cursor = db.rawQuery("SELECT * FROM biodata WHERE no = '" +
                Integer.parseInt(getIntent().getStringExtra("no_id")) + "'",null);
        cursor.moveToFirst();
        
        if (cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            outputDiagnose.setText(cursor.getString(6).toString());
        }

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pred){
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("update biodata set " +
                            "diagnosa ='"+ outputDiagnose.getText().toString() +"', " +
                            "verifikasi='"+ "Verified" + "' " +
                            "where no='" + Integer.parseInt(getIntent().getStringExtra("no_id")) +"'");
                    Toast.makeText(getApplicationContext(), "Dianognosa Berhasil", Toast.LENGTH_LONG).show();
                    ViewPasien.vp.Refresh();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Anda Belum Melakukan Diagnosa", Toast.LENGTH_LONG).show();
                }
            }
        });
        
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent move = new Intent(diagnosa.this,ViewPasien.class);
//                move.putExtra("no_id", getIntent().getStringExtra("no_id"));
//                startActivity(move);
                finish();
            }
        });

        pilihFile.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        camera.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 3);
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ClassifyImage classifier = new ClassifyImage();
        float prediction;

        if (resultCode == RESULT_OK) {
            // compare the resultCode with the

            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

                prediction = classifier.classifyImage(image, getApplicationContext());

                String predictionResult = "Classified as ";
                if (prediction > 0){
                    if (prediction > 0.7) {
                        predictionResult += "tumor";
                    } else {
                        predictionResult += "non-tumor";
                    }
                }
                pred=true;
                outputDiagnose.setText(predictionResult);
            }else{
                // SELECT_PICTURE constant
                if (requestCode == PICK_IMAGE) {
                    // Get the url of the image from data
                    Uri selectedImageUri = data.getData();
                    Bitmap image = null;

                    if (null != selectedImageUri) {
                        try {
                            image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // update the preview image in the layout
                        imageView.setImageBitmap(image);
                        prediction = classifier.classifyImage(image, getApplicationContext());

                        String predictionResult = "Classified as ";
                        if (prediction > 0){
                            if (prediction > 0.7) {
                                predictionResult += "tumor ";
                            } else {
                                predictionResult += "non-tumor ";
                            }
                        }
                        pred=true;
                        outputDiagnose.setText(predictionResult);
                    }
                }
            }
        }
    }
}