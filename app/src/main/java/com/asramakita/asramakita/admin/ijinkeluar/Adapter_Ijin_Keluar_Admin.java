package com.asramakita.asramakita.admin.ijinkeluar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.mahasiswa.ijinkeluar.Model_ijin_keluar;

import java.util.ArrayList;

public class Adapter_Ijin_Keluar_Admin extends RecyclerView.Adapter<Adapter_Ijin_Keluar_Admin.ViewHolder> {

    Context context;
    ArrayList<Model_ijin_keluar> list;

    public Adapter_Ijin_Keluar_Admin(Context context, ArrayList<Model_ijin_keluar> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_Ijin_Keluar_Admin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.list_ijin_keluar_admin, parent, false);
       return new Adapter_Ijin_Keluar_Admin.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Ijin_Keluar_Admin.ViewHolder holder, int position) {
        Model_ijin_keluar modelIjinKeluar = list.get(position);
        holder.namaMahasiswa.setText(modelIjinKeluar.getNama());
        String date = modelIjinKeluar.getTanggalKeluar() + " " + modelIjinKeluar.getJam_keluar();
        holder.jam_keluar.setText(date);
        holder.npmMahasiswa.setText(modelIjinKeluar.getNpm());
        holder.cvIjinKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Activity_Sub_Ijin_Keluar_Admin.SUBMENUIJIN == 1){
                    context.startActivity(new Intent(context, Activity_Desc_Ijin_Keluar_Admin.class).putExtra("deskripsi", list.get(holder.getAdapterPosition())));
                } else if (Activity_Sub_Ijin_Keluar_Admin.SUBMENUIJIN == 2) {
                    context.startActivity(new Intent(context, Activity_Desc_Lapor_Kembali_Admin.class).putExtra("deskripsi", list.get(holder.getAdapterPosition())));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView namaMahasiswa, npmMahasiswa, jam_keluar;
        CardView cvIjinKeluar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaMahasiswa = itemView.findViewById(R.id.namaMahasiswa);
            npmMahasiswa = itemView.findViewById(R.id.npmMahasiswa);
            jam_keluar = itemView.findViewById(R.id.jam_keluar);
            cvIjinKeluar = itemView.findViewById(R.id.cvIjinKeluar);
        }
    }
}
