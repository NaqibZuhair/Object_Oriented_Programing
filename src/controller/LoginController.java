package controller;

import model.Login;
import view.LoginView;
import view.DashboardView;
import javax.swing.*;
import java.awt.event.*;

public class LoginController {
    private Login model;
    private LoginView view;

    public LoginController(Login model, LoginView view) {
        this.model = model;
        this.view = view;

        view.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = view.getUsername();
                String password = view.getPassword();
                
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Username dan password harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (model.validate(username, password)) {
                    view.setVisible(false);
                    new DashboardController().getView().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(view, "Username atau password salah!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}