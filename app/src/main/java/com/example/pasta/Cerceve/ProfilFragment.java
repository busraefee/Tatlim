package com.example.pasta.Cerceve;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pasta.Adapter.ResimAdapter;
import com.example.pasta.BaslangicActivity;
import com.example.pasta.Model.Gonderi;
import com.example.pasta.Model.Kullanici;
import com.example.pasta.ProfilDuzenleActivity;
import com.example.pasta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {

    ImageView profil_foto;
    TextView txt_gonderiler,txt_takipciler,txt_takipEdilenler,txt_name, txt_bio, txt_username;
    Button btn_profili_duzenle, cıkıs_yap;
    RecyclerView viewresimler;
    ResimAdapter resimAdapter;
    List<Gonderi> gonderiList;

    FirebaseUser user;
    String profilId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);  //context.getSharedPreferences ile kaydedilen bir verinin farklı activityde de kullanımına izin vermiş olduk.
        profilId = prefs.getString("profileid","none");

        cıkıs_yap = view.findViewById(R.id.cıkıs_profilCercevesi);
        profil_foto = view.findViewById(R.id.profil_resmi_profilCercevesi);

        txt_gonderiler = view.findViewById(R.id.txt_gonderiler_profilCercevesi);
        txt_takipciler = view.findViewById(R.id.txt_takipciler_profilCercevesi);
        txt_takipEdilenler= view.findViewById(R.id.txt_takipEdilenler_profilCercevesi);
        txt_bio = view.findViewById(R.id.txt_bio_profilCercevesi);
        txt_name = view.findViewById(R.id.txt_ad_profilCercevesi);
        txt_username = view.findViewById(R.id.txt_kullaniciadi_profilCerceve);

        btn_profili_duzenle = view.findViewById(R.id.btn_profiliDuzenle_profilCercevesi);

        viewresimler = view.findViewById(R.id.recycler_view_profilCercevesi);
        viewresimler.setHasFixedSize(true);
        //arka planı linear değil de ızgara seklinde istediğimiz icin (layoutmanager yerleşim yoneticisi)
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),2);
        viewresimler.setLayoutManager(linearLayoutManager);
        gonderiList = new ArrayList<>();
        //listeyi adaptera bağlama
        resimAdapter = new ResimAdapter(getContext(),gonderiList);
        //adapteri resimadapter olarak ayarla   //adapteri recyclera bağlama
        viewresimler.setAdapter(resimAdapter);


        kullaniciBilgisi();
        takipcileriAl();
        gonderisayisial();
        resimlerim();



        if (profilId.equals(user.getUid())){

            btn_profili_duzenle.setText("Profili Düzenle");
        }
        else{
            takipEdiliyormu();
        }



        btn_profili_duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buton = btn_profili_duzenle.getText().toString();

                if (buton.equals("Profili Düzenle"))
                {
                    //profili duzenleye git
                    startActivity(new Intent(getContext(), ProfilDuzenleActivity.class));

                }
                else if (buton.equals("Takip Et"))
                {
                    //takip edilenlere ekle
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(user.getUid())
                            .child("takipEdilenler").child(profilId).setValue(true);

                    //karsı tarafın takipcilerine ekleme
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                            .child("takipciler").child(user.getUid()).setValue(true);
                }

                else if (buton.equals("Takip Ediliyor"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(user.getUid())
                            .child("takipEdilenler").child(profilId).removeValue();


                    FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId)
                            .child("takipciler").child(user.getUid()).removeValue();

                }
            }
        });



        cıkıs_yap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), BaslangicActivity.class));
            }
        });


        return view;
    }

    private void kullaniciBilgisi()
    {
        //kullanicilara yol verme (Kullanicilar dan yol al)
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(profilId);
        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null)
                {
                    return;
                }


                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                //kullanıcının mevcut vt deki resim url sini al (glide = resim-url dönüşümüne yarar)(into - yükle)
                Glide.with(getContext()).load(kullanici.getResimurl()).into(profil_foto);
                txt_username.setText(kullanici.getKullaniciadi());
                txt_name.setText(kullanici.getAd());
                txt_bio.setText(kullanici.getBio());


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void takipEdiliyormu()
    {
         DatabaseReference takipreference = FirebaseDatabase.getInstance().getReference().child("Takip").child(user.getUid())
                 .child("takipEdilenler");

         takipreference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {

                 //takipcisinin icinde ben varsam(mevcut kullanıcı)
                 if (snapshot.child(profilId).exists())
                 {
                    btn_profili_duzenle.setText("Takip Ediliyor"); //butonun textini değistirsin
                 }

                 else
                 {
                     btn_profili_duzenle.setText("Takip Et");

                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

    }

    //takipci sayısını alma
    private void takipcileriAl()
    {

        //takipcilere yol verme
        DatabaseReference takipcireference = FirebaseDatabase.getInstance().getReference().child("Takip").child(profilId).child("takipciler");

        takipcireference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txt_takipciler.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //takip edilen sayısını alır
        DatabaseReference takipedilenreference = FirebaseDatabase.getInstance().getReference().child("Takip")
                .child(profilId).child("takipEdilenler");

        takipedilenreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txt_takipEdilenler.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void gonderisayisial()
    {
        DatabaseReference gonderireference = FirebaseDatabase.getInstance().getReference("Gonderiler");

        gonderireference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Gonderi gonderi = snapshot1.getValue(Gonderi.class);

                    if (gonderi.getGonderen().equals(profilId))
                    {
                         i++;
                    }
                }

                txt_gonderiler.setText(""+i);//string olduğu icin i.tostring de olabiilr (arastır)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void resimlerim(){
        DatabaseReference resimreference = FirebaseDatabase.getInstance().getReference("Gonderiler");
        resimreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //vt den verileri alma
                gonderiList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Gonderi gonderi = snapshot1.getValue(Gonderi.class);
                    if (gonderi.getGonderen().equals(profilId)){
                        gonderiList.add(gonderi);
                    }
                }

                Collections.reverse(gonderiList);
                resimAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}






















