package controller;

import view.DashboardView;
import view.BarangView;
import view.TransaksiView;
import view.OperasionalView;
import view.LaporanView;
import model.Barang;
import model.Transaksi;
import model.Operasional;
import model.Laporan;
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