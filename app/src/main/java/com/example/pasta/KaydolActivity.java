package com.example.pasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class KaydolActivity extends AppCompatActivity {

    EditText edt_kullaniciAdi, edt_Ad, edt_mail, edt_sifre;
    Button btn_kaydol;
    TextView txt_girisSayfasinaGit;

    FirebaseAuth yetki;
    DatabaseReference yol;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaydol);

        edt_kullaniciAdi = findViewById(R.id.edt_kullaniciAdi);
        edt_Ad = findViewById(R.id.edt_Ad);
        edt_mail = findViewById(R.id.edt_mail);
        edt_sifre = findViewById(R.id.edt_sifre);

        btn_kaydol = findViewById(R.id.btn_kaydol_activity);

        yetki = FirebaseAuth.getInstance();

        txt_girisSayfasinaGit = findViewById(R.id.txt_giris_sayfasi_git);

        txt_girisSayfasinaGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KaydolActivity.this,GirisActivity.class));
            }
        });


        btn_kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(KaydolActivity.this);
                pd.setMessage("Lütfen bekleyin..");
                pd.show();

                String str_kullaniciadi = edt_kullaniciAdi.getText().toString();
                String str_Ad = edt_Ad.getText().toString();
                String str_mail = edt_mail.getText().toString();
                String str_sifre = edt_sifre.getText().toString();


                if (TextUtils.isEmpty(str_Ad) || TextUtils.isEmpty(str_kullaniciadi) || TextUtils.isEmpty(str_mail) || TextUtils.isEmpty(str_sifre))
                    {
                        
                        Toast.makeText(KaydolActivity.this, "Alanları Doldurunuz", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //yeni kullanici kaydetme metodunu cağır
                    kaydet(str_kullaniciadi,str_Ad,str_mail,str_sifre);

                }

            }
        });

    }

        public void kaydet(String kullaniciadi,String ad, String mail, String sifre){

        yetki.createUserWithEmailAndPassword(mail,sifre)
                .addOnCompleteListener(KaydolActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //vt acma basarılı ise kullanıcı ekle
                        if (task.isSuccessful()){

                            FirebaseUser firebaseKullanici = yetki.getCurrentUser();
                            String kullaniciId = firebaseKullanici.getUid();
                            yol = FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(kullaniciId);

                            HashMap<String, Object> hashMap = new HashMap<>();

                            hashMap.put("id",kullaniciId);
                            hashMap.put("kullaniciadi",kullaniciadi.toLowerCase());
                            hashMap.put("ad",ad);
                            hashMap.put("bio","");
                            hashMap.put("resimurl","https://firebasestorage.googleapis.com/v0/b/pasta-2e081.appspot.com/o/placeholder.png?alt=media&token=7df99009-3ed8-4746-aab9-af7cdbd891b4");

                            yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    //eğer kullanıcı olusturma islemi basarılıysa
                                    if (task.isSuccessful()){
                                        pd.dismiss();

                                        Intent intent = new Intent(KaydolActivity.this,AnaSayfaActivity.class);

                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);    //gittikten sonra geri gönmemesi için
                                        startActivity(intent);

                                    }

                                }
                            });

                        }
                        else{

                            pd.dismiss();
                            Toast.makeText(KaydolActivity.this, "Kayıt Başarısız", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }


}



















