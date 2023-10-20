package com.asramakita.asramakita.admin.kamar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.asramakita.asramakita.Activity_Pindahkan_Penghuni_Kamar;
import com.asramakita.asramakita.Adapter_Kamar_Grid;
import com.asramakita.asramakita.Adapter_Lantai;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.UIDStorage;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Kamar_Adapter extends RecyclerView.Adapter<Kamar_Adapter.MyViewHolder> {

    Context context;
    ArrayList<Kamar_Model> myList;

    public Kamar_Adapter(Context context, ArrayList<Kamar_Model> myList) {
        this.context = context;
        this.myList = myList;
    }

    @NonNull
    @Override
    public Kamar_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.penghuni_kamar, parent, false);
        return new Kamar_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Kamar_Adapter.MyViewHolder holder, int position) {
        Kamar_Model kamarModel = myList.get(position);
        holder.namaMhs.setText(kamarModel.getNama());
        holder.npmMhs.setText(kamarModel.getNpm());
        holder.fakultasMhs.setText(kamarModel.getFakultas());
        holder.alamatMhs.setText(kamarModel.getAlamat());
        if (Activity_Dashboard_Admin.userRole==1) {
            holder.btn_pindahkan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, Activity_Pindahkan_Penghuni_Kamar.class).putExtra("detail", myList.get(holder.getAdapterPosition())));
                }
            });
        } else holder.btn_pindahkan.setVisibility(View.GONE);

        if (Activity_Dashboard_Admin.userRole==1) {
            holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set PermintaanPendaftaran status penghuni to diterima
                    // so it will show again inside Tambah Penghuni list
                    DatabaseReference dbPendaftaran = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                    dbPendaftaran.child("PermintaanPendaftaran").child("Data").child(kamarModel.getIdMhs()).child("status").setValue("diterima");

                    dbPendaftaran.child("Kamar").child(UIDStorage.getInstance().getNamaLantai()).child(Adapter_Kamar_Grid.namaKamar).child(kamarModel.getIdMhs()).setValue(null);
                    dbPendaftaran.child("Users").child(kamarModel.getIdMhs()).child("status").removeValue();
                }
            });
        } else holder.btn_hapus.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView namaMhs, npmMhs, fakultasMhs, alamatMhs;
        ConstraintLayout data_penghuni;
        Button btn_pindahkan, btn_hapus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            namaMhs = itemView.findViewById(R.id.namaMhs);
            npmMhs = itemView.findViewById(R.id.npmMhs);
            fakultasMhs = itemView.findViewById(R.id.fakultasMhs);
            alamatMhs = itemView.findViewById(R.id.alamatMhs);
            data_penghuni = itemView.findViewById(R.id.data_penghuni);
            btn_pindahkan = itemView.findViewById(R.id.btn_pindahkan);
            btn_hapus = itemView.findViewById(R.id.btn_hapus);
        }
    }
}
