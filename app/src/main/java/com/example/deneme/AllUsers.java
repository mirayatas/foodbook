package com.example.deneme;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.data.model.User;
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

public class AllUsers extends RecyclerView.Adapter<AllUsers.VH>{

    private FirebaseAuth vt;
    private Context mcontext;
    private List<Users> mkullanicilar;
    private FirebaseUser firebaseKullanici=FirebaseAuth.getInstance().getCurrentUser();

    public AllUsers(Context mcontext, List<Users> mkullanicilar) {
        this.mcontext = mcontext;
        this.mkullanicilar = mkullanicilar;

    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mcontext).inflate(R.layout.kullaniciprofil,parent,false);

        return new AllUsers.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final Users kullanici=mkullanicilar.get(position);
        holder.ekle.setVisibility(View.VISIBLE);
        holder.username.setText(kullanici.getUsername());
        Eklenenler(kullanici.getUserid(),holder.ekle);


        if(kullanici.getUserid().equals(firebaseKullanici.getUid())){
            holder.ekle.setVisibility(View.GONE);
        }

      holder.ekle.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              vt=FirebaseAuth.getInstance();
              FirebaseUser user=vt.getCurrentUser();
              String authuid=user.getUid();
              HashMap<Object, String> hm=new HashMap<>();
              hm.put("TakipEden",firebaseKullanici.getUid());
              hm.put("TakipEdilen",kullanici.getUserid());
              if(holder.ekle.getText().equals("Takip Et")){
                  FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference().child("Takip").child(authuid).child("takipEdilenler").child(kullanici.getUserid()).setValue(true);
                  FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference().child("Takip").child(kullanici.getUserid()).child("takipciler").child(authuid).setValue(true);
              }
              else{
                  FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference().child("Takip").child(kullanici.getUserid()).child("takipciler").child(authuid).removeValue();
                  FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference().child("Takip").child(authuid).child("takipEdilenler").child(kullanici.getUserid()).removeValue();
              }


          }
      });
    }

    @Override
    public int getItemCount() {
        return mkullanicilar.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView resim;
        public Button ekle;

        public VH(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.isim);
            resim=itemView.findViewById(R.id.Profile);
            ekle=itemView.findViewById(R.id.arkadasekle);

        }
    }

    private void Eklenenler(String kullaniciid,Button button){
        DatabaseReference takipyolu=FirebaseDatabase.getInstance("https://deneme-462ee-default-rtdb.firebaseio.com/").getReference().child("Takip")
                .child(firebaseKullanici.getUid()).child("takipEdilenler");
        Log.d("CIKTI3", takipyolu.toString() + " ---- "  + takipyolu.getKey() + " ||| ");
       takipyolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ss) {
                if(ss.child(kullaniciid).exists())
                    button.setText("Takip Ediliyor");
                else
                    button.setText("Takip Et");
                /*
                Log.d("CIKTI5", ss.getChildrenCount() + " ccc");
                Log.d("CIKTI5", ss.getChildren().toString() + " fff");
                if(ss.getChildrenCount() == 0)
                    button.setText("Takip Et");
                else {
                    for (DataSnapshot sss : ss.getChildren()) {
                        Log.d("CIKTI5", sss.getKey().toString());
                        if ((sss.child("TakipEdilen").getValue() != null && sss.child("TakipEdilen").getValue().equals(firebaseKullanici.getUid()) && sss.child("TakipEden").getValue() != null && sss.child("TakipEden").getValue().equals(kullaniciid))
                                || (sss.child("TakipEdilen").getValue() != null && sss.child("TakipEdilen").getValue().equals(kullaniciid) && sss.child("TakipEden").getValue() != null && sss.child("TakipEden").getValue().equals(firebaseKullanici.getUid()))) {
                            button.setText("Takip Ediliyor");
                        } else {
                            button.setText("Takip Et");
                        }
                    }
                }

                 */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
