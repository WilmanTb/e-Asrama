package com.asramakita.asramakita.admin.informasiasrama;

public class pdfClass {
    public String nama,url;

    public pdfClass(){

    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public pdfClass(String nama, String url) {
        this.nama = nama;
        this.url = url;
    }
}
