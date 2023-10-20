package com.asramakita.asramakita.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.mahasiswa.Activity_Dashboard_Mhs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Activity_Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText edit_npm, edit_nama, edit_email, edit_nohp, edit_password;
    private Spinner spin_fakultas;
    private Button btn_register;
    private ImageView img_back;
    private String NPM = "", Nama = "", Email= "", Hp = "", Password, Fakultas, UID;
    private FirebaseAuth userAuth;
    private ArrayAdapter<CharSequence> adapterFakultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getId();
        getFakultas();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmpty();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Register.this, Landing_Page.class));
                finish();
            }
        });
    }

    private void getId() {
        edit_npm = findViewById(R.id.edit_npm);
        edit_nama = findViewById(R.id.edit_nama);
        edit_email = findViewById(R.id.edit_email);
        edit_nohp = findViewById(R.id.edit_hp);
        edit_password = findViewById(R.id.edit_password);
        spin_fakultas = findViewById(R.id.spin_fakultas);
        btn_register = findViewById(R.id.btn_register);
        img_back = findViewById(R.id.img_back);
    }

    private void getValue() {
        NPM = edit_npm.getText().toString();
        Nama = edit_nama.getText().toString();
        Email = edit_email.getText().toString();
        Password = edit_password.getText().toString();
        Hp = edit_nohp.getText().toString();
    }

    private void checkEmpty() {
        getValue();
        if (NPM.isEmpty() || Nama.isEmpty() || Email.isEmpty() || Password.isEmpty()) {
            Toast.makeText(this, "Form Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            checkPassword();
        }
    }

    private void checkPassword() {
        if (Password.length() < 8) {
            Toast.makeText(this, "Panjang password minimal 8 karakter", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(NPM, Nama, Email, Password, Fakultas, Hp);
        }
    }

    private void getFakultas() {
        adapterFakultas = ArrayAdapter.createFromResource(this, R.array.Fakultas, android.R.layout.simple_spinner_item);
        adapterFakultas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_fakultas.setAdapter(adapterFakultas);
        spin_fakultas.setOnItemSelectedListener(this);
    }

    private void registerUser(String npm, String nama, String email, String password, String fakultas, String hp) {
        userAuth = FirebaseAuth.getInstance();
        userAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    UID = currentUser.getUid();
                    DatabaseReference dbRegister = FirebaseDatabase.
                            getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").
                            getReference().child("Users").child(UID);

                    HashMap hashMap = new HashMap<>();
                    hashMap.put("npm",npm);
                    hashMap.put("nama",nama);
                    hashMap.put("email",email);
                    hashMap.put("hp",hp);
                    hashMap.put("fakultas",fakultas);
                    hashMap.put("password",password);
                    hashMap.put("foto", "https://firebasestorage.googleapis.com/v0/b/asramakita-173a4.appspot.com/o/foto_profil%2Fuser.png?alt=media&token=cdc98f87-c37c-44dc-b2dd-86be41cd2661");
                    dbRegister.setValue(hashMap);
                    Toast.makeText(Activity_Register.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Activity_Register.this, Activity_Dashboard_Mhs.class));
                    finish();
                }else {
                    Toast.makeText(Activity_Register.this, "Registrasi gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spin_fakultas =(Spinner) adapterView;
        Fakultas = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}