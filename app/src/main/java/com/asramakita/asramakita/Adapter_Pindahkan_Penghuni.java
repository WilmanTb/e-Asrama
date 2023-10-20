package com.asramakita.asramakita;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asramakita.asramakita.admin.kamar.Activity_Sub_Kamar;
import com.asramakita.asramakita.admin.kamar.Informasi_Kamar;
import com.asramakita.asramakita.admin.kamar.Kamar_Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter_Pindahkan_Penghuni extends RecyclerView.Adapter<Adapter_Pindahkan_Penghuni.ViewHolder> {

    Context context;
    ArrayList<CharSequence> myList;
    DatabaseReference dbKamar;

    public Adapter_Pindahkan_Penghuni(Context context, ArrayList<CharSequence> myList) {
        this.context = context;
        this.myList = myList;
    }

    @NonNull
    @Override
    public Adapter_Pindahkan_Penghuni.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_kamar_pindahkan_kamar, parent, false);
        return new Adapter_Pindahkan_Penghuni.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Pindahkan_Penghuni.ViewHolder holder, int position) {
        String kamarName = myList.get(position).toString();
        String UID = UIDStorage.getInstance().getUid();
        String namaMhs = UIDStorage.getInstance().getNama();
        String npmMhs = UIDStorage.getInstance().getNpm();
        String alamatMhs = UIDStorage.getInstance().getAlamat();
        String fakultasMhs = UIDStorage.getInstance().getFakultas();
        String nomorLantai = UIDStorage.getInstance().getSelectedLantai();
        String previousLantai = UIDStorage.getInstance().getNamaLantai();
        String previousKamar = Adapter_Kamar_Grid.namaKamar;
        holder.nama_kamar.setText(kamarName);
        Kamar_Model kamar_model = new Kamar_Model(namaMhs, npmMhs, alamatMhs, fakultasMhs, UID);
        holder.btn_pindahkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbKamar = FirebaseDatabase
                        .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

                //delete penghuni from previous kamar
                dbKamar.child("Kamar").child(previousLantai).child(previousKamar).child(UID).setValue(null);

                //set penghuni to the new kamar
                dbKamar.child("Kamar").child(nomorLantai).child(kamarName).child(UID).setValue(kamar_model);

                Toast.makeText(context, "Mahasiswa berhasil dipindahkan ke " + kamarName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama_kamar;
        Button btn_pindahkan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_kamar = itemView.findViewById(R.id.nama_kamar);
            btn_pindahkan = itemView.findViewById(R.id.btn_pindahkan);
        }
    }
}
