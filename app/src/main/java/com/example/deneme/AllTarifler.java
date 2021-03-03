package com.example.deneme;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class  AllTarifler extends RecyclerView.Adapter<AllTarifler.TVH>{



    public FirebaseAuth vt;
    public Context mcontext;
    public List<Tarif> mGonderis;


    private FirebaseUser mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    public AllTarifler(Context mcontext, List<Tarif> mtarifler) {
        this.mcontext = mcontext;
        this.mGonderis = mtarifler;
    }

    @NonNull
    @Override
    public TVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mcontext).inflate(R.layout.tarifprofili,parent,false);
        return new AllTarifler.TVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TVH holder, int position) {

        mevcutFirebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        Tarif tarif=mGonderis.get(position);


        gonderenbilgi(holder.username,tarif.getUsername(), "Kullanıcı");
        gonderenbilgi(holder.baslik,tarif.getBaslik(), "Başlık");
        gonderenbilgi(holder.message,tarif.getMessage(), "Tarif");
        DatabaseReference konum=FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference().child("Begeniler").child(tarif.getMessageid());
        konum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if(dataSnapshot.child(mevcutFirebaseUser.getUid()).exists()){
                   holder.begen.setTag("begenildi");
            }
            else{
                   holder.begen.setTag("begen");
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        konum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.lsayisi.setText(dataSnapshot.getChildrenCount() + " beğeni");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.begen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.begen.getTag().equals("begen")){
                    FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference().child("Begeniler").child(tarif.getMessageid()).child(mevcutFirebaseUser.getUid()).setValue(true);
                }
                else{
                    FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference().child("Begeniler").child(tarif.getMessageid()).child(mevcutFirebaseUser.getUid()).removeValue();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGonderis.size();
    }

    public class TVH extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView baslik;
        public TextView message;
        public ImageView begen;
        public TextView lsayisi;
        Boolean kontrol=true;


        public TVH(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.homeusername);
            baslik=itemView.findViewById(R.id.hometarifbaslik);
            message=itemView.findViewById(R.id.hometarificerigi);
            begen=itemView.findViewById(R.id.likebutton);
            lsayisi=itemView.findViewById(R.id.begenisayisi);
        }



    }

    private void gonderenbilgi(TextView textView,String value, String ek){
        textView.setText(ek + ": "+ value);

    }


}