package com.asramakita.asramakita;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.asramakita.asramakita.admin.kamar.Kamar_Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Activity_Pindahkan_Penghuni_Kamar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spin_lantai;
    private ArrayAdapter<CharSequence> adapterSpinnerLantai;
    private String selectedLantai;
    private DatabaseReference dbKamar;
    private Adapter_Pindahkan_Penghuni adapter_pindahkan_penghuni;
    private ArrayList<CharSequence> kamarArrayList;
    private RecyclerView rc_list_kamar;
    private Kamar_Model kamar_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pindahkan_penghuni_kamar);

        initComponents();
        getSpinnerData();

    }

    private void initComponents() {
        spin_lantai = findViewById(R.id.spin_lantai);
        rc_list_kamar = findViewById(R.id.rc_list_kamar);
        rc_list_kamar.setHasFixedSize(true);
        rc_list_kamar.setLayoutManager(new LinearLayoutManager(this));
        kamarArrayList = new ArrayList<>();
        adapter_pindahkan_penghuni = new Adapter_Pindahkan_Penghuni(this, kamarArrayList);
        rc_list_kamar.setAdapter(adapter_pindahkan_penghuni);

        dbKamar = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    private void getSpinnerData() {
        adapterSpinnerLantai = ArrayAdapter.createFromResource(this, R.array.Lantai, android.R.layout.simple_spinner_item);
        adapterSpinnerLantai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_lantai.setAdapter(adapterSpinnerLantai);
        spin_lantai.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spin_lantai = (Spinner) parent;
        if (!parent.getItemAtPosition(position).equals("--Pilih Lantai--")) {
            String lantai = parent.getItemAtPosition(position).toString();
            if (lantai.equals("Lantai 2")) selectedLantai = "Lt2";
            else selectedLantai = "Lt3";
            Toast.makeText(this, lantai + " dipilih", Toast.LENGTH_SHORT).show();
            getKamarData(selectedLantai);
            getMhsUid(selectedLantai);
        } else {
            kamarArrayList.clear();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void getKamarData(String selectedLantai) {
        dbKamar.child("Kamar").child(selectedLantai).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    kamarArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String kamarName = dataSnapshot.getKey();
                        // Initialize isAvailable to true for each kamarName
                        boolean isAvailable = true;
                        DataSnapshot kamarSnapshot = dataSnapshot;
                        long qoutaKamar = kamarSnapshot.getChildrenCount();
                        if (qoutaKamar != 4) {
                            isAvailable = false;
                        }
                        if (!isAvailable) {
                            kamarArrayList.add(kamarName);
                        }
                    }
                    // Sort and update the adapter after checking all kamar rooms
                    Collections.sort(kamarArrayList, new Comparator<CharSequence>() {
                        @Override
                        public int compare(CharSequence k1, CharSequence k2) {
                            String kamar1 = k1.toString();
                            String kamar2 = k2.toString();
                            // Extract the numeric part and compare as integers
                            int num1 = Integer.parseInt(kamar1.replace("Kamar ", ""));
                            int num2 = Integer.parseInt(kamar2.replace("Kamar ", ""));
                            return Integer.compare(num1, num2);
                        }
                    });
                    adapter_pindahkan_penghuni.notifyDataSetChanged();
                } else {
                    // Handle the case when no data is found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });
    }


    private void getMhsUid(String selectedLantai) {
        Object obj = getIntent().getSerializableExtra("detail");
        if (obj instanceof Kamar_Model){
            kamar_model = (Kamar_Model) obj;
        }

        if (kamar_model!=null){
            String uid = kamar_model.getIdMhs();
            String nama = kamar_model.getNama();
            String npm = kamar_model.getNpm();
            String alamat = kamar_model.getAlamat();
            String fakultas = kamar_model.getFakultas();
            UIDStorage.getInstance().setUid(uid);
            UIDStorage.getInstance().setNama(nama);
            UIDStorage.getInstance().setNpm(npm);
            UIDStorage.getInstance().setAlamat(alamat);
            UIDStorage.getInstance().setFakultas(fakultas);
            UIDStorage.getInstance().setSelectedLantai(selectedLantai);
        }
    }
}