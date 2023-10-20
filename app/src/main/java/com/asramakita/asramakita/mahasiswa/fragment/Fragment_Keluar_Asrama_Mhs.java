package com.asramakita.asramakita.mahasiswa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.mahasiswa.Activity_Dashboard_Mhs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Fragment_Keluar_Asrama_Mhs extends Fragment {

    View view;
    EditText et_alasan;
    FirebaseAuth mAuth;
    DatabaseReference dbUser;
    Button btn_submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_keluar_asrama_mhs, container, false);

        dbUser = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        initComponents();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatus();
            }
        });

        return view;
    }

    private void initComponents(){
        et_alasan = view.findViewById(R.id.et_alasan);
        btn_submit = view.findViewById(R.id.btn_submit);
        mAuth = FirebaseAuth.getInstance();

    }

    private void checkStatus(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String UID = firebaseUser.getUid();

        dbUser.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String Sts = snapshot.child("status").getValue().toString();
                    if (Sts.equals("penghuni")){
                        String Nama = snapshot.child("nama").getValue().toString();
                        String Npm = snapshot.child("npm").getValue().toString();
                        String Hp = snapshot.child("hp").getValue().toString();
                        String Email = snapshot.child("email").getValue().toString();
                        String Fakultas = snapshot.child("fakultas").getValue().toString();
                        requestCheckOut(UID, Nama, Npm, Fakultas, Hp, Email);
                    }
                }else {
                    Toast.makeText(view.getContext(), "Anda belum menjadi penghuni asrama", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void requestCheckOut(String UID, String nama, String npm, String fakultas, String hp, String email) {
        String Alasan = et_alasan.getText().toString();
        if (!Alasan.isEmpty()) {
            DatabaseReference dbCheckOut = FirebaseDatabase
                    .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("CheckOut");

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = simpleDateFormat.format(calendar.getTime());

            HashMap hashMap = new HashMap<>();
            hashMap.put("nama", nama);
            hashMap.put("npm", npm);
            hashMap.put("fakultas", fakultas);
            hashMap.put("hp", hp);
            hashMap.put("email", email);
            hashMap.put("status", "diminta");
            hashMap.put("alasan", Alasan);
            hashMap.put("tanggal", currentDate);
            dbCheckOut.child(UID).setValue(hashMap);
            Toast.makeText(view.getContext(), "Permintaan Check Out berhasil dikirim", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(view.getContext(), Activity_Dashboard_Mhs.class));
        } else {
            Toast.makeText(view.getContext(), "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
    }
}
