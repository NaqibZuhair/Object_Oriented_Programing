package controller;

import model.Operasional;
import view.OperasionalView;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.*;

public class OperasionalController {
    private ArrayList<Operasional> operasionalList;
    private OperasionalView view;

    public OperasionalController(OperasionalView view, ArrayList<Operasional> operasionalList) {
        this.view = view;
        this.operasionalList = operasionalList;

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
        Operasional operasional = view.getInputData();
        if (operasional != null && !operasional.getIdTransaksi().isEmpty()) {
            operasionalList.add(operasional);

            // Menambahkan data ke database
            try (Connection conn = DataBaseConnector.connect()) {
                String query = "INSERT INTO operasional (idTransaksi, tanggal, biayaMobil, biayaSupir, totalBiaya) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, operasional.getIdTransaksi());
                    stmt.setString(2, operasional.getTanggal());
                    stmt.setDouble(3, operasional.getBiayaMobil());
                    stmt.setDouble(4, operasional.getBiayaSupir());
                    stmt.setDouble(5, operasional.getTotalBiaya());

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Data operasional berhasil ditambahkan ke database.");
                    } else {
                        System.out.println("Data operasional gagal ditambahkan.");
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
        Operasional newData = view.getInputData();
        if (newData != null && !id.isEmpty()) {
            for (int i = 0; i < operasionalList.size(); i++) {
                if (operasionalList.get(i).getIdTransaksi().equals(id)) {
                    operasionalList.set(i, newData);

                    // Mengupdate data di database
                    try (Connection conn = DataBaseConnector.connect()) {
                        String query = "UPDATE operasional SET tanggal = ?, biayaMobil = ?, biayaSupir = ?, totalBiaya = ? WHERE idTransaksi = ?";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setString(1, newData.getTanggal());
                            stmt.setDouble(2, newData.getBiayaMobil());
                            stmt.setDouble(3, newData.getBiayaSupir());
                            stmt.setDouble(4, newData.getTotalBiaya());
                            stmt.setString(5, id);

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
            for (int i = 0; i < operasionalList.size(); i++) {
                if (operasionalList.get(i).getIdTransaksi().equals(id)) {
                    operasionalList.remove(i);

                    // Menghapus data di database
                    try (Connection conn = DataBaseConnector.connect()) {
                        String query = "DELETE FROM operasional WHERE idTransaksi = ?";
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
            ArrayList<Operasional> filteredList = new ArrayList<>();
            for (Operasional operasional : operasionalList) {
                if (operasional.getIdTransaksi().equals(searchTerm)) {
                    filteredList.add(operasional);
                }
            }
            if (!filteredList.isEmpty()) {
                view.updateTable(filteredList);
            } else {
                view.setDisplayText("Data tidak ditemukan!");
            }
        } else {
            view.setDisplayText("Masukkan ID Transaksi untuk pencarian!");
        }
    }

    private void updateDisplay() {
        try (Connection conn = DataBaseConnector.connect()) {
            String query = "SELECT * FROM operasional";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                ArrayList<Operasional> dbList = new ArrayList<>();
                while (rs.next()) {
                    dbList.add(new Operasional(
                        rs.getString("idTransaksi"),
                        rs.getString("tanggal"),
                        rs.getDouble("biayaMobil"),
                        rs.getDouble("biayaSupir"),
                        rs.getDouble("totalBiaya")
                    ));
                }
                view.updateTable(dbList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            view.setDisplayText("Error fetching data from database!");
        }
    }

    public OperasionalView getView() { return view; }
}