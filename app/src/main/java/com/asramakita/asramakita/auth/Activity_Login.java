package com.asramakita.asramakita.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.asramakita.asramakita.mahasiswa.Activity_Dashboard_Mhs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_Login extends AppCompatActivity {

    private ImageView img_back;
    private EditText edit_email, edit_password;
    private Button btn_login;
    private String Email, Password, UID, mPassword;
    private FirebaseAuth userAuth;
    private DatabaseReference dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userAuth = FirebaseAuth.getInstance();
        dbUser = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users");
        getID();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Login.this, Landing_Page.class));
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = edit_email.getText().toString();
                Password = edit_password.getText().toString();
                if (Email.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(Activity_Login.this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else if (Email.equals("admin") && Password.equals("admin")){
                    startActivity(new Intent(Activity_Login.this, Activity_Dashboard_Admin.class));
                    finish();
                }else {
                    getUID(new FirebaseCallback() {
                        @Override
                        public void onCallback(String UID) {
                            checkPassword(UID);
                        }
                    });
                }
            }
        });
    }

    private void getID(){
        img_back = findViewById(R.id.img_back);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        btn_login = findViewById(R.id.btn_login);
    }

    private void loginUser() {
        userAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(Activity_Login.this, Activity_Dashboard_Mhs.class));
                    finish();
                }else {
                    Toast.makeText(Activity_Login.this, "Login gagal\nMohon cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUID(FirebaseCallback firebaseCallback){
        Email = edit_email.getText().toString();
        dbUser.orderByChild("email")
                .equalTo(Email)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                if (dataSnapshot.exists()){
                                    UID = dataSnapshot.getKey();
                                }
                                break;
                            }
                            firebaseCallback.onCallback(UID);
                        }else {
                            Toast.makeText(Activity_Login.this, "Email tidak terdaftar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkPassword(String UID){
        Password = edit_password.getText().toString();
        dbUser.child(UID).child("password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPassword = snapshot.getValue().toString();
                if (mPassword.equals(Password)){
                    loginUser();
                }else {
                    Toast.makeText(Activity_Login.this, "Password salah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface FirebaseCallback{
        void onCallback(String UID);
    }

}