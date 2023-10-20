package com.asramakita.asramakita.mahasiswa.pendaftaranmhs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.mahasiswa.Activity_Dashboard_Mhs;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Activity_Pendaftaran_Mhs extends AppCompatActivity {

    private EditText edit_altOrtu, edit_hpOrtu, edit_asalSekolah, edit_alamatMhs;
    private TextInputLayout input_nama, input_npm, input_hp, input_email;
    private Spinner spin_fakultas;
    private Button btnSubmit;
    private DatabaseReference dbUser;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String UID, Nama = "", NPM, Email, HP, Fakultas, AltOrangTua, HpOrangTua, AsalSekolah, Tanggal, Alamat;
    private ArrayAdapter<CharSequence> adapterFakultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftaran_mhs);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbUser = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        initComponents();
        spin_fakultas.setEnabled(false);
        getUserData();
        getDate();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AltOrangTua = edit_altOrtu.getText().toString();
                HpOrangTua = edit_hpOrtu.getText().toString();
                AsalSekolah = edit_asalSekolah.getText().toString();
                Alamat = edit_alamatMhs.getText().toString();
                ajukanPendaftaran(NPM, Nama, Email, HP, Fakultas, AltOrangTua, HpOrangTua, AsalSekolah, Tanggal, Alamat);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Dashboard_Mhs.class));
        finish();
    }

    private void initComponents() {
        edit_altOrtu = findViewById(R.id.edit_altOrtu);
        edit_hpOrtu = findViewById(R.id.edit_hpOrtu);
        edit_asalSekolah = findViewById(R.id.edit_asalSekolah);
        input_nama = findViewById(R.id.input_nama);
        input_npm = findViewById(R.id.input_npm);
        input_hp = findViewById(R.id.input_hp);
        input_email = findViewById(R.id.input_email);
        edit_alamatMhs = findViewById(R.id.edit_alamatMhs);
        spin_fakultas = findViewById(R.id.spin_fakultas);
        btnSubmit = findViewById(R.id.btnSubmit);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        UID = currentUser.getUid();
    }

    private void getUserData() {
        dbUser.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Nama = snapshot.child("nama").getValue().toString();
                    NPM = snapshot.child("npm").getValue().toString();
                    Fakultas = snapshot.child("fakultas").getValue().toString();
                    HP = snapshot.child("hp").getValue().toString();
                    Email = snapshot.child("email").getValue().toString();
                    input_email.setHint(Email);
                    input_nama.setHint(Nama);
                    input_npm.setHint(NPM);
                    input_hp.setHint(HP);
                    spinnerFakultas(Fakultas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void spinnerFakultas(String Fakultas) {
        String[] stringArray = getResources().getStringArray(R.array.Fakultas);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stringArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_fakultas.setAdapter(spinnerAdapter);

        int selectedIndex = -1;
        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray[i].equals(Fakultas)) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex != -1) {
            spin_fakultas.setSelection(selectedIndex);
        } else {
            // Handle the case when the desired value is not found in the string array
        }

    }

    private void ajukanPendaftaran(String npm, String nama, String email, String hp, String fakultas, String altorangtua, String hporangtua, String asalSekolah, String tanggal, String alamat) {
        DatabaseReference dbRegister = FirebaseDatabase.
                getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").
                getReference("PermintaanPendaftaran").child("Data").child(UID);
        HashMap hashMap = new HashMap<>();
        hashMap.put("npm", npm);
        hashMap.put("nama", nama);
        hashMap.put("email", email);
        hashMap.put("nohp", hp);
        hashMap.put("fakultas", fakultas);
        hashMap.put("almtorangtua", altorangtua);
        hashMap.put("hporangtua", hporangtua);
        hashMap.put("asalsekolah", asalSekolah);
        hashMap.put("status", "diminta");
        hashMap.put("tanggal", tanggal);
        hashMap.put("alamat", alamat);
        dbRegister.setValue(hashMap);
        Toast.makeText(this, "Permintaan pendaftaran berhasil diajukan", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Activity_Dashboard_Mhs.class));
        finish();
    }

    private void getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Tanggal = simpleDateFormat.format(calendar.getTime());
    }

}