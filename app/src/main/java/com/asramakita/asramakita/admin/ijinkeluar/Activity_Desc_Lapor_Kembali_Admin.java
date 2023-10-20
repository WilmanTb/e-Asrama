package com.asramakita.asramakita.admin.ijinkeluar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.asramakita.asramakita.mahasiswa.ijinkeluar.Model_ijin_keluar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_Desc_Lapor_Kembali_Admin extends AppCompatActivity {

    private TextView namaPengaju,npmPengaju, jamKeluar, jamKembali, keteranganKeluar;
    private Button btn_konfirmasi;
    private DatabaseReference dbIjinKeluar;
    private Model_ijin_keluar modelIjinKeluar;
    private String id_ijin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc_lapor_kembali_admin);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();
        getdata();
        getIdIjin();

        btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmIjin();
            }
        });
    }

    private void initComponents(){
        namaPengaju = findViewById(R.id.namaPengaju);
        npmPengaju = findViewById(R.id.npmPengaju);
        jamKeluar = findViewById(R.id.jamKeluar);
        jamKembali = findViewById(R.id.jamKembali);
        keteranganKeluar = findViewById(R.id.keteranganKeluar);
        btn_konfirmasi = findViewById(R.id.btn_konfirmasi);
        dbIjinKeluar = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    private void getdata(){
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

        }
    }

    private void getIdIjin(){
        dbIjinKeluar.child("IjinKeluar").orderByChild("npm").equalTo(modelIjinKeluar.getNpm()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        String status = dataSnapshot.child("status").getValue().toString();
                        if (status.equals("diajukan")){
                            id_ijin = dataSnapshot.child("id_ijin").getValue().toString();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void confirmIjin(){
        dbIjinKeluar.child("IjinKeluar").orderByChild("id_ijin").equalTo(id_ijin).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String key = dataSnapshot.getKey();
                    dbIjinKeluar.child("IjinKeluar").child(key).child("status").setValue("false");
                }
                Toast.makeText(Activity_Desc_Lapor_Kembali_Admin.this, "Laporan kembali mahasiswa berhasi dikonfirmasi", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Activity_Desc_Lapor_Kembali_Admin.this, Activity_Dashboard_Admin.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Lapor_Kembali_Admin.class));
        finish();
    }
}