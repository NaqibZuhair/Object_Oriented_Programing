package controller;

import Model.Laporan;
import view.LaporanView;
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
        Laporan laporan = view.getInputData();  // Mendapatkan data laporan yang baru dimasukkan
        if (laporan != null && !laporan.getIdLaporan().isEmpty()) {
            laporanList.add(laporan);

            try (Connection conn = DataBaseConnector.connect()) {
                // Query untuk menambahkan data laporan
                String query = "INSERT INTO laporan (idLaporan, Alamat_Kirim, Deskripsi, Tanggal) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, laporan.getIdLaporan());
                    stmt.setString(2, laporan.getAlamatKirim());
                    stmt.setString(3, laporan.getDeskripsi());
                    stmt.setString(4, laporan.getTanggal());

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        // Menampilkan pesan di notifikasi GUI (dialog box)
                        JOptionPane.showMessageDialog(view, "Data laporan berhasil ditambahkan ke database.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(view, "Data laporan gagal ditambahkan.", "Error", JOptionPane.ERROR_MESSAGE);
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
        // Ambil data yang sudah diedit di kolom input
        Laporan newData = view.getInputData();  // Mendapatkan data yang telah diubah

        if (newData != null) {
            String id = newData.getIdLaporan();  // Ambil ID yang akan diedit

            try (Connection conn = DataBaseConnector.connect()) {
                // Query untuk mengupdate data Laporan
                String query = "UPDATE laporan SET Alamat_Kirim = ?, Deskripsi = ?, Tanggal = ? WHERE idLaporan = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    // Set data baru yang diinputkan ke dalam query
                    stmt.setString(1, newData.getAlamatKirim());
                    stmt.setString(2, newData.getDeskripsi());
                    stmt.setString(3, newData.getTanggal());
                    stmt.setString(4, id);  // ID Laporan untuk menentukan data yang akan diupdate

                    int rowsAffected = stmt.executeUpdate(); // Eksekusi query UPDATE

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(view, "Data laporan berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                        // Setelah data berhasil diubah, perbarui tampilan tabel
                        updateDisplay();  // Memperbarui tabel dengan data terbaru
                    } else {
                        JOptionPane.showMessageDialog(view, "Gagal mengubah data laporan!", "Error", JOptionPane.ERROR_MESSAGE);
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
        // Ambil search term dari input dan hilangkan spasi tambahan
        String searchTerm = view.getSearchId().trim();
        System.out.println("Search Term: " + searchTerm); // Debugging output

        if (searchTerm != null && !searchTerm.isEmpty()) {
            try (Connection conn = DataBaseConnector.connect()) {
                // Query untuk mencari berdasarkan ID Laporan atau Nama
                String query = "SELECT * FROM laporan WHERE idLaporan = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, searchTerm);  // Cari berdasarkan ID Laporan

                    try (ResultSet rs = stmt.executeQuery()) {
                        ArrayList<Laporan> filteredList = new ArrayList<>();  // List untuk hasil pencarian

                        // Proses setiap data yang ditemukan
                        while (rs.next()) {
                            // Menambahkan hasil pencarian ke filteredList
                            filteredList.add(new Laporan(
                                    rs.getString("idLaporan"),
                                    rs.getString("Alamat_Kirim"),
                                    rs.getString("Deskripsi"),
                                    rs.getString("Tanggal")
                            ));

                            // Mengisi kolom input dengan data yang ditemukan
                            view.setInputData(  // Mengisi kolom input dengan data yang ditemukan
                                    rs.getString("idLaporan"),
                                    rs.getString("Alamat_Kirim"),
                                    rs.getString("Deskripsi"),
                                    rs.getString("Tanggal")
                            );
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
            JOptionPane.showMessageDialog(view, "Masukkan ID Laporan atau Nama untuk pencarian!", "Error", JOptionPane.ERROR_MESSAGE);
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
                        rs.getString("alamat_Kirim"),
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