package com.asramakita.asramakita.admin.kamar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asramakita.asramakita.Adapter_Kamar_Grid;
import com.asramakita.asramakita.Adapter_Lantai;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.UIDStorage;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.asramakita.asramakita.mahasiswa.infokamar.Activity_InfoKamar_Mhs;
import com.asramakita.asramakita.mahasiswa.infokamar.Activity_Sub_Kamar_Mhs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Detail_Kamar extends AppCompatActivity {

    private String Key, Parent;
    private Kamar_Adapter kamarAdapter;
    private ArrayList<Kamar_Model> kamarModels;
    private DatabaseReference dbKamar;
    private TextView namaKamar, jlhPenghuni, ranjangKosong;
    private RecyclerView rcDetailKamar;
    private Button btnTambah;
    private final int Kuota = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kamar);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
        Parent = UIDStorage.getInstance().getNamaLantai();
        Key = Adapter_Kamar_Grid.namaKamar;

        dbKamar = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Kamar");

        getID();

        rcDetailKamar.setHasFixedSize(true);
        rcDetailKamar.setLayoutManager(new LinearLayoutManager(this));
        kamarModels = new ArrayList<>();
        kamarAdapter = new Kamar_Adapter(this, kamarModels);
        rcDetailKamar.setAdapter(kamarAdapter);

        namaKamar.setText(Key);


        setData();

        if (Activity_Dashboard_Admin.userRole==1) {
            btnTambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (jlhPenghuni.getText().toString().equals("4"))
                        Toast.makeText(Detail_Kamar.this, "Kamar Sudah Penuh", Toast.LENGTH_SHORT).show();
                    else
                        startActivity(new Intent(Detail_Kamar.this, Tambah_Penghuni.class));
                }
            });
        } else btnTambah.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Activity_Dashboard_Admin.userRole==1){
            startActivity(new Intent(this, Informasi_Kamar.class));
            finish();
        }else {
            startActivity(new Intent(this, Activity_InfoKamar_Mhs.class));
            finish();
        }

    }



    private void getID() {
        rcDetailKamar = findViewById(R.id.rcDetailKamar);
        namaKamar = findViewById(R.id.namaKamar);
        jlhPenghuni = findViewById(R.id.jlhPenghuni);
        ranjangKosong = findViewById(R.id.ranjangKosong);
        btnTambah = findViewById(R.id.btnTambah);
    }

    private void setData() {
        dbKamar.child(Parent).child(Key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    kamarModels.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Kamar_Model kamarModel = dataSnapshot.getValue(Kamar_Model.class);
                        kamarModels.add(kamarModel);
                    }
                    kamarAdapter.notifyDataSetChanged();
                }
                if (snapshot.getChildrenCount() > 0) {
                    jlhPenghuni.setText(String.valueOf(snapshot.getChildrenCount()));
                    ranjangKosong.setText(String.valueOf(Kuota - snapshot.getChildrenCount()));
                } else {
                    jlhPenghuni.setText("0");
                    ranjangKosong.setText(String.valueOf(Kuota));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}