package com.asramakita.asramakita.admin.ijinkeluar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.ijinkeluar.Activity_Sub_Ijin_Keluar_Admin;
import com.asramakita.asramakita.admin.ijinkeluar.Adapter_Ijin_Keluar_Admin;
import com.asramakita.asramakita.mahasiswa.ijinkeluar.Model_ijin_keluar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Lapor_Kembali_Admin extends AppCompatActivity {

    private RecyclerView rcLaporKembali;
    private ArrayList<Model_ijin_keluar> modelIjinKeluar;
    private Adapter_Ijin_Keluar_Admin adapterIjinKeluarAdmin;
    private DatabaseReference dbIjin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor_kembali_admin);

        Activity_Sub_Ijin_Keluar_Admin.SUBMENUIJIN = 2;

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();
        getData();
    }

    private void initComponents(){
        rcLaporKembali = findViewById(R.id.rc_laporKembali);
        rcLaporKembali.setHasFixedSize(true);
        rcLaporKembali.setLayoutManager(new LinearLayoutManager(this));
        modelIjinKeluar = new ArrayList<>();
        adapterIjinKeluarAdmin = new Adapter_Ijin_Keluar_Admin(this, modelIjinKeluar);
        rcLaporKembali.setAdapter(adapterIjinKeluarAdmin);
        dbIjin = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    }

    private void getData(){
        dbIjin.child("IjinKeluar").orderByChild("status").equalTo("diajukan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    modelIjinKeluar.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        String sbg = dataSnapshot.child("sbg").getValue().toString();
                        if (sbg.equals("pengaju")) {
                            Model_ijin_keluar model_ijin_keluar = dataSnapshot.getValue(Model_ijin_keluar.class);
                            modelIjinKeluar.add(model_ijin_keluar);
                        }
                    }
                    adapterIjinKeluarAdmin.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Sub_Ijin_Keluar_Admin.class));
        finish();
    }
}