package controller;

import view.DashboardView;
import view.BarangView;
import view.TransaksiView;
import view.OperasionalView;
import view.LaporanView;
import model.Barang;
import Model.Transaksi;
import Model.Operasional;
import Model.Laporan;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.*;

public class DashboardController {
    private DashboardView view;
    private ArrayList<Barang> barangList;
    private ArrayList<Transaksi> transaksiList;
    private ArrayList<Operasional> operasionalList;
    private ArrayList<Laporan> laporanList;

    public DashboardController() {
        this.view = new DashboardView();
        this.barangList = new ArrayList<>();
        this.transaksiList = new ArrayList<>();
        this.operasionalList = new ArrayList<>();
        this.laporanList = new ArrayList<>();

        updateSummary();

        view.getBarangButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BarangController controller = new BarangController(new BarangView(), barangList);
                controller.getView().setVisible(true);
                updateSummary();
            }
        });

        view.getTransaksiButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransaksiController controller = new TransaksiController(new TransaksiView(), transaksiList);
                controller.getView().setVisible(true);
                updateSummary();
            }
        });

        view.getOperasionalButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OperasionalController controller = new OperasionalController(new OperasionalView(), operasionalList);
                controller.getView().setVisible(true);
                updateSummary();
            }
        });

        view.getLaporanButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LaporanController controller = new LaporanController(new LaporanView(), laporanList);
                controller.getView().setVisible(true);
                updateSummary();
            }
        });
    }

    private void updateSummary() {
        // Mengambil data barang dari database
        loadDataFromDatabase();

        String summary = String.format(
                "Ringkasan:\n- Barang: %d item\n- Transaksi: %d transaksi\n- Operasional: %d transaksi\n- Laporan: %d laporan",
                barangList.size(), transaksiList.size(), operasionalList.size(), laporanList.size()
        );
        view.setSummaryText(summary);
    }

    private void loadDataFromDatabase() {
        try (Connection conn = DataBaseConnector.connect()) {
            String query = "SELECT * FROM barang";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                barangList.clear();  // Pastikan barangList kosong sebelum mengisi data baru
                while (rs.next()) {
                    barangList.add(new Barang(
                            rs.getString("idBarang"),
                            rs.getString("jenisBarang"),
                            rs.getInt("stokGudang"),
                            rs.getInt("barangMasuk"),
                            rs.getInt("barangKeluar"),
                            rs.getString("tanggal")
                    ));
                }
            }

            // Query untuk mengambil data transaksi
            String queryTransaksi = "SELECT * FROM transaksi";
            try (PreparedStatement stmtTransaksi = conn.prepareStatement(queryTransaksi);
                 ResultSet rsTransaksi = stmtTransaksi.executeQuery()) {
                transaksiList.clear();  // Pastikan transaksiList kosong sebelum mengisi data baru
                while (rsTransaksi.next()) {
                    transaksiList.add(new Transaksi(
                            rsTransaksi.getString("idTransaksi"),
                            rsTransaksi.getString("tanggal"),
                            rsTransaksi.getString("nama_Pelanggan"),
                            rsTransaksi.getString("jenis_Barang"),
                            rsTransaksi.getInt("jumlah"),
                            rsTransaksi.getString("status_Bayar")
                    ));
                }
            }

            // Query untuk mengambil data operasional
            String queryOperasional = "SELECT * FROM operasional";
            try (PreparedStatement stmtOperasional = conn.prepareStatement(queryOperasional);
                 ResultSet rsOperasional = stmtOperasional.executeQuery()) {
                operasionalList.clear();  // Pastikan operasionalList kosong sebelum mengisi data baru
                while (rsOperasional.next()) {
                    operasionalList.add(new Operasional(
                            rsOperasional.getString("idTransaksi"),
                            rsOperasional.getString("tanggal"),
                            rsOperasional.getDouble("biaya_Mobil"),
                            rsOperasional.getDouble("biaya_Supir"),
                            rsOperasional.getDouble("total_Biaya")
                    ));
                }
            }

            // Query untuk mengambil data laporan
            String queryLaporan = "SELECT * FROM laporan";
            try (PreparedStatement stmtLaporan = conn.prepareStatement(queryLaporan);
                 ResultSet rsLaporan = stmtLaporan.executeQuery()) {
                laporanList.clear();  // Pastikan laporanList kosong sebelum mengisi data baru
                while (rsLaporan.next()) {
                    laporanList.add(new Laporan(
                            rsLaporan.getString("idLaporan"),       // ID Laporan
                            rsLaporan.getString("Alamat_Kirim"),    // Alamat Kirim
                            rsLaporan.getString("Deskripsi"),       // Deskripsi
                            rsLaporan.getString("Tanggal")          // Tanggal
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error fetching data from database!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public DashboardView getView() { return view; }
    public ArrayList<Barang> getBarangList() { return barangList; }
    public ArrayList<Transaksi> getTransaksiList() { return transaksiList; }
    public ArrayList<Operasional> getOperasionalList() { return operasionalList; }
    public ArrayList<Laporan> getLaporanList() { return laporanList; }
}