package com.example.projecttugasbesarardi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn1;
    EditText usernameEdit, passwordEdit;
    public static MainActivity lg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEdit = findViewById(R.id.username);
        passwordEdit = findViewById(R.id.password);

        btn1 = findViewById(R.id.loginButton);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usernameEdit.getText().toString().equals("") || passwordEdit.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Username dan Password Tidak Boleh Kosong!", Toast.LENGTH_LONG).show();
                }else{
                    if (usernameEdit.getText().toString().equals("asdok") && passwordEdit.getText().toString().equals("asdok")) {
                        Intent move = new Intent(MainActivity.this,Dashboard.class);
                        startActivity(move);
                    }else{
                        Toast.makeText(getApplicationContext(), "Username atau Password Salah!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        lg = this;
        clear();
    }

    public void clear(){
        usernameEdit.setText("");
        passwordEdit.setText("");
    }
}