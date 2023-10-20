package com.asramakita.asramakita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.asramakita.asramakita.admin.ijinkeluar.Activity_Ijin_Keluar_Admin;
import com.asramakita.asramakita.admin.ijinkeluar.Activity_Sub_Ijin_Keluar_Admin;
import com.asramakita.asramakita.mahasiswa.ijinkeluar.Model_ijin_keluar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Acitivity_History_Keluar_Admin extends AppCompatActivity {

    private Adapter_History_Keluar adapterHistoryKeluar;
    private ArrayList<Model_ijin_keluar> modelIjinKeluar;
    private RecyclerView rc_historyIjin;
    private DatabaseReference dbIjinKeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acitivity_history_keluar_admin);
        initComponents();
        getData();
    }

    private void initComponents(){
        rc_historyIjin = findViewById(R.id.rc_historyKeluar);
        rc_historyIjin.setHasFixedSize(true);
        rc_historyIjin.setLayoutManager(new LinearLayoutManager(this));
        modelIjinKeluar = new ArrayList<>();
        adapterHistoryKeluar = new Adapter_History_Keluar(this, modelIjinKeluar);
        rc_historyIjin.setAdapter(adapterHistoryKeluar);
        dbIjinKeluar = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("IjinKeluar");
    }

    private void getData(){
        dbIjinKeluar.orderByChild("status").equalTo("false").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        modelIjinKeluar.clear();
                        Model_ijin_keluar model_ijin_keluar = dataSnapshot.getValue(Model_ijin_keluar.class);
                        modelIjinKeluar.add(model_ijin_keluar);
                    }
                    adapterHistoryKeluar.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Acitivity_History_Keluar_Admin.this, Activity_Sub_Ijin_Keluar_Admin.class));
        finish();
    }
}