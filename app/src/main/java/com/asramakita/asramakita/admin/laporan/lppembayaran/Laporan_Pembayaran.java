package com.asramakita.asramakita.admin.laporan.lppembayaran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.asramakita.asramakita.R;
import com.asramakita.asramakita.admin.Activity_Dashboard_Admin;
import com.asramakita.asramakita.admin.laporan.Sub_Laporan;
import com.asramakita.asramakita.admin.laporan.penghuni.Model_Laporan_Penghuni;
import com.asramakita.asramakita.admin.laporan.penghuni.Penghuni_Asrama;
import com.asramakita.asramakita.admin.pembayaran.Pembayaran_Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Laporan_Pembayaran extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseReference dbPembayaran;
    ArrayList<Pembayaran_Model> myList;
    RecyclerView rcLaporanPembayaran;
    Adapter_Lap_Pembayaran myAdapter;
    Spinner spin_jenisPembayaran, spin_tanggal, spin_status;
    ArrayAdapter<CharSequence> adapterJenisPembayaran;
    ArrayAdapter<CharSequence> adapterTanggalBayar;
    ArrayAdapter<CharSequence> adapterStatus;
    String JnsPembayaran, TglBayar, StsBayar;
    ImageButton filter_Jns, filter_Sts, filter_Tgl;
    String startDate, currentDate, tanggal;
    Date minDate;
    FloatingActionButton downloadFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_pembayaran);

        dbPembayaran = FirebaseDatabase
                .getInstance("https://asramakita-173a4-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Pembayaran").child("Data");
        initData();
        getData();
        spinnerJenisPembayaran();
        spinnerTanggalBayar();
        spinnerStatus();

        filter_Jns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredJNS();
            }
        });

        filter_Tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredTGL();
            }
        });

        filter_Sts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredSTS();
            }
        });

        downloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importData(myList);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Sub_Laporan.class));
        finish();
    }

    private void initData() {
        rcLaporanPembayaran = findViewById(R.id.rcTable);
        rcLaporanPembayaran.setHasFixedSize(true);
        rcLaporanPembayaran.setLayoutManager(new LinearLayoutManager(this));
        myList = new ArrayList<>();
        myAdapter = new Adapter_Lap_Pembayaran(this, myList);
        rcLaporanPembayaran.setAdapter(myAdapter);

        spin_jenisPembayaran = findViewById(R.id.spin_jenisPembayaran);
        spin_tanggal = findViewById(R.id.spin_tanggal);
        spin_status = findViewById(R.id.spin_status);
        filter_Jns = findViewById(R.id.filterJns);
        filter_Tgl = findViewById(R.id.filterTgl);
        filter_Sts = findViewById(R.id.filterSts);
        downloadFile = findViewById(R.id.downloadFile);

    }

    private void getData() {
        dbPembayaran.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    myList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Pembayaran_Model pembayaran_model = dataSnapshot.getValue(Pembayaran_Model.class);
                        myList.add(pembayaran_model);
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void spinnerJenisPembayaran() {
        adapterJenisPembayaran = ArrayAdapter.createFromResource(this, R.array.JenisPembayaran, android.R.layout.simple_spinner_item);
        adapterJenisPembayaran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_jenisPembayaran.setAdapter(adapterJenisPembayaran);
        spin_jenisPembayaran.setOnItemSelectedListener(this);
    }

    private void spinnerTanggalBayar() {
        adapterTanggalBayar = ArrayAdapter.createFromResource(this, R.array.WaktuBayar, android.R.layout.simple_spinner_item);
        adapterTanggalBayar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_tanggal.setAdapter(adapterTanggalBayar);
        spin_tanggal.setOnItemSelectedListener(this);
    }

    private void spinnerStatus() {
        adapterStatus = ArrayAdapter.createFromResource(this, R.array.Status, android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_status.setAdapter(adapterStatus);
        spin_status.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spin_jenisPembayaran = (Spinner) parent;
        spin_tanggal = (Spinner) parent;
        spin_status = (Spinner) parent;
        if (spin_jenisPembayaran.getId() == R.id.spin_jenisPembayaran) {
            if (!parent.getItemAtPosition(position).equals("...Jenis Pembayaran...")) {
                JnsPembayaran = parent.getItemAtPosition(position).toString();

            }
        } else if (spin_tanggal.getId() == R.id.spin_tanggal) {
            if (!parent.getItemAtPosition(position).equals("...Waktu Bayar...")) {
                TglBayar = parent.getItemAtPosition(position).toString();

            }
        } else if (spin_status.getId() == R.id.spin_status) {
            if (!parent.getItemAtPosition(position).equals("...Status...")) {
                StsBayar = parent.getItemAtPosition(position).toString();

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void filteredJNS() {
        if (JnsPembayaran != null) {
            dbPembayaran.orderByChild("jenis").equalTo(JnsPembayaran).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        myList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Pembayaran_Model pembayaranModel = dataSnapshot.getValue(Pembayaran_Model.class);
                            myList.add(pembayaranModel);
                        }
                        myAdapter.notifyDataSetChanged();
                    } else {
                        myList.clear();
                        Toast.makeText(Laporan_Pembayaran.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(this, "Pilih opsi filter!", Toast.LENGTH_SHORT).show();
        }
    }

    private void filteredSTS() {
        if (StsBayar != null) {
            dbPembayaran.orderByChild("status").equalTo(StsBayar.toLowerCase()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        myList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Pembayaran_Model pembayaranModel = dataSnapshot.getValue(Pembayaran_Model.class);
                            myList.add(pembayaranModel);
                        }
                        myAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(Laporan_Pembayaran.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            myList.clear();
            Toast.makeText(Laporan_Pembayaran.this, "Pilih opsi filter!", Toast.LENGTH_SHORT).show();
        }
    }

    private void filteredTGL() {
        if (TglBayar != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance();
            if (TglBayar.equals("1 Minggu")) {
                calendar.add(Calendar.DAY_OF_WEEK, -7);
            } else if (TglBayar.equals("1 Bulan")) {
                calendar.add(Calendar.DAY_OF_WEEK, -30);
            } else if (TglBayar.equals("3 Bulan")) {
                calendar.add(Calendar.DAY_OF_WEEK, -90);
            } else if (TglBayar.equals("6 Bulan")) {
                calendar.add(Calendar.DAY_OF_WEEK, -180);
            }
            startDate = dateFormat.format(calendar.getTime());
            dbPembayaran.orderByChild("tanggal").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        myList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            tanggal = dataSnapshot.child("tanggal").getValue().toString();
                            try {
                                Date date = dateFormat.parse(startDate);
                                Date date2 = dateFormat.parse(tanggal);
                                if (date2.getTime() >= date.getTime()) {
                                    Pembayaran_Model pembayaranModel = dataSnapshot.getValue(Pembayaran_Model.class);
                                    myList.add(pembayaranModel);
                                }
                                myAdapter.notifyDataSetChanged();
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        Toast.makeText(Laporan_Pembayaran.this, "Data tidak ditemukan2", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


//        try {
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.DAY_OF_WEEK, -7);
//            Date oneMonthAgo = calendar.getTime();
//            String oneMonthAgoString = dateFormat.format(oneMonthAgo);
//            Date parsedOneMonthAgo = dateFormat.parse(oneMonthAgoString);
//            Query query = dbPembayaran.orderByChild("tanggal").startAt(parsedOneMonthAgo.getTime());
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            Toast.makeText(Laporan_Pembayaran.this, String.valueOf(dataSnapshot), Toast.LENGTH_SHORT).show();
//                            Pembayaran_Model pembayaranModel = dataSnapshot.getValue(Pembayaran_Model.class);
//                            myList.add(pembayaranModel);
//                        }
//                        myAdapter.notifyDataSetChanged();
//                    }else {
//                        Toast.makeText(Laporan_Pembayaran.this, "NULL", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Handle any errors
//                }
//            });
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        } else {
            Toast.makeText(this, "Pilih opsi filter!", Toast.LENGTH_SHORT).show();
        }
    }
    private void importData(ArrayList<Pembayaran_Model> pembayaranModels) {
        try {
            String strDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.getDefault()).format(new Date());
            File root = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Laporan Pembayaran");
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

            XSSFSheet sheet = workbook.createSheet("Data Laporan Pembayaran");
            XSSFRow row = sheet.createRow(0);

            XSSFCell cell = row.createCell(0);
            cell.setCellValue("Nama");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(1);
            cell.setCellValue("NPM");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(2);
            cell.setCellValue("Jenis Pembayaran");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(3);
            cell.setCellValue("Jumlah Dana");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(4);
            cell.setCellValue("Tanggal");
            cell.setCellStyle(headerStyle);

            cell = row.createCell(5);
            cell.setCellValue("Status");
            cell.setCellStyle(headerStyle);

            for (int i = 0; i < pembayaranModels.size(); i++) {
                row = sheet.createRow(i + 1);

                cell = row.createCell(0);
                cell.setCellValue(pembayaranModels.get(i).getNama());
                sheet.setColumnWidth(0, (pembayaranModels.get(i).getNama().length() + 30) * 256);

                cell = row.createCell(1);
                cell.setCellValue(pembayaranModels.get(i).getNpm());
                sheet.setColumnWidth(1, pembayaranModels.get(i).getNpm().length() * 400);

                cell = row.createCell(2);
                cell.setCellValue(pembayaranModels.get(i).getJenis());
                sheet.setColumnWidth(2, pembayaranModels.get(i).getJenis().length() * 400);

                cell = row.createCell(3);
                cell.setCellValue(pembayaranModels.get(i).getJumlah_dana());
                sheet.setColumnWidth(3, pembayaranModels.get(i).getJumlah_dana().length() * 400);

                cell = row.createCell(4);
                cell.setCellValue(pembayaranModels.get(i).getTanggal());
                sheet.setColumnWidth(4, pembayaranModels.get(i).getTanggal().length() * 400);

                cell = row.createCell(5);
                cell.setCellValue(pembayaranModels.get(i).getStatus());
                sheet.setColumnWidth(5, pembayaranModels.get(i).getStatus().length() * 400);

            }
            workbook.write(outputStream);
            outputStream.close();
            Toast.makeText(Laporan_Pembayaran.this, "Data berhasil di ekspor!", Toast.LENGTH_SHORT).show();

            Uri uri = FileProvider.getUriForFile(Laporan_Pembayaran.this, "com.asramakita.asramakita.provider", path);
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