package controller;

import Model.Transaksi;
import view.TransaksiView;
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
        Transaksi transaksi = view.getInputData();  // Mendapatkan data transaksi yang baru dimasukkan
        if (transaksi != null && !transaksi.getIdTransaksi().isEmpty()) {
            transaksiList.add(transaksi);

            try (Connection conn = DataBaseConnector.connect()) {
                // Query untuk menambahkan data transaksi
                String query = "INSERT INTO transaksi (idTransaksi, tanggal, nama_Pelanggan, jenis_Barang, jumlah, status_bayar) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, transaksi.getIdTransaksi());
                    stmt.setString(2, transaksi.getTanggal());
                    stmt.setString(3, transaksi.getNamaPelanggan());
                    stmt.setString(4, transaksi.getJenisBarang());
                    stmt.setInt(5, transaksi.getJumlah());
                    stmt.setString(6, transaksi.getStatusPembayaran());

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        // Menampilkan pesan di notifikasi GUI (dialog box)
                        JOptionPane.showMessageDialog(view, "Data transaksi berhasil ditambahkan ke database.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(view, "Data transaksi gagal ditambahkan.", "Error", JOptionPane.ERROR_MESSAGE);
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
        Transaksi newData = view.getInputData();  // Mendapatkan data yang telah diubah

        if (newData != null) {
            String id = newData.getIdTransaksi();  // Ambil ID yang akan diedit

            try (Connection conn = DataBaseConnector.connect()) {
                String query = "UPDATE transaksi SET tanggal = ?, nama_Pelanggan = ?, jenis_Barang = ?, jumlah = ?, status_bayar = ? WHERE idTransaksi = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    // Set data baru yang diinputkan ke dalam query
                    stmt.setString(1, newData.getTanggal());
                    stmt.setString(2, newData.getNamaPelanggan());
                    stmt.setString(3, newData.getJenisBarang());
                    stmt.setInt(4, newData.getJumlah());
                    stmt.setString(5, newData.getStatusPembayaran());
                    stmt.setString(6, id);  // ID Transaksi untuk menentukan data yang akan diupdate

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
        // Ambil search term dari input dan hilangkan spasi tambahan
        String searchTerm = view.getSearchId().trim();
        System.out.println("Search Term: " + searchTerm); // Debugging output

        if (searchTerm != null && !searchTerm.isEmpty()) {
            try (Connection conn = DataBaseConnector.connect()) {
                // Query untuk mencari berdasarkan ID Transaksi atau Nama Pelanggan
                String query = "SELECT * FROM transaksi WHERE idTransaksi = ? OR nama_Pelanggan LIKE ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, searchTerm);  // Cari berdasarkan ID Transaksi
                    stmt.setString(2, "%" + searchTerm + "%");  // Cari berdasarkan Nama Pelanggan (menggunakan LIKE)

                    try (ResultSet rs = stmt.executeQuery()) {
                        ArrayList<Transaksi> filteredList = new ArrayList<>();  // List untuk hasil pencarian

                        // Proses setiap data yang ditemukan
                        while (rs.next()) {
                            // Menambahkan hasil pencarian ke filteredList
                            filteredList.add(new Transaksi(
                                    rs.getString("idTransaksi"),
                                    rs.getString("tanggal"),
                                    rs.getString("nama_Pelanggan"),
                                    rs.getString("jenis_Barang"),
                                    rs.getInt("jumlah"),
                                    rs.getString("status_Bayar")
                            ));

                            // Mengisi kolom input dengan data yang ditemukan
                            view.setInputData(  // Mengisi kolom input dengan data yang ditemukan
                                    rs.getString("idTransaksi"),
                                    rs.getString("tanggal"),
                                    rs.getString("nama_Pelanggan"),
                                    rs.getString("jenis_Barang"),
                                    rs.getInt("jumlah"),
                                    rs.getString("status_Bayar")
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
            JOptionPane.showMessageDialog(view, "Masukkan ID Transaksi atau Nama Pelanggan untuk pencarian!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void updateDisplay() {
        try (Connection conn = DataBaseConnector.connect()) {
            String query = "SELECT * FROM transaksi";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                ArrayList<Model.Transaksi> dbList = new ArrayList<>();
                while (rs.next()) {
                    dbList.add(new Model.Transaksi(
                            rs.getString("idTransaksi"),
                            rs.getString("Tanggal"),
                            rs.getString("Nama_Pelanggan"),
                            rs.getString("Jenis_Barang"),
                            rs.getInt("Jumlah"),
                            rs.getString("Status_bayar")
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