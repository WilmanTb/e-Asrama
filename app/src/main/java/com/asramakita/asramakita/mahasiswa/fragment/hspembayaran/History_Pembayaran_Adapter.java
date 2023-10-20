package com.asramakita.asramakita.mahasiswa.fragment.hspembayaran;

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
import com.asramakita.asramakita.admin.pembayaran.Pembayaran_Model;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class History_Pembayaran_Adapter extends RecyclerView.Adapter<History_Pembayaran_Adapter.MyViewHolder> {

    Context context;
    ArrayList<Pembayaran_Model> myList;

    public History_Pembayaran_Adapter(Context context, ArrayList<Pembayaran_Model> myList) {
        this.context = context;
        this.myList = myList;
    }

    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }

    @NonNull
    @Override
    public History_Pembayaran_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.list_history_pembayaran_mhs, parent, false);
       return new History_Pembayaran_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull History_Pembayaran_Adapter.MyViewHolder holder, int position) {
        Pembayaran_Model pembayaranModel = myList.get(position);
        holder.jns_pembayaran.setText(pembayaranModel.getJenis());
        holder.totalBayar.setText(formatRupiah(Double.parseDouble(pembayaranModel.getJumlah_dana())));
        holder.tanggalBayar.setText(pembayaranModel.getTanggal());
        holder.cvPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Detail_History_Pembayaran.class).putExtra("detail",myList.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView jns_pembayaran, totalBayar, tanggalBayar;
        CardView cvPembayaran;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            jns_pembayaran = itemView.findViewById(R.id.jns_pembayaran);
            totalBayar = itemView.findViewById(R.id.totalBayar);
            tanggalBayar = itemView.findViewById(R.id.tanggalBayar);
            cvPembayaran = itemView.findViewById(R.id.cvPembayaran);
        }
    }
}
