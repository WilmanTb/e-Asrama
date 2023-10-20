package com.asramakita.asramakita.admin.pembayaran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class Desc_Pembayaran extends AppCompatActivity {

    private TextView namaMahasiswa, npmMahasiswa, jns_pembayaran, tanggalPembayaran, jlh_dana;
    private ImageView imgBukti;
    private DatabaseReference dbPembayaran;
    private Button btnKonfirmasi;
    private Pembayaran_Model pembayaranModel;
    private String idMhs, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc_pembayaran);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbPembayaran = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Pembayaran");

        getId();
        getData();
        getKey();

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbPembayaran.child("Data").child(key).child("status").setValue("dibayar");
                Toast.makeText(Desc_Pembayaran.this, "Pembayaran berhasil dikonfirmasi", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Desc_Pembayaran.this, Activity_Dashboard_Admin.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Konfirmasi_Pembayaran.class));
        finish();
    }

    private void getId() {
        namaMahasiswa = findViewById(R.id.namaMahasiswa);
        npmMahasiswa = findViewById(R.id.npmMahasiswa);
        jns_pembayaran = findViewById(R.id.jns_pembayaran);
        tanggalPembayaran = findViewById(R.id.tanggalPembayaran);
        imgBukti = findViewById(R.id.imgBukti);
        btnKonfirmasi = findViewById(R.id.btnKonfirmasi);
        jlh_dana = findViewById(R.id.jlh_dana);
    }

    private void getData() {
        final Object object = getIntent().getSerializableExtra("deskripsi");
        if (object instanceof Pembayaran_Model) {
            pembayaranModel = (Pembayaran_Model) object;
        }

        if (pembayaranModel != null) {
            idMhs = pembayaranModel.getId_mhs();
            namaMahasiswa.setText(pembayaranModel.getNama());
            jns_pembayaran.setText(pembayaranModel.getJenis());
            npmMahasiswa.setText(pembayaranModel.getNpm());
            tanggalPembayaran.setText(pembayaranModel.getTanggal());
            namaMahasiswa.setText(pembayaranModel.getNama());
            jlh_dana.setText(formatRupiah(Double.parseDouble(pembayaranModel.getJumlah_dana())));
            Glide.with(getApplicationContext()).load(pembayaranModel.getBukti()).into(imgBukti);
        }
    }

    private void getKey(){
        dbPembayaran.child("Data").orderByChild("npm").equalTo(pembayaranModel.getNpm()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        String status = dataSnapshot.child("status").getValue().toString();
                        if (status.equals("diajukan")) {
                            key = dataSnapshot.getKey();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }
}