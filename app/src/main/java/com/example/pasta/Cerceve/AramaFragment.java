package com.example.pasta.Cerceve;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.pasta.Adapter.GonderiAdapter;
import com.example.pasta.Adapter.KullaniciAdapter;
import com.example.pasta.Model.Gonderi;
import com.example.pasta.Model.Kullanici;
import com.example.pasta.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AramaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AramaFragment extends Fragment {

    private RecyclerView recyclerView;
    private KullaniciAdapter kullaniciAdapter;
    private List<Kullanici> mKullaniciler;
    private GonderiAdapter gonderiAdapter; //
    private List<Gonderi> mGonderi; //

    EditText arama_bar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AramaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AramaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AramaFragment newInstance(String param1, String param2, String param3, String param4) {
        AramaFragment fragment = new AramaFragment();
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
        View view =  inflater.inflate(R.layout.fragment_arama, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_Arama);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        arama_bar = view.findViewById(R.id.edt_arama_bar);

        mKullaniciler = new ArrayList<>();
        kullaniciAdapter = new KullaniciAdapter(getContext(),mKullaniciler);

        //kullaniciAdapter i  recyclerViewe bağlama
        recyclerView.setAdapter(kullaniciAdapter);

        kullanicileriOku();

        arama_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                kullaniciAra(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        return view;
    }

    //arama yaptıracağım kodlar
    private void kullaniciAra(String s){

        Query sorgu = FirebaseDatabase.getInstance().getReference("Kullanicilar").orderByChild("kullaniciadi")
                .startAt(s)         //değişkende basla
                .endAt(s+"\uf8ff");

        sorgu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mKullaniciler.clear();
                    // veritabanındaki verileri snapshot1 e aktar
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    //snaphottaki verileri kullanıcıya aktar kullanıcıyı da listeye aktardık.
                    Kullanici kullanici = snapshot1.getValue(Kullanici.class);
                    mKullaniciler.add(kullanici);
                }
                //veriler her güncellendiğinde liste de güncellensin
                kullaniciAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void kullanicileriOku()
    {
        DatabaseReference kullanicilerYolu = FirebaseDatabase.getInstance().getReference("Kullanicilar");

        kullanicilerYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // arama yeri boş ise
                if (arama_bar.getText().toString().equals(""))
                {
                    mKullaniciler.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        Kullanici kullanici = snapshot1.getValue(Kullanici.class);
                        mKullaniciler.add(kullanici);
                    }
                    kullaniciAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

