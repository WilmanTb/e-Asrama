package com.asramakita.asramakita.admin.kamar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asramakita.asramakita.Activity_Hapus_Kamar;
import com.asramakita.asramakita.Adapter_Kamar_Grid;
import com.asramakita.asramakita.Adapter_Lantai;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.UIDStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Informasi_Kamar extends AppCompatActivity {

    public static String Key;
    private DatabaseReference dbKamar;
    private RecyclerView rcKamar;
    private Adapter_Kamar_Grid adapter_kamar_grid;
    private ArrayList<Kamar_Model> kamar_models;
    private Button btn_tambah_kamar, btn_hapus_kamar;
    private long kamarCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_kamar);


        getID();

        getKamarData();

        btn_tambah_kamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Key = "Kamar " + (kamarCount+1);
                Dialog dialog = new Dialog(Informasi_Kamar.this);
                dialog.setContentView(R.layout.add_kamar);
                dialog.show();

                Button btn_tambah = dialog.findViewById(R.id.btn_tambah);
                Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
                TextView info_kamar = dialog.findViewById(R.id.infoKamar);

                info_kamar.setText("Tambah Kamar? Kamar selanjutnya adalah " + Key);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_tambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(Informasi_Kamar.this, Tambah_Penghuni.class));
                        finish();
                    }
                });
            }
        });

        btn_hapus_kamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Informasi_Kamar.this, Activity_Hapus_Kamar.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Sub_Kamar.class));
        finish();
    }

    private void getID() {

        rcKamar = findViewById(R.id.rcKamar);
        rcKamar.setHasFixedSize(true);
        rcKamar.setLayoutManager(new GridLayoutManager(this, 3));
        kamar_models = new ArrayList<>();
        adapter_kamar_grid = new Adapter_Kamar_Grid(this, kamar_models);
        rcKamar.setAdapter(adapter_kamar_grid);
        btn_tambah_kamar = findViewById(R.id.btn_tambah_kamar);
        btn_hapus_kamar = findViewById(R.id.btn_hapus_kamar);

        dbKamar = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();
    }

    private void getKamarData() {
        dbKamar.child("Kamar").child(UIDStorage.getInstance().getNamaLantai()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Kamar_Model listKamar = dataSnapshot.getValue(Kamar_Model.class);
                        kamar_models.add(listKamar);
                    }
                    adapter_kamar_grid.notifyDataSetChanged();
                    kamarCount = snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}