package model;

public class Barang {
    private String idBarang;
    private String jenisBarang;
    private int stokGudang;
    private int barangMasuk;
    private int barangKeluar;
    private String tanggal;
    
    public Barang(String idBarang, String jenisBarang, int stokGudang, int barangMasuk, int barangKeluar, String tanggal) {
        this.idBarang = idBarang;
        this.jenisBarang = jenisBarang;
        this.stokGudang = stokGudang;
        this.barangMasuk = barangMasuk;
        this.barangKeluar = barangKeluar;
        this.tanggal = tanggal;
    }
    
    public String getIdBarang() { return idBarang; }
    public String getJenisBarang() { return jenisBarang; }
    public int getStokGudang() { return stokGudang; }
    public int getBarangMasuk() { return barangMasuk; }
    public int getBarangKeluar() { return barangKeluar; }
    public String getTanggal() { return tanggal; }
    
    public void setJenisBarang(String jenisBarang) { this.jenisBarang = jenisBarang; }
    public void setStokGudang(int stokGudang) { this.stokGudang = stokGudang; }
    public void setBarangMasuk(int barangMasuk) { this.barangMasuk = barangMasuk; }
    public void setBarangKeluar(int barangKeluar) { this.barangKeluar = barangKeluar; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
}