package com.asramakita.asramakita.mahasiswa.ijinkeluar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.mahasiswa.Activity_Dashboard_Mhs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_Lapor_Kembali_Mhs extends AppCompatActivity {

    private TextView namaPengaju, npmPengaju, jamKeluar, jamKembali, keteranganKeluar;
    private Button btn_lapor;
    private DatabaseReference dbIjin;
    private FirebaseAuth userAuth;
    private Model_ijin_keluar modelIjinKeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor_kembali_mhs);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();
        getDetail();
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        String UID = firebaseUser.getUid();

        btn_lapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbIjin.child("IjinKeluar").child(UID).child("status").setValue("diajukan");
                startActivity(new Intent(Activity_Lapor_Kembali_Mhs.this, Activity_Dashboard_Mhs.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Dashboard_Mhs.class));
        finish();
    }

    private void initComponents(){
        namaPengaju = findViewById(R.id.namaPengaju);
        npmPengaju = findViewById(R.id.npmPengaju);
        jamKeluar = findViewById(R.id.jamKeluar);
        jamKembali = findViewById(R.id.jamKembali);
        keteranganKeluar = findViewById(R.id.keteranganKeluar);
        btn_lapor = findViewById(R.id.btn_lapor);
        userAuth = FirebaseAuth.getInstance();
        dbIjin = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    private void getDetail(){
        final Object obj = getIntent().getSerializableExtra("deskripsi");
        if (obj instanceof Model_ijin_keluar){
            modelIjinKeluar = (Model_ijin_keluar) obj;
        }

        if (modelIjinKeluar != null){
            String dateKeluar = modelIjinKeluar.getTanggalKeluar() + " " + modelIjinKeluar.getJam_keluar();
            String dateKembali = modelIjinKeluar.getTanggalKembali() + " " + modelIjinKeluar.getJam_kembali();
            namaPengaju.setText(modelIjinKeluar.getNama());
            npmPengaju.setText(modelIjinKeluar.getNpm());
            jamKeluar.setText(dateKeluar);
            jamKembali.setText(dateKembali);
            keteranganKeluar.setText(modelIjinKeluar.getKeterangan());
        }
    }

}