import view.View;
import controller.Controller;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        View view = new View();
        Controller controller = new Controller(view);
        view.controller = controller;
        
        while (true) {
            if (!controller.isLoggedIn()) {
                view.showLoginMenu();
                continue;
            }
            
            view.showMainMenu();
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    handleSubMenu("Barang", view, scanner);
                    break;
                case "2":
                    handleSubMenu("Transaksi", view, scanner);
                    break;
                case "3":
                    handleSubMenu("Operasional", view, scanner);
                    break;
                case "4":
                    handleSubMenu("Laporan", view, scanner);
                    break;
                case "5":
                    System.out.println("Terima kasih telah menggunakan sistem!");
                    System.exit(0);
                default:
                    view.displayMessage("Pilihan tidak valid!");
            }
        }
    }
    
    private static void handleSubMenu(String entity, View view, Scanner scanner) {
        while (true) {
            view.showSubMenu(entity);
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    switch (entity) {
                        case "Barang": view.showBarangForm(); break;
                        case "Transaksi": view.showTransaksiForm(); break;
                        case "Operasional": view.showOperasionalForm(); break;
                        case "Laporan": view.showLaporanForm(); break;
                    }
                    break;
                case "4":
                    view.showSearchForm(entity);
                    break;
                case "5":
                    return;
                default:
                    view.displayMessage("Pilihan tidak valid!");
            }
        }
    }
}