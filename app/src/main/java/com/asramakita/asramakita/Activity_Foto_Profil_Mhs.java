package com.asramakita.asramakita;

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
import android.widget.Toast;

import com.asramakita.asramakita.mahasiswa.Activity_Dashboard_Mhs;
import com.asramakita.asramakita.mahasiswa.pembayaranmhs.Detail_Pembayaran_Mhs;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Foto_Profil_Mhs extends AppCompatActivity {

    private CircleImageView imgProfile;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference storageFoto;
    private StorageTask mUploadTask;
    private DatabaseReference dbUser;
    private FirebaseAuth mAuth;
    private String UID, URL;
    private Button btn_uploadFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_profil_mhs);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBukti(v);
            }
        });

        btn_uploadFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile(new CallbackFoto() {
                    @Override
                    public void onCallback(String URL) {
                        if (URL!=null){
                            dbUser.child(UID).child("foto").setValue(URL);
                            Toast.makeText(Activity_Foto_Profil_Mhs.this, "Foto berhasil diupload", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Activity_Foto_Profil_Mhs.this, Activity_Dashboard_Mhs.class));
                            finish();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Activity_Foto_Profil_Mhs.this, Activity_Dashboard_Mhs.class));
        finish();
    }

    private void initComponents(){
        imgProfile = findViewById(R.id.imgProfile);
        btn_uploadFoto = findViewById(R.id.uploadFoto);
        storageFoto = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbUser = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
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
            Glide.with(getApplicationContext()).load(mImageUri).into(imgProfile);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(CallbackFoto callBackFoto){
        if (mImageUri != null){
            StorageReference fileReference = storageFoto.child("foto_profil").child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            URL = uri.toString();
                            callBackFoto.onCallback(URL);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Activity_Foto_Profil_Mhs.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Activity_Foto_Profil_Mhs.this, "Pilih foto terlebih dahulu", Toast.LENGTH_SHORT).show();
        }

    }

    private interface CallbackFoto{
        void onCallback(String URL);
    }

}