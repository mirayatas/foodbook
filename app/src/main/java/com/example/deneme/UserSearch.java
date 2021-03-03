package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserSearch extends AppCompatActivity {
    Toolbar toolbar;
    AllUsers allusers;
    List<Users> mkullaniciler;
    ImageView aramabtn;
    EditText aramabar;
    RecyclerView recyclerView;
    DatabaseReference firebase=FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search);

        toolbar=findViewById(R.id.search_toolbar);
        aramabtn=findViewById(R.id.search_icon);
        aramabar=findViewById(R.id.search);
        recyclerView=findViewById(R.id.list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mkullaniciler=new ArrayList<>();
        allusers=new AllUsers(getApplicationContext(),mkullaniciler);

        recyclerView.setAdapter(allusers);

        aramabar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            kullaniciara(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void kullaniciara(String s)
    {
        Query sorgu= firebase.orderByChild("Username").startAt(s).endAt(s+"\uf8ff");
        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mkullaniciler.clear();
                for(DataSnapshot ss:dataSnapshot.getChildren()){
                    Users kullanici=ss.getValue(Users.class);
                    Log.d("CIKTI", ss.getKey().toString());
                    kullanici.setUserid(ss.getKey());
                    mkullaniciler.add(kullanici);
                }

                allusers.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void kullanicioku(){

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(aramabar.getText().toString().equals("")){
                    mkullaniciler.clear();
                    for(DataSnapshot ss:dataSnapshot.getChildren()){
                        Users kullanici=ss.getValue(Users.class);
                        mkullaniciler.add(kullanici);
                    }
                    allusers.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}