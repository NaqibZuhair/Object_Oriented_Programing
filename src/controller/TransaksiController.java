package controller;

import model.Transaksi;
import view.TransaksiView;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;

public class TransaksiController {
    private ArrayList<Transaksi> transaksiList;
    private TransaksiView view;

    public TransaksiController(TransaksiView view, ArrayList<Transaksi> transaksiList) {
        this.view = view;
        this.transaksiList = transaksiList;

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
        Transaksi transaksi = view.getInputData();
        if (transaksi != null && !transaksi.getIdTransaksi().isEmpty()) {
            transaksiList.add(transaksi);
            updateDisplay();
            JOptionPane.showMessageDialog(view, "Data berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Transaksi tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editData() {
        String id = view.getSearchId();
        Transaksi newData = view.getInputData();
        if (newData != null && !id.isEmpty()) {
            for (int i = 0; i < transaksiList.size(); i++) {
                if (transaksiList.get(i).getIdTransaksi().equals(id)) {
                    transaksiList.set(i, newData);
                    updateDisplay();
                    JOptionPane.showMessageDialog(view, "Data berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(view, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Transaksi tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        String id = view.getSearchId();
        if (!id.isEmpty()) {
            for (int i = 0; i < transaksiList.size(); i++) {
                if (transaksiList.get(i).getIdTransaksi().equals(id)) {
                    transaksiList.remove(i);
                    updateDisplay();
                    JOptionPane.showMessageDialog(view, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(view, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Transaksi tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariData() {
        String id = view.getSearchId();
        if (!id.isEmpty()) {
            for (Transaksi transaksi : transaksiList) {
                if (transaksi.getIdTransaksi().equals(id)) {
                    view.setDisplayText(
                        "ID Transaksi: " + transaksi.getIdTransaksi() + "\n" +
                        "Tanggal: " + transaksi.getTanggal() + "\n" +
                        "Nama Pelanggan: " + transaksi.getNamaPelanggan() + "\n" +
                        "Jenis Barang: " + transaksi.getJenisBarang() + "\n" +
                        "Jumlah: " + transaksi.getJumlah() + "\n" +
                        "Status Pembayaran: " + transaksi.getStatusPembayaran()
                    );
                    return;
                }
            }
            view.setDisplayText("Data tidak ditemukan!");
        } else {
            view.setDisplayText("Masukkan ID Transaksi untuk pencarian!");
        }
    }

    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Transaksi transaksi : transaksiList) {
            sb.append("ID: ").append(transaksi.getIdTransaksi())
              .append(", Tanggal: ").append(transaksi.getTanggal())
              .append(", Pelanggan: ").append(transaksi.getNamaPelanggan())
              .append(", Jenis: ").append(transaksi.getJenisBarang())
              .append(", Jumlah: ").append(transaksi.getJumlah())
              .append(", Status: ").append(transaksi.getStatusPembayaran())
              .append("\n");
        }
        view.setDisplayText(sb.toString());
    }

    public TransaksiView getView() { return view; }
}