package com.asramakita.asramakita.admin.informasiasrama;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.asramakita.asramakita.Fasilitas_Kamar;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;

public class Informasi_Asrama extends AppCompatActivity {

    private CardView cvTentang, cvPeraturan, cvFasilitas, cvFasilitasKamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_asrama);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cvTentang = findViewById(R.id.cvTentangAsrama);
        cvPeraturan = findViewById(R.id.cvPeraturan);
        cvFasilitas = findViewById(R.id.cvFasilitas);
        cvFasilitasKamar = findViewById(R.id.cvFasilitasKamar);

        cvTentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Informasi_Asrama.this, Tentang_Asrama.class));
            }
        });

        cvPeraturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Informasi_Asrama.this, Peraturan_Asrama.class));
            }
        });

        cvFasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Informasi_Asrama.this, Fasilitas_Asrama.class));
            }
        });

        cvFasilitasKamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Informasi_Asrama.this, Fasilitas_Kamar.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Dashboard_Admin.class));
        finish();
    }
}