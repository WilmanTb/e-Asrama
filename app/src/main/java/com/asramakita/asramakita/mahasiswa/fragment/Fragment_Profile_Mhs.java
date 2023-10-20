package com.asramakita.asramakita.mahasiswa.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asramakita.asramakita.Activity_Foto_Profil_Mhs;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.mahasiswa.pembayaranmhs.Detail_Pembayaran_Mhs;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Profile_Mhs extends Fragment {

    TextView namaMahasiswa, npmMahasiswa, fakultasMahasiswa, emailMahasiswa, hpMahasiswa, kamarMahasiswa;
    DatabaseReference dbUser;
    FirebaseAuth mAuth;
    String UID, URL, kamarChild;
    View view;
    CircleImageView imgProfile;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri mImageUri;
    private StorageReference storageFoto;
    private StorageTask mUploadTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_mhs, container, false);

        dbUser = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        initComponent();
        getData();
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), Activity_Foto_Profil_Mhs.class));
            }
        });
        return view;
    }

    private void initComponent() {
        namaMahasiswa = view.findViewById(R.id.namaMahasiswa);
        npmMahasiswa = view.findViewById(R.id.npmMahasiswa);
        fakultasMahasiswa = view.findViewById(R.id.fakultasMahasiswa);
        emailMahasiswa = view.findViewById(R.id.emailMahasiswa);
        hpMahasiswa = view.findViewById(R.id.hpMahasiswa);
        mAuth = FirebaseAuth.getInstance();
        imgProfile = view.findViewById(R.id.imgProfile);
        storageFoto = FirebaseStorage.getInstance().getReference();
        kamarMahasiswa = view.findViewById(R.id.kamarMahasiswa);
    }

    private void getData() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbUser.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    namaMahasiswa.setText(String.valueOf(snapshot.child("nama").getValue()));
                    npmMahasiswa.setText(String.valueOf(snapshot.child("npm").getValue()));
                    fakultasMahasiswa.setText(String.valueOf(snapshot.child("fakultas").getValue()));
                    emailMahasiswa.setText(String.valueOf(snapshot.child("email").getValue()));
                    hpMahasiswa.setText(String.valueOf(snapshot.child("hp").getValue()));
                    Glide.with(view).load(snapshot.child("foto").getValue()).into(imgProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        dbUser.child("Kamar").child("Lt3").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    kamarChild = dataSnapshot.getKey();
//                    if (kamarChild!=null){
//                        dbUser.child("Kamar").child("Lt3").child(kamarChild).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
//                                    String test = dataSnapshot1.getKey();
//                                    if (test.equals(UID)){
//                                        Toast.makeText(view.getContext(), test + "ini", Toast.LENGTH_SHORT).show();
//                                        kamarMahasiswa.setText(test);
//                                        break;
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        dbUser.child("Kamar").child("Lt2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    dbUser.child("Kamar").child("Lt2").child(key).orderByChild("idMhs").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                kamarMahasiswa.setText(key + " Lantai 2");
                            } else {
                                dbUser.child("Kamar").child("Lt3").child(key).orderByChild("idMhs").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            kamarMahasiswa.setText(key + " Lantai 3");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors
            }
        });
    }
}
