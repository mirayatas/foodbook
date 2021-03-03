package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tariflerim extends AppCompatActivity {
    private Toolbar appbar;
    private FirebaseAuth vt;
    private DatabaseReference reference;
    private FirebaseUser user;
    ImageView begen;
    TextView hosgeldin,username;
    Button ekle;
    RecyclerView recyclerView;
    List<Tarif> tariflist;
    AllTarifler allTarifler;
    List <String> takiplistesi;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tariflerim);
        baslat();
    }

    private void baslat() {
        appbar=findViewById(R.id.appbartariflerim);
        setSupportActionBar(appbar);
        getSupportActionBar().setTitle("Tariflerim");
        begen=findViewById(R.id.likebutton);

        vt=FirebaseAuth.getInstance();
        user=vt.getCurrentUser();
        reference= FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference().child("Users");

        hosgeldin=findViewById(R.id.hosgeldin);
        username=findViewById(R.id.usrnm);

        Query qry = reference.orderByChild("mail").equalTo(user.getEmail());
        qry.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ss: dataSnapshot.getChildren()) {
                    String kulladi = "" + ss.child("Username").getValue();
                    username.setText(kulladi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ref= FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference("Tarifler");
        recyclerView=findViewById(R.id.tarifrv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        tariflist=new ArrayList<>();
        allTarifler=new AllTarifler(getApplicationContext(),tariflist);

        recyclerView.setAdapter(allTarifler);

        takiplistesi=new ArrayList<>();
        takipkontrol();

        ekle=findViewById(R.id.tarifekle);
        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addekranı = new Intent(Tariflerim.this,Add.class);
                startActivity(addekranı);
                Tariflerim.this.finish();
            }
        });

    }

    private void gonderioku(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference("Tarifler");
        DatabaseReference classicalMechanicsRef = rootRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Query query = classicalMechanicsRef.orderByChild("userid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    tariflist.add(new Tarif(
                            ds.child("Baslik").getValue(String.class),
                            ds.child("message").getValue(String.class),
                            ds.child("userid").getValue(String.class),
                            ds.child("username").getValue(String.class),
                            ds.child("messageid").getValue(String.class)
                    ));
                    //Log.d("CIKTI10", ds.child("message").getValue(String.class);
                }
                allTarifler.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("CIKTI10", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    private void takipkontrol(){
        takiplistesi=new ArrayList<>();
        DatabaseReference takipyolu=FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference("Tarifler").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        takipyolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                takiplistesi.clear();
                for (DataSnapshot ss:dataSnapshot.getChildren()){
                    takiplistesi.add(ss.getKey());
                }
                gonderioku();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}