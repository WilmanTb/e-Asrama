package com.asramakita.asramakita;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.asramakita.asramakita.mahasiswa.ijinkeluar.Model_ijin_keluar;

public class Activity_Detail_History_Keluar_Admin extends AppCompatActivity {

    private TextView namaPengaju,npmPengaju, jamKeluar, jamKembali, keteranganKeluar, statusijin;
    private Model_ijin_keluar modelIjinKeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history_keluar_admin);

        initComponents();
        getData();
    }

    private void initComponents(){
        namaPengaju = findViewById(R.id.namaPengaju);
        npmPengaju = findViewById(R.id.npmPengaju);
        jamKeluar = findViewById(R.id.jamKeluar);
        jamKembali = findViewById(R.id.jamKembali);
        keteranganKeluar = findViewById(R.id.keteranganKeluar);
        statusijin = findViewById(R.id.statusIjin);
    }

    private void getData(){
        final Object obj = getIntent().getSerializableExtra("deskripsi");
        if (obj instanceof Model_ijin_keluar){
            modelIjinKeluar = (Model_ijin_keluar) obj;
        }

        if (modelIjinKeluar!=null){
            String jamkeluar = modelIjinKeluar.getTanggalKeluar() + " " + modelIjinKeluar.getJam_keluar();
            String jamkembali = modelIjinKeluar.getTanggalKembali() + " " + modelIjinKeluar.getJam_kembali();
            namaPengaju.setText(modelIjinKeluar.getNama());
            npmPengaju.setText(modelIjinKeluar.getNpm());
            jamKeluar.setText(jamkeluar);
            jamKembali.setText(jamkembali);
            keteranganKeluar.setText(modelIjinKeluar.getKeterangan());
            String status = modelIjinKeluar.getStatus();
            if (status.equals("false")){
                statusijin.setText("Selesai");
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Acitivity_History_Keluar_Admin.class));
        finish();
    }
}