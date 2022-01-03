package com.example.pasta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pasta.Cerceve.ProfilFragment;
import com.example.pasta.Cerceve.TarifDetayFragment;
import com.example.pasta.CommentsActivity;
import com.example.pasta.Model.Gonderi;
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

public class GonderiAdapter extends RecyclerView.Adapter<GonderiAdapter.ViewHolder> {

    public Context mContext;
    public List<Gonderi> mGonderi;

    FirebaseUser mevcutFirebaseUser;

    public GonderiAdapter(Context mContext, List<Gonderi> mGonderi) {
        this.mContext = mContext;
        this.mGonderi = mGonderi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.gonderi_ogesi,parent,false);

        return new GonderiAdapter.ViewHolder(view);

    }

    //recyclerViewdeki her satırı temsil eder (satırdaki butonları) position -> satırların pozisyonu

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Gonderi gonderi = mGonderi.get(position);

        Glide.with(mContext).load(gonderi.getGonderiResmi()).into(holder.gonderi_resmi);


        //metotları cagırma
        gonderenBilgileri(holder.profil_resmi,holder.txt_kullanici_adi,gonderi.getGonderen());
        begenildi(gonderi.getGonderiId(),holder.begeni_resmi);
        begeniSayisi(holder.txt_begeni,gonderi.getGonderiId());
        yorumlarıAl(gonderi.getGonderiId(),holder.txt_yorumlar);
        yemekAdi(gonderi.getGonderiId(),holder.txt_yemekadi);


        //progil resmine tıklama
        holder.profil_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //gonderi.getGonderen() i gonder
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid",gonderi.getGonderen());
                //putstring ile aldığımız seyi gonderiyoruz
                editor.apply();

                //profil cercevesine gitme
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                        new ProfilFragment()).commit();
            }
        });

        //kullanıcı adına tıkladığında
        holder.txt_kullanici_adi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid",gonderi.getGonderen());
                editor.apply();

                //profil cercevesine gitme
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                        new ProfilFragment()).commit();
                    //gelmesini istediğim cerceve
            }
        });


        //gonderi resmine tıkladığında
        holder.gonderi_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid",gonderi.getGonderiId());
                //getGonderiId()yi gonder
                editor.apply();

                //profil cercevesine gitme
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                        new TarifDetayFragment()).commit();
            }
        });






        //begeni_resmi ye tıkladığımda firebasede begeni olsun
        holder.begeni_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.begeni_resmi.getTag().equals("beğen"))
                {
                    //firebase veritabanına beğeniler kısmına gönder
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(gonderi.getGonderiId())
                            .child(mevcutFirebaseUser.getUid()).setValue(true);
                }
                else
                {
                    //zaten beğenilmişse silsin
                    FirebaseDatabase.getInstance().getReference().child("Begeniler").child(gonderi.getGonderiId())
                            .child(mevcutFirebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.yorum_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);

                //GonderiId() ile Gonderen() intent ile diğer sayfaya gönderdik
                intent.putExtra("gonderiId",gonderi.getGonderiId());
                intent.putExtra("gonderenId",gonderi.getGonderen());
                mContext.startActivity(intent);
            }
        });

        holder.txt_yorumlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);

                //GonderiId() ile Gonderen() intent ile diğer sayfaya gönderdik
                intent.putExtra("gonderiId",gonderi.getGonderiId());
                intent.putExtra("gonderenId",gonderi.getGonderen());
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mGonderi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

    public ImageView profil_resmi,gonderi_resmi,begeni_resmi,yorum_resmi;
    public TextView txt_kullanici_adi,txt_begeni,txt_yemekadi,txt_yorumlar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //gonderi ogesindeki ogelerin tanımlamaları
            profil_resmi = itemView.findViewById(R.id.profil_resmi_Gonderi_Ogesi);
            gonderi_resmi = itemView.findViewById(R.id.gonderi_resmi_Gonderi_Ogesi);
            begeni_resmi = itemView.findViewById(R.id.begeni_Gonderi_Ogesi);
            yorum_resmi = itemView.findViewById(R.id.yorum_Gonderi_Ogesi);

            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_Gonderi_Ogesi);
            txt_begeni = itemView.findViewById(R.id.txt_begeniler_Gonderi_Ogesi);
            txt_yorumlar = itemView.findViewById(R.id.txt_yorum_Gonderi_Ogesi);
            txt_yemekadi = itemView.findViewById(R.id.txt_yemekadi_Gonderi_Ogesi);
            //diğer ogeler eklenebilir
        }
    }

    //yorumları vt den alma metodu
    private void  yorumlarıAl(String gonderiId, TextView yorumlar){

        DatabaseReference yorumlarıAlmaYolu = FirebaseDatabase.getInstance().getReference("Yorumlar").child(gonderiId);

        yorumlarıAlmaYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                yorumlar.setText(snapshot.getChildrenCount() + " yorumun hepsini gör");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void begenildi(String gonderiId, ImageView imageView){

        FirebaseUser mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference begeniVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("Begeniler")
                .child(gonderiId);

        begeniVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(mevcutKullanici.getUid()).exists())
                {
                    imageView.setImageResource(R.drawable.ic_begenildi);
                    imageView.setTag("beğenildi");
                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("beğen");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void begeniSayisi(TextView begeniler, String gonderiId)
    {
        DatabaseReference begeniSayisiVeriTabaniYolu = FirebaseDatabase.getInstance().getReference()
                .child("Begeniler")
                .child(gonderiId);

        begeniSayisiVeriTabaniYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                begeniler.setText(snapshot.getChildrenCount() + " beğeni");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void gonderenBilgileri(ImageView profil_resmi,TextView kullaniciadi, String kullaniciId)
    {
        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(kullaniciId);

        veriYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //verilerim vt de snapshotta      veriyi kullanıcı sınıfından al
                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                //                      resimin urlsini al        profil resmine yükle
                Glide.with(mContext).load(kullanici.getResimurl()).into(profil_resmi);

                //hem gonderene hem de kullanıcıadına kullanici sınıfından kull adını alcak.
                kullaniciadi.setText(kullanici.getKullaniciadi());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void yemekAdi(String gonderiId, TextView yemekadi)
    {
        DatabaseReference yemekadiyolu = FirebaseDatabase.getInstance().getReference("Gonderiler").child(gonderiId);

        yemekadiyolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             Gonderi gonderi = snapshot.getValue(Gonderi.class);
               yemekadi.setText(gonderi.getYemekadi());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}











