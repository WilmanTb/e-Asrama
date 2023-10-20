package com.asramakita.asramakita.admin.laporan.penghuni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Detail_Penghuni_Asrama extends AppCompatActivity {

    private TextView namaMhs, npmMhs, fakultasMhs, hpMhs, emailMhs;
    private DatabaseReference dbAsrama;
    private Model_Laporan_Penghuni modelLaporanPenghuni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_penghuni_asrama);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbAsrama = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users");

        getID();
        getData();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Penghuni_Asrama.class));
        finish();
    }

    private void getData() {
        final Object object = getIntent().getSerializableExtra("deskripsi");
        if (object instanceof Model_Laporan_Penghuni){
            modelLaporanPenghuni = (Model_Laporan_Penghuni) object;
        }

        if (modelLaporanPenghuni != null){
            namaMhs.setText(modelLaporanPenghuni.getNama());
            npmMhs.setText(modelLaporanPenghuni.getNpm());
            fakultasMhs.setText(modelLaporanPenghuni.getFakultas());
            hpMhs.setText(modelLaporanPenghuni.getHp());
            emailMhs.setText(modelLaporanPenghuni.getEmail());
        }
    }

    private void getID(){
        namaMhs = findViewById(R.id.namaMhs);
        npmMhs = findViewById(R.id.npmMhs);
        fakultasMhs = findViewById(R.id.fakultasMhs);
        hpMhs = findViewById(R.id.hpMhs);
        emailMhs = findViewById(R.id.emailMhs);
    }
}