package com.asramakita.asramakita.admin.permintaanpendaftaran;

import java.io.Serializable;

public class Pendaftaran_Class implements Serializable {

    public String npm;
    public String nama;
    public String email;
    public String nohp;
    public String fakultas;
    public String password;
    public String asalsekolah;
    public String almtorangtua;
    public String hporangtua;
    public String status;
    public String tanggal;
    public String alamat;

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Pendaftaran_Class() {
    }

    public Pendaftaran_Class(String npm, String nama, String email, String nohp, String fakultas, String password, String asalsekolah, String almtorangtua, String hporangtua, String status, String tanggal, String alamat) {
        this.npm = npm;
        this.nama = nama;
        this.email = email;
        this.nohp = nohp;
        this.fakultas = fakultas;
        this.password = password;
        this.asalsekolah = asalsekolah;
        this.almtorangtua = almtorangtua;
        this.hporangtua = hporangtua;
        this.status = status;
        this.tanggal = tanggal;
        this.alamat = alamat;
    }

    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAsalsekolah() {
        return asalsekolah;
    }

    public void setAsalsekolah(String asalsekolah) {
        this.asalsekolah = asalsekolah;
    }

    public String getAlmtorangtua() {
        return almtorangtua;
    }

    public void setAlmtorangtua(String almtorangtua) {
        this.almtorangtua = almtorangtua;
    }

    public String getHporangtua() {
        return hporangtua;
    }

    public void setHporangtua(String hporangtua) {
        this.hporangtua = hporangtua;
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
