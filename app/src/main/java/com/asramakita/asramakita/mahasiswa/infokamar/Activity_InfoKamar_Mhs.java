package com.asramakita.asramakita.mahasiswa.infokamar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.asramakita.asramakita.Adapter_Kamar_Grid;
import com.asramakita.asramakita.Adapter_Lantai;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.UIDStorage;
import com.asramakita.asramakita.admin.kamar.Activity_Sub_Kamar;
import com.asramakita.asramakita.admin.kamar.Kamar_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_InfoKamar_Mhs extends AppCompatActivity {
    public static String Key;
    private RecyclerView rcKamar;
    private DatabaseReference dbKamar;
    private Adapter_Kamar_Grid adapter_kamar_grid;
    private ArrayList<Kamar_Model> kamar_models;
    private TextView info_kamar_mhs;
    private FirebaseAuth userAuth;
    private FirebaseUser firebaseUser;
    private String UID;
    private long kamarCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_kamar_mhs);

        getID();
        getKamarData();
        getInfoKamarUser();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Sub_Kamar_Mhs.class));
        finish();
    }

    private void getID(){
        dbKamar = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        rcKamar = findViewById(R.id.rcKamar);
        rcKamar.setHasFixedSize(true);
        rcKamar.setLayoutManager(new GridLayoutManager(this, 3));
        kamar_models = new ArrayList<>();
        adapter_kamar_grid = new Adapter_Kamar_Grid(this, kamar_models);
        rcKamar.setAdapter(adapter_kamar_grid);

        info_kamar_mhs = findViewById(R.id.info_kamar_mhs);
        userAuth = FirebaseAuth.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
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

    private void getInfoKamarUser(){
        dbKamar.child("Kamar").child("Lt2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    dbKamar.child("Kamar").child("Lt2").child(key).orderByChild("idMhs").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                info_kamar_mhs.setVisibility(View.VISIBLE);
                                info_kamar_mhs.setText("Kamu penghuni " + key + " Lantai 2");
                            } else {
                                dbKamar.child("Kamar").child("Lt3").child(key).orderByChild("idMhs").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            info_kamar_mhs.setVisibility(View.VISIBLE);
                                            info_kamar_mhs.setText("Kamu penghuni " + key + " Lantai 3");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
    }
}