package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.Laporan;

public class LaporanView extends JFrame {
    private JTextField idField, alamatField, deskripsiField, tanggalField, searchField;
    private JButton tambahButton, editButton, hapusButton, cariButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public LaporanView() {
        setTitle("Manajemen Laporan");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari (ID/Alamat):"));
        searchField = new JTextField(20);
        cariButton = new JButton("Cari");
        searchPanel.add(searchField);
        searchPanel.add(cariButton);

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

        // Table
        String[] columns = {"ID Laporan", "Alamat Kirim", "Deskripsi", "Tanggal"};
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
                alamatField.setText((String) tableModel.getValueAt(row, 1));
                deskripsiField.setText((String) tableModel.getValueAt(row, 2));
                tanggalField.setText((String) tableModel.getValueAt(row, 3));
            }
        });
    }

    public Laporan getInputData() {
        return new Laporan(
            idField.getText(),
            alamatField.getText(),
            deskripsiField.getText(),
            tanggalField.getText()
        );
    }

    public String getSearchId() {
        return searchField.getText();
    }

    public void setDisplayText(String text) {
        tableModel.setRowCount(0);
        if (text.equals("Data tidak ditemukan!") || text.equals("Masukkan ID Laporan untuk pencarian!")) {
            JOptionPane.showMessageDialog(this, text, "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] lines = text.split("\n");
        for (String line : lines) {
            String[] parts = line.split(", ");
            if (parts.length == 4) {
                tableModel.addRow(new Object[]{
                        parts[0].replace("ID Laporan: ", ""),
                        parts[1].replace("Alamat Kirim: ", ""),
                        parts[2].replace("Deskripsi: ", ""),
                        parts[3].replace("Tanggal: ", "")
                });
            }
        }
    }

    public void updateTable(java.util.List<Laporan> laporanList) {
        tableModel.setRowCount(0);
        for (Laporan laporan : laporanList) {
            tableModel.addRow(new Object[]{
                    laporan.getIdLaporan(),
                    laporan.getAlamatKirim(),
                    laporan.getDeskripsi(),
                    laporan.getTanggal()
            });
        }
    }

    public JButton getTambahButton() { return tambahButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getHapusButton() { return hapusButton; }
    public JButton getCariButton() { return cariButton; }
}