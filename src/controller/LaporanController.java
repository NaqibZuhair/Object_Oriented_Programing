package controller;

import model.Laporan;
import view.LaporanView;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.*;

public class LaporanController {
    private ArrayList<Laporan> laporanList;
    private LaporanView view;

    public LaporanController(LaporanView view, ArrayList<Laporan> laporanList) {
        this.view = view;
        this.laporanList = laporanList;

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

    private void tambahData() {
        Laporan laporan = view.getInputData();
        if (laporan != null && !laporan.getIdLaporan().isEmpty()) {
            laporanList.add(laporan);

            // Menambahkan data ke database
            try (Connection conn = DataBaseConnector.connect()) {
                String query = "INSERT INTO laporan (idLaporan, alamatKirim, deskripsi, tanggal) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, laporan.getIdLaporan());
                    stmt.setString(2, laporan.getAlamatKirim());
                    stmt.setString(3, laporan.getDeskripsi());
                    stmt.setString(4, laporan.getTanggal());

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Data laporan berhasil ditambahkan ke database.");
                    } else {
                        System.out.println("Data laporan gagal ditambahkan.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error saat menambahkan data ke database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            updateDisplay();
            JOptionPane.showMessageDialog(view, "Data berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Laporan tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editData() {
        String id = view.getSearchId();
        Laporan newData = view.getInputData();
        if (newData != null && !id.isEmpty()) {
            for (int i = 0; i < laporanList.size(); i++) {
                if (laporanList.get(i).getIdLaporan().equals(id)) {
                    laporanList.set(i, newData);

                    // Mengupdate data di database
                    try (Connection conn = DataBaseConnector.connect()) {
                        String query = "UPDATE laporan SET alamatKirim = ?, deskripsi = ?, tanggal = ? WHERE idLaporan = ?";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setString(1, newData.getAlamatKirim());
                            stmt.setString(2, newData.getDeskripsi());
                            stmt.setString(3, newData.getTanggal());
                            stmt.setString(4, id);

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
            JOptionPane.showMessageDialog(view, "ID Laporan tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        String id = view.getSearchId();
        if (!id.isEmpty()) {
            for (int i = 0; i < laporanList.size(); i++) {
                if (laporanList.get(i).getIdLaporan().equals(id)) {
                    laporanList.remove(i);

                    // Menghapus data di database
                    try (Connection conn = DataBaseConnector.connect()) {
                        String query = "DELETE FROM laporan WHERE idLaporan = ?";
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
            JOptionPane.showMessageDialog(view, "ID Laporan tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariData() {
        String searchTerm = view.getSearchId();
        if (!searchTerm.isEmpty()) {
            ArrayList<Laporan> filteredList = new ArrayList<>();
            for (Laporan laporan : laporanList) {
                if (laporan.getIdLaporan().equals(searchTerm) || laporan.getAlamatKirim().toLowerCase().contains(searchTerm.toLowerCase())) {
                    filteredList.add(laporan);
                }
            }
            if (!filteredList.isEmpty()) {
                view.updateTable(filteredList);
            } else {
                view.setDisplayText("Data tidak ditemukan!");
            }
        } else {
            view.setDisplayText("Masukkan ID Laporan atau Alamat Kirim untuk pencarian!");
        }
    }

    private void updateDisplay() {
        try (Connection conn = DataBaseConnector.connect()) {
            String query = "SELECT * FROM laporan";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                ArrayList<Laporan> dbList = new ArrayList<>();
                while (rs.next()) {
                    dbList.add(new Laporan(
                        rs.getString("idLaporan"),
                        rs.getString("alamatKirim"),
                        rs.getString("deskripsi"),
                        rs.getString("tanggal")
                    ));
                }
                view.updateTable(dbList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.setDisplayText("Error fetching data dari database!");
        }
    }

    public LaporanView getView() { return view; }
}