package controller;

import model.Barang;
import view.BarangView;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;

public class BarangController {
    private ArrayList<Barang> barangList;
    private BarangView view;

    public BarangController(BarangView view, ArrayList<Barang> barangList) {
        this.view = view;
        this.barangList = barangList;

        view.getTambahButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahData();
            }
        });

        view.getEditButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editData();
            }
        });

        view.getHapusButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusData();
            }
        });

        view.getCariButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cariData();
            }
        });
        
        updateDisplay();
    }

    private void tambahData() {
        Barang barang = view.getInputData();
        if (barang != null && !barang.getIdBarang().isEmpty()) {
            barangList.add(barang);
            updateDisplay();
            JOptionPane.showMessageDialog(view, "Data berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Barang tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editData() {
        String id = view.getSearchId();
        Barang newData = view.getInputData();
        if (newData != null && !id.isEmpty()) {
            for (int i = 0; i < barangList.size(); i++) {
                if (barangList.get(i).getIdBarang().equals(id)) {
                    barangList.set(i, newData);
                    updateDisplay();
                    JOptionPane.showMessageDialog(view, "Data berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(view, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Barang tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        String id = view.getSearchId();
        if (!id.isEmpty()) {
            for (int i = 0; i < barangList.size(); i++) {
                if (barangList.get(i).getIdBarang().equals(id)) {
                    barangList.remove(i);
                    updateDisplay();
                    JOptionPane.showMessageDialog(view, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(view, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Barang tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariData() {
        String id = view.getSearchId();
        if (!id.isEmpty()) {
            for (Barang barang : barangList) {
                if (barang.getIdBarang().equals(id)) {
                    view.setDisplayText(
                        "ID Barang: " + barang.getIdBarang() + "\n" +
                        "Jenis Barang: " + barang.getJenisBarang() + "\n" +
                        "Stok Gudang: " + barang.getStokGudang() + "\n" +
                        "Barang Masuk: " + barang.getBarangMasuk() + "\n" +
                        "Barang Keluar: " + barang.getBarangKeluar() + "\n" +
                        "Tanggal: " + barang.getTanggal()
                    );
                    return;
                }
            }
            view.setDisplayText("Data tidak ditemukan!");
        } else {
            view.setDisplayText("Masukkan ID Barang untuk pencarian!");
        }
    }

    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Barang barang : barangList) {
            sb.append("ID: ").append(barang.getIdBarang())
              .append(", Jenis: ").append(barang.getJenisBarang())
              .append(", Stok: ").append(barang.getStokGudang())
              .append(", Masuk: ").append(barang.getBarangMasuk())
              .append(", Keluar: ").append(barang.getBarangKeluar())
              .append(", Tanggal: ").append(barang.getTanggal())
              .append("\n");
        }
        view.setDisplayText(sb.toString());
    }

    public BarangView getView() { return view; }
}