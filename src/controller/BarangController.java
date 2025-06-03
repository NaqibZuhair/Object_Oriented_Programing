package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import model.Barang;
import view.BarangView;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.*;

public class BarangController {
    private ArrayList<Barang> barangList;
    private BarangView view;



    public BarangController(BarangView view, ArrayList<Barang> barangList) {
        this.view = view;
        this.barangList = barangList;

        view.getTambahButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahData();
            }
        });

        view.getEditButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editData();
            }
        });

        view.getHapusButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusData();
            }
        });

        view.getCariButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cariData();
            }
        });
        
        updateDisplay();
    }

    // Metode untuk mengubah format tanggal
    public String formatDate(String inputDate) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");  // Format input
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");  // Format yang diterima MySQL
        Date date = inputFormat.parse(inputDate);  // Mengubah string tanggal ke format Date
        return outputFormat.format(date);  // Mengubah Date ke string dengan format yang diinginkan
    }

    private void tambahData() {
        Barang barang = view.getInputData();
        if (barang != null && !barang.getIdBarang().isEmpty()) {
            barangList.add(barang);

            // Menambahkan data ke database
            try (Connection conn = DataBaseConnector.connect()) {
                String query = "INSERT INTO barang (idBarang, jenisBarang, stokGudang, barangMasuk, barangKeluar, tanggal) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, barang.getIdBarang());
                    stmt.setString(2, barang.getJenisBarang());
                    stmt.setInt(3, barang.getStokGudang());
                    stmt.setInt(4, barang.getBarangMasuk());
                    stmt.setInt(5, barang.getBarangKeluar());

                    // Format tanggal dan masukkan ke dalam query
                    String tanggalFormatted = formatDate(barang.getTanggal());  // Panggil formatDate untuk mengubah format tanggal
                    stmt.setString(6, tanggalFormatted);  // Menggunakan tanggal yang sudah diformat

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Data berhasil ditambahkan ke database.");
                    } else {
                        System.out.println("Data gagal ditambahkan.");
                    }
                }
            } catch (SQLException | ParseException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saat menambahkan data ke database: " + e.getMessage());
            }
        }
    }

    private void editData() {
        String id = view.getSearchId();
        Barang newData = view.getInputData();
        if (newData != null && !id.isEmpty()) {
            for (int i = 0; i < barangList.size(); i++) {
                if (barangList.get(i).getIdBarang().equals(id)) {
                    barangList.set(i, newData);

                    // Mengupdate data di database
                    try (Connection conn = DataBaseConnector.connect()) {
                        String query = "UPDATE barang SET jenisBarang = ?, stokGudang = ?, barangMasuk = ?, barangKeluar = ?, tanggal = ? WHERE idBarang = ?";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setString(1, newData.getJenisBarang());
                            stmt.setInt(2, newData.getStokGudang());
                            stmt.setInt(3, newData.getBarangMasuk());
                            stmt.setInt(4, newData.getBarangKeluar());
                            stmt.setString(5, newData.getTanggal());
                            stmt.setString(6, id);

                            stmt.executeUpdate();
                            JOptionPane.showMessageDialog(view, "Data berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(view, "Gagal mengubah data!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    updateDisplay();
                    JOptionPane.showMessageDialog(view, "Data berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(view, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Barang tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        String id = view.getSearchId();
        if (!id.isEmpty()) {
            for (int i = 0; i < barangList.size(); i++) {
                if (barangList.get(i).getIdBarang().equals(id)) {
                    barangList.remove(i);

                    // Menghapus data di database
                    try (Connection conn = DataBaseConnector.connect()) {
                        String query = "DELETE FROM barang WHERE idBarang = ?";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setString(1, id);

                            stmt.executeUpdate();
                            JOptionPane.showMessageDialog(view, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(view, "Gagal menghapus data!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    updateDisplay();
                    JOptionPane.showMessageDialog(view, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(view, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Barang tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariData() {
        String id = view.getSearchId();
        if (!id.isEmpty()) {
            for (Barang barang : barangList) {
                if (barang.getIdBarang().equals(id)) {
                    view.setDisplayText(
                        "ID Barang: " + barang.getIdBarang() + "\n" +
                        "Jenis Barang: " + barang.getJenisBarang() + "\n" +
                        "Stok Gudang: " + barang.getStokGudang() + "\n" +
                        "Barang Masuk: " + barang.getBarangMasuk() + "\n" +
                        "Barang Keluar: " + barang.getBarangKeluar() + "\n" +
                        "Tanggal: " + barang.getTanggal()
                    );
                    return;
                }
            }
            view.setDisplayText("Data tidak ditemukan!");
        } else {
            view.setDisplayText("Masukkan ID Barang untuk pencarian!");
        }
    }

    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = DataBaseConnector.connect()) {
            String query = "SELECT * FROM barang"; // Pastikan query benar
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String idBarang = rs.getString("idBarang");
                    String jenisBarang = rs.getString("jenisBarang");
                    int stokGudang = rs.getInt("stokGudang");
                    int barangMasuk = rs.getInt("barangMasuk");
                    int barangKeluar = rs.getInt("barangKeluar");
                    String tanggal = rs.getString("tanggal");

                    sb.append("ID: ").append(idBarang)
                            .append(", Jenis: ").append(jenisBarang)
                            .append(", Stok: ").append(stokGudang)
                            .append(", Masuk: ").append(barangMasuk)
                            .append(", Keluar: ").append(barangKeluar)
                            .append(", Tanggal: ").append(tanggal)
                            .append("\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        view.setDisplayText(sb.toString());
    }

    public BarangView getView() { return view; }
}