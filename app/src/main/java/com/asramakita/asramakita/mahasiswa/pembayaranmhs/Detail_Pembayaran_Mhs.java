package com.asramakita.asramakita.mahasiswa.pembayaranmhs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.pembayaran.Pembayaran_Model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class Detail_Pembayaran_Mhs extends AppCompatActivity {

    private TextView namaMahasiswa, npmMahasiswa, jns_pembayaran, tanggalPembayaran, jlh_dana,tv_pilihFoto;
    private RelativeLayout rlFoto;
    private DatabaseReference dbPembayaran;
    private StorageReference storageFoto;
    private StorageTask mUploadTask;
    private Button btnKonfirmasi;
    private Pembayaran_Model pembayaranModel;
    private String idMhs, key, UID, URL;
    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembayaran_mhs);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbPembayaran = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Pembayaran");

        storageFoto = FirebaseStorage.getInstance().getReference();

        initComponents();
        getData();
        getKey();

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile(new CallBackFoto() {
                    @Override
                    public void onCallBack(String URL) {
                        if (URL != null){
                            dbPembayaran.child("Data").child(key).child("status").setValue("diajukan");
                            dbPembayaran.child("Data").child(key).child("bukti").setValue(URL);
                            Toast.makeText(Detail_Pembayaran_Mhs.this, "Pembayaran berhasil diajukan", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Detail_Pembayaran_Mhs.this, Activity_Pembayaran_Mhs.class));
                            finish();
                        }
                    }
                });
            }
        });

        rlFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBukti(v);
            }
        });


    }

    private void initComponents() {
        namaMahasiswa = findViewById(R.id.namaMahasiswa);
        npmMahasiswa = findViewById(R.id.npmMahasiswa);
        jns_pembayaran = findViewById(R.id.jns_pembayaran);
        tanggalPembayaran = findViewById(R.id.tanggalPembayaran);
        btnKonfirmasi = findViewById(R.id.btnKonfirmasi);
        jlh_dana = findViewById(R.id.jlh_dana);
        mAuth = FirebaseAuth.getInstance();
        tv_pilihFoto = findViewById(R.id.tv_pilihFoto);
        rlFoto = findViewById(R.id.rlfoto);
    }

    private void getData() {
        final Object object = getIntent().getSerializableExtra("deskripsi");
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
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Pembayaran_Mhs.class));
        finish();
    }

    private void getKey(){
        dbPembayaran.child("Data").orderByChild("npm").equalTo(pembayaranModel.getNpm()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        String status = dataSnapshot.child("status").getValue().toString();
                        if (status.equals("belum dibayar")) {
                            key = dataSnapshot.getKey();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadBukti(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri);
            tv_pilihFoto.setText(String.valueOf(mImageUri));
        }
    }

    private String formatRupiah(Double number){
        Locale locale = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        return formatRupiah.format(number);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(CallBackFoto callBackFoto){
        if (mImageUri != null){
            StorageReference fileReference = storageFoto.child("Pembayaran").child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            URL = uri.toString();
                            callBackFoto.onCallBack(URL);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Detail_Pembayaran_Mhs.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Detail_Pembayaran_Mhs.this, "Foto bukti pembayaran wajib ditambahkan", Toast.LENGTH_SHORT).show();
        }

    }

    private interface CallBackFoto {
        void onCallBack(String URL);
    }

}