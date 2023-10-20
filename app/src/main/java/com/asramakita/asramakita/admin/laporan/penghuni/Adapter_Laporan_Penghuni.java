package com.asramakita.asramakita.admin.laporan.penghuni;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.asramakita.asramakita.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter_Laporan_Penghuni extends RecyclerView.Adapter<Adapter_Laporan_Penghuni.MyViewHolder> {

    Context context;
    ArrayList<Model_Laporan_Penghuni> myList;

    public Adapter_Laporan_Penghuni(Context context, ArrayList<Model_Laporan_Penghuni> myList) {
        this.context = context;
        this.myList = myList;
    }

    @NonNull
    @Override
    public Adapter_Laporan_Penghuni.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_penghuni_asrama, parent, false);
        return new Adapter_Laporan_Penghuni.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Laporan_Penghuni.MyViewHolder holder, int position) {
        Model_Laporan_Penghuni modelLaporanPenghuni = myList.get(position);
        holder.namaMhs.setText(modelLaporanPenghuni.getNama());
        holder.npmMhs.setText(modelLaporanPenghuni.getNpm());
        holder.fakultasMhs.setText(modelLaporanPenghuni.getFakultas());
        holder.dataPenhuni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Detail_Penghuni_Asrama.class).putExtra("deskripsi", myList.get(holder.getAdapterPosition())));
            }
        });
        Glide.with(context).load(myList.get(position).getFoto()).into(holder.img_profile);
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView namaMhs, npmMhs, fakultasMhs;
        ConstraintLayout dataPenhuni;
        ImageView img_profile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            namaMhs = itemView.findViewById(R.id.namaMhs);
            npmMhs = itemView.findViewById(R.id.npmMhs);
            fakultasMhs = itemView.findViewById(R.id.fakultasMhs);
            dataPenhuni = itemView.findViewById(R.id.dataPenghuni);
            img_profile = itemView.findViewById(R.id.img_profil);
        }
    }
}
