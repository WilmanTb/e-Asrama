package com.asramakita.asramakita.admin.checkout;

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
import com.asramakita.asramakita.admin.laporan.penghuni.Model_Laporan_Penghuni;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import papaya.in.sendmail.SendMail;

public class Desc_Permintaan_CheckOut extends AppCompatActivity {

    private TextView namaMhs, npmMhs, fakultasMhs, hpMhs, emailMhs, tanggal, alasanKeluar;
    private Model_CheckOut modelCheckOut;
    private DatabaseReference dbCheckOut;
    private Button btn_konfirmasi;
    private String checkOutKey, kamarKey, IDKamar = "", IDMhs = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc_permintaan_check_out);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbCheckOut = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        initComponents();
        getData();
        getCheckOutKey();
        getKamarKey();

        btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateField();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Permintaan_CheckOut.class));
        finish();
    }

    private void initComponents() {
        namaMhs = findViewById(R.id.namaMhs);
        npmMhs = findViewById(R.id.npmMhs);
        fakultasMhs = findViewById(R.id.fakultasMhs);
        hpMhs = findViewById(R.id.hpMhs);
        emailMhs = findViewById(R.id.emailMhs);
        tanggal = findViewById(R.id.tanggal);
        alasanKeluar = findViewById(R.id.alasanKeluar);
        btn_konfirmasi = findViewById(R.id.btn_konfirmasi);
    }

    private void getData() {
        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Model_CheckOut) {
            modelCheckOut = (Model_CheckOut) object;
        }

        if (modelCheckOut != null) {
            namaMhs.setText(modelCheckOut.getNama());
            npmMhs.setText(modelCheckOut.getNpm());
            fakultasMhs.setText(modelCheckOut.getFakultas());
            hpMhs.setText(modelCheckOut.getHp());
            emailMhs.setText(modelCheckOut.getEmail());
            tanggal.setText(modelCheckOut.getTanggal());
            alasanKeluar.setText(modelCheckOut.getAlasan());
        }
    }

    private void getCheckOutKey() {
        dbCheckOut.child("CheckOut").orderByChild("npm").equalTo(modelCheckOut.getNpm())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                checkOutKey = dataSnapshot.getKey();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void getKamarKey() {
        dbCheckOut.child("Kamar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        kamarKey = dataSnapshot.getKey();
                        getKamarMhs(kamarKey);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getKamarMhs(String key) {
        dbCheckOut.child("Kamar").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String keyLt = dataSnapshot.getKey();
                        dbCheckOut.child("Kamar").child(key).child(keyLt).orderByChild("npm").equalTo(modelCheckOut.getNpm()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    String keyltMhs = dataSnapshot1.getKey();
                                    IDMhs = keyltMhs;
                                    IDKamar = snapshot.getKey();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateField() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = simpleDateFormat.format(calendar.getTime());

        dbCheckOut.child("CheckOut").child(checkOutKey).child("status").setValue("diterima");
        dbCheckOut.child("CheckOut").child(checkOutKey).child("tanggal").setValue(currentDate);
        dbCheckOut.child("Users").child(checkOutKey).child("status").setValue("keluar");
        dbCheckOut.child("Kamar").child(IDKamar).child(IDMhs).setValue(null);

        SendMail mail = new SendMail("easramaunika@gmail.com", "buxjgrsshtvvxgbs",
                modelCheckOut.getEmail(),
                "Permintaan Check Out Asrama",
                "Halo " + modelCheckOut.getNama() + " Permintaan check out dari asrama anda telah diterima. Terimakasih telah menjadi anggota asrama putri Unika :) ");
        mail.execute();

        startActivity(new Intent(this, Activity_Dashboard_Admin.class));
        finish();
        Toast.makeText(this, "Permintaan Check Out berhasil dikonfirmasi", Toast.LENGTH_SHORT).show();

    }
}