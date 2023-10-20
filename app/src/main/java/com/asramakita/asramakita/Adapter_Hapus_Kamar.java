package com.asramakita.asramakita;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter_Hapus_Kamar extends RecyclerView.Adapter<Adapter_Hapus_Kamar.ViewHolder> {

    Context context;
    ArrayList<CharSequence> myList;

    public Adapter_Hapus_Kamar(Context context, ArrayList<CharSequence> myList) {
        this.context = context;
        this.myList = myList;
    }

    @NonNull
    @Override
    public Adapter_Hapus_Kamar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_kamar_hapus_kamar, parent, false);
        return new Adapter_Hapus_Kamar.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Hapus_Kamar.ViewHolder holder, int position) {
        String namaKamar = myList.get(position).toString();
        holder.nama_kamar.setText(namaKamar);
        String nomorLantai = UIDStorage.getInstance().getSelectedLantai();
        DatabaseReference dbKamar = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        holder.btn_hapus_kamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbKamar.child("Kamar").child(nomorLantai).child(namaKamar).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                String npmKey = dataSnapshot.getKey();
                                String npmValue = dataSnapshot.child("npm").getValue().toString();
                                dbKamar.child("PermintaanPendaftaran").child("Data").child(npmKey).child("status").setValue("diterima");
                                dbKamar.child("Users").child(npmKey).child("status").removeValue();
                                dbKamar.child("Kamar").child(nomorLantai).child(namaKamar).setValue(null);
                                Toast.makeText(context, "Kamar berhasil dihapus", Toast.LENGTH_SHORT).show();
                            }
                        }
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_kamar;
        Button btn_hapus_kamar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_kamar = itemView.findViewById(R.id.nama_kamar);
            btn_hapus_kamar = itemView.findViewById(R.id.btn_hapus);
        }
    }
}
