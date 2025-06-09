package controller;

import java.sql.*;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;

public class DataBaseConnector {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/pbo_inventori_inhuntani";
    private static final String USER = "root";
    private static final String PASSWORD = "Naqibhudri11";
    public static Connection connect() throws SQLException {
        try {
            // Memuat driver MySQL secara eksplisit (untuk kompatibilitas lebih baik)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL tidak ditemukan.");
            e.printStackTrace();  // Menampilkan pesan error jika driver tidak ditemukan
            throw new SQLException("Driver MySQL tidak ditemukan", e);  // Mengubahnya menjadi SQLException
        }

        // Jika driver berhasil dimuat, lakukan koneksi
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
