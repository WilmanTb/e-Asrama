package com.asramakita.asramakita.admin.informasiasrama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tentang_Asrama extends AppCompatActivity {

    private Button btnTambah, btnEdit, btnHapus, btnSimpan;
    private EditText etTentang;
    private String tentangAsrama;
    private int counter;
    private DatabaseReference dbTentang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang_asrama);

        getID();
        getData();

        dbTentang = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        counter = 0;

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = 1;
                btnAction(counter);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = 2;
                btnAction(counter);
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = 3;
                btnAction(counter);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Informasi_Asrama.class));
        finish();
    }

    private void getID(){
        btnEdit = findViewById(R.id.btnEdit);
        btnTambah = findViewById(R.id.btnTambah);
        btnHapus = findViewById(R.id.btnHapus);
        btnSimpan = findViewById(R.id.btnSimpan);
        etTentang = findViewById(R.id.etTentang);
    }

    private void getData(){
        dbTentang = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Asrama").child("Tentang");
        dbTentang.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    tentangAsrama = snapshot.getValue().toString();
                    btnTambah.setVisibility(View.GONE);
                    etTentang.setText(tentangAsrama);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void btnAction(int counter){
        if (counter == 1){
            etTentang.setEnabled(true);
            btnSimpan.setVisibility(View.VISIBLE);
        }else if (counter == 2){
            etTentang.setEnabled(true);
            btnSimpan.setVisibility(View.VISIBLE);
        }else if (counter == 3){
            dbTentang = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Asrama").child("Tentang");
            dbTentang.setValue(null);
        }
    }

    private void saveData(){
        tentangAsrama = etTentang.getText().toString();
        if (tentangAsrama.isEmpty()){
            Toast.makeText(this, "Form tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        }else {
            dbTentang = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Asrama").child("Tentang");
            dbTentang.setValue(tentangAsrama);

            Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
            etTentang.setEnabled(false);
            btnSimpan.setVisibility(View.INVISIBLE);

        }
    }
}