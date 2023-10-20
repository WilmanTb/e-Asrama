package com.asramakita.asramakita.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.asramakita.asramakita.admin.ijinkeluar.Activity_Sub_Ijin_Keluar_Admin;
import com.asramakita.asramakita.admin.kamar.Activity_Sub_Kamar;
import com.asramakita.asramakita.admin.checkout.Permintaan_CheckOut;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.informasiasrama.Informasi_Asrama;
import com.asramakita.asramakita.admin.laporan.Sub_Laporan;
import com.asramakita.asramakita.admin.pembayaran.Konfirmasi_Pembayaran;
import com.asramakita.asramakita.admin.permintaanpendaftaran.Permintaan_Pendaftaran;

public class Activity_Dashboard_Admin extends AppCompatActivity {

    private CardView cvInfoAsrama, cvPermintaanPendaftaran, cvPembayaran, cvInfoKamar, cvLaporan, cvCheckOut, cvIjin;
    private int backPressCount;
    public static int userRole = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        getID();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        cvInfoAsrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Dashboard_Admin.this, Informasi_Asrama.class));
            }
        });

        cvPermintaanPendaftaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Dashboard_Admin.this, Permintaan_Pendaftaran.class));
            }
        });

        cvPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Dashboard_Admin.this, Konfirmasi_Pembayaran.class));
            }
        });

        cvInfoKamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Dashboard_Admin.this, Activity_Sub_Kamar.class));
            }
        });

        cvLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Dashboard_Admin.this, Sub_Laporan.class));
            }
        });

        cvCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Dashboard_Admin.this, Permintaan_CheckOut.class));
            }
        });

        cvIjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Dashboard_Admin.this, Activity_Sub_Ijin_Keluar_Admin.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
            backPressCount++;

            if (backPressCount == 1) {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

                // Reset the back press count after a certain duration (e.g., 2 seconds)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressCount = 0;
                    }
                }, 2000);
            } else if (backPressCount == 2) {
                // If the back button is pressed twice within the specified duration, exit the app
                super.onBackPressed();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finishAffinity();
            }
    }

    private void getID(){
        cvInfoAsrama = findViewById(R.id.cvInfoAsrama);
        cvPermintaanPendaftaran = findViewById(R.id.cvPermintaanPendaftaran);
        cvPembayaran = findViewById(R.id.cvPembayaran);
        cvInfoKamar = findViewById(R.id.cvInfoKamar);
        cvLaporan = findViewById(R.id.cvLaporan);
        cvCheckOut = findViewById(R.id.cvCheckOut);
        cvIjin = findViewById(R.id.cvIjin);
    }
}