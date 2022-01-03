package com.example.pasta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pasta.AnaSayfaActivity;
import com.example.pasta.Model.Comment;
import com.example.pasta.Model.Kullanici;
import com.example.pasta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class YorumAdapter extends RecyclerView.Adapter<YorumAdapter.ViewHolder> {

    private Context mContext;
    private List<Comment> mYorumListesi;

    private FirebaseUser mevcutKullanici;

    public YorumAdapter(Context context, List<Comment> mYorumListesi) {
        this.mContext = context;
        this.mYorumListesi = mYorumListesi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //recyclenin ogesini baglama
        View view = LayoutInflater.from(mContext).inflate(R.layout.yorum_ogesi,parent,false);
        return new YorumAdapter.ViewHolder(view);

    }


    //listedeki her satırdaki ogelere tıkladığımızda ne yapsın
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        final Comment comment = mYorumListesi.get(position);

         holder.txt_yorum.setText(comment.getYorum());

        kullaniciBilgisiAl(holder.profilresmi,holder.txt_kullanici_adi,comment.getGonderen());

        holder.txt_yorum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AnaSayfaActivity.class);
                intent.putExtra("gonderenId",comment.getGonderen());
                mContext.startActivity(intent);
            }
        });


        holder.profilresmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AnaSayfaActivity.class);
                intent.putExtra("gonderenId",comment.getGonderen());
                mContext.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {

        return mYorumListesi.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profilresmi;
        public TextView txt_kullanici_adi,txt_yorum;


         public ViewHolder(@NonNull View itemView) {

             super(itemView);

             profilresmi = itemView.findViewById(R.id.profil_resmi_yorumOgesi);
             txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_yorumOgesi);
             txt_yorum = itemView.findViewById(R.id.txt_yorum_yorumOgesi);
        }
    }

    public void kullaniciBilgisiAl(ImageView imageView,TextView kullaniciAdi, String gonderenId){

        //gonderenId için vt den yol alıyoruz
        DatabaseReference gonderenIdYolu = FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(gonderenId);

        gonderenIdYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //vt den bir kullanici aldim
                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                Glide.with(mContext).load(kullanici.getResimurl()).into(imageView);

                kullaniciAdi.setText(kullanici.getAd());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}








