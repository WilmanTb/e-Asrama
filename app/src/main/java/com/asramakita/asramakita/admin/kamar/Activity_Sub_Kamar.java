package com.asramakita.asramakita.admin.kamar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.asramakita.asramakita.Adapter_Lantai;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.asramakita.asramakita.mahasiswa.Activity_Dashboard_Mhs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_Sub_Kamar extends AppCompatActivity {

    private RecyclerView rc_lantaiKamar;
    private Button btn_tambah_lantai;
    private Adapter_Lantai adapter_lantai;
    private ArrayList<CharSequence> lantai_list;
    private DatabaseReference dbLantai;
    private long totalLantai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_kamar);

        initComponents();
        getLantaiName();

        btn_tambah_lantai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Activity_Sub_Kamar.this)
                        .setMessage("Yakin menambah lantai ?\nLantai berikutnya adalah Lantai "+ (totalLantai + 2))
                        .setPositiveButton("Tambah", null)
                        .setNegativeButton("Batal", null)
                        .show();
                Button positiveButton = alertDialog.getButton(alertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbLantai.child("Kamar").child("Lt"+(totalLantai+2)).setValue("");
                        alertDialog.dismiss();
                        Toast.makeText(Activity_Sub_Kamar.this, "Lantai berhasil ditambah", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void getLantaiName() {
        dbLantai.child("Kamar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    totalLantai = 0;
                    lantai_list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String key = dataSnapshot.getKey();
                        lantai_list.add(key);
                    }
                    adapter_lantai.notifyDataSetChanged();
                    totalLantai = snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initComponents() {
        btn_tambah_lantai = findViewById(R.id.btn_tambah_lantai);
        rc_lantaiKamar = findViewById(R.id.rc_lantaiKamar);
        rc_lantaiKamar.setHasFixedSize(true);
        rc_lantaiKamar.setLayoutManager(new GridLayoutManager(this, 2));
        lantai_list = new ArrayList<>();
        adapter_lantai = new Adapter_Lantai(this, lantai_list);
        rc_lantaiKamar.setAdapter(adapter_lantai);
        dbLantai = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Dashboard_Admin.class));
        finish();
    }

}