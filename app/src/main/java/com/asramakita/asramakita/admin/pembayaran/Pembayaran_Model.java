package com.asramakita.asramakita.admin.pembayaran;

import java.io.Serializable;

public class Pembayaran_Model implements Serializable {
    public String bukti;
    public String id_mhs;
    public String jenis;
    public String status;
    public String tanggal;

    public String nama;
    public String npm;
    public String jumlah_dana;

    public String getJumlah_dana() {
        return jumlah_dana;
    }

    public void setJumlah_dana(String jumlah_dana) {
        this.jumlah_dana = jumlah_dana;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public Pembayaran_Model() {
    }

    public Pembayaran_Model(String bukti, String id_mhs, String jenis, String status, String tanggal, String nama, String npm, String jumlah_dana) {
        this.bukti = bukti;
        this.id_mhs = id_mhs;
        this.jenis = jenis;
        this.status = status;
        this.tanggal = tanggal;
        this.nama = nama;
        this.npm = npm;
        this.jumlah_dana = jumlah_dana;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }

    public String getId_mhs() {
        return id_mhs;
    }

    public void setId_mhs(String id_mhs) {
        this.id_mhs = id_mhs;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
