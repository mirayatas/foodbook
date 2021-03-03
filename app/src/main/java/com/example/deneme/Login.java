package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    private FirebaseAuth vt;
    private DatabaseReference reference;
    private Toolbar appbar;
    EditText mail;
    EditText pass;
    Button login;

    public void baslat(){
        appbar=(Toolbar) findViewById(R.id.appbarlogin);
        setSupportActionBar(appbar);
        getSupportActionBar().setTitle("Giriş Yap");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vt=FirebaseAuth.getInstance();

        mail=findViewById(R.id.loginmail);
        pass=findViewById(R.id.loginpass);
        login=findViewById(R.id.loginbutton);


    }

    private void loginuser() {
        String email=mail.getText().toString();
        String passwrd=pass.getText().toString();
        FirebaseUser user=vt.getCurrentUser();


        if(email.isEmpty() || passwrd.isEmpty())
        {
            Toast.makeText(this,"Lütfen boş alanları doldurunuz...", Toast.LENGTH_LONG).show();
        }

        else {
        vt.signInWithEmailAndPassword(email,passwrd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    reference = FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference().child("Users").child(vt.getCurrentUser().getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Intent l= new Intent(Login.this, Home.class);
                            startActivity(l);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                else {
                    Toast.makeText(Login.this,"Bir hata oluştu",Toast.LENGTH_LONG).show();
                }
            }
            });
        }


        }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        baslat();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            loginuser();
            }
        });
    }

}