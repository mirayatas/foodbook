package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity {
    FirebaseAuth vt;
    private Toolbar appbar;

    ImageView begen;

    RecyclerView recyclerView;
    List<Tarif> tariflist;
    AllTarifler allTarifler;
    List <String> takiplistesi;
    DatabaseReference ref;



    public Home(){}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        begen=findViewById(R.id.likebutton);
        appbar=findViewById(R.id.appbarhome);
        setSupportActionBar(appbar);
        getSupportActionBar().setTitle("Foodbook");
        vt=FirebaseAuth.getInstance();

        ref= FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference("Tarifler");
        recyclerView=findViewById(R.id.homerv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        tariflist=new ArrayList<>();
        allTarifler=new AllTarifler(getApplicationContext(),tariflist);

        recyclerView.setAdapter(allTarifler);

        takipkontrol();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_design,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.tariflerim:
                Intent tarif=new Intent(Home.this,Tariflerim.class);
                startActivity(tarif);
                break;

            case R.id.cikis:
                vt.signOut();
                Intent login=new Intent(Home.this,Login.class);
                startActivity(login);
                finish();
                break;
            case R.id.ara:
                Intent ara= new Intent(Home.this, UserSearch.class);
                startActivity(ara);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void takipkontrol () {

        takiplistesi=new ArrayList<>();

        DatabaseReference takipyolu=FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference("Takip")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("takipEdilenler");
        takipyolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                takiplistesi.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    takiplistesi.add(snapshot.getKey());
                }
                gonderioku(takiplistesi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void gonderioku(List<String> takiplistesi) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference("Tarifler");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tariflist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Tarif tarif = snapshot1.getValue(Tarif.class);
                        tarif.setUserid(snapshot.getKey());
                        for (String a : takiplistesi) {
                            if (tarif.getUserid().equals(a)) {

                                Map<String, String> hm = (Map<String, String>) snapshot1.getValue();
                                String baslik = hm.get("Baslik");
                                String mesagge = hm.get("message");
                                String messageid = hm.get("messageid");
                                String username = hm.get("username");
                                tarif.setUsername(username);
                                tarif.setBaslik(baslik);
                                tarif.setMessageid(messageid);
                                tarif.setMessage(mesagge);
                                tariflist.add(tarif);

                            }
                        }
                        allTarifler.notifyDataSetChanged();
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}