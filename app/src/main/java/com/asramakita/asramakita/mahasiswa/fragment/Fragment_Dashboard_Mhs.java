package com.asramakita.asramakita.mahasiswa.fragment;

import android.app.Notification;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.pembayaran.Konfirmasi_Pembayaran;
import com.asramakita.asramakita.mahasiswa.pembayaranmhs.Activity_Pembayaran_Mhs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Fragment_Dashboard_Mhs extends Fragment {

    View view;
    DatabaseReference dbPembayaran;
    FirebaseAuth mAuth;
    CardView cvNotif;
    TextView jlhNotif;

    int code = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard_mhs, container, false);

        dbPembayaran = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        code = Activity_Pembayaran_Mhs.code;
        initComponents();
        checkNewData();


        return view;
    }

    private void initComponents() {
        mAuth = FirebaseAuth.getInstance();
        cvNotif = view.findViewById(R.id.cvNotif);
        jlhNotif = view.findViewById(R.id.jlhNotif);

    }

    private void checkNewData() {
        if (code != 1) {
            int counter = 1;
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String UID = currentUser.getUid();
            dbPembayaran.child("Pembayaran").child("Data").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String Key = snapshot.getKey();
                    dbPembayaran.child("Pembayaran").child("Data").child(Key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String idMhs = snapshot.child("id_mhs").getValue().toString();
                                String status = snapshot.child("status").getValue().toString();
                                if (idMhs.equals(UID) && status.equals("belum dibayar")) {
                                    cvNotif.setVisibility(View.VISIBLE);
                                    jlhNotif.setText(String.valueOf(counter));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        cvNotif.setVisibility(View.GONE);
        jlhNotif.setText("0");
    }
}
