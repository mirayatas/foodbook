package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Add extends AppCompatActivity {
    private Toolbar addappbar;
    public EditText add,baslik;
    public Button kaydet;
    private FirebaseAuth vt;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        baslat();
    }

    public void baslat(){
       addappbar=(Toolbar) findViewById(R.id.appbaradd);
       baslik=findViewById(R.id.baslik);
       add=findViewById(R.id.add);
       kaydet=findViewById(R.id.kaydet);
       setSupportActionBar(addappbar);
       getSupportActionBar().setTitle("Tarif Ekle");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        vt=FirebaseAuth.getInstance();
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Postekle();
            }
        });
    }

    public void Postekle(){

        String Baslik=baslik.getText().toString();
        String Message=add.getText().toString();


        if(Message.isEmpty() || Baslik.isEmpty())
        {
            Toast.makeText(this,"Lütfen boş alanları doldurunuz...", Toast.LENGTH_LONG).show();
        }

        else {
            HashMap<Object, Object> hm=new HashMap<>();
        FirebaseUser user=vt.getCurrentUser();
        DatabaseReference konum2=FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference("Users").child(user.getUid()).child("Username");
        konum2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username=dataSnapshot.getValue().toString();

                String uid = user.getUid();


                reference = FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference().child("Tarifler").child(user.getUid());
                DatabaseReference konum=reference.push();
                String messageid=konum.getKey();
                hm.put("Baslik",Baslik);
                hm.put("message",Message);
                hm.put("messageid",messageid);
                hm.put("userid",uid);
                hm.put("username",username);


                konum.setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent profil = new Intent(Add.this, Tariflerim.class);
                            startActivity(profil);
                            finish();
                            baslik.setText("");
                            add.setText("");


                        }
                        else {
                            Toast.makeText(Add.this, "Bir hata oluştu", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}}