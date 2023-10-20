package com.asramakita.asramakita;

public class UIDStorage {
    private static UIDStorage instance;
    private String uid;
    private String selectedLantai;
    public String nama;
    public String npm;
    public String fakultas;
    public String alamat;
    public String idMhs;
    public String namaLantai;

    public static void setInstance(UIDStorage instance) {
        UIDStorage.instance = instance;
    }

    public String getNamaLantai() {
        return namaLantai;
    }

    public void setNamaLantai(String namaLantai) {
        this.namaLantai = namaLantai;
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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getIdMhs() {
        return idMhs;
    }

    public void setIdMhs(String idMhs) {
        this.idMhs = idMhs;
    }

    public String getSelectedLantai() {
        return selectedLantai;
    }

    public void setSelectedLantai(String selectedLantai) {
        this.selectedLantai = selectedLantai;
    }

    private UIDStorage() {}

    public static synchronized UIDStorage getInstance() {
        if (instance == null) {
            instance = new UIDStorage();
        }
        return instance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

