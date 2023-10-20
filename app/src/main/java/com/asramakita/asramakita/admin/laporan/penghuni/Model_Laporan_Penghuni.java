package com.asramakita.asramakita.admin.laporan.penghuni;

import java.io.Serializable;

public class Model_Laporan_Penghuni implements Serializable {
    public String email;
    public String fakultas;
    public String hp;
    public String nama;
    public String npm;
    public String password;
    public String status;
    public String foto;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Model_Laporan_Penghuni() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Model_Laporan_Penghuni(String email, String fakultas, String hp, String nama, String npm, String password, String status, String foto) {
        this.email = email;
        this.fakultas = fakultas;
        this.hp = hp;
        this.nama = nama;
        this.npm = npm;
        this.password = password;
        this.status = status;
        this.foto = foto;
    }
}
