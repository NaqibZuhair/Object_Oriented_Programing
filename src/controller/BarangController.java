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
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = inputFormat.parse(inputDate);
        return outputFormat.format(date);
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
        String id = view.getSearchId();
        Barang newData = view.getInputData();
        if (newData != null && !id.isEmpty()) {
            for (int i = 0; i < barangList.size(); i++) {
                if (barangList.get(i).getIdBarang().equals(id)) {
                    barangList.set(i, newData);

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
                    return;
                }
            }
            JOptionPane.showMessageDialog(view, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Barang tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariData() {
        String searchTerm = view.getSearchId();
        if (!searchTerm.isEmpty()) {
            ArrayList<Barang> filteredList = new ArrayList<>();
            for (Barang barang : barangList) {
                if (barang.getIdBarang().equals(searchTerm) || barang.getJenisBarang().toLowerCase().contains(searchTerm.toLowerCase())) {
                    filteredList.add(barang);
                }
            }
            if (!filteredList.isEmpty()) {
                view.updateTable(filteredList);
            } else {
                view.setDisplayText("Data tidak ditemukan!");
            }
        } else {
            view.setDisplayText("Masukkan ID Barang untuk pencarian!");
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