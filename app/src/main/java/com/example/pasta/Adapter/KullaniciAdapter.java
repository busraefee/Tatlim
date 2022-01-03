package com.example.pasta.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pasta.Cerceve.ProfilFragment;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.ViewHolder> {

    private Context mContext;
    private List<Kullanici> mKullanicilar;

    private FirebaseUser firebaseKullanici;

    public KullaniciAdapter(Context mContext, List<Kullanici> mKullanicilar) {
        this.mContext = mContext;
        this.mKullanicilar = mKullanicilar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.kullanici_ogesi,parent,false);

        return new KullaniciAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseKullanici = FirebaseAuth.getInstance().getCurrentUser();

        final Kullanici kullanici = mKullanicilar.get(position);

        holder.btn_takipEt.setVisibility(View.VISIBLE);

        holder.kullaniciAdi.setText(kullanici.getKullaniciadi());
        holder.ad.setText(kullanici.getAd());
        Glide.with(mContext).load(kullanici.getResimurl()).into(holder.profil_resmi);
        takipEdiliyor(kullanici.getId(),holder.btn_takipEt);

        if (kullanici.getId().equals(firebaseKullanici.getUid())){
            holder.btn_takipEt.setVisibility(View.GONE);
        }


        //takip et butonuna tıkladığımda(holder için) profil fragmenti açma
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",kullanici.getId());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new ProfilFragment()).commit();
            }
        });

        //buton takip ete tıkladığımda
        holder.btn_takipEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btn_takipEt.getText().toString().equals("Takip Et"))
                {
                //kullanıcıyı firebase ile takip edecek sekilde ayarla
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseKullanici.getUid())
                            .child("takipEdilenler").child(kullanici.getId()).setValue(true);

                    //karşı tarafın takipçilerine de beni eklemeli
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(kullanici.getId())
                            .child("takipciler").child(firebaseKullanici.getUid()).setValue(true);

                }
                else {
                    //takip ediliyora tıklayıp takibi bırakma   removeValue -> değeri kaldır
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseKullanici.getUid())
                            .child("takipEdilenler").child(kullanici.getId()).removeValue();


                    FirebaseDatabase.getInstance().getReference().child("Takip").child(kullanici.getId())
                            .child("takipciler").child(firebaseKullanici.getUid()).removeValue();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mKullanicilar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView kullaniciAdi;
        public TextView ad;
        public CircleImageView profil_resmi;
        public Button btn_takipEt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            kullaniciAdi = itemView.findViewById(R.id.txt_kullaniciadi_Oge);
            ad = itemView.findViewById(R.id.txt_ad_Oge);
            profil_resmi = itemView.findViewById(R.id.profil_resmi_Oge);
            btn_takipEt = itemView.findViewById(R.id.btn_takipet_Oge);
        }
    }


    public void takipEdiliyor (String kullaniciId,Button button){

        DatabaseReference takipYolu = FirebaseDatabase.getInstance().getReference().child("Takip")
                .child(firebaseKullanici.getUid()).child("takipEdilenler");

        takipYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //verilerin içinde user id var ise butonun textini takip ediliyor yap
                if (snapshot.child(kullaniciId).exists()){
                    button.setText("Takip Ediliyor");
                }
                else {
                    button.setText("Takip Et");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
