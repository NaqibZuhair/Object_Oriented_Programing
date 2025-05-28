package view;

import javax.swing.*;
import java.awt.*;
import model.Transaksi;

public class TransaksiView extends JFrame {
    private JTextField idField, tanggalField, pelangganField, jenisField, jumlahField, statusField;
    private JButton tambahButton, editButton, hapusButton, cariButton;
    private JTextArea displayArea;

    public TransaksiView() {
        setTitle("Manajemen Transaksi");
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        tambahButton = new JButton("Tambah");
        editButton = new JButton("Edit");
        hapusButton = new JButton("Hapus");
        cariButton = new JButton("Cari");
        buttonPanel.add(tambahButton);
        buttonPanel.add(editButton);
        buttonPanel.add(hapusButton);
        buttonPanel.add(cariButton);

        // Display Area
        displayArea = new JTextArea(10, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Center Panel to hold buttons and display
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
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

    public String getSearchId() { return idField.getText(); }
    public void setDisplayText(String text) { displayArea.setText(text); }
    public JButton getTambahButton() { return tambahButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getHapusButton() { return hapusButton; }
    public JButton getCariButton() { return cariButton; }
}