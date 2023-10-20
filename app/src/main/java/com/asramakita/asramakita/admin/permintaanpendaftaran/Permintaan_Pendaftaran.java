package com.asramakita.asramakita.admin.permintaanpendaftaran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Permintaan_Pendaftaran extends AppCompatActivity {

    private Konfirmasi_Pendaftaran_Adapter myAdapter;
    private ArrayList<Pendaftaran_Class> myList;
    private DatabaseReference dbPendaftaran;
    private RecyclerView rcPendaftaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permintaan_pendaftaran);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbPendaftaran = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("PermintaanPendaftaran");

        rcPendaftaran = findViewById(R.id.rcPendaftaran);
        rcPendaftaran.setHasFixedSize(true);
        rcPendaftaran.setLayoutManager(new LinearLayoutManager(this));
        myList = new ArrayList<>();
        myAdapter = new Konfirmasi_Pendaftaran_Adapter(this, myList);
        rcPendaftaran.setAdapter(myAdapter);

        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Dashboard_Admin.class));
        finish();
    }

    private void getData() {
        dbPendaftaran.child("Data").orderByChild("status").equalTo("diminta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    myList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Pendaftaran_Class pendaftaranClass = dataSnapshot.getValue(Pendaftaran_Class.class);
                        myList.add(pendaftaranClass);
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}