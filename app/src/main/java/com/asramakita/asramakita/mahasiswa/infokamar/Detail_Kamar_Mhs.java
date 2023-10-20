package com.asramakita.asramakita.mahasiswa.infokamar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.asramakita.asramakita.Adapter_Lantai;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.UIDStorage;
import com.asramakita.asramakita.admin.kamar.Informasi_Kamar;
import com.asramakita.asramakita.admin.kamar.Kamar_Adapter;
import com.asramakita.asramakita.admin.kamar.Kamar_Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Detail_Kamar_Mhs extends AppCompatActivity {

    private String Key, Parent;
    private Kamar_Adapter kamarAdapter;
    private ArrayList<Kamar_Model> kamarModels;
    private DatabaseReference dbKamar;
    private TextView namaKamar, jlhPenghuni;
    private RecyclerView rcDetailKamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kamar_mhs);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Key = Activity_InfoKamar_Mhs.Key;
        Parent = UIDStorage.getInstance().getNamaLantai();

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_InfoKamar_Mhs.class));
        finish();
    }

    private void getID(){
        rcDetailKamar = findViewById(R.id.rcDetailKamar);
        namaKamar = findViewById(R.id.namaKamar);
        jlhPenghuni = findViewById(R.id.jlhPenghuni);
    }

    private void setData(){
        dbKamar.child(Parent).child(Key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    kamarModels.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Kamar_Model kamarModel = dataSnapshot.getValue(Kamar_Model.class);
                        kamarModels.add(kamarModel);
                    }
                    kamarAdapter.notifyDataSetChanged();
                }
                if (snapshot.getChildrenCount() > 0) {
                    jlhPenghuni.setText(String.valueOf(snapshot.getChildrenCount()));
                }else {
                    jlhPenghuni.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}