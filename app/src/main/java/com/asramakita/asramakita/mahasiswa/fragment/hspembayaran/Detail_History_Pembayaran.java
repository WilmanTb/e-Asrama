package com.asramakita.asramakita.mahasiswa.fragment.hspembayaran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.pembayaran.Pembayaran_Model;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

public class Detail_History_Pembayaran extends AppCompatActivity {

    private TextView namaMahasiswa, npmMahasiswa, jns_pembayaran, tanggalPembayaran, jlh_dana, statusBayar;
    private ImageView imgBukti;
    private DatabaseReference dbPembayaran;
    private Pembayaran_Model pembayaranModel;
    private String idMhs, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history_pembayaran);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbPembayaran = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Pembayaran");

        getId();
        getData();

    }

    private void getId() {
        namaMahasiswa = findViewById(R.id.namaMahasiswa);
        npmMahasiswa = findViewById(R.id.npmMahasiswa);
        jns_pembayaran = findViewById(R.id.jns_pembayaran);
        tanggalPembayaran = findViewById(R.id.tanggalPembayaran);
        imgBukti = findViewById(R.id.imgBukti);
        jlh_dana = findViewById(R.id.jlh_dana);
        statusBayar = findViewById(R.id.statusBayar);
    }

    private void getData() {
        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Pembayaran_Model) {
            pembayaranModel = (Pembayaran_Model) object;
        }

        if (pembayaranModel != null) {
            idMhs = pembayaranModel.getId_mhs();
            namaMahasiswa.setText(pembayaranModel.getNama());
            jns_pembayaran.setText(pembayaranModel.getJenis());
            npmMahasiswa.setText(pembayaranModel.getNpm());
            tanggalPembayaran.setText(pembayaranModel.getTanggal());
            namaMahasiswa.setText(pembayaranModel.getNama());
            jlh_dana.setText(formatRupiah(Double.parseDouble(pembayaranModel.getJumlah_dana())));
            statusBayar.setText(pembayaranModel.getStatus());
            Glide.with(getApplicationContext()).load(pembayaranModel.getBukti()).into(imgBukti);
        }
    }

    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }
}