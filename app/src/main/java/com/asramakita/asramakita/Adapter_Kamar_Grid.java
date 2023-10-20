package com.asramakita.asramakita;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.asramakita.asramakita.admin.kamar.Activity_Sub_Kamar;
import com.asramakita.asramakita.admin.kamar.Detail_Kamar;
import com.asramakita.asramakita.admin.kamar.Informasi_Kamar;
import com.asramakita.asramakita.admin.kamar.Kamar_Model;
import com.asramakita.asramakita.mahasiswa.infokamar.Activity_Sub_Kamar_Mhs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter_Kamar_Grid extends RecyclerView.Adapter<Adapter_Kamar_Grid.ViewHolder> {

    Context context;
    ArrayList<Kamar_Model> listKamar;
    int kuotaKamar = 4, newColor, newColor2;
    public static String namaKamar;

    public Adapter_Kamar_Grid(Context context, ArrayList<Kamar_Model> listKamar) {
        this.context = context;
        this.listKamar = listKamar;
    }

    @NonNull
    @Override
    public Adapter_Kamar_Grid.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_grid_kamar, parent, false);
        return new Adapter_Kamar_Grid.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Kamar_Grid.ViewHolder holder, int position) {
        Kamar_Model kamarModel = listKamar.get(position);
        DatabaseReference dbKamar = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        String kamarKey = "Kamar " + (position + 1); // Assuming kamar keys are like "Kamar 1", "Kamar 2", ...
        newColor = ContextCompat.getColor(context, R.color.cv);
        newColor2 = ContextCompat.getColor(context, R.color.cv2);

        dbKamar.child("Kamar").child(UIDStorage.getInstance().getNamaLantai()).child(kamarKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Set the namaKamar text view with the kamarKey for this specific card view
                    holder.namaKamar.setText(kamarKey);
                    long kouta = snapshot.getChildrenCount();
                    if (kouta == kuotaKamar) {
                        holder.cv_grid_kamar.setCardBackgroundColor(newColor2);
                    } else {
                        holder.cv_grid_kamar.setCardBackgroundColor(newColor);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.cv_grid_kamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namaKamar = kamarKey;
                context.startActivity(new Intent(context, Detail_Kamar.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return listKamar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv_grid_kamar;
        TextView namaKamar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv_grid_kamar = itemView.findViewById(R.id.cv_grid_kamar);
            namaKamar = itemView.findViewById(R.id.namaKamar);
        }
    }
}
