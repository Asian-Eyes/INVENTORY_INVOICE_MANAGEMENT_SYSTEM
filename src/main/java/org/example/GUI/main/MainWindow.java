package org.example.GUI.main;

import javax.swing.*;
import org.example.GUI.panels.AboutPanel;
import org.example.GUI.panels.HomePanel;
import org.example.GUI.panels.InvoiceCartPanel;
import org.example.GUI.panels.ProductsPanel;
import org.example.GUI.panels.ProfilePanel;
import org.example.GUI.panels.SettingsPanel;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Inventory Invoice Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Product Management", new ProductsPanel());
        tabbedPane.addTab("Invoice Cart", new InvoiceCartPanel());
        tabbedPane.addTab("Home", new HomePanel());
        tabbedPane.addTab("Profile", new ProfilePanel());
        tabbedPane.addTab("Settings", new SettingsPanel());
        tabbedPane.addTab("About", new AboutPanel());

        add(tabbedPane);

        setVisible(true);
    }
}
