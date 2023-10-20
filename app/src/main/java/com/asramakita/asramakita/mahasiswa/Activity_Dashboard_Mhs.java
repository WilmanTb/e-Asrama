package com.asramakita.asramakita.mahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.asramakita.asramakita.mahasiswa.fragment.Fragment_Dashboard_Mhs;
import com.asramakita.asramakita.mahasiswa.fragment.Fragment_Ijin_Keluar_Mhs;
import com.asramakita.asramakita.mahasiswa.fragment.Fragment_Keluar_Asrama_Mhs;
import com.asramakita.asramakita.mahasiswa.fragment.hspembayaran.Fragment_Pembayaran_Mhs;
import com.asramakita.asramakita.mahasiswa.fragment.Fragment_Profile_Mhs;
import com.asramakita.asramakita.auth.Landing_Page;
import com.asramakita.asramakita.R;
import com.asramakita.asramakita.mahasiswa.infoasrama.Activity_Informasi_Asrama_Mhs;
import com.asramakita.asramakita.mahasiswa.infokamar.Activity_InfoKamar_Mhs;
import com.asramakita.asramakita.mahasiswa.infokamar.Activity_Sub_Kamar_Mhs;
import com.asramakita.asramakita.mahasiswa.pembayaranmhs.Activity_Pembayaran_Mhs;
import com.asramakita.asramakita.mahasiswa.pendaftaranmhs.Activity_Pendaftaran_Mhs;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Dashboard_Mhs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout dashboard_mhs;
    private FirebaseAuth mAuth;
    private DatabaseReference dbUser;
    private String namaUser, emailUser, urlFoto;
    private int backPressCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_mhs);

        Activity_Dashboard_Admin.userRole = 2;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        dbUser = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();

        dashboard_mhs = findViewById(R.id.dashboard_mhs);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView textView = (TextView) headerView.findViewById(R.id.userEmail);
        textView.setText(emailUser);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dashboard_mhs, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dashboard_mhs.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameDashboard, new Fragment_Dashboard_Mhs()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(Activity_Dashboard_Mhs.this, Landing_Page.class));
            finish();
        } else {
            getInfoUser();
        }
    }

    @Override
    public void onBackPressed() {
        if (dashboard_mhs.isDrawerOpen(GravityCompat.START)) {
            dashboard_mhs.closeDrawer(GravityCompat.START);
        } else {
            backPressCount++;

            if (backPressCount == 1) {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

                // Reset the back press count after a certain duration (e.g., 2 seconds)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressCount = 0;
                    }
                }, 2000);
            } else if (backPressCount == 2) {
                // If the back button is pressed twice within the specified duration, exit the app
                super.onBackPressed();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameDashboard,
                        new Fragment_Dashboard_Mhs()).commit();
                break;

            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameDashboard,
                        new Fragment_Profile_Mhs()).commit();
                break;

            case R.id.nav_logout:
                mAuth.signOut();
                startActivity(new Intent(this, Landing_Page.class));
                finish();
                break;

            case R.id.nav_pembayaran:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameDashboard,
                        new Fragment_Pembayaran_Mhs()).commit();
                break;

            case R.id.nav_keluar:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameDashboard,
                        new Fragment_Keluar_Asrama_Mhs()).commit();
                break;
            case R.id.nav_ijin:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameDashboard,
                        new Fragment_Ijin_Keluar_Mhs()).commit();
                break;
        }
        dashboard_mhs.closeDrawer(GravityCompat.START);
        return true;
    }

    public void menuClick(View view) {
        switch (view.getId()) {
            case R.id.cvInfoAsrama:
                startActivity(new Intent(this, Activity_Informasi_Asrama_Mhs.class));
                break;

            case R.id.ajkPendaftaran:
                cekStatusUser();
                break;

            case R.id.cvPembayaran:
                startActivity(new Intent(this, Activity_Pembayaran_Mhs.class));
                break;

            case R.id.cvInformasiKamar:
                startActivity(new Intent(this, Activity_Sub_Kamar_Mhs.class));
                break;
        }
    }

    private void getInfoUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String UID = firebaseUser.getUid();
        dbUser.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    namaUser = snapshot.child("nama").getValue().toString();
                    emailUser = snapshot.child("email").getValue().toString();
                    urlFoto = snapshot.child("foto").getValue().toString();
                    dashboard_mhs = findViewById(R.id.dashboard_mhs);
                    NavigationView navigationView = findViewById(R.id.nav_view);
                    navigationView.setNavigationItemSelectedListener(Activity_Dashboard_Mhs.this);
                    View headerView = navigationView.getHeaderView(0);
                    TextView email = (TextView) headerView.findViewById(R.id.userEmail);
                    TextView nama = (TextView) headerView.findViewById(R.id.userName);
                    CircleImageView imageView = (CircleImageView) headerView.findViewById(R.id.img_profilUser);
                    email.setText(emailUser);
                    nama.setText(namaUser);
                    Glide.with(getApplicationContext()).load(urlFoto).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cekStatusUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String UID = firebaseUser.getUid();
        dbUser.child("PermintaanPendaftaran").child("Data").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.child("status").getValue().toString();
                    if (status.equals("diminta")) {
                        Dialog dialog = new Dialog(Activity_Dashboard_Mhs.this);
                        dialog.setContentView(R.layout.pop_up_batal_daftar);
                        dialog.setCancelable(false);
                        dialog.show();

                        Button btn_ok = dialog.findViewById(R.id.btn_ok);
                        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dbUser.child("PermintaanPendaftaran").child("Data").child(UID).setValue(null);
                                dialog.dismiss();
                            }
                        });

                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    } else if (status.equals("ditempatkan")) {
                        Toast.makeText(Activity_Dashboard_Mhs.this, "Anda sudah menjadi penghuni asrama!", Toast.LENGTH_SHORT).show();
                    } else if (status.equals("diterima")) {
                        Toast.makeText(Activity_Dashboard_Mhs.this, "Pendaftaran anda telah diterima\n Mohon tunggu penempatan kamar anda", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    startActivity(new Intent(Activity_Dashboard_Mhs.this, Activity_Pendaftaran_Mhs.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}