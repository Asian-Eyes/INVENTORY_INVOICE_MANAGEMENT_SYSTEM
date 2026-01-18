package org.example.GUI;

import org.example.GUI.InvoiceCartGUI;
import javax.swing.*;
import java.awt.*;

public class tab extends JFrame {

    public tab() {
        setTitle("Inventory Invoice Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Use the separate products panel
        tabbedPane.addTab("Product Management", new Products());
        tabbedPane.addTab("Invoice Cart", new InvoiceCartGUI());
        tabbedPane.addTab("Home", createHomePanel());
        tabbedPane.addTab("Profile", createProfilePanel());
        tabbedPane.addTab("Settings", createSettingsPanel());
        tabbedPane.addTab("About", createAboutPanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Welcome to the Home Tab", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea textArea = new JTextArea();
        textArea.setText("This is the home panel.\n\nYou can add any content here such as:\n" +
                "- Dashboard widgets\n" +
                "- Recent activities\n" +
                "- Quick actions\n" +
                "- Statistics and charts");
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setMargin(new Insets(10, 10, 10, 10));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("User Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(new JTextField(20), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(new JTextField(20), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(new JTextField(20), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Save Profile");
        saveButton.setPreferredSize(new Dimension(150, 30));
        saveButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Profile saved!"));
        panel.add(saveButton, gbc);

        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(new Color(245, 245, 245));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JCheckBox darkModeCheckBox = new JCheckBox("Enable Dark Mode");
        JCheckBox notificationsCheckBox = new JCheckBox("Enable Notifications");
        JCheckBox autoSaveCheckBox = new JCheckBox("Enable Auto-Save");

        darkModeCheckBox.setBackground(new Color(245, 245, 245));
        notificationsCheckBox.setBackground(new Color(245, 245, 245));
        autoSaveCheckBox.setBackground(new Color(245, 245, 245));

        optionsPanel.add(darkModeCheckBox);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(notificationsCheckBox);
        optionsPanel.add(Box.createVerticalStrut(10));
        optionsPanel.add(autoSaveCheckBox);
        optionsPanel.add(Box.createVerticalStrut(20));

        JButton applyButton = new JButton("Apply Settings");
        applyButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Settings applied!"));
        optionsPanel.add(applyButton);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(optionsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAboutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 250, 240));

        JLabel titleLabel = new JLabel("About This Application", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea aboutText = new JTextArea();
        aboutText.setText("\n\nInventory Invoice Management System\n\n" +
                "Version: 1.0.0\n\n" +
                "This application manages inventory and invoices.\n\n" +
                "Features:\n" +
                "• Product Management\n" +
                "• Invoice Creation\n" +
                "• Invoice History\n" +
                "• Stock Management\n\n" +
                "Created with Java Swing.\n\n" +
                "© 2026 - All rights reserved");
        aboutText.setEditable(false);
        aboutText.setFont(new Font("Arial", Font.PLAIN, 14));
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutText.setMargin(new Insets(10, 30, 10, 30));
        aboutText.setBackground(new Color(255, 250, 240));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(aboutText, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new tab());
    }
}