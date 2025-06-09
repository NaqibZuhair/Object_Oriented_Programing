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
        view.setController(this);

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

    public String formatDate(String inputDate) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");  // Format yang dimasukkan oleh user
        inputFormat.setLenient(false);  // Nonaktifkan leniency untuk memastikan input valid

        try {
            Date date = inputFormat.parse(inputDate);  // Mengonversi input ke Date
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format yang diterima oleh database
            return outputFormat.format(date);  // Mengonversi Date ke format yang diterima oleh database
        } catch (ParseException e) {
            // Jika format salah, lemparkan kembali exception
            throw new ParseException("Format tanggal salah! Harus dalam format yyyy-MM-dd.", e.getErrorOffset());
        }
    }

    private void tambahData() {
        Barang barang = view.getInputData();
        if (barang != null && !barang.getIdBarang().isEmpty()) {
            barangList.add(barang);

            try (Connection conn = DataBaseConnector.connect()) {
                String query = "INSERT INTO barang (idBarang, jenisBarang, stokGudang, barangMasuk, barangKeluar, tanggal) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, barang.getIdBarang());
                    stmt.setString(2, barang.getJenisBarang());
                    stmt.setInt(3, barang.getStokGudang());
                    stmt.setInt(4, barang.getBarangMasuk());
                    stmt.setInt(5, barang.getBarangKeluar());
                    String tanggalFormatted = formatDate(barang.getTanggal());
                    stmt.setString(6, tanggalFormatted);

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
            updateDisplay();
        }
    }

    private void editData() {
        // Ambil data yang sudah diedit di kolom input
        Barang newData = view.getInputData();  // Mendapatkan data yang telah diubah

        if (newData != null) {
            String id = newData.getIdBarang();  // Ambil ID yang akan diedit

            try (Connection conn = DataBaseConnector.connect()) {
                String query = "UPDATE barang SET jenisBarang = ?, stokGudang = ?, barangMasuk = ?, barangKeluar = ?, tanggal = ? WHERE idBarang = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    // Set data baru yang diinputkan ke dalam query
                    stmt.setString(1, newData.getJenisBarang());
                    stmt.setInt(2, newData.getStokGudang());
                    stmt.setInt(3, newData.getBarangMasuk());
                    stmt.setInt(4, newData.getBarangKeluar());
                    stmt.setString(5, newData.getTanggal());
                    stmt.setString(6, id);  // ID Barang untuk menentukan data yang akan diupdate

                    int rowsAffected = stmt.executeUpdate(); // Eksekusi query UPDATE

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(view, "Data berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                        // Setelah data berhasil diubah, perbarui tampilan tabel
                        updateDisplay();  // Memperbarui tabel dengan data terbaru
                    } else {
                        JOptionPane.showMessageDialog(view, "Gagal mengubah data!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Gagal mengubah data di database!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Data tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void hapusData() {
        // Ambil ID Barang dari input dan hilangkan spasi tambahan
        String id = view.getSearchId().trim();
        System.out.println("ID yang dicari: " + id); // Debugging output

        if (!id.isEmpty()) {
            try (Connection conn = DataBaseConnector.connect()) {
                // Query untuk menghapus barang dari database berdasarkan ID Barang
                String query = "DELETE FROM barang WHERE idBarang = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, id); // Set ID Barang yang akan dihapus

                    // Debugging untuk memeriksa ID yang dikirim ke query
                    System.out.println("Menjalankan query untuk menghapus ID: " + id);

                    int rowsAffected = stmt.executeUpdate(); // Eksekusi query

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(view, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(view, "Gagal menghapus data!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Gagal menghapus data di database!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            updateDisplay(); // Update tampilan tabel setelah data dihapus
        } else {
            JOptionPane.showMessageDialog(view, "ID Barang tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariData() {
        // Ambil search term dari input dan hilangkan spasi tambahan
        String searchTerm = view.getSearchId().trim();
        System.out.println("Search Term: " + searchTerm); // Debugging output

        if (searchTerm != null && !searchTerm.isEmpty()) {
            try (Connection conn = DataBaseConnector.connect()) {
                String query = "SELECT * FROM barang WHERE idBarang = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, searchTerm);  // Cari berdasarkan ID Barang

                    try (ResultSet rs = stmt.executeQuery()) {
                        ArrayList<Barang> filteredList = new ArrayList<>();  // List untuk hasil pencarian

                        if (rs.next()) {
                            // Jika ID ditemukan, tampilkan data di kolom input
                            view.setInputData(  // Mengisi kolom input dengan data yang ditemukan
                                    rs.getString("idBarang"),
                                    rs.getString("jenisBarang"),
                                    rs.getInt("stokGudang"),
                                    rs.getInt("barangMasuk"),
                                    rs.getInt("barangKeluar"),
                                    rs.getString("tanggal")
                            );

                            // Menambahkan hasil pencarian ke filteredList
                            filteredList.add(new Barang(
                                    rs.getString("idBarang"),
                                    rs.getString("jenisBarang"),
                                    rs.getInt("stokGudang"),
                                    rs.getInt("barangMasuk"),
                                    rs.getInt("barangKeluar"),
                                    rs.getString("tanggal")
                            ));

                        } else {
                            JOptionPane.showMessageDialog(view, "ID Barang tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error saat mencari data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Masukkan ID Barang untuk pencarian!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateDisplay() {
        try (Connection conn = DataBaseConnector.connect()) {
            String query = "SELECT * FROM barang";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                ArrayList<Barang> dbList = new ArrayList<>();
                while (rs.next()) {
                    dbList.add(new Barang(
                        rs.getString("idBarang"),
                        rs.getString("jenisBarang"),
                        rs.getInt("stokGudang"),
                        rs.getInt("barangMasuk"),
                        rs.getInt("barangKeluar"),
                        rs.getString("tanggal")
                    ));
                }
                view.updateTable(dbList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.setDisplayText("Error fetching data from database!");
        }
    }

    public BarangView getView() { return view; }
}