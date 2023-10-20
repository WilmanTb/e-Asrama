package com.asramakita.asramakita.admin.pembayaran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.asramakita.asramakita.admin.permintaanpendaftaran.Konfirmasi_Pendaftaran_Adapter;
import com.asramakita.asramakita.mahasiswa.fragment.Fragment_Dashboard_Mhs;
import com.asramakita.asramakita.mahasiswa.pembayaranmhs.Activity_Pembayaran_Mhs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Konfirmasi_Pembayaran extends AppCompatActivity {

    private RecyclerView rcPembayaran;
    private DatabaseReference dbPembayaran;
    private ArrayList<Pembayaran_Model> pembayaranList;
    private Pembayaran_Adapter pembayaranAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembayaran);

        Activity_Pembayaran_Mhs.code = 2;

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbPembayaran = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Pembayaran");

        rcPembayaran = findViewById(R.id.rcPembayaran);
        rcPembayaran.setHasFixedSize(true);
        rcPembayaran.setLayoutManager(new LinearLayoutManager(this));
        pembayaranList = new ArrayList<>();
        pembayaranAdapter = new Pembayaran_Adapter(this, pembayaranList);
        rcPembayaran.setAdapter(pembayaranAdapter);

        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Dashboard_Admin.class));
        finish();
    }

    private void getData() {
        dbPembayaran.child("Data").orderByChild("status").equalTo("diajukan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    pembayaranList.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Pembayaran_Model pembayaran_model = dataSnapshot.getValue(Pembayaran_Model.class);
                        pembayaranList.add(pembayaran_model);
                    }
                    pembayaranAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}