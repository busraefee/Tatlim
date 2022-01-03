package com.example.pasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.pasta.Cerceve.AramaFragment;
import com.example.pasta.Cerceve.HomeFragment;
import com.example.pasta.Cerceve.ProfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class  AnaSayfaActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment seciliCerceve = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //navigation öğesi seciliyken dinleyeceği sey
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        //gelen intenti extrasıyla birlikte alma
        Bundle intent = getIntent().getExtras();
        if (intent != null)
        {
            String gonderen = intent.getString("gonderenId");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
            editor.putString("profileid", gonderen);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new ProfilFragment()).commit();
        }
        else
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new HomeFragment()).commit();
            }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){

                        case R.id.nav_home:
                            //ana cerceve ceğır
                            seciliCerceve = new HomeFragment();
                            break;

                        case R.id.nav_arama:
                            //arama cerceve ceğır
                            seciliCerceve = new AramaFragment();
                            break;

                        case R.id.nav_ekle:
                            //gonderi aktivitesine git

                            seciliCerceve = null;
                            startActivity(new Intent(AnaSayfaActivity.this,GonderiActivity.class));
                            break;


                        case R.id.nav_profil:

                            SharedPreferences.Editor editor = getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();

                            //profil cerceve ceğır
                            seciliCerceve = new ProfilFragment();
                            break;
                    }

                    if (seciliCerceve != null){

                        //cerceveleri cağır         geçişe başla     yer değiştir   çerçeve kapsayıcının içine secilicerceve yi ekle
                        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,seciliCerceve).commit();

                    }
                    return true;
                }
            };
}