package com.asramakita.asramakita.admin.permintaanpendaftaran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.pembayaran.Pembayaran_Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import papaya.in.sendmail.SendMail;

public class Desc_Pendaftar extends AppCompatActivity {

    private TextView namaPendaftar,npmPendaftar,fakultasPendaftar,asalSekolah,emailPendaftar,hpPendaftar,altOrangTua,hpOrangTua,tglDaftar, alamatPendaftar;
    private Button btnKonfirmasi;
    private Pendaftaran_Class pendaftaranClass;
    private DatabaseReference dbRef;
    private String key, EMAIL, NAMA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc_pendaftar);

        dbRef = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        getId();
        getData();
        getNpm();

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.child("PermintaanPendaftaran").child("Data").child(key).child("status").setValue("diterima");
                setPembayaran();
                Toast.makeText(Desc_Pendaftar.this, "Permintaan pendaftaran berhasil di konfirmasi", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Desc_Pendaftar.this, Permintaan_Pendaftaran.class));
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Permintaan_Pendaftaran.class));
        finish();
    }

    private void getId(){
        namaPendaftar = findViewById(R.id.namaPendaftar);
        npmPendaftar = findViewById(R.id.npmPendaftar);
        emailPendaftar = findViewById(R.id.emailPendaftar);
        fakultasPendaftar = findViewById(R.id.fakultasPendaftar);
        asalSekolah = findViewById(R.id.asalSekolah);
        hpPendaftar = findViewById(R.id.hpPendaftar);
        altOrangTua = findViewById(R.id.altOrangTua);
        hpOrangTua = findViewById(R.id.hpOrangTua);
        tglDaftar = findViewById(R.id.tglDaftar);
        btnKonfirmasi = findViewById(R.id.btnKonfirmasi);
        alamatPendaftar = findViewById(R.id.alamatPendaftar);
    }

    public void getData(){
        final Object object = getIntent().getSerializableExtra("deskripsi");
        if (object instanceof Pendaftaran_Class){
            pendaftaranClass = (Pendaftaran_Class) object;
        }

        if (pendaftaranClass != null){
            namaPendaftar.setText(pendaftaranClass.getNama());
            npmPendaftar.setText(pendaftaranClass.getNpm());
            fakultasPendaftar.setText(pendaftaranClass.getFakultas());
            asalSekolah.setText(pendaftaranClass.getAsalsekolah());
            hpPendaftar.setText(pendaftaranClass.getNohp());
            altOrangTua.setText(pendaftaranClass.getAlmtorangtua());
            hpOrangTua.setText(pendaftaranClass.getHporangtua());
            tglDaftar.setText(pendaftaranClass.getTanggal());
            emailPendaftar.setText(pendaftaranClass.getEmail());
            alamatPendaftar.setText(pendaftaranClass.getAlamat());
        }
    }

    private void getNpm(){
        dbRef.child("PermintaanPendaftaran").child("Data").orderByChild("npm").equalTo(pendaftaranClass.getNpm()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        key = dataSnapshot.getKey();
                        EMAIL = dataSnapshot.child("email").getValue().toString();
                        NAMA = dataSnapshot.child("nama").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setPembayaran(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String tanggal = simpleDateFormat.format(calendar.getTime());

        Pembayaran_Model pembayaranModel = new Pembayaran_Model("empty", key, "Uang Pembangunan", "belum dibayar",  tanggal, pendaftaranClass.getNama(), pendaftaranClass.getNpm(), "3500000");
        Pembayaran_Model pembayaranModel1 = new Pembayaran_Model("empty", key, "Uang Bulanan", "belum dibayar",  tanggal, pendaftaranClass.getNama(), pendaftaranClass.getNpm(), "1110000");
        DatabaseReference dbPembayaran = dbRef.child("Pembayaran").child("Data");
        dbPembayaran.child(dbPembayaran.push().getKey()).setValue(pembayaranModel);
        dbPembayaran.child(dbPembayaran.push().getKey()).setValue(pembayaranModel1);
        SendMail mail = new SendMail("easramaunika@gmail.com", "buxjgrsshtvvxgbs",
                EMAIL,
                "Pembayaran Uang Pembangunan Asrama",
                "Halo "+ NAMA + " Permintaan pendaftaran masuk asrama anda telah diterima\nSilahkan melakukan pembayaran Uang Pembangunan & Uang Pembinaan sebesar Rp 3.500.000 ke nomor rekening berikut :\n0360679397(BNI)  YAYASAN ST. THOMAS");
        mail.execute();

        SendMail mail1 = new SendMail("easramaunika@gmail.com", "buxjgrsshtvvxgbs",
                EMAIL,
                "Pembayaran Uang Bulanan Asrama",
                "Halo "+ NAMA + " Silahkan melakukan pembayaran Uang Bulanan asrama sebesar Rp 1.110.000 ke nomor rekening berikut :\n0360679397(BNI)  YAYASAN ST. THOMAS");
        mail1.execute();
    }
}