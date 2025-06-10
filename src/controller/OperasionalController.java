package controller;

import Model.Operasional;
import view.OperasionalView;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.awt.event.*;
import java.util.Date;

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
        Operasional operasional = view.getInputData();  // Mendapatkan data operasional yang baru dimasukkan
        if (operasional != null && !operasional.getIdTransaksi().isEmpty()) {
            operasionalList.add(operasional);

            try (Connection conn = DataBaseConnector.connect()) {
                // Query untuk menambahkan data operasional
                String query = "INSERT INTO operasional (idtransaksi, tanggal, biaya_mobil, biaya_supir) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, operasional.getIdTransaksi());
                    stmt.setString(2, operasional.getTanggal());
                    stmt.setDouble(3, operasional.getBiayaMobil());
                    stmt.setDouble(4, operasional.getBiayaSupir());

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(view, "Data operasional berhasil ditambahkan ke database.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(view, "Data operasional gagal ditambahkan.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saat menambahkan data ke database: " + e.getMessage());
            }
            updateDisplay();  // Memperbarui tampilan tabel setelah data ditambahkan
        }
    }

    private void editData() {
        Operasional operasional = view.getInputData();  // Mendapatkan data yang telah diubah
        String id = operasional.getIdTransaksi();  // Ambil ID yang akan diedit

        if (operasional != null && !id.isEmpty()) {
            try (Connection conn = DataBaseConnector.connect()) {
                String query = "UPDATE operasional SET tanggal = ?, biaya_mobil = ?, biaya_supir = ? WHERE idtransaksi = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, operasional.getTanggal());
                    stmt.setDouble(2, operasional.getBiayaMobil());
                    stmt.setDouble(3, operasional.getBiayaSupir());
                    stmt.setString(4, id);  // ID Transaksi untuk menentukan data yang akan diupdate

                    int rowsAffected = stmt.executeUpdate();  // Eksekusi query UPDATE

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(view, "Data berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
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
        // Ambil search term dari input dan hilangkan spasi tambahan
        String searchTerm = view.getSearchId().trim();
        System.out.println("Search Term: " + searchTerm); // Debugging output

        if (searchTerm != null && !searchTerm.isEmpty()) {
            try (Connection conn = DataBaseConnector.connect()) {
                // Query untuk mencari berdasarkan ID Operasional
                String query = "SELECT * FROM operasional WHERE idTransaksi = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, searchTerm);  // Cari berdasarkan ID Operasional

                    try (ResultSet rs = stmt.executeQuery()) {
                        ArrayList<Operasional> filteredList = new ArrayList<>();  // List untuk hasil pencarian

                        // Proses setiap data yang ditemukan
                        if (rs.next()) {
                            // Menambahkan hasil pencarian ke filteredList
                            filteredList.add(new Operasional(
                                    rs.getString("idTransaksi"),
                                    rs.getString("tanggal"),
                                    rs.getDouble("biaya_Mobil"),
                                    rs.getDouble("biaya_Supir"),
                                    rs.getDouble("total_Biaya")
                            ));

                            // Mengisi kolom input dengan data yang ditemukan
                            view.setInputData(  // Mengisi kolom input dengan data yang ditemukan
                                    rs.getString("idTransaksi"),
                                    rs.getString("tanggal"),
                                    rs.getDouble("biaya_Mobil"),
                                    rs.getDouble("biaya_Supir"),
                                    rs.getDouble("total_Biaya")
                            );
                        } else {
                            view.setDisplayText("Data tidak ditemukan!");  // Jika tidak ada data ditemukan
                        }

                        // Jika ada hasil pencarian, update tabel dengan filteredList
                        if (!filteredList.isEmpty()) {
                            view.updateTable(filteredList);  // Memperbarui tabel dengan data yang ditemukan
                        } else {
                            view.setDisplayText("Data tidak ditemukan!");  // Jika tidak ada data ditemukan
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error saat mencari data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Masukkan ID Operasional untuk pencarian!", "Error", JOptionPane.ERROR_MESSAGE);
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
                        rs.getDouble("biaya_Mobil"),
                        rs.getDouble("biaya_Supir"),
                        rs.getDouble("total_Biaya")
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