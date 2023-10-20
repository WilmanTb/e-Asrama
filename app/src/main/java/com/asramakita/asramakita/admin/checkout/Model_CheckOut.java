package com.asramakita.asramakita.admin.checkout;

import java.io.Serializable;

public class Model_CheckOut implements Serializable {
    public String alasan;
    public String email;
    public String fakultas;
    public String hp;
    public String nama;
    public String npm;
    public String tanggal;

    public Model_CheckOut() {
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
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

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Model_CheckOut(String alasan, String email, String fakultas, String hp, String nama, String npm, String tanggal, String status) {
        this.alasan = alasan;
        this.email = email;
        this.fakultas = fakultas;
        this.hp = hp;
        this.nama = nama;
        this.npm = npm;
        this.tanggal = tanggal;
        this.status = status;
    }

    public String status;
}
