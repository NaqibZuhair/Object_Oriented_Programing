package view;

import javax.swing.*;
import java.awt.*;
import model.Laporan;

public class LaporanView extends JFrame {
    private JTextField idField, alamatField, deskripsiField, tanggalField;
    private JButton tambahButton, editButton, hapusButton, cariButton;
    private JTextArea displayArea;

    public LaporanView() {
        setTitle("Manajemen Laporan");
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.add(new JLabel("ID Laporan:"));
        idField = new JTextField();
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Alamat Kirim:"));
        alamatField = new JTextField();
        inputPanel.add(alamatField);
        inputPanel.add(new JLabel("Deskripsi:"));
        deskripsiField = new JTextField();
        inputPanel.add(deskripsiField);
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

    public Laporan getInputData() {
        return new Laporan(
            idField.getText(),
            alamatField.getText(),
            deskripsiField.getText(),
            tanggalField.getText()
        );
    }

    public String getSearchId() { return idField.getText(); }
    public void setDisplayText(String text) { displayArea.setText(text); }
    public JButton getTambahButton() { return tambahButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getHapusButton() { return hapusButton; }
    public JButton getCariButton() { return cariButton; }
}