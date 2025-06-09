package view;

import java.text.ParseException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.Barang;
import controller.BarangController;
import java.awt.event.ActionListener;  // Untuk ActionListener
import java.awt.event.ActionEvent;     // Untuk ActionEvent


public class BarangView extends JFrame {
    private JTextField idField, jenisField, stokField, masukField, keluarField, tanggalField, searchField;
    private JButton tambahButton, editButton, hapusButton, cariButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private BarangController controller;

    public BarangView() {
        setTitle("Manajemen Barang");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari (ID/Jenis):"));
        searchField = new JTextField(20);
        cariButton = new JButton("Cari");
        searchPanel.add(searchField);
        searchPanel.add(cariButton);

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
        inputPanel.add(new JLabel("Tanggal (YYYY-MM-DD):"));
        tanggalField = new JTextField();
        inputPanel.add(tanggalField);

        // Table
        String[] columns = {"ID Barang", "Jenis Barang", "Stok Gudang", "Barang Masuk", "Barang Keluar", "Tanggal"};
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

        // Table row selection listener to populate input fields
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                idField.setText((String) tableModel.getValueAt(row, 0));
                jenisField.setText((String) tableModel.getValueAt(row, 1));
                stokField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                masukField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                keluarField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                tanggalField.setText((String) tableModel.getValueAt(row, 5));
            }
        });
    }

    public Barang getInputData() {
        try {
            // Menggunakan format yang benar
            String tanggalFormatted = controller != null ? controller.formatDate(tanggalField.getText()) : tanggalField.getText();

            return new Barang(
                    idField.getText(),
                    jenisField.getText(),
                    Integer.parseInt(stokField.getText()),
                    Integer.parseInt(masukField.getText()),
                    Integer.parseInt(keluarField.getText()),
                    tanggalFormatted  // Menggunakan tanggal yang telah diformat
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stok, Barang Masuk, dan Barang Keluar harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);  // Menampilkan pesan kesalahan yang lebih jelas
            return null;
        }
    }

    public void setInputData(String idBarang, String jenisBarang, int stokGudang, int barangMasuk, int barangKeluar, String tanggal) {
        idField.setText(idBarang);
        jenisField.setText(jenisBarang);
        stokField.setText(String.valueOf(stokGudang));
        masukField.setText(String.valueOf(barangMasuk));
        keluarField.setText(String.valueOf(barangKeluar));
        tanggalField.setText(tanggal);
    }

    public String getSearchId() {
        return searchField.getText();
    }

    public void setDisplayText(String text) {
        // Clear existing table data
        tableModel.setRowCount(0);
        if (text.equals("Data tidak ditemukan!") || text.equals("Masukkan ID Barang untuk pencarian!")) {
            JOptionPane.showMessageDialog(this, text, "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // Parse text to populate table (assuming text is from cariData)
        String[] lines = text.split("\n");
        for (String line : lines) {
            String[] parts = line.split(", ");
            if (parts.length == 6) {
                tableModel.addRow(new Object[]{
                        parts[0].replace("ID Barang: ", ""),
                        parts[1].replace("Jenis Barang: ", ""),
                        parts[2].replace("Stok Gudang: ", ""),
                        parts[3].replace("Barang Masuk: ", ""),
                        parts[4].replace("Barang Keluar: ", ""),
                        parts[5].replace("Tanggal (YYYY-MM-DD): ", "")
                });
            }
        }
    }

    public void updateTable(java.util.List<Barang> barangList) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);  // Clear existing data

        // Menambahkan data terbaru ke tabel
        for (Barang barang : barangList) {
            tableModel.addRow(new Object[]{
                    barang.getIdBarang(),
                    barang.getJenisBarang(),
                    barang.getStokGudang(),
                    barang.getBarangMasuk(),
                    barang.getBarangKeluar(),
                    barang.getTanggal()
            });
        }
    }

    public JButton getTambahButton() { return tambahButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getHapusButton() { return hapusButton; }
    public JButton getCariButton() { return cariButton; }
    public void setController(BarangController controller) { this.controller = controller; }
}