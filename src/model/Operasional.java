package model;

public class Operasional {
    private String idTransaksi;
    private String tanggal;
    private double biayaMobil;
    private double biayaSupir;
    private double totalBiaya;
    
    public Operasional(String idTransaksi, String tanggal, double biayaMobil, double biayaSupir) {
        this.idTransaksi = idTransaksi;
        this.tanggal = tanggal;
        this.biayaMobil = biayaMobil;
        this.biayaSupir = biayaSupir;
        this.totalBiaya = biayaMobil + biayaSupir;
    }
    
    public String getIdTransaksi() { return idTransaksi; }
    public String getTanggal() { return tanggal; }
    public double getBiayaMobil() { return biayaMobil; }
    public double getBiayaSupir() { return biayaSupir; }
    public double getTotalBiaya() { return totalBiaya; }
    
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public void setBiayaMobil(double biayaMobil) { this.biayaMobil = biayaMobil; }
    public void setBiayaSupir(double biayaSupir) { this.biayaSupir = biayaSupir; }
    public void setTotalBiaya() { this.totalBiaya = this.biayaMobil + this.biayaSupir; }
}