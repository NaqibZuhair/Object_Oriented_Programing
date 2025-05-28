package model;

public class Transaksi {
    private String idTransaksi;
    private String tanggal;
    private String namaPelanggan;
    private String jenisBarang;
    private int jumlah;
    private String statusPembayaran;

    public Transaksi(String idTransaksi, String tanggal, String namaPelanggan, String jenisBarang, int jumlah, String statusPembayaran) {
        this.idTransaksi = idTransaksi;
        this.tanggal = tanggal;
        this.namaPelanggan = namaPelanggan;
        this.jenisBarang = jenisBarang;
        this.jumlah = jumlah;
        this.statusPembayaran = statusPembayaran;
    }

    public String getIdTransaksi() { return idTransaksi; }
    public String getTanggal() { return tanggal; }
    public String getNamaPelanggan() { return namaPelanggan; }
    public String getJenisBarang() { return jenisBarang; }
    public int getJumlah() { return jumlah; }
    public String getStatusPembayaran() { return statusPembayaran; }
}