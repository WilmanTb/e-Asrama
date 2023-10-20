package com.asramakita.asramakita.mahasiswa.ijinkeluar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.mahasiswa.Activity_Dashboard_Mhs;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class Activity_Tambah_Ijin_Mhs extends AppCompatActivity {

    private Button btn_submit, btn_tambah, datePickerButtonKeluar, timePickerButtonKeluar, datePickerButtonKembali, timePickerButtonKembali;
    private EditText et_keterangan;
    private AutoCompleteTextView et_anggotaIjin;
    private DatePickerDialog datePicker;
    private TextView tv_pilihFoto, txt_list;
    private RelativeLayout rlFoto;
    int hour, minute;
    private String dateKeluar = "", timeKeluar = "", dateKembali = "", timeKembali = "", namaAnggotaIjin = "", KeteranganIjin, url, IdIjin, UID, IdAnggota, npmAnggota, namaAnggota, npmPengaju, namaPengaju;
    private DatabaseReference dbIjinKeluar;
    private FirebaseAuth userAuth;
    private ArrayList<String> arrayAnggota = new ArrayList<>();
    private ArrayAdapter<String> adapterListView;
    private ArrayList<String> arrayList;
    private ListView list_IjinAnggota;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference storageFoto;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_ijin_mhs);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();
        initializeDatePickers(this, datePickerButtonKeluar, datePickerButtonKembali);
        initializeTimePickers(this, timePickerButtonKeluar, timePickerButtonKembali);
        getAnggotaIjin();
        IdIjin = String.valueOf(generateSixDigitNumber());


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeteranganIjin = et_keterangan.getText().toString();
                if (KeteranganIjin.isEmpty()) {
                    Toast.makeText(Activity_Tambah_Ijin_Mhs.this, "Keterangan ijin tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    createIjin();
                }
            }
        });

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namaAnggotaIjin = et_anggotaIjin.getText().toString();
                if (namaAnggotaIjin.isEmpty()) {
                    Toast.makeText(Activity_Tambah_Ijin_Mhs.this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {

                    et_anggotaIjin.setText("");
                    list_IjinAnggota.setVisibility(View.VISIBLE);
                    txt_list.setVisibility(View.VISIBLE);
                    arrayList.add(namaAnggotaIjin);
                    adjustListView();
                    adapterListView.notifyDataSetChanged();
                }

            }
        });

        rlFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFoto(v);
            }
        });

        list_IjinAnggota.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true; // Disable scrolling
            }
        });
    }


    private void initComponents() {
        btn_submit = findViewById(R.id.btn_submit);
        btn_tambah = findViewById(R.id.btn_tambah);
        txt_list = findViewById(R.id.txt_list);
        datePickerButtonKeluar = findViewById(R.id.datePickerButtonKeluar);
        datePickerButtonKembali = findViewById(R.id.datePickerButtonKembali);
        timePickerButtonKeluar = findViewById(R.id.timeButtonKeluar);
        timePickerButtonKembali = findViewById(R.id.timeButtonKembali);
        et_keterangan = findViewById(R.id.et_keterangan);
        et_anggotaIjin = findViewById(R.id.et_anggotaIjin);
        tv_pilihFoto = findViewById(R.id.tv_pilihFoto);
        rlFoto = findViewById(R.id.rlfoto);
        userAuth = FirebaseAuth.getInstance();
        list_IjinAnggota = findViewById(R.id.list_anggotaIjin);
        storageFoto = FirebaseStorage.getInstance().getReference();
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        dbIjinKeluar = FirebaseDatabase.getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Dashboard_Mhs.class));
        finish();
    }

    private void initializeDatePickers(final Context context, final Button startDateEditText, final Button endDateEditText) {
        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDateEditText.setText(formatDate(calendar));
                dateKeluar = formatDate(calendar);
            }
        };

        final DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDateEditText.setText(formatDate(calendar));
                dateKembali = formatDate(calendar);
            }
        };

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, startDateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, endDateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void initializeTimePickers(final Context context, final Button startTimeEditText, final Button endTimeEditText) {
        final Calendar calendar = Calendar.getInstance();

        final TimePickerDialog.OnTimeSetListener startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                startTimeEditText.setText(formatTime(hourOfDay, minute));
                timeKeluar = formatTime(hourOfDay, minute);
            }
        };

        final TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                endTimeEditText.setText(formatTime(hourOfDay, minute));
                timeKembali = formatTime(hourOfDay, minute);
            }
        };

        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                new TimePickerDialog(context, startTimeSetListener, currentHour, currentMinute, false).show();
            }
        });

        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                new TimePickerDialog(context, endTimeSetListener, currentHour, currentMinute, false).show();
            }
        });
    }

    private String formatTime(int hour, int minute) {
        String hourString = hour < 10 ? "0" + hour : String.valueOf(hour);
        String minuteString = minute < 10 ? "0" + minute : String.valueOf(minute);
        return hourString + ":" + minuteString;
    }

    private String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void getAnggotaIjin() {
        dbIjinKeluar.child("Users").orderByChild("status").equalTo("penghuni").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    arrayAnggota.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        arrayAnggota.add(dataSnapshot.child("nama").getValue(String.class));
                    }
                    ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(Activity_Tambah_Ijin_Mhs.this,
                            android.R.layout.simple_dropdown_item_1line, arrayAnggota);
                    et_anggotaIjin.setAdapter(autoCompleteAdapter);
                    arrayList = new ArrayList<>();
                    adapterListView = new ArrayAdapter<>(Activity_Tambah_Ijin_Mhs.this, android.R.layout.simple_list_item_1, arrayList);
                    list_IjinAnggota.setAdapter(adapterListView);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void adjustListView() {
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(list_IjinAnggota.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < adapterListView.getCount(); i++) {
            View listItem = adapterListView.getView(i, null, list_IjinAnggota);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

// Set the height of the ListView
        ViewGroup.LayoutParams layoutParams = list_IjinAnggota.getLayoutParams();
        layoutParams.height = totalHeight + (list_IjinAnggota.getDividerHeight() * (adapterListView.getCount() - 1));
        list_IjinAnggota.setLayoutParams(layoutParams);
        list_IjinAnggota.requestLayout();
    }

    private void uploadFoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri);
            tv_pilihFoto.setText(String.valueOf(mImageUri));
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(CallbackFoto callbackFoto) {
        if (mImageUri != null) {
            StorageReference fileReference = storageFoto.child("lampiranijin").child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    url = uri.toString();
                                    callbackFoto.onCallback(url);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Activity_Tambah_Ijin_Mhs.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            url = "empty";
            callbackFoto.onCallback(url);
        }
    }

    private void createIjin() {
        dbIjinKeluar.child("Users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    npmPengaju = snapshot.child("npm").getValue(String.class);
                    namaPengaju = snapshot.child("nama").getValue(String.class);
                    uploadFile(new CallbackFoto() {
                        @Override
                        public void onCallback(String URL) {
                            if (list_IjinAnggota.getAdapter() != null && list_IjinAnggota.getAdapter().getCount() > 0) {
                                int count = list_IjinAnggota.getCount();
                                for (int i = 0; i < count; i++) {
                                    String name = (String) list_IjinAnggota.getItemAtPosition(i);
                                    dbIjinKeluar.child("Users").orderByChild("nama").equalTo(name).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    IdAnggota = dataSnapshot.getKey();
                                                    npmAnggota = dataSnapshot.child("npm").getValue(String.class);
                                                    namaAnggota = dataSnapshot.child("nama").getValue(String.class);
                                                    Model_ijin_keluar modelIjinKeluar = new Model_ijin_keluar(IdIjin, dateKeluar, dateKembali, timeKeluar, timeKembali, "true", "anggota", URL, KeteranganIjin, IdAnggota, npmAnggota, namaAnggota);
                                                    dbIjinKeluar.child("IjinKeluar").child(IdAnggota).setValue(modelIjinKeluar);
                                                }
                                                Model_ijin_keluar modelIjinKeluar = new Model_ijin_keluar(IdIjin, dateKeluar, dateKembali, timeKeluar, timeKembali, "true", "pengaju", URL, KeteranganIjin, UID, npmPengaju, namaPengaju);
                                                dbIjinKeluar.child("IjinKeluar").child(UID).setValue(modelIjinKeluar);
                                                Toast.makeText(Activity_Tambah_Ijin_Mhs.this, "Ijin keluar berhasil diajukan", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Activity_Tambah_Ijin_Mhs.this, Activity_Dashboard_Mhs.class));
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            } else {
                                Model_ijin_keluar modelIjinKeluar = new Model_ijin_keluar(IdIjin, dateKeluar, dateKembali, timeKeluar, timeKembali, "true", "pengaju", URL, KeteranganIjin, UID, npmPengaju, namaPengaju);
                                dbIjinKeluar.child("IjinKeluar").child(UID).setValue(modelIjinKeluar);
                                Toast.makeText(Activity_Tambah_Ijin_Mhs.this, "Ijin keluar berhasil diajukan", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Activity_Tambah_Ijin_Mhs.this, Activity_Dashboard_Mhs.class));
                                finish();
                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface CallbackFoto {
        void onCallback(String URL);
    }

    private int generateSixDigitNumber() {
        Random random = new Random();
        int min = 100_000; // Minimum 6-digit number
        int max = 999_999; // Maximum 6-digit number
        return random.nextInt((max - min) + 1) + min;
    }
}