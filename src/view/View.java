package view;

import controller.Controller;
import model.*;
import java.util.Scanner;

public class View {
    private Scanner scanner = new Scanner(System.in);
    public Controller controller;
    
    public void showLoginMenu() {
        System.out.println("\n=== Login Sistem ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.println("-------------------");
        controller.validateLogin(username, password);
    }
    
    public void showMainMenu() {
        System.out.println("\n=== Sistem Manajemen PT. Inhutani V ===");
        System.out.println("1. Manajemen Barang");
        System.out.println("2. Manajemen Transaksi");
        System.out.println("3. Manajemen Operasional");
        System.out.println("4. Manajemen Laporan");
        System.out.println("5. Keluar");
        System.out.print("Pilih menu (1-5): ");
    }
    
    public void showSubMenu(String entity) {
        System.out.println("\n=== Manajemen " + entity + " ===");
        System.out.println("1. Tambah " + entity);
        System.out.println("2. Edit " + entity);
        System.out.println("3. Hapus " + entity);
        System.out.println("4. Cari " + entity);
        System.out.println("5. Kembali");
        System.out.print("Pilih menu (1-5): ");
    }
    
    public void showBarangForm() {
        System.out.print("ID Barang: ");
        String id = scanner.nextLine();
        System.out.print("Jenis Barang: ");
        String jenis = scanner.nextLine();
        System.out.print("Stok Gudang: ");
        int stok = Integer.parseInt(scanner.nextLine());
        System.out.print("Barang Masuk: ");
        int masuk = Integer.parseInt(scanner.nextLine());
        System.out.print("Barang Keluar: ");
        int keluar = Integer.parseInt(scanner.nextLine());
        System.out.print("Tanggal (YYYY-MM-DD): ");
        String tanggal = scanner.nextLine();
        controller.addBarang(id, jenis, stok, masuk, keluar, tanggal);
    }
    
    public void showTransaksiForm() {
        System.out.print("ID Transaksi: ");
        String id = scanner.nextLine();
        System.out.print("Tanggal (YYYY-MM-DD): ");
        String tanggal = scanner.nextLine();
        System.out.print("Nama Pelanggan: ");
        String nama = scanner.nextLine();
        System.out.print("Jenis Barang: ");
        String jenis = scanner.nextLine();
        System.out.print("Jumlah: ");
        int jumlah = Integer.parseInt(scanner.nextLine());
        System.out.print("Status Bayar: ");
        String status = scanner.nextLine();
        controller.addTransaksi(id, tanggal, nama, jenis, jumlah, status);
    }
    
    public void showOperasionalForm() {
        System.out.print("ID Transaksi: ");
        String id = scanner.nextLine();
        System.out.print("Tanggal (YYYY-MM-DD): ");
        String tanggal = scanner.nextLine();
        System.out.print("Biaya Mobil: ");
        double biayaMobil = Double.parseDouble(scanner.nextLine());
        System.out.print("Biaya Supir: ");
        double biayaSupir = Double.parseDouble(scanner.nextLine());
        controller.addOperasional(id, tanggal, biayaMobil, biayaSupir);
    }
    
    public void showLaporanForm() {
        System.out.print("ID Laporan: ");
        String id = scanner.nextLine();
        System.out.print("Alamat Kirim: ");
        String alamat = scanner.nextLine();
        System.out.print("Deskripsi: ");
        String deskripsi = scanner.nextLine();
        System.out.print("Tanggal (YYYY-MM-DD): ");
        String tanggal = scanner.nextLine();
        controller.addLaporan(id, alamat, deskripsi, tanggal);
    }
    
    public void showSearchForm(String entity) {
        System.out.print("Masukkan ID " + entity + ": ");
        String id = scanner.nextLine();
        switch (entity) {
            case "Barang":
                controller.searchBarang(id);
                break;
            case "Transaksi":
                controller.searchTransaksi(id);
                break;
            case "Operasional":
                controller.searchOperasional(id);
                break;
            case "Laporan":
                controller.searchLaporan(id);
                break;
        }
    }
    
    public void displayBarang(Barang barang) {
        if (barang != null) {
            System.out.println("ID: " + barang.getIdBarang());
            System.out.println("Jenis: " + barang.getJenisBarang());
            System.out.println("Stok: " + barang.getStokGudang());
            System.out.println("Masuk: " + barang.getBarangMasuk());
            System.out.println("Keluar: " + barang.getBarangKeluar());
            System.out.println("Tanggal: " + barang.getTanggal());
        } else {
            System.out.println("Data tidak ditemukan!");
        }
    }
    
    public void displayTransaksi(Transaksi transaksi) {
        if (transaksi != null) {
            System.out.println("ID: " + transaksi.getIdTransaksi());
            System.out.println("Tanggal: " + transaksi.getTanggal());
            System.out.println("Pelanggan: " + transaksi.getNamaPelanggan());
            System.out.println("Jenis Barang: " + transaksi.getJenisBarang());
            System.out.println("Jumlah: " + transaksi.getJumlah());
            System.out.println("Status Bayar: " + transaksi.getStatusBayar());
        } else {
            System.out.println("Data tidak ditemukan!");
        }
    }
    
    public void displayOperasional(Operasional operasional) {
        if (operasional != null) {
            System.out.println("ID: " + operasional.getIdTransaksi());
            System.out.println("Tanggal: " + operasional.getTanggal());
            System.out.println("Biaya Mobil: " + operasional.getBiayaMobil());
            System.out.println("Biaya Supir: " + operasional.getBiayaSupir());
            System.out.println("Total Biaya: " + operasional.getTotalBiaya());
        } else {
            System.out.println("Data tidak ditemukan!");
        }
    }
    
    public void displayLaporan(Laporan laporan) {
        if (laporan != null) {
            System.out.println("ID: " + laporan.getIdLaporan());
            System.out.println("Alamat: " + laporan.getAlamatKirim());
            System.out.println("Deskripsi: " + laporan.getDeskripsi());
            System.out.println("Tanggal: " + laporan.getTanggal());
        } else {
            System.out.println("Data tidak ditemukan!");
        }
    }
    
    public void displayMessage(String message) {
        System.out.println(message);
    }
}