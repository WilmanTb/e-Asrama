package com.asramakita.asramakita.admin.fasilitas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.informasiasrama.Fasilitas_Asrama;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_Fasilitas extends AppCompatActivity {

    private Button btnSimpan, btnHapus;
    private EditText etFasilitas;
    private DatabaseReference dbFasilitas;
    private Toolbar toolbar;
    private String Fasilitas, Key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fasilitas);

        getID();

        dbFasilitas = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Fasilitas");

        Fasilitas = Fasilitas_Asrama.Fasilitas;

        etFasilitas.setText(Fasilitas);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFasilitas = etFasilitas.getText().toString();
                editFasilitas(new IDCallBack() {
                    @Override
                    public void onCallBack(String ID) {
                        dbFasilitas.child(ID).child("nama").setValue(newFasilitas);
                    }
                });
                startActivity(new Intent(Edit_Fasilitas.this, Fasilitas_Asrama.class));
                finish();
                Toast.makeText(Edit_Fasilitas.this, "Fasilitas berhasil diedit", Toast.LENGTH_SHORT).show();
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFasilitas(new IDCallBack() {
                    @Override
                    public void onCallBack(String ID) {
                        dbFasilitas.child(ID).setValue(null);
                    }
                });
                startActivity(new Intent(Edit_Fasilitas.this, Fasilitas_Asrama.class));
                finish();
                Toast.makeText(Edit_Fasilitas.this, "Fasilitas berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getID(){
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnHapus = findViewById(R.id.btnHapus);
        btnSimpan = findViewById(R.id.btnSimpan);
        etFasilitas = findViewById(R.id.etFasilitas);
    }

    private void editFasilitas(IDCallBack idCallBack){
        dbFasilitas.orderByChild("nama").equalTo(Fasilitas).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.exists()){
                        Key = dataSnapshot.getKey();
                    }
                    break;
                }
                idCallBack.onCallBack(Key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface IDCallBack{
        void onCallBack(String ID);
    }
}