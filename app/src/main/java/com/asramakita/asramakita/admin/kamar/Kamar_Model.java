package com.asramakita.asramakita.admin.kamar;

import java.io.Serializable;

public class Kamar_Model implements Serializable {
    public String nama;
    public String npm;
    public String fakultas;
    public String alamat;
    public String idMhs;

    public String getIdMhs() {
        return idMhs;
    }

    public void setIdMhs(String idMhs) {
        this.idMhs = idMhs;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
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

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public Kamar_Model(String nama, String npm, String fakultas, String alamat, String idMhs) {
        this.nama = nama;
        this.npm = npm;
        this.fakultas = fakultas;
        this.alamat = alamat;
        this.idMhs = idMhs;
    }

    public Kamar_Model() {
    }
}
