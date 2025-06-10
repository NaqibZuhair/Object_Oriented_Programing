package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import Model.Operasional;
import controller.OperasionalController;

import java.sql.*;

public class OperasionalView extends JFrame {
    private JTextField idField, tanggalField, mobilField, supirField, totalField, searchField;
    private JButton tambahButton, editButton, hapusButton, cariButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private OperasionalController controller;

    public OperasionalView() {
        setTitle("Manajemen Operasional");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari (ID):"));
        searchField = new JTextField(20);
        cariButton = new JButton("Cari");
        searchPanel.add(searchField);
        searchPanel.add(cariButton);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.add(new JLabel("ID Transaksi:"));
        idField = new JTextField();
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Tanggal (YYYY-MM-DD):"));
        tanggalField = new JTextField();
        inputPanel.add(tanggalField);
        inputPanel.add(new JLabel("Biaya Mobil:"));
        mobilField = new JTextField();
        inputPanel.add(mobilField);
        inputPanel.add(new JLabel("Biaya Supir:"));
        supirField = new JTextField();
        inputPanel.add(supirField);
        inputPanel.add(new JLabel("Total Biaya:"));
        totalField = new JTextField();
        totalField.setEditable(false);  // Tidak bisa diubah
        inputPanel.add(totalField);  // Menambahkan ke panel input

        // Table
        String[] columns = {"ID Transaksi", "Tanggal", "Biaya Mobil", "Biaya Supir", "Total Biaya"};
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
                mobilField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                supirField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                totalField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
            }
        });
    }

    public void setInputData(String idTransaksi, String tanggal, double biayaMobil, double biayaSupir, double totalBiaya) {
        idField.setText(idTransaksi);        // Set ID Transaksi
        tanggalField.setText(tanggal);       // Set Tanggal
        mobilField.setText(String.valueOf(biayaMobil));  // Set Biaya Mobil
        supirField.setText(String.valueOf(biayaSupir));  // Set Biaya Supir
        totalField.setText(String.valueOf(totalBiaya));  // Set Total Biaya
    }



    public Operasional getInputData() {
        try {
            return new Operasional(
                    idField.getText(),
                    tanggalField.getText(),
                    Double.parseDouble(mobilField.getText()),
                    Double.parseDouble(supirField.getText()),
                    0 // Total Biaya dihitung otomatis
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Biaya harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
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
            if (parts.length == 5) {
                tableModel.addRow(new Object[]{
                        parts[0].replace("ID Transaksi: ", ""),
                        parts[1].replace("Tanggal: ", ""),
                        parts[2].replace("Biaya Mobil: ", ""),
                        parts[3].replace("Biaya Supir: ", ""),
                        parts[4].replace("Total Biaya: ", "")
                });
            }
        }
    }

    public void updateTable(java.util.List<Operasional> operasionalList) {
        tableModel.setRowCount(0);
        for (Operasional operasional : operasionalList) {
            tableModel.addRow(new Object[]{
                    operasional.getIdTransaksi(),
                    operasional.getTanggal(),
                    operasional.getBiayaMobil(),
                    operasional.getBiayaSupir(),
                    operasional.getTotalBiaya()
            });
        }
    }

    public JButton getTambahButton() { return tambahButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getHapusButton() { return hapusButton; }
    public JButton getCariButton() { return cariButton; }
}