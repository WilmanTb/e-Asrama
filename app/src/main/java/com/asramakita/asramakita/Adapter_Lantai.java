package com.asramakita.asramakita;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.asramakita.asramakita.admin.kamar.Informasi_Kamar;
import com.asramakita.asramakita.mahasiswa.infokamar.Activity_InfoKamar_Mhs;

import java.util.ArrayList;

public class Adapter_Lantai extends RecyclerView.Adapter<Adapter_Lantai.ViewHolder> {

    Context context;
    ArrayList<CharSequence> myList;

    public Adapter_Lantai(Context context, ArrayList<CharSequence> myList) {
        this.context = context;
        this.myList = myList;
    }

    @NonNull
    @Override
    public Adapter_Lantai.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_lantai, parent, false);
        return new Adapter_Lantai.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Lantai.ViewHolder holder, int position) {
        String nama_lantai = myList.get(position).toString();
        holder.nama_lantai.setText(nama_lantai);
        holder.cv_lantai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIDStorage.getInstance().setNamaLantai(nama_lantai);
                if (Activity_Dashboard_Admin.userRole == 1) context.startActivity(new Intent(context, Informasi_Kamar.class).putExtra("lantai", myList.get(holder.getAdapterPosition())));
                else context.startActivity(new Intent(context, Activity_InfoKamar_Mhs.class).putExtra("lantai", myList.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv_lantai;
        TextView nama_lantai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cv_lantai = itemView.findViewById(R.id.cv_lantai);
            nama_lantai = itemView.findViewById(R.id.nama_lantai);
        }
    }
}
