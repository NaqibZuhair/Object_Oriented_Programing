package view;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JFrame {
    private JButton barangButton, transaksiButton, operasionalButton, laporanButton;
    private JTextArea summaryArea;

    public DashboardView() {
        setTitle("Dashboard - Sistem Manajemen Inventori");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Navigation Panel
        JPanel navPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        barangButton = new JButton("Barang");
        transaksiButton = new JButton("Transaksi");
        operasionalButton = new JButton("Operasional");
        laporanButton = new JButton("Laporan");
        navPanel.add(barangButton);
        navPanel.add(transaksiButton);
        navPanel.add(operasionalButton);
        navPanel.add(laporanButton);

        // Summary Area
        summaryArea = new JTextArea(10, 40);
        summaryArea.setEditable(false);
        summaryArea.setText("Ringkasan:\n- Barang: 0 item\n- Transaksi: 0 transaksi\n- Operasional: 0 transaksi\n- Laporan: 0 laporan");

        mainPanel.add(new JLabel("Dashboard", SwingConstants.CENTER), BorderLayout.NORTH);
        mainPanel.add(navPanel, BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(summaryArea), BorderLayout.SOUTH);

        add(mainPanel);
    }

    public JButton getBarangButton() { return barangButton; }
    public JButton getTransaksiButton() { return transaksiButton; }
    public JButton getOperasionalButton() { return operasionalButton; }
    public JButton getLaporanButton() { return laporanButton; }
    public void setSummaryText(String text) { summaryArea.setText(text); }
}