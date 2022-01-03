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
import com.example.pasta.Model.TarifDetay;
import com.example.pasta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TarifDetayAdapter extends RecyclerView.Adapter<TarifDetayAdapter.ViewHolder>{

    public Context mContext;
    public List<Gonderi> mTarifdetay;

    FirebaseUser mevcutFirebaseUser;

    public TarifDetayAdapter(Context mContext, List<Gonderi> mTarifdetay) {
        this.mContext = mContext;
        this.mTarifdetay = mTarifdetay;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.tarif_detay_ogesi,parent,false);

        return new TarifDetayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        mevcutFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Gonderi gonderi = mTarifdetay.get(position);

        Glide.with(mContext).load(gonderi.getGonderiResmi()).into(holder.gonderi_resmi);


        //metotları cagırma
        gonderenBilgileri(holder.profil_resmi,holder.txt_kullanici_adi,holder.txt_gonderen,gonderi.getGonderen());  //
        begeniSayisi(holder.txt_begeni,gonderi.getGonderiId()); //
        yorumlarıAl(gonderi.getGonderiId(),holder.txt_yorumlar);    //
        tarifBilgileri(gonderi.getGonderiId(),holder.txt_yemekadi,holder.txt_malzemeler,holder.txt_yapilis);


        //gonderi resmine tıkladığında
        holder.gonderi_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid",gonderi.getGonderiId());  //bak
                editor.apply();

                //profil cercevesine gitme
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                        new TarifDetayFragment()).commit();
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
        return mTarifdetay.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profil_resmi,gonderi_resmi;
        public TextView txt_kullanici_adi,txt_begeni,txt_gonderen ,txt_yemekadi,txt_yorumlar,txt_malzemeler,txt_yapilis;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profil_resmi = itemView.findViewById(R.id.profil_resmi_Gonderidetay_Ogesi);
            gonderi_resmi = itemView.findViewById(R.id.gonderi_resmi_Gonderidetay_Ogesi);

            txt_kullanici_adi = itemView.findViewById(R.id.txt_kullaniciadi_Gonderidetay_Ogesi);
            txt_begeni = itemView.findViewById(R.id.txt_begeniler_Gonderidetay_Ogesi);
            txt_gonderen = itemView.findViewById(R.id.txt_gonderen_Gonderidetay_Ogesi);
            txt_yorumlar = itemView.findViewById(R.id.txt_yorum_Gonderidetay_Ogesi);
            txt_yemekadi = itemView.findViewById(R.id.txt_yemekadi_Gonderidetay_Ogesi);
            txt_malzemeler = itemView.findViewById(R.id.txt_malzemeler_Gonderidetay_Ogesi);
            txt_yapilis = itemView.findViewById(R.id.txt_yapilis_Gonderidetay_Ogesi);


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



    private void gonderenBilgileri(ImageView profil_resmi,TextView kullaniciadi,TextView gonderen, String kullaniciId)
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
                gonderen.setText(kullanici.getKullaniciadi());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void tarifBilgileri(String gonderiId,TextView yemekadi, TextView malzeme, TextView yapilis)
    {
        DatabaseReference tarifYolu = FirebaseDatabase.getInstance().getReference("Gonderiler").child(gonderiId);

        tarifYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Gonderi gonderi = snapshot.getValue(Gonderi.class);

                yemekadi.setText(gonderi.getYemekadi());
                yapilis.setText(gonderi.getYapilis());
                malzeme.setText(gonderi.getMalzemeler());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}
