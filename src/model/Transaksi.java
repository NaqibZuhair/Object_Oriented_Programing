package model;

public class Transaksi {
    private String idTransaksi;
    private String tanggal;
    private String namaPelanggan;
    private String jenisBarang;
    private int jumlah;
    private String statusBayar;
    
    public Transaksi(String idTransaksi, String tanggal, String namaPelanggan, String jenisBarang, int jumlah, String statusBayar) {
        this.idTransaksi = idTransaksi;
        this.tanggal = tanggal;
        this.namaPelanggan = namaPelanggan;
        this.jenisBarang = jenisBarang;
        this.jumlah = jumlah;
        this.statusBayar = statusBayar;
    }
    
    public String getIdTransaksi() { return idTransaksi; }
    public String getTanggal() { return tanggal; }
    public String getNamaPelanggan() { return namaPelanggan; }
    public String getJenisBarang() { return jenisBarang; }
    public int getJumlah() { return jumlah; }
    public String getStatusBayar() { return statusBayar; }
    
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }
    public void setJenisBarang(String jenisBarang) { this.jenisBarang = jenisBarang; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public void setStatusBayar(String statusBayar) { this.statusBayar = statusBayar; }
}