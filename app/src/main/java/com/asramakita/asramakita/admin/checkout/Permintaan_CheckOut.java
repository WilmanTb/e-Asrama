package com.asramakita.asramakita.admin.checkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Permintaan_CheckOut extends AppCompatActivity {

    private RecyclerView rcCheckOut;
    private DatabaseReference dbCheckOut;
    private String Key;
    private Adapter_CheckOut myAdapter;
    private ArrayList<Model_CheckOut> myModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permintaan_check_out);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbCheckOut = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("CheckOut");

        initComponents();
        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Dashboard_Admin.class));
        finish();
    }

    private void initComponents() {
        rcCheckOut = findViewById(R.id.rcCheckOut);
        rcCheckOut.setHasFixedSize(true);
        rcCheckOut.setLayoutManager(new LinearLayoutManager(this));
        myModel = new ArrayList<>();
        myAdapter = new Adapter_CheckOut(this, myModel);
        rcCheckOut.setAdapter(myAdapter);
    }

    private void getData(){
        dbCheckOut.orderByChild("status").equalTo("diminta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    myModel.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Model_CheckOut modelCheckOut = dataSnapshot.getValue(Model_CheckOut.class);
                        myModel.add(modelCheckOut);
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}