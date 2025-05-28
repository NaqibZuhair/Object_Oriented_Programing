package controller;

import model.Operasional;
import view.OperasionalView;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;

public class OperasionalController {
    private ArrayList<Operasional> operasionalList;
    private OperasionalView view;

    public OperasionalController(OperasionalView view, ArrayList<Operasional> operasionalList) {
        this.view = view;
        this.operasionalList = operasionalList;

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
        Operasional operasional = view.getInputData();
        if (operasional != null && !operasional.getIdTransaksi().isEmpty()) {
            operasionalList.add(operasional);
            updateDisplay();
            JOptionPane.showMessageDialog(view, "Data berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, "ID Transaksi tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editData() {
        String id = view.getSearchId();
        Operasional newData = view.getInputData();
        if (newData != null && !id.isEmpty()) {
            for (int i = 0; i < operasionalList.size(); i++) {
                if (operasionalList.get(i).getIdTransaksi().equals(id)) {
                    operasionalList.set(i, newData);
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
            for (int i = 0; i < operasionalList.size(); i++) {
                if (operasionalList.get(i).getIdTransaksi().equals(id)) {
                    operasionalList.remove(i);
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
            for (Operasional operasional : operasionalList) {
                if (operasional.getIdTransaksi().equals(id)) {
                    view.setDisplayText(
                        "ID Transaksi: " + operasional.getIdTransaksi() + "\n" +
                        "Tanggal: " + operasional.getTanggal() + "\n" +
                        "Biaya Mobil: " + operasional.getBiayaMobil() + "\n" +
                        "Biaya Supir: " + operasional.getBiayaSupir() + "\n" +
                        "Total Biaya: " + operasional.getTotalBiaya()
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
        for (Operasional operasional : operasionalList) {
            sb.append("ID: ").append(operasional.getIdTransaksi())
              .append(", Tanggal: ").append(operasional.getTanggal())
              .append(", Biaya Mobil: ").append(operasional.getBiayaMobil())
              .append(", Biaya Supir: ").append(operasional.getBiayaSupir())
              .append(", Total: ").append(operasional.getTotalBiaya())
              .append("\n");
        }
        view.setDisplayText(sb.toString());
    }

    public OperasionalView getView() { return view; }
}