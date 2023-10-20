package com.asramakita.asramakita.admin.fasilitas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.informasiasrama.Fasilitas_Asrama;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Tambah_Fasilitas extends AppCompatActivity {

    private Button btnTambahFasilitas;
    private EditText etFasilitas;
    private DatabaseReference dbFasilitas;
    private String Fasilitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_fasilitas);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnTambahFasilitas = findViewById(R.id.btnTambahFasilitas);
        etFasilitas = findViewById(R.id.etFasilitas);

        dbFasilitas = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Fasilitas");

        btnTambahFasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fasilitas = etFasilitas.getText().toString();
                inputFasilitas();
            }
        });
    }

    private void inputFasilitas() {
        if (Fasilitas.isEmpty()) {
            Toast.makeText(this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            dbFasilitas.child(dbFasilitas.push().getKey()).child("nama").setValue(Fasilitas);
            Toast.makeText(this, "Fasilitas berhasil ditambah", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Tambah_Fasilitas.this, Fasilitas_Asrama.class));
            finish();
        }
    }
}