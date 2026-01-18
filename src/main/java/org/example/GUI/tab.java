package org.example.GUI;

import org.example.DAO.ProductDAO;
import org.example.Model.ProductModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class tab extends JFrame {

    private ProductDAO productDAO;
    private DefaultTableModel productTableModel;
    private JTable productTable;
    private JTextField skuField;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField stockField;
    private int selectedProductId = -1;

    public tab() {
        productDAO = new ProductDAO();

        setTitle("Inventory Invoice Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Product Management", createProductManagementPanel());
        tabbedPane.addTab("Home", createHomePanel());
        tabbedPane.addTab("Profile", createProfilePanel());
        tabbedPane.addTab("Settings", createSettingsPanel());
        tabbedPane.addTab("About", createAboutPanel());

        add(tabbedPane);

        setVisible(true);
    }

    private JPanel createProductManagementPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Product Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("SKU:"), gbc);
        gbc.gridx = 1;
        skuField = new JTextField(20);
        panel.add(skuField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(20);
        panel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        stockField = new JTextField(20);
        panel.add(stockField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        addButton.setPreferredSize(new Dimension(80, 30));
        updateButton.setPreferredSize(new Dimension(80, 30));
        deleteButton.setPreferredSize(new Dimension(80, 30));
        clearButton.setPreferredSize(new Dimension(80, 30));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        String[] columnNames = {"ID", "SKU", "Product Name", "Price", "Stock"};
        productTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(productTableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setPreferredSize(new Dimension(700, 200));

        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(tableScrollPane, gbc);

        addButton.addActionListener(e -> {
            try {
                String sku = skuField.getText().trim();
                String name = nameField.getText().trim();
                String priceText = priceField.getText().trim();
                String stockText = stockField.getText().trim();

                if (sku.isEmpty() || name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields!");
                    return;
                }

                double price = Double.parseDouble(priceText);
                int stock = Integer.parseInt(stockText);

                ProductModel product = new ProductModel(0, sku, name, price, stock);
                productDAO.createProduct(product);

                JOptionPane.showMessageDialog(this, "Product added successfully!");
                loadProducts();
                clearFields();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        updateButton.addActionListener(e -> {
            try {
                if (selectedProductId == -1) {
                    JOptionPane.showMessageDialog(this, "Please select a product to update!");
                    return;
                }

                String sku = skuField.getText().trim();
                String name = nameField.getText().trim();
                String priceText = priceField.getText().trim();
                String stockText = stockField.getText().trim();

                if (sku.isEmpty() || name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields!");
                    return;
                }

                double price = Double.parseDouble(priceText);
                int stock = Integer.parseInt(stockText);

                ProductModel product = new ProductModel(selectedProductId, sku, name, price, stock);
                productDAO.updateProduct(product);

                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                loadProducts();
                clearFields();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this product?");
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        productDAO.deleteProduct(selectedProductId);
                        JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                        loadProducts();
                        clearFields();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a product to delete!");
            }
        });

        clearButton.addActionListener(e -> clearFields());

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedProductId = (int) productTableModel.getValueAt(selectedRow, 0);
                    skuField.setText(productTableModel.getValueAt(selectedRow, 1).toString());
                    nameField.setText(productTableModel.getValueAt(selectedRow, 2).toString());
                    priceField.setText(productTableModel.getValueAt(selectedRow, 3).toString());
                    stockField.setText(productTableModel.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        loadProducts();

        return panel;
    }

    private void loadProducts() {
        productTableModel.setRowCount(0);
        try {
            var products = productDAO.getAllProducts();
            for (ProductModel product : products) {
                productTableModel.addRow(new Object[]{
                        product.id(),
                        product.sku(),
                        product.name(),
                        product.price(),
                        product.stock()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
    }

    private void clearFields() {
        skuField.setText("");
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
        selectedProductId = -1;
        productTable.clearSelection();
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
