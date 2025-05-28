package controller;

import model.Laporan;
import view.LaporanView;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;

public class LaporanController {
    private ArrayList<Laporan> laporanList;
    private LaporanView view;

    public LaporanController(LaporanView view, ArrayList<Laporan> laporanList) {
        this.view = view;
        this.laporanList = laporanList;

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
        Laporan laporan = view.getInputData();
        if (laporan != null && !laporan.getIdLaporan().isEmpty()) {
            laporanList.add(laporan);
            updateDisplay();
            JOptionPane.showMessageDialog(view, "Data berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Laporan tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editData() {
        String id = view.getSearchId();
        Laporan newData = view.getInputData();
        if (newData != null && !id.isEmpty()) {
            for (int i = 0; i < laporanList.size(); i++) {
                if (laporanList.get(i).getIdLaporan().equals(id)) {
                    laporanList.set(i, newData);
                    updateDisplay();
                    JOptionPane.showMessageDialog(view, "Data berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(view, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Laporan tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        String id = view.getSearchId();
        if (!id.isEmpty()) {
            for (int i = 0; i < laporanList.size(); i++) {
                if (laporanList.get(i).getIdLaporan().equals(id)) {
                    laporanList.remove(i);
                    updateDisplay();
                    JOptionPane.showMessageDialog(view, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(view, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Laporan tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariData() {
        String id = view.getSearchId();
        if (!id.isEmpty()) {
            for (Laporan laporan : laporanList) {
                if (laporan.getIdLaporan().equals(id)) {
                    view.setDisplayText(
                        "ID Laporan: " + laporan.getIdLaporan() + "\n" +
                        "Alamat Kirim: " + laporan.getAlamatKirim() + "\n" +
                        "Deskripsi: " + laporan.getDeskripsi() + "\n" +
                        "Tanggal: " + laporan.getTanggal()
                    );
                    return;
                }
            }
            view.setDisplayText("Data tidak ditemukan!");
        } else {
            view.setDisplayText("Masukkan ID Laporan untuk pencarian!");
        }
    }

    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Laporan laporan : laporanList) {
            sb.append("ID: ").append(laporan.getIdLaporan())
              .append(", Alamat: ").append(laporan.getAlamatKirim())
              .append(", Deskripsi: ").append(laporan.getDeskripsi())
              .append(", Tanggal: ").append(laporan.getTanggal())
              .append("\n");
        }
        view.setDisplayText(sb.toString());
    }

    public LaporanView getView() { return view; }
}