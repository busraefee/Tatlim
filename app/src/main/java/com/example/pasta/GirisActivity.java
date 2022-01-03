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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GirisActivity extends AppCompatActivity {

    EditText edt_mail,edt_sifre;
    Button btn_girisYap;
    TextView tview_ayitSayfasina_git;

    FirebaseAuth girisYetkisi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        edt_mail = findViewById(R.id.edt_mail_giris);
        edt_sifre = findViewById(R.id.edt_sifre_giris);
        btn_girisYap = findViewById(R.id.btn_giris_activity);

        girisYetkisi = FirebaseAuth.getInstance();


        tview_ayitSayfasina_git = findViewById(R.id.txt_kayitSayfasina_git);

        tview_ayitSayfasina_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(GirisActivity.this,KaydolActivity.class));
            }
        });

        btn_girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog pdGiris = new ProgressDialog(GirisActivity.this);
                pdGiris.setMessage("Giris Yapılıyor..");
                pdGiris.show();


                String str_emailgiris = edt_mail.getText().toString();
                String str_sifreGiris = edt_sifre.getText().toString();

                if (TextUtils.isEmpty(str_emailgiris) || TextUtils.isEmpty(str_sifreGiris))
                {
                    Toast.makeText(GirisActivity.this, "Bütün alanları doldurun..", Toast.LENGTH_LONG).show();
                }

                else {
                    //giris yapma kodu

                    girisYetkisi.signInWithEmailAndPassword(str_emailgiris,str_sifreGiris)
                            .addOnCompleteListener(GirisActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        DatabaseReference yolGiris = FirebaseDatabase.getInstance().getReference()
                                                .child("Kullanicilar").child(girisYetkisi.getCurrentUser().getUid());


                                        yolGiris.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                pdGiris.dismiss();

                                                Intent intent = new Intent(GirisActivity.this,AnaSayfaActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                                //işlem iptal edilirse
                                                pdGiris.dismiss();

                                            }
                                        });
                                    }
                                    else {
                                        pdGiris.dismiss();
                                        Toast.makeText(GirisActivity.this, "Giriş Başarısız", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}















