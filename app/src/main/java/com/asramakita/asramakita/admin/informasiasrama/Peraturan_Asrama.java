package com.asramakita.asramakita.admin.informasiasrama;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Peraturan_Asrama extends AppCompatActivity {

    private Button btnUpload;
    private EditText etNamaFile;
    private StorageReference strPeraturan;
    private DatabaseReference dbPeraturan;
    private ListView listFile;
    private List<pdfClass> pdfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peraturan_asrama);

        btnUpload = findViewById(R.id.btnUpload);
        etNamaFile = findViewById(R.id.etNamaFile);
        listFile = findViewById(R.id.listFile);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pdfList = new ArrayList<>();
        viewAllFiles();

        strPeraturan = FirebaseStorage.getInstance().getReference();
        dbPeraturan = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Peraturan");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaFile = etNamaFile.getText().toString();
                if (namaFile.isEmpty()) {
                    Toast.makeText(Peraturan_Asrama.this, "Nama file tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    pilihFile();
                }
            }
        });

        listFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pdfClass pdfUpload = pdfList.get(position);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf");
                intent.setData(Uri.parse(pdfUpload.getUrl()));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Informasi_Asrama.class));
        finish();
    }

    private void pilihFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih file pdf..."), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uploadFile(data.getData());
        }
    }

    private void uploadFile(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sedang upload...");
        progressDialog.show();

        StorageReference storageReference = strPeraturan.child("Peraturan/" + System.currentTimeMillis() + ".pdf");
        storageReference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri url = uriTask.getResult();

                        pdfClass pdfClass = new pdfClass(etNamaFile.getText().toString() + ".pdf", url.toString());
                        dbPeraturan.child(dbPeraturan.push().getKey()).setValue(pdfClass);

                        Toast.makeText(Peraturan_Asrama.this, "File berhasil diupload.", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Sedang upload:" + (int) progress + "%");

                    }
                });
    }

    private void viewAllFiles() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Peraturan");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    pdfClass pdfClass = dataSnapshot.getValue(com.asramakita.asramakita.admin.informasiasrama.pdfClass.class);
                    pdfList.add(pdfClass);
                }
                String[] Uploads = new String[pdfList.size()];
                for (int i = 0; i < Uploads.length; i++) {
                    Uploads[i] = pdfList.get(i).getNama();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, Uploads) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.BLACK);
                        textView.setTextSize(20);

                        return view;
                    }
                };
                listFile.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}