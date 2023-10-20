package com.asramakita.asramakita.admin.laporan.penghuni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.asramakita.asramakita.admin.laporan.Sub_Laporan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Penghuni_Asrama extends AppCompatActivity {

    private TextView totalPenghuni;
    private DatabaseReference dbAsrama;
    private RecyclerView rcPenghuniAsrama;
    private Adapter_Laporan_Penghuni myAdapter;
    private ArrayList<Model_Laporan_Penghuni> myArrayList;
    private Button btnCari;
    private FloatingActionButton downloadFile;
    private AutoCompleteTextView etCariNpm;
    private String NPM;
    private ArrayList<String> arrayListPelanggar = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penghuni_asrama);

//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dbAsrama = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users");

        autoCompleteListener();


        totalPenghuni = findViewById(R.id.totalPenghuni);
        btnCari = findViewById(R.id.btnCari);
        downloadFile = findViewById(R.id.downloadFile);
        etCariNpm = findViewById(R.id.etCariNpm);
        rcPenghuniAsrama = findViewById(R.id.rcPenghuniAsrama);
        rcPenghuniAsrama.setHasFixedSize(true);
        myArrayList = new ArrayList<>();
        rcPenghuniAsrama.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new Adapter_Laporan_Penghuni(this, myArrayList);
        rcPenghuniAsrama.setAdapter(myAdapter);

        getTotalPenghuni();

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NPM = etCariNpm.getText().toString();
                if (NPM.isEmpty()){
                    Toast.makeText(Penghuni_Asrama.this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    getTotalPenghuni();
                }else{
                    getNPM(new NpmCallBack() {
                        @Override
                        public void onCallBack(String npm) {
                            cariNPM(npm);
                            Toast.makeText(Penghuni_Asrama.this, npm, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        downloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importData(myArrayList);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Sub_Laporan.class));
        finish();
    }

    private void getTotalPenghuni() {
            dbAsrama.orderByChild("status").equalTo("penghuni").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String total = String.valueOf(snapshot.getChildrenCount());
                        totalPenghuni.setText(total);
                        myArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Model_Laporan_Penghuni modelLaporanPenghuni = dataSnapshot.getValue(Model_Laporan_Penghuni.class);
                            myArrayList.add(modelLaporanPenghuni);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void getNPM(NpmCallBack npmCallBack){
        NPM = etCariNpm.getText().toString();
        npmCallBack.onCallBack(NPM);
    }

    private void cariNPM(String NPM){
        dbAsrama.orderByChild("nama").equalTo(NPM).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    myArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (snapshot.child(dataSnapshot.getKey()).child("status").getValue().toString().equals("penghuni")) {
                            Model_Laporan_Penghuni modelLaporanPenghuni = dataSnapshot.getValue(Model_Laporan_Penghuni.class);
                            myArrayList.add(modelLaporanPenghuni);
                        }
                        myAdapter.notifyDataSetChanged();
                    }
                }else{
                    Log.d("NPM", "Searching for NPM: " + NPM);
                    Toast.makeText(Penghuni_Asrama.this, "Nama tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void autoCompleteListener(){
        dbAsrama.orderByChild("status").equalTo("penghuni").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListPelanggar.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    arrayListPelanggar.add(dataSnapshot.child("nama").getValue(String.class));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Penghuni_Asrama.this, android.R.layout.simple_list_item_1, arrayListPelanggar);
                etCariNpm.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private interface NpmCallBack{
        void onCallBack(String npm);
    }

    private void importData(ArrayList<Model_Laporan_Penghuni> modelLaporanPenghuni) {
        try {
            String strDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault()).format(new Date());
            File root = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Laporan Penghuni");
            if (!root.exists())
                root.mkdirs();
            File path = new File(root, "/" + strDate + ".xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook();
            FileOutputStream outputStream = new FileOutputStream(path);

            XSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderTop(BorderStyle.MEDIUM);
            headerStyle.setBorderBottom(BorderStyle.MEDIUM);
            headerStyle.setBorderRight(BorderStyle.MEDIUM);
            headerStyle.setBorderLeft(BorderStyle.MEDIUM);

            XSSFFont font = workbook.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setColor(IndexedColors.WHITE.getIndex());
            font.setBold(true);
            headerStyle.setFont(font);

            XSSFSheet sheet = workbook.createSheet("Data Penghuni Asrama");
            XSSFRow row = sheet.createRow(0);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue("Nama");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(1);
            cell.setCellValue("NPM");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(2);
            cell.setCellValue("Fakultas");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(3);
            cell.setCellValue("No Hp");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(4);
            cell.setCellValue("Email");
            cell.setCellStyle(headerStyle);

            for (int i = 0; i < modelLaporanPenghuni.size(); i++) {
                row = sheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellValue(modelLaporanPenghuni.get(i).getNama());
                sheet.setColumnWidth(0, (modelLaporanPenghuni.get(i).getNama().length() + 30) * 256);

                cell = row.createCell(1);
                cell.setCellValue(modelLaporanPenghuni.get(i).getNpm());
                sheet.setColumnWidth(1, modelLaporanPenghuni.get(i).getNpm().length() * 400);

                cell = row.createCell(2);
                cell.setCellValue(modelLaporanPenghuni.get(i).getFakultas());
                sheet.setColumnWidth(2, modelLaporanPenghuni.get(i).getFakultas().length() * 400);

                cell = row.createCell(3);
                cell.setCellValue(modelLaporanPenghuni.get(i).getHp());
                sheet.setColumnWidth(3, modelLaporanPenghuni.get(i).getHp().length() * 400);

                cell = row.createCell(4);
                cell.setCellValue(modelLaporanPenghuni.get(i).getEmail());
                sheet.setColumnWidth(4, modelLaporanPenghuni.get(i).getEmail().length() * 400);
            }

            workbook.write(outputStream);
            outputStream.close();
            Toast.makeText(Penghuni_Asrama.this, "Data berhasil di ekspor!", Toast.LENGTH_SHORT).show();

            Uri uri = FileProvider.getUriForFile(Penghuni_Asrama.this, "com.asramakita.asramakita.provider", path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String mimeType = getContentResolver().getType(uri);
            intent.setDataAndType(uri, mimeType);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent chooser = Intent.createChooser(intent, "Buka dengan...");
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(chooser);
//            }
            startActivity(chooser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}