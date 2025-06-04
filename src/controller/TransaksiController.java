package controller;

import model.Transaksi;
import view.TransaksiView;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.*;

public class TransaksiController {
    private ArrayList<Transaksi> transaksiList;
    private TransaksiView view;

    public TransaksiController(TransaksiView view, ArrayList<Transaksi> transaksiList) {
        this.view = view;
        this.transaksiList = transaksiList;

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
        Transaksi transaksi = view.getInputData();
        if (transaksi != null && !transaksi.getIdTransaksi().isEmpty()) {
            transaksiList.add(transaksi);

            // Menambahkan data ke database
            try (Connection conn = DataBaseConnector.connect()) {
                String query = "INSERT INTO transaksi (idTransaksi, tanggal, namaPelanggan, jenisBarang, jumlah, statusPembayaran) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, transaksi.getIdTransaksi());
                    stmt.setString(2, transaksi.getTanggal());
                    stmt.setString(3, transaksi.getNamaPelanggan());
                    stmt.setString(4, transaksi.getJenisBarang());
                    stmt.setInt(5, transaksi.getJumlah());
                    stmt.setString(6, transaksi.getStatusPembayaran());

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Data transaksi berhasil ditambahkan ke database.");
                    } else {
                        System.out.println("Data transaksi gagal ditambahkan.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error saat menambahkan data ke database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            updateDisplay();
            JOptionPane.showMessageDialog(view, "Data berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Transaksi tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editData() {
        String id = view.getSearchId();
        Transaksi newData = view.getInputData();
        if (newData != null && !id.isEmpty()) {
            for (int i = 0; i < transaksiList.size(); i++) {
                if (transaksiList.get(i).getIdTransaksi().equals(id)) {
                    transaksiList.set(i, newData);

                    // Mengupdate data di database
                    try (Connection conn = DataBaseConnector.connect()) {
                        String query = "UPDATE transaksi SET tanggal = ?, namaPelanggan = ?, jenisBarang = ?, jumlah = ?, statusPembayaran = ? WHERE idTransaksi = ?";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setString(1, newData.getTanggal());
                            stmt.setString(2, newData.getNamaPelanggan());
                            stmt.setString(3, newData.getJenisBarang());
                            stmt.setInt(4, newData.getJumlah());
                            stmt.setString(5, newData.getStatusPembayaran());
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
            JOptionPane.showMessageDialog(view, "ID Transaksi tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        String id = view.getSearchId();
        if (!id.isEmpty()) {
            for (int i = 0; i < transaksiList.size(); i++) {
                if (transaksiList.get(i).getIdTransaksi().equals(id)) {
                    transaksiList.remove(i);

                    // Menghapus data di database
                    try (Connection conn = DataBaseConnector.connect()) {
                        String query = "DELETE FROM transaksi WHERE idTransaksi = ?";
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
            JOptionPane.showMessageDialog(view, "ID Transaksi tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariData() {
        String searchTerm = view.getSearchId();
        if (!searchTerm.isEmpty()) {
            ArrayList<Transaksi> filteredList = new ArrayList<>();
            for (Transaksi transaksi : transaksiList) {
                if (transaksi.getIdTransaksi().equals(searchTerm) || transaksi.getNamaPelanggan().toLowerCase().contains(searchTerm.toLowerCase())) {
                    filteredList.add(transaksi);
                }
            }
            if (!filteredList.isEmpty()) {
                view.updateTable(filteredList);
            } else {
                view.setDisplayText("Data tidak ditemukan!");
            }
        } else {
            view.setDisplayText("Masukkan ID Transaksi atau Nama Pelanggan untuk pencarian!");
        }
    }

    private void updateDisplay() {
        try (Connection conn = DataBaseConnector.connect()) {
            String query = "SELECT * FROM transaksi";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                ArrayList<Transaksi> dbList = new ArrayList<>();
                while (rs.next()) {
                    dbList.add(new Transaksi(
                        rs.getString("idTransaksi"),
                        rs.getString("tanggal"),
                        rs.getString("namaPelanggan"),
                        rs.getString("jenisBarang"),
                        rs.getInt("jumlah"),
                        rs.getString("statusPembayaran")
                    ));
                }
                view.updateTable(dbList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.setDisplayText("Error fetching data from database!");
        }
    }

    public TransaksiView getView() { return view; }
}