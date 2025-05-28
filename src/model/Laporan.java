package model;

public class Laporan {
    private String idLaporan;
    private String alamatKirim;
    private String deskripsi;
    private String tanggal;
    
    public Laporan(String idLaporan, String alamatKirim, String deskripsi, String tanggal) {
        this.idLaporan = idLaporan;
        this.alamatKirim = alamatKirim;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
    }
    
    public String getIdLaporan() { return idLaporan; }
    public String getAlamatKirim() { return alamatKirim; }
    public String getDeskripsi() { return deskripsi; }
    public String getTanggal() { return tanggal; }
    
    public void setAlamatKirim(String alamatKirim) { this.alamatKirim = alamatKirim; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
}