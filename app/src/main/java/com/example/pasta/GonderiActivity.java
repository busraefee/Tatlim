package com.example.pasta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;


public class GonderiActivity extends AppCompatActivity {

    Uri resimUri;
    String benimUrim = "";

    StorageTask yuklemeGorevi;
    StorageReference resimYukleYolu;

    ImageView image_Kapat, image_Eklendi;
    TextView txt_Gonder, txt_malzemeler, txt_yapilis;

    EditText edt_yemekadi, edt_yapilis, edt_malzemeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gonderi);

        image_Kapat = findViewById(R.id.close_Gonderi);
        image_Eklendi = findViewById(R.id.eklenen_Resim_Gonderi);

        txt_Gonder = findViewById(R.id.txt_Gonder);
        txt_malzemeler = findViewById(R.id.txt_malzemeler);
        txt_yapilis = findViewById(R.id.txt_yapilis);

        edt_malzemeler = findViewById(R.id.edt_malzemeler);
        edt_yapilis = findViewById(R.id.edt_yapilisi);
        edt_yemekadi = findViewById(R.id.edt_yemek_adi);


        resimYukleYolu = FirebaseStorage.getInstance().getReference("gonderiler");

        image_Kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GonderiActivity.this,AnaSayfaActivity.class));
                finish();
            }
        });

        txt_Gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimYukle();
            }
        });

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(GonderiActivity.this);

    }

    private String dosyaUzantisiAl(Uri uri){

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void resimYukle() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Gönderiliyor...");
        progressDialog.show();

        //resim yukleme kodları
        if (resimUri != null)
        {
            StorageReference dosyaYolu = resimYukleYolu.child(System.currentTimeMillis()
                    + "." + dosyaUzantisiAl(resimUri));

            yuklemeGorevi = dosyaYolu.putFile(resimUri);
            yuklemeGorevi.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){

                        throw task.getException();
                    }



                    return dosyaYolu.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        //gorev basarılı ise yüklemeleri yap

                        Uri indirmeUrisi = task.getResult();
                        benimUrim = indirmeUrisi.toString();

                        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");

                        String gonderiId = veriYolu.push().getKey();

                        HashMap<String,Object> hashMap = new HashMap<>();

                        hashMap.put("gonderiId",gonderiId);
                        hashMap.put("gonderiResmi",benimUrim);
                        hashMap.put("yemekadi",edt_yemekadi.getText().toString());
                        hashMap.put("yapilis",edt_yapilis.getText().toString());
                        hashMap.put("malzemeler",edt_malzemeler.getText().toString());
                        hashMap.put("gonderen", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        //hashmapi veriyoluna bağlama

                        veriYolu.child(gonderiId).setValue(hashMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(GonderiActivity.this,AnaSayfaActivity.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(GonderiActivity.this, "Gönderme Başarısız!", Toast.LENGTH_SHORT).show();
                        
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GonderiActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(this, "Resim Seçilemedi!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            resimUri = result.getUri();

            image_Eklendi.setImageURI(resimUri);
        }
        else
        {
            Toast.makeText(this, "Resim Seçilemedi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GonderiActivity.this,AnaSayfaActivity.class));
            finish();
        }
    }




}