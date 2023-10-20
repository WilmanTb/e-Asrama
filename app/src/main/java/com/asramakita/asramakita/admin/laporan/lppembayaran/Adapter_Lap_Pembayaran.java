package com.asramakita.asramakita.admin.laporan.lppembayaran;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.pembayaran.Pembayaran_Model;

import java.util.ArrayList;

public class Adapter_Lap_Pembayaran extends RecyclerView.Adapter<Adapter_Lap_Pembayaran.MyViewHolder> {

    Context context;
    ArrayList<Pembayaran_Model> myList;

    public Adapter_Lap_Pembayaran(Context context, ArrayList<Pembayaran_Model> myList) {
        this.context = context;
        this.myList = myList;
    }

    @NonNull
    @Override
    public Adapter_Lap_Pembayaran.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_laporan_pembayaran, parent, false);
        return new Adapter_Lap_Pembayaran.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Lap_Pembayaran.MyViewHolder holder, int position) {
        Pembayaran_Model pembayaranModel = myList.get(position);
        holder.namaMahasiswa.setText(pembayaranModel.getNama());
        holder.npmMahasiswa.setText(pembayaranModel.getNpm());
        holder.jenisPembayaran.setText(pembayaranModel.getJenis());
        holder.jlhDana.setText(pembayaranModel.getJumlah_dana());
        holder.tanggalPembayaran.setText(pembayaranModel.getTanggal());
        holder.statusPembayaran.setText(pembayaranModel.getStatus());
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView namaMahasiswa, npmMahasiswa, jenisPembayaran, jlhDana, tanggalPembayaran, statusPembayaran;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            namaMahasiswa = itemView.findViewById(R.id.namaMahasiswa);
            npmMahasiswa = itemView.findViewById(R.id.npmMahasiswa);
            jenisPembayaran = itemView.findViewById(R.id.jenisPembayaran);
            jlhDana = itemView.findViewById(R.id.jlhDana);
            tanggalPembayaran = itemView.findViewById(R.id.tanggalPembayaran);
            statusPembayaran = itemView.findViewById(R.id.statusPembayaran);
        }
    }
}
