package view;

import javax.swing.*;
import java.awt.*;
import model.Barang;

public class BarangView extends JFrame {
    private JTextField idField, jenisField, stokField, masukField, keluarField, tanggalField;
    private JButton tambahButton, editButton, hapusButton, cariButton;
    private JTextArea displayArea;

    public BarangView() {
        setTitle("Manajemen Barang");
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.add(new JLabel("ID Barang:"));
        idField = new JTextField();
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Jenis Barang:"));
        jenisField = new JTextField();
        inputPanel.add(jenisField);
        inputPanel.add(new JLabel("Stok Gudang:"));
        stokField = new JTextField();
        inputPanel.add(stokField);
        inputPanel.add(new JLabel("Barang Masuk:"));
        masukField = new JTextField();
        inputPanel.add(masukField);
        inputPanel.add(new JLabel("Barang Keluar:"));
        keluarField = new JTextField();
        inputPanel.add(keluarField);
        inputPanel.add(new JLabel("Tanggal:"));
        tanggalField = new JTextField();
        inputPanel.add(tanggalField);

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

    public Barang getInputData() {
        try {
            return new Barang(
                idField.getText(),
                jenisField.getText(),
                Integer.parseInt(stokField.getText()),
                Integer.parseInt(masukField.getText()),
                Integer.parseInt(keluarField.getText()),
                tanggalField.getText()
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stok, Barang Masuk, dan Barang Keluar harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
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