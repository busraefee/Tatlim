package com.example.pasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pasta.Adapter.YorumAdapter;
import com.example.pasta.Model.Comment;
import com.example.pasta.Model.Kullanici;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private YorumAdapter yorumAdapter;
    private List<Comment> yorumListesi;


    EditText edt_yorum_ekle;
    ImageView profil_resmi;
    TextView txt_gonder;

    String gonderiId;
    String gonderenId;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Toolbar toolbar = findViewById(R.id.toolbar_yorumlarActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Yorumlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView = findViewById(R.id.recycler_view_yorumlarActivity);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        yorumListesi = new ArrayList<>();
        //yorum adapterini listeye ba??lama
        yorumAdapter = new YorumAdapter(this,yorumListesi);
        //adapteri recycleviewe ba??lama
        recyclerView.setAdapter(yorumAdapter);


        edt_yorum_ekle = findViewById(R.id.edt_yorumEkle_yorumlarActivity);
        profil_resmi = findViewById(R.id.profil_resmi_yorumlarActivity);
        txt_gonder = findViewById(R.id.txt_gonder_yorumlarActivity);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //intent ile extralar?? ald??k
        Intent intent = getIntent();
        gonderenId = intent.getStringExtra("gonderenId");
        gonderiId = intent.getStringExtra("gonderiId");

        txt_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_yorum_ekle.getText().toString().equals(""))
                {
                    Toast.makeText(CommentsActivity.this, "Yorum Yaz??n??z...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    yorumEkle();  //firebaseye yorumlar?? ekleyece??imiz metot
                }
            }

        });

        //metotlar?? cag??rma
        yorumlariOku();
        resimAl();

    }

    private void yorumEkle() {

        DatabaseReference yorumlarYolu = FirebaseDatabase.getInstance().getReference("Yorumlar").child(gonderiId);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("yorum",edt_yorum_ekle.getText().toString());
        hashMap.put("gonderen",firebaseUser.getUid());

        //verilerin ust uste binmemesi i??in alta yeni veri olarak eklenmesi i??in
        yorumlarYolu.push().setValue(hashMap);
        edt_yorum_ekle.setText("");

    }

    private void resimAl()
    {
        DatabaseReference resimAlmaYolu = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(firebaseUser.getUid());

        resimAlmaYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //vt deki verilerin de??erini kullanici s??n??f??ndan al
                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                Glide.with(getApplicationContext()).load(kullanici.getResimurl()).into(profil_resmi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void yorumlariOku(){

        DatabaseReference yorumlariOkumaYolu = FirebaseDatabase.getInstance().getReference("Yorumlar").child(gonderiId);
         yorumlariOkumaYolu.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 //verilerin ??st ??ste binmemesi icin
                 yorumListesi.clear();
                 //verileri okutma (d??ng??de vt deki b??t??n cocuklar?? al)
                 for (DataSnapshot snapshot1 : snapshot.getChildren())
                 {
                     //snapshot1.getValue de??erini comment s??n??f??nda al comment de??i??ken??ne ata bu commenti de listeye aktar.
                    Comment comment = snapshot1.getValue(Comment.class);
                    yorumListesi.add(comment);
                    //listeyi de recycle ye ba??lad??????m i??in oraya gitmi?? olur.
                 }

                 //her de??i??iklikte refresh edilmesi icin
                 yorumAdapter.notifyDataSetChanged();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
    }
}











