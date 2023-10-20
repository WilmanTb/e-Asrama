package com.asramakita.asramakita.admin.pembayaran;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.asramakita.asramakita.mahasiswa.pembayaranmhs.Detail_Pembayaran_Mhs;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.mahasiswa.pembayaranmhs.Activity_Pembayaran_Mhs;

import java.util.ArrayList;

public class Pembayaran_Adapter extends RecyclerView.Adapter<Pembayaran_Adapter.MyViewHolder> {

    Context context;
    ArrayList<Pembayaran_Model> list;
    private Pembayaran_Adapter.descPembayaran Pembayaran;

    int code = Activity_Pembayaran_Mhs.code;
    String btn = "x";

    public interface descPembayaran {
        void clickCardView(int position);
    }

    public void descPembayaran(Pembayaran_Adapter.descPembayaran Pembayaran) {
        this.Pembayaran = Pembayaran;
    }

    public Pembayaran_Adapter(Context context, ArrayList<Pembayaran_Model> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Pembayaran_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_konfirmasi_pembayaran,parent, false);
        getCode(code);
        return new Pembayaran_Adapter.MyViewHolder(view, Pembayaran);
    }

    @Override
    public void onBindViewHolder(@NonNull Pembayaran_Adapter.MyViewHolder holder, int position) {
        Pembayaran_Model pembayaran_model = list.get(position);
        holder.namaMahasiswa.setText(pembayaran_model.getNama());
        holder.npmMahasiswa.setText(pembayaran_model.getNpm());
        holder.jns_pembayaran.setText(pembayaran_model.getJenis());
        holder.konfirmasi.setText(btn);
        holder.cvPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn.equals("Konfirmasi")) {
                    context.startActivity(new Intent(context, Desc_Pembayaran.class).putExtra("deskripsi", list.get(holder.getAdapterPosition())));
                } else {
                    context.startActivity(new Intent(context, Detail_Pembayaran_Mhs.class).putExtra("deskripsi", list.get(holder.getAdapterPosition())));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView namaMahasiswa, npmMahasiswa, jns_pembayaran,konfirmasi;
        CardView cvPembayaran;


        public MyViewHolder(@NonNull View itemView, descPembayaran pembayaran) {
            super(itemView);

            namaMahasiswa = itemView.findViewById(R.id.namaMahasiswa);
            npmMahasiswa = itemView.findViewById(R.id.npmMahasiswa);
            jns_pembayaran = itemView.findViewById(R.id.jns_pembayaran);
            cvPembayaran = itemView.findViewById(R.id.cvPembayaran);
            konfirmasi = itemView.findViewById(R.id.konfirmasi);
        }
    }

    private void getCode(int code){
        if (code == 1){
            btn = "Bayar";
        } else {
            btn = "Konfirmasi";
        }
    }
}
