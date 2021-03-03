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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private FirebaseAuth vt;
    private Toolbar appbar;
    private DatabaseReference reference;
    EditText email;
    EditText pass;
    EditText username;
    Button register;

    public void baslat(){
        appbar=(Toolbar) findViewById(R.id.appbarregister);
        setSupportActionBar(appbar);
        getSupportActionBar().setTitle("Kaıt Ol");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vt=FirebaseAuth.getInstance();

        email=findViewById(R.id.registeremail);
        pass=findViewById(R.id.registerpass);
        username=findViewById(R.id.registerusername);
        register=findViewById(R.id.registerbutton);


    }

    private void saveduser (){
        String authemail = email.getText().toString();
        String authpass = pass.getText().toString();
        String authusername = username.getText().toString();



        if(authemail.isEmpty() || authpass.isEmpty() || authusername.isEmpty())
        {
            Toast.makeText(this,"Lütfen boş alanları doldurunuz...", Toast.LENGTH_LONG).show();
        }
        else {
            vt.createUserWithEmailAndPassword(authemail, authpass).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user=vt.getCurrentUser();
                            String authuid=user.getUid();
                            reference = FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference().child("Users").child(authuid);

                            HashMap<Object, String> hm=new HashMap<>();
                            hm.put("Username",authusername);
                            hm.put("mail",authemail);
                            hm.put("password",authpass);
                            hm.put("User Id",authuid);

                            reference.setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent r = new Intent(Register.this, Home.class);
                                        startActivity(r);
                                        finish();
                                    }
                                }
                            });

                        }
                        else {
                            Toast.makeText(Register.this, "Bir hata oluştu", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }


        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        baslat();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            saveduser();            }
        });

    }


}
