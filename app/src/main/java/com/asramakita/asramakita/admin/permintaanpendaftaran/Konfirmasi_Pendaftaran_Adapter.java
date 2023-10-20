package com.asramakita.asramakita.admin.permintaanpendaftaran;

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

import java.util.ArrayList;

public class Konfirmasi_Pendaftaran_Adapter extends RecyclerView.Adapter<Konfirmasi_Pendaftaran_Adapter.MyViewHolder> {

    Context context;
    ArrayList<Pendaftaran_Class> myList;

    private Konfirmasi_Pendaftaran_Adapter.descPendaftaran Permintaan_Pendaftaran;

    public interface descPendaftaran {
        void clickCardView(int position);
    }

    public void descPendataran(Konfirmasi_Pendaftaran_Adapter.descPendaftaran Permintaan_Pendaftaran) {
        this.Permintaan_Pendaftaran = Permintaan_Pendaftaran;
    }

    public Konfirmasi_Pendaftaran_Adapter(Context context, ArrayList<Pendaftaran_Class> myList){
        this.context = context;
        this.myList = myList;
    }

    @NonNull
    @Override
    public Konfirmasi_Pendaftaran_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_konfirmasi_pendaftaran,parent, false);
        return new Konfirmasi_Pendaftaran_Adapter.MyViewHolder(view, Permintaan_Pendaftaran);
    }

    @Override
    public void onBindViewHolder(@NonNull Konfirmasi_Pendaftaran_Adapter.MyViewHolder holder, int position) {
        Pendaftaran_Class pendaftaran_class = myList.get(position);
        holder.namaPendaftar.setText(pendaftaran_class.getNama());
        holder.npmPendaftar.setText(pendaftaran_class.getNpm());
        holder.tglDaftar.setText(pendaftaran_class.getTanggal());
        holder.cvKonfrimasiPendaftaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Desc_Pendaftar.class).putExtra("deskripsi", myList.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView namaPendaftar, npmPendaftar, tglDaftar;
        CardView cvKonfrimasiPendaftaran;

        public MyViewHolder(@NonNull View itemView, descPendaftaran descPendaftaran) {
            super(itemView);

            namaPendaftar = itemView.findViewById(R.id.namaPendaftar);
            npmPendaftar = itemView.findViewById(R.id.npmPendaftar);
            tglDaftar = itemView.findViewById(R.id.tglDaftar);
            cvKonfrimasiPendaftaran = itemView.findViewById(R.id.cvKonfirmasiPendaftaran);

        }
    }
}
