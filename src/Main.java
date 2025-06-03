import model.Login;
import view.LoginView;
import controller.LoginController;
import javax.swing.*;

import controller.DataBaseConnector;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login model = new Login("admin", "admin123");
            LoginView view = new LoginView();
            new LoginController(model, view);
            view.setVisible(true);
        });

        // Mencoba untuk menghubungkan ke database
        try (Connection conn = DataBaseConnector.connect()) {
            // Jika koneksi berhasil, tampilkan pesan keberhasilan
            if (conn != null) {
                System.out.println("Koneksi ke database berhasil!");
            }
        } catch (SQLException e) {
            // Menangani kesalahan jika koneksi gagal
            System.out.println("Gagal terhubung ke database.");
            e.printStackTrace();  // Menampilkan rincian error
        }
    }
}