package com.example.pasta.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pasta.Cerceve.TarifDetayFragment;
import com.example.pasta.Model.Gonderi;
import com.example.pasta.R;

import java.util.List;

public class ResimAdapter extends RecyclerView.Adapter<ResimAdapter.ViewHolder>{

    private Context context;
    private List<Gonderi> gonderilerim;

    public ResimAdapter(Context context, List<Gonderi> gonderilerim) {
        this.context = context;
        this.gonderilerim = gonderilerim;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //resimler ogesini baglama
        View view = LayoutInflater.from(context).inflate(R.layout.resimler_ogesi,parent,false);

        return new ResimAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Gonderi gonderi = gonderilerim.get(position);
        Glide.with(context).load(gonderi.getGonderiResmi()).into(holder.gonderi_resmi);

        //kendi profilindeki gonderi resmine tıkladığında
        holder.gonderi_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid",gonderi.getGonderiId());
                editor.apply();

                //profil cercevesine gitme
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                        new TarifDetayFragment()).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return gonderilerim.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView gonderi_resmi;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gonderi_resmi = itemView.findViewById(R.id.gonderi_resmi_resimler_ogesi);



        }
    }
}





