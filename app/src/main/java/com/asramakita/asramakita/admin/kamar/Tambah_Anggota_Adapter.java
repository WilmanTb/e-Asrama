package com.asramakita.asramakita.admin.kamar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asramakita.asramakita.Adapter_Kamar_Grid;
import com.asramakita.asramakita.Adapter_Lantai;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.UIDStorage;
import com.asramakita.asramakita.admin.permintaanpendaftaran.Pendaftaran_Class;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tambah_Anggota_Adapter extends RecyclerView.Adapter<Tambah_Anggota_Adapter.MyViewHolder> {

    Context context;
    ArrayList<Pendaftaran_Class> myList;

    String key, nama, npm, fakultas,ID, IDUSER, Parent, alamat;

    public Tambah_Anggota_Adapter(Context context, ArrayList<Pendaftaran_Class> myList) {
        this.context = context;
        this.myList = myList;
    }

    private DatabaseReference mDatabase;

    public Tambah_Anggota_Adapter(DatabaseReference databaseReference) {
        mDatabase = databaseReference;
    }
    public void updateDataInFirebase(String key,String key2, String newValue) {
        mDatabase.child(key).child(key2).setValue(newValue);
    }

    @NonNull
    @Override
    public Tambah_Anggota_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_tambah_penghuni,parent, false);
        return new Tambah_Anggota_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Tambah_Anggota_Adapter.MyViewHolder holder, int position) {
        Pendaftaran_Class kamarModel = myList.get(position);
        holder.namaPenghuni.setText(kamarModel.getNama());
        holder.btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase
                        .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .getReference("PermintaanPendaftaran").child("Data");

                mDatabase.orderByChild("npm").equalTo(kamarModel.getNpm()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                           key = dataSnapshot.getKey();
                        }
                        updateDataInFirebase(key, "status","ditempatkan");
                        mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                nama = snapshot.child("nama").getValue().toString();
                                npm = snapshot.child("npm").getValue().toString();
                                fakultas = snapshot.child("fakultas").getValue().toString();
                                alamat = snapshot.child("alamat").getValue().toString();
                                if (Adapter_Kamar_Grid.namaKamar != null)ID = Adapter_Kamar_Grid.namaKamar;
                                if (Informasi_Kamar.Key != null)ID = Informasi_Kamar.Key;
                                Parent = UIDStorage.getInstance().getNamaLantai();
                                DatabaseReference dbKamar = FirebaseDatabase
                                        .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                        .getReference("Kamar");
                                DatabaseReference dbUser = FirebaseDatabase
                                        .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                        .getReference("Users");
                                dbUser.orderByChild("npm").equalTo(npm).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            IDUSER = dataSnapshot.getKey();
                                        }
                                        dbUser.child(IDUSER).child("status").setValue("penghuni");
                                        context.startActivity(new Intent(context, Informasi_Kamar.class));
                                        Kamar_Model kamar_model = new Kamar_Model(nama, npm, fakultas, alamat, IDUSER);
                                        dbKamar.child(Parent).child(ID).child(key).setValue(kamar_model);
                                        Toast.makeText(context, "Mahasiswa berhasil ditambah ke "+ID, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(context, IDUSER, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

//                                Toast.makeText(context, "Mahasiswa berhasil ditambah ke "+ID, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(context, IDUSER, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView namaPenghuni;
        Button btnTambah;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            namaPenghuni = itemView.findViewById(R.id.namaPenghuni);
            btnTambah = itemView.findViewById(R.id.btnTambah);
        }
    }
}
