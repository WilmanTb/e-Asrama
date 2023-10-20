package com.asramakita.asramakita.admin.ijinkeluar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.asramakita.asramakita.Acitivity_History_Keluar_Admin;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;

public class Activity_Sub_Ijin_Keluar_Admin extends AppCompatActivity {

    private CardView cv_lapor_kembali, cv_permintaan_ijin, cv_historyIjin;
    public static int SUBMENUIJIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_ijin_keluar_admin);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SUBMENUIJIN = 0;

        cv_lapor_kembali = findViewById(R.id.cv_lapor_kembali);
        cv_permintaan_ijin = findViewById(R.id.cv_permintaan_ijin);
        cv_historyIjin = findViewById(R.id.cv_historyIjin);

        cv_permintaan_ijin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Sub_Ijin_Keluar_Admin.this, Activity_Ijin_Keluar_Admin.class));
            }
        });

        cv_lapor_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Sub_Ijin_Keluar_Admin.this, Activity_Lapor_Kembali_Admin.class));
            }
        });

        cv_historyIjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Sub_Ijin_Keluar_Admin.this, Acitivity_History_Keluar_Admin.class));
                finish();
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