package com.asramakita.asramakita.mahasiswa.pembayaranmhs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.pembayaran.Pembayaran_Adapter;
import com.asramakita.asramakita.admin.pembayaran.Pembayaran_Model;
import com.asramakita.asramakita.mahasiswa.Activity_Dashboard_Mhs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Pembayaran_Mhs extends AppCompatActivity {

    private RecyclerView rcPembayaran;
    private Pembayaran_Adapter pembayaranAdapter;
    private ArrayList<Pembayaran_Model> pembayaranModels;
    private DatabaseReference dbPembayaran;
    private FirebaseAuth mAuth;
    private String UID;
    public static int code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_mhs);

        code = 1;

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbPembayaran = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        initComponents();
        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Dashboard_Mhs.class));
        finish();
    }

    private void getData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UID = currentUser.getUid();

        dbPembayaran.child("Pembayaran").child("Data").orderByChild("id_mhs").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    pembayaranModels.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Pembayaran_Model pembayaranModel = dataSnapshot.getValue(Pembayaran_Model.class);
                        if (pembayaranModel.getStatus().equals("belum dibayar")){
                            pembayaranModels.add(pembayaranModel);
                        }
                    }
                    pembayaranAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initComponents() {
        rcPembayaran = findViewById(R.id.rcPembayaran);
        rcPembayaran.setHasFixedSize(true);
        rcPembayaran.setLayoutManager(new LinearLayoutManager(this));
        pembayaranModels = new ArrayList<>();
        pembayaranAdapter = new Pembayaran_Adapter(this, pembayaranModels);
        rcPembayaran.setAdapter(pembayaranAdapter);
        mAuth = FirebaseAuth.getInstance();
    }
}