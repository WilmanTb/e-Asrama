package com.asramakita.asramakita.admin.informasiasrama;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.asramakita.asramakita.admin.fasilitas.Edit_Fasilitas;
import com.asramakita.asramakita.admin.fasilitas.Tambah_Fasilitas;
import com.asramakita.asramakita.admin.fasilitas.fasilitas_Class;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fasilitas_Asrama extends AppCompatActivity {

    private Button btnTambah;
    private ListView listFasilitas;
    private List<fasilitas_Class> fasilitasClass;
    private DatabaseReference dbFasilitas;

    public static String Fasilitas = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fasilitas_asrama);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnTambah = findViewById(R.id.btnTambah);
        listFasilitas = findViewById(R.id.listFasilitas);
        fasilitasClass = new ArrayList<>();

        dbFasilitas = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Fasilitas");

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Fasilitas_Asrama.this, Tambah_Fasilitas.class));
            }
        });

        listFasilitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fasilitas_Class fasilitas = fasilitasClass.get(position);
                Fasilitas = fasilitas.getNama();
                startActivity(new Intent(Fasilitas_Asrama.this, Edit_Fasilitas.class));
            }
        });

        viewAllFasilitas();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Informasi_Asrama.class));
        finish();
    }

    private void viewAllFasilitas(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Fasilitas");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fasilitasClass.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    fasilitas_Class fasilitas_class = dataSnapshot.getValue(fasilitas_Class.class);
                    fasilitasClass.add(fasilitas_class);
                }
                String[] Uploads = new String[fasilitasClass.size()];
                for (int i = 0; i < Uploads.length; i++) {
                    Uploads[i] = fasilitasClass.get(i).getNama();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, Uploads) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.BLACK);
                        textView.setTextSize(20);

                        return view;
                    }
                };
                listFasilitas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}