package com.asramakita.asramakita.mahasiswa.infoasrama;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.fasilitas.fasilitas_Class;
import com.asramakita.asramakita.admin.informasiasrama.pdfClass;
import com.asramakita.asramakita.mahasiswa.Activity_Dashboard_Mhs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Activity_Informasi_Asrama_Mhs extends AppCompatActivity {

    private ImageView dropTentang, dropFasilitas, dropPeraturan, dropPembayaran;
    private TextView tentangAsrama, infoPembayaran;
    private ListView listPeraturan, listFasilitas;
    private DatabaseReference dbAsrama;
    private List<fasilitas_Class> fasilitasClassList;
    private List<pdfClass> pdfClassList;
    private int drop = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_asrama_mhs);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbAsrama = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        initComponents();
        setVisibility();
        getTentang();
        getFasilitas();
        getPeraturan();

        listPeraturan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pdfClass pdfUpload = pdfClassList.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf");
                intent.setData(Uri.parse(pdfUpload.getUrl()));
                startActivity(intent);
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
        dropTentang = findViewById(R.id.dropTentang);
        dropFasilitas = findViewById(R.id.dropFasilitas);
        dropPeraturan = findViewById(R.id.dropPeraturan);
        dropPembayaran = findViewById(R.id.dropPembayaran);
        tentangAsrama = findViewById(R.id.tentangAsrama);
        infoPembayaran = findViewById(R.id.infoPembayaran);
        listPeraturan = findViewById(R.id.listPeraturan);
        listFasilitas = findViewById(R.id.listFasilitas);
        fasilitasClassList = new ArrayList<>();
        pdfClassList = new ArrayList<>();
    }

    public void dropClick(View view) {
        switch (view.getId()) {
            case R.id.dropTentang:
                drop = 1;
                dropItem(drop);
                break;

            case R.id.dropFasilitas:
                drop = 2;
                dropItem(drop);
                break;

            case R.id.dropPeraturan:
                drop = 3;
                dropItem(drop);
                break;

            case R.id.dropPembayaran:
                drop = 4;
                dropItem(drop);
                break;
        }
    }

    private void dropItem(int drop) {
        if (drop == 1) {
            tentangAsrama.setVisibility(View.VISIBLE);
            listFasilitas.setVisibility(View.GONE);
            listPeraturan.setVisibility(View.GONE);
            infoPembayaran.setVisibility(View.GONE);
        } else if (drop == 2) {
            tentangAsrama.setVisibility(View.GONE);
            listFasilitas.setVisibility(View.VISIBLE);
            listPeraturan.setVisibility(View.GONE);
            infoPembayaran.setVisibility(View.GONE);
        } else if (drop == 3) {
            tentangAsrama.setVisibility(View.GONE);
            listFasilitas.setVisibility(View.GONE);
            listPeraturan.setVisibility(View.VISIBLE);
            infoPembayaran.setVisibility(View.GONE);
        } else if (drop == 4) {
            tentangAsrama.setVisibility(View.GONE);
            listFasilitas.setVisibility(View.GONE);
            listPeraturan.setVisibility(View.GONE);
            infoPembayaran.setVisibility(View.VISIBLE);
        }
    }

    private void getTentang() {
        dbAsrama.child("Asrama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String tentang = snapshot.child("Tentang").getValue().toString();
                    tentangAsrama.setText(tentang);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFasilitas() {
        dbAsrama.child("Fasilitas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    fasilitasClassList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        fasilitas_Class fasilitasClass = dataSnapshot.getValue(fasilitas_Class.class);
                        fasilitasClassList.add(fasilitasClass);
                    }
                    String[] Uploads = new String[fasilitasClassList.size()];
                    for (int i = 0; i < Uploads.length; i++) {
                        Uploads[i] = fasilitasClassList.get(i).getNama();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, Uploads) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView textView = (TextView) view.findViewById(android.R.id.text1);
                            textView.setTextSize(15);
                            return view;
                        }
                    };
                    listFasilitas.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPeraturan(){
        dbAsrama.child("Peraturan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    pdfClassList.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        pdfClass PDFclass = dataSnapshot.getValue(pdfClass.class);
                        pdfClassList.add(PDFclass);
                    }
                    String[] Uploads = new String[pdfClassList.size()];
                    for (int i = 0; i < Uploads.length; i++) {
                        Uploads[i] = pdfClassList.get(i).getNama();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, Uploads) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView textView = (TextView) view.findViewById(android.R.id.text1);
                            textView.setTextSize(15);

                            return view;
                        }
                    };
                    listPeraturan.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setVisibility() {
        tentangAsrama.setVisibility(View.GONE);
        listFasilitas.setVisibility(View.GONE);
        listPeraturan.setVisibility(View.GONE);
        infoPembayaran.setVisibility(View.GONE);
    }
}