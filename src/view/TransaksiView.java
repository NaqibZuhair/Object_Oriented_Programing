package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.Transaksi;

public class TransaksiView extends JFrame {
    private JTextField idField, tanggalField, pelangganField, jenisField, jumlahField, statusField, searchField;
    private JButton tambahButton, editButton, hapusButton, cariButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public TransaksiView() {
        setTitle("Manajemen Transaksi");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari (ID/Nama Pelanggan):"));
        searchField = new JTextField(20);
        cariButton = new JButton("Cari");
        searchPanel.add(searchField);
        searchPanel.add(cariButton);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.add(new JLabel("ID Transaksi:"));
        idField = new JTextField();
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Tanggal:"));
        tanggalField = new JTextField();
        inputPanel.add(tanggalField);
        inputPanel.add(new JLabel("Nama Pelanggan:"));
        pelangganField = new JTextField();
        inputPanel.add(pelangganField);
        inputPanel.add(new JLabel("Jenis Barang:"));
        jenisField = new JTextField();
        inputPanel.add(jenisField);
        inputPanel.add(new JLabel("Jumlah:"));
        jumlahField = new JTextField();
        inputPanel.add(jumlahField);
        inputPanel.add(new JLabel("Status Pembayaran:"));
        statusField = new JTextField();
        inputPanel.add(statusField);

        // Table
        String[] columns = {"ID Transaksi", "Tanggal", "Nama Pelanggan", "Jenis Barang", "Jumlah", "Status Pembayaran"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tambahButton = new JButton("Tambah");
        editButton = new JButton("Edit");
        hapusButton = new JButton("Hapus");
        buttonPanel.add(tambahButton);
        buttonPanel.add(editButton);
        buttonPanel.add(hapusButton);

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Table row selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                idField.setText((String) tableModel.getValueAt(row, 0));
                tanggalField.setText((String) tableModel.getValueAt(row, 1));
                pelangganField.setText((String) tableModel.getValueAt(row, 2));
                jenisField.setText((String) tableModel.getValueAt(row, 3));
                jumlahField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                statusField.setText((String) tableModel.getValueAt(row, 5));
            }
        });
    }

    public Transaksi getInputData() {
        try {
            return new Transaksi(
                idField.getText(),
                tanggalField.getText(),
                pelangganField.getText(),
                jenisField.getText(),
                Integer.parseInt(jumlahField.getText()),
                statusField.getText()
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public String getSearchId() {
        return searchField.getText();
    }

    public void setDisplayText(String text) {
        tableModel.setRowCount(0);
        if (text.equals("Data tidak ditemukan!") || text.equals("Masukkan ID Transaksi untuk pencarian!")) {
            JOptionPane.showMessageDialog(this, text, "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] lines = text.split("\n");
        for (String line : lines) {
            String[] parts = line.split(", ");
            if (parts.length == 6) {
                tableModel.addRow(new Object[]{
                        parts[0].replace("ID Transaksi: ", ""),
                        parts[1].replace("Tanggal: ", ""),
                        parts[2].replace("Nama Pelanggan: ", ""),
                        parts[3].replace("Jenis Barang: ", ""),
                        parts[4].replace("Jumlah: ", ""),
                        parts[5].replace("Status Pembayaran: ", "")
                });
            }
        }
    }

    public void updateTable(java.util.List<Transaksi> transaksiList) {
        tableModel.setRowCount(0);
        for (Transaksi transaksi : transaksiList) {
            tableModel.addRow(new Object[]{
                    transaksi.getIdTransaksi(),
                    transaksi.getTanggal(),
                    transaksi.getNamaPelanggan(),
                    transaksi.getJenisBarang(),
                    transaksi.getJumlah(),
                    transaksi.getStatusPembayaran()
            });
        }
    }

    public JButton getTambahButton() { return tambahButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getHapusButton() { return hapusButton; }
    public JButton getCariButton() { return cariButton; }
}