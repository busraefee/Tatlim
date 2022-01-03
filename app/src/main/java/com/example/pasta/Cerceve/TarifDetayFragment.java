package com.example.pasta.Cerceve;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pasta.Adapter.GonderiAdapter;
import com.example.pasta.Adapter.TarifDetayAdapter;
import com.example.pasta.Model.Gonderi;
import com.example.pasta.Model.TarifDetay;
import com.example.pasta.R;
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
 * Use the {@link TarifDetayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TarifDetayFragment extends Fragment {

    private RecyclerView recyclerView;
    private TarifDetayAdapter tarifDetayAdapter;
  //  private GonderiAdapter gonderiAdapter;
    private List<Gonderi> tarifdetayList;

    String gonderiId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TarifDetayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TarifDetayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TarifDetayFragment newInstance(String param1, String param2) {
        TarifDetayFragment fragment = new TarifDetayFragment();
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
        View view = inflater.inflate(R.layout.fragment_tarif_detay, container, false);


        //gonderi adapter dan gonderi.getGonderen()yi alır gonderioku metodunda kullanıyoruz.
        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        gonderiId = preferences.getString("postid","none");
                                //getstring ile alıyoruz

        recyclerView = view.findViewById(R.id.view_tarifdetay);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        tarifdetayList = new ArrayList<>();
        tarifDetayAdapter = new TarifDetayAdapter(getContext(),tarifdetayList);
        recyclerView.setAdapter(tarifDetayAdapter);
        //gonderiAdapter = new GonderiAdapter(getContext(),tarifdetayList);
       // recyclerView.setAdapter(gonderiAdapter);

        gonderiOku();

        return view;
    }

    private void gonderiOku()
    {
        DatabaseReference gonderiyolu = FirebaseDatabase.getInstance().getReference("Gonderiler").child(gonderiId);

        gonderiyolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tarifdetayList.clear();
                Gonderi gonderi = snapshot.getValue(Gonderi.class);
                tarifdetayList.add(gonderi);

              //  gonderiAdapter.notifyDataSetChanged();
               tarifDetayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}













