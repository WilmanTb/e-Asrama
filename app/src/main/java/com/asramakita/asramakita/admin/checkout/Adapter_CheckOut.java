package com.asramakita.asramakita.admin.checkout;

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

public class Adapter_CheckOut extends RecyclerView.Adapter<Adapter_CheckOut.MyViewHolder> {

    Context context;
    ArrayList<Model_CheckOut> list;

    public Adapter_CheckOut(Context context, ArrayList<Model_CheckOut> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Adapter_CheckOut.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_checkout, parent, false);
        return new Adapter_CheckOut.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_CheckOut.MyViewHolder holder, int position) {
        Model_CheckOut modelCheckOut = list.get(position);
        holder.namaMhs.setText(modelCheckOut.getNama());
        holder.npmMhs.setText(modelCheckOut.getNpm());
        holder.tanggal.setText(modelCheckOut.getTanggal());
        holder.cvListCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Desc_Permintaan_CheckOut.class).putExtra("detail",list.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView namaMhs, npmMhs, tanggal;
        CardView cvListCheckOut;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            namaMhs = itemView.findViewById(R.id.namaMahasiswa);
            npmMhs = itemView.findViewById(R.id.npmMahasiswa);
            tanggal = itemView.findViewById(R.id.tanggalPembayaran);
            cvListCheckOut = itemView.findViewById(R.id.cvListCheckOut);
        }
    }
}
