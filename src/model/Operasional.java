package Model;

public class Operasional {
    private String idTransaksi;
    private String tanggal;
    private double biayaMobil;
    private double biayaSupir;
    private double totalBiaya;

    public Operasional(String idTransaksi, String tanggal, double biayaMobil, double biayaSupir, double totalBiaya) {
        this.idTransaksi = idTransaksi;
        this.tanggal = tanggal;
        this.biayaMobil = biayaMobil;
        this.biayaSupir = biayaSupir;
        this.totalBiaya = totalBiaya;
    }

    public String getIdTransaksi() { return idTransaksi; }
    public String getTanggal() { return tanggal; }
    public double getBiayaMobil() { return biayaMobil; }
    public double getBiayaSupir() { return biayaSupir; }
    public double getTotalBiaya() { return totalBiaya; }
}