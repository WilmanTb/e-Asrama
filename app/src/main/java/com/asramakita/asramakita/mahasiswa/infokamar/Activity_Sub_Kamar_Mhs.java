package com.asramakita.asramakita.mahasiswa.infokamar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.asramakita.asramakita.Adapter_Lantai;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.mahasiswa.Activity_Dashboard_Mhs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Sub_Kamar_Mhs extends AppCompatActivity {
    private RecyclerView rc_lantaiKamar;
    private Adapter_Lantai adapter_lantai;
    private ArrayList<CharSequence> lantai_list;
    private DatabaseReference dbLantai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_kamar_mhs);
        initComponents();
        getLantaiName();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Dashboard_Mhs.class));
        finish();
    }

    private void getLantaiName() {
        dbLantai.child("Kamar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    lantai_list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String key = dataSnapshot.getKey();
                        lantai_list.add(key);
                    }
                    adapter_lantai.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initComponents() {
        rc_lantaiKamar = findViewById(R.id.rc_lantaiKamar);
        rc_lantaiKamar.setHasFixedSize(true);
        rc_lantaiKamar.setLayoutManager(new GridLayoutManager(this, 2));
        lantai_list = new ArrayList<>();
        adapter_lantai = new Adapter_Lantai(this, lantai_list);
        rc_lantaiKamar.setAdapter(adapter_lantai);
        dbLantai = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }
}