package com.asramakita.asramakita.mahasiswa.ijinkeluar;

import java.io.Serializable;

public class Model_ijin_keluar implements Serializable {
    public String id_ijin;
    public String tanggalKeluar;
    public String tanggalKembali;
    public String jam_keluar;
    public String jam_kembali;
    public String status;
    public String sbg;
    public String lampiran;
    public String id_mhs;
    public String npm;
    public String nama;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getId_mhs() {
        return id_mhs;
    }

    public void setId_mhs(String id_mhs) {
        this.id_mhs = id_mhs;
    }

    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String keterangan;

    public String getId_ijin() {
        return id_ijin;
    }

    public void setId_ijin(String id_ijin) {
        this.id_ijin = id_ijin;
    }

    public String getTanggalKeluar() {
        return tanggalKeluar;
    }

    public void setTanggalKeluar(String tanggalKeluar) {
        this.tanggalKeluar = tanggalKeluar;
    }

    public String getTanggalKembali() {
        return tanggalKembali;
    }

    public void setTanggalKembali(String tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }

    public String getJam_keluar() {
        return jam_keluar;
    }

    public void setJam_keluar(String jam_keluar) {
        this.jam_keluar = jam_keluar;
    }

    public String getJam_kembali() {
        return jam_kembali;
    }

    public void setJam_kembali(String jam_kembali) {
        this.jam_kembali = jam_kembali;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSbg() {
        return sbg;
    }

    public void setSbg(String sbg) {
        this.sbg = sbg;
    }

    public Model_ijin_keluar() {
    }

    public String getLampiran() {
        return lampiran;
    }

    public void setLampiran(String lampiran) {
        this.lampiran = lampiran;
    }

    public Model_ijin_keluar(String id_ijin, String tanggalKeluar, String tanggalKembali, String jam_keluar, String jam_kembali, String status, String sbg, String lampiran, String keterangan, String id_mhs, String npm, String nama) {
        this.id_ijin = id_ijin;
        this.tanggalKeluar = tanggalKeluar;
        this.tanggalKembali = tanggalKembali;
        this.jam_keluar = jam_keluar;
        this.jam_kembali = jam_kembali;
        this.status = status;
        this.sbg = sbg;
        this.lampiran = lampiran;
        this.keterangan = keterangan;
        this.id_mhs = id_mhs;
        this.npm = npm;
        this.nama = nama;
    }
}
