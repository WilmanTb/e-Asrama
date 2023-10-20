package com.asramakita.asramakita.admin.laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.asramakita.asramakita.admin.laporan.lpkamar.Laporan_Kamar;
import com.asramakita.asramakita.admin.laporan.lppembayaran.Laporan_Pembayaran;
import com.asramakita.asramakita.admin.laporan.penghuni.Penghuni_Asrama;

public class Sub_Laporan extends AppCompatActivity {

    private CardView penghuniAsrama, pembayaranAsrama, kamarAsrama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_laporan);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        penghuniAsrama = findViewById(R.id.penghuniAsrama);
        pembayaranAsrama = findViewById(R.id.pembayaranAsrama);
//        kamarAsrama = findViewById(R.id.kamarAsrama);

        penghuniAsrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sub_Laporan.this, Penghuni_Asrama.class));
            }
        });

        pembayaranAsrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sub_Laporan.this, Laporan_Pembayaran.class));
            }
        });

//        kamarAsrama.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Sub_Laporan.this, Laporan_Kamar.class));
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Dashboard_Admin.class));
        finish();
    }
}