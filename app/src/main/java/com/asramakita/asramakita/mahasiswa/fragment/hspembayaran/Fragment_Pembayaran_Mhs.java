package com.asramakita.asramakita.mahasiswa.fragment.hspembayaran;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.pembayaran.Pembayaran_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_Pembayaran_Mhs extends Fragment {

    View view;
    RecyclerView rcHsPembayaran;
    History_Pembayaran_Adapter myAdapter;
    ArrayList<Pembayaran_Model> myModel;
    DatabaseReference dbPembayaran;
    FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pembayaran_mhs, container, false);

        dbPembayaran = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference();

        initComponent();
        getData();

        return view;
    }

    private void initComponent(){
        rcHsPembayaran = view.findViewById(R.id.rcHsPembayaran);
        rcHsPembayaran.setHasFixedSize(true);
        rcHsPembayaran.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myModel = new ArrayList<>();
        myAdapter = new History_Pembayaran_Adapter(view.getContext(), myModel);
        rcHsPembayaran.setAdapter(myAdapter);
        mAuth = FirebaseAuth.getInstance();
    }

    private void getData(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String UID = firebaseUser.getUid();

        dbPembayaran.child("Pembayaran").child("Data")
                .orderByChild("id_mhs").equalTo(UID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            myModel.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String status = dataSnapshot.child("status").getValue().toString();
                                if (status.equals("dibayar")){
                                    Pembayaran_Model pembayaranModel = dataSnapshot.getValue(Pembayaran_Model.class);
                                    myModel.add(pembayaranModel);
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
