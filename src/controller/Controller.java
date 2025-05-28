package controller;

import model.*;
import view.View;
import java.util.ArrayList;

public class Controller {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Barang> barangs = new ArrayList<>();
    private ArrayList<Transaksi> transaksis = new ArrayList<>();
    private ArrayList<Operasional> operasinals = new ArrayList<>();
    private ArrayList<Laporan> laporans = new ArrayList<>();
    private View view;
    private boolean isLoggedIn = false;
    
    public Controller(View view) {
        this.view = view;
        users.add(new User("admin", "admin123"));
    }
    
    public void validateLogin(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                isLoggedIn = true;
                view.displayMessage("Login berhasil!");
                return;
            }
        }
        view.displayMessage("Login gagal! Username atau password salah.");
    }
    
    public void addBarang(String id, String jenis, int stok, int masuk, int keluar, String tanggal) {
        barangs.add(new Barang(id, jenis, stok, masuk, keluar, tanggal));
        view.displayMessage("Barang berhasil ditambahkan!");
    }
    
    public void addTransaksi(String id, String tanggal, String nama, String jenis, int jumlah, String status) {
        transaksis.add(new Transaksi(id, tanggal, nama, jenis, jumlah, status));
        view.displayMessage("Transaksi berhasil ditambahkan!");
    }
    
    public void addOperasional(String id, String tanggal, double biayaMobil, double biayaSupir) {
        operasinals.add(new Operasional(id, tanggal, biayaMobil, biayaSupir));
        view.displayMessage("Data operasional berhasil ditambahkan!");
    }
    
    public void addLaporan(String id, String alamat, String deskripsi, String tanggal) {
        laporans.add(new Laporan(id, alamat, deskripsi, tanggal));
        view.displayMessage("Laporan berhasil ditambahkan!");
    }
    
    public void searchBarang(String id) {
        for (Barang barang : barangs) {
            if (barang.getIdBarang().equals(id)) {
                view.displayBarang(barang);
                return;
            }
        }
        view.displayBarang(null);
    }
    
    public void searchTransaksi(String id) {
        for (Transaksi transaksi : transaksis) {
            if (transaksi.getIdTransaksi().equals(id)) {
                view.displayTransaksi(transaksi);
                return;
            }
        }
        view.displayTransaksi(null);
    }
    
    public void searchOperasional(String id) {
        for (Operasional operasional : operasinals) {
            if (operasional.getIdTransaksi().equals(id)) {
                view.displayOperasional(operasional);
                return;
            }
        }
        view.displayOperasional(null);
    }
    
    public void searchLaporan(String id) {
        for (Laporan laporan : laporans) {
            if (laporan.getIdLaporan().equals(id)) {
                view.displayLaporan(laporan);
                return;
            }
        }
        view.displayLaporan(null);
    }
    
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}