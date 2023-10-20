package com.asramakita.asramakita.admin.kamar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.permintaanpendaftaran.Pendaftaran_Class;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tambah_Penghuni extends AppCompatActivity {

    private RecyclerView rcTambahPenghuni;
    private Tambah_Anggota_Adapter tambahAnggotaAdapter;
    private ArrayList<Pendaftaran_Class> kamarModels;
    private DatabaseReference dbRef;
    public static int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_penghuni);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();


        rcTambahPenghuni = findViewById(R.id.rcTambahPenghuni);
        rcTambahPenghuni.setHasFixedSize(true);
        rcTambahPenghuni.setLayoutManager(new LinearLayoutManager(this));
        kamarModels = new ArrayList<>();
        tambahAnggotaAdapter = new Tambah_Anggota_Adapter(this, kamarModels);
        rcTambahPenghuni.setAdapter(tambahAnggotaAdapter);

        getData();
        getNPM();

        if (Informasi_Kamar.Key!=null){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Tambah_Penghuni.this);
            alertDialog.setTitle("Perhatian");
            alertDialog.setMessage("Kamar baru wajib memiliki minimal 1 penghuni!");
            alertDialog.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = alertDialog.create();
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Informasi_Kamar.class));
        finish();
    }

    private void getData() {
        dbRef.child("PermintaanPendaftaran")
                .child("Data").orderByChild("status")
                .equalTo("diterima").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            kamarModels.clear();
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                Pendaftaran_Class kamarModel = dataSnapshot.getValue(Pendaftaran_Class.class);
                                kamarModels.add(kamarModel);
                            }
                            tambahAnggotaAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getNPM(){
        Tambah_Anggota_Adapter tambah_anggota_adapter = new Tambah_Anggota_Adapter(dbRef);
    }
}