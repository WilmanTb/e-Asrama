package com.asramakita.asramakita.mahasiswa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asramakita.asramakita.mahasiswa.ijinkeluar.Adapter_Ijin_Keluar_Mhs;
import com.asramakita.asramakita.mahasiswa.ijinkeluar.Activity_Tambah_Ijin_Mhs;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.mahasiswa.ijinkeluar.Model_ijin_keluar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_Ijin_Keluar_Mhs extends Fragment {

    View view;
    Button btn_baru;
    RecyclerView rcIjinKeluar;
    ArrayList<Model_ijin_keluar> modelIjinKeluar;
    Adapter_Ijin_Keluar_Mhs adapterIjinKeluarMhs;
    DatabaseReference dbIjin;
    FirebaseAuth userAuth;
    String Nama, Npm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ijin_keluar_mhs, container, false);

        initComponents();
        getData();

        btn_baru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), Activity_Tambah_Ijin_Mhs.class));
            }
        });

        return view;
    }

    private void initComponents(){
        btn_baru = view.findViewById(R.id.btn_baru);
        rcIjinKeluar = view.findViewById(R.id.rc_ijinKeluar);
        rcIjinKeluar.setHasFixedSize(true);
        rcIjinKeluar.setLayoutManager(new LinearLayoutManager(view.getContext()));
        modelIjinKeluar = new ArrayList<>();
        adapterIjinKeluarMhs = new Adapter_Ijin_Keluar_Mhs(view.getContext(), modelIjinKeluar);
        rcIjinKeluar.setAdapter(adapterIjinKeluarMhs);
        userAuth = FirebaseAuth.getInstance();
        dbIjin = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    }

    private void getData(){
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        String UID = firebaseUser.getUid();

        dbIjin.child("IjinKeluar").orderByChild("sbg").equalTo("pengaju").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    modelIjinKeluar.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        String id = dataSnapshot.getKey();
                        if (id.equals(UID)){
                            String status = dataSnapshot.child("status").getValue(String.class);
                            if (status.equals("true") || status.equals("diterima")) {
                                Model_ijin_keluar model_ijin_keluar = dataSnapshot.getValue(Model_ijin_keluar.class);
                                modelIjinKeluar.add(model_ijin_keluar);
                            }
                        }
                    }
                    adapterIjinKeluarMhs.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
