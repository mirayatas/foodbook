package com.example.deneme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button buttonlogin;
    private Button buttonregister;

    public void baslat() {
       buttonlogin = (Button) findViewById(R.id.btnlogin);
       buttonregister = (Button) findViewById(R.id.btnregister);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        baslat();

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginekranı = new Intent(MainActivity.this, Login.class);
                startActivity(loginekranı);
            }
        });

        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerekranı = new Intent(MainActivity.this, Register.class);
                startActivity(registerekranı);
            }
        });
    }

}