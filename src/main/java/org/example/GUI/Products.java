package org.example.GUI;

import org.example.DAO.ProductDAO;
import org.example.Model.ProductModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Products extends JPanel {

    private ProductDAO productDAO;
    private DefaultTableModel productTableModel;
    private JTable productTable;
    private JTextField idField;
    private JTextField skuField;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField stockField;
    private int selectedProductId = -1;

    public Products() {
        productDAO = new ProductDAO();
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Product Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // ID Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(20);
        add(idField, gbc);

        // SKU Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("SKU:"), gbc);
        gbc.gridx = 1;
        skuField = new JTextField(20);
        add(skuField, gbc);

        // Product Name Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);

        // Price Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(20);
        add(priceField, gbc);

        // Stock Field
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        stockField = new JTextField(20);
        add(stockField, gbc);

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
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

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

        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(tableScrollPane, gbc);

        // Button Actions
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        clearButton.addActionListener(e -> clearFields());

        // Table selection listener
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedProductId = (int) productTableModel.getValueAt(selectedRow, 0);
                    idField.setText(String.valueOf(selectedProductId));
                    skuField.setText(productTableModel.getValueAt(selectedRow, 1).toString());
                    nameField.setText(productTableModel.getValueAt(selectedRow, 2).toString());
                    priceField.setText(productTableModel.getValueAt(selectedRow, 3).toString());
                    stockField.setText(productTableModel.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        loadProducts();
    }

    private void addProduct() {
        try {
            String idText = idField.getText().trim();
            String sku = skuField.getText().trim();
            String name = nameField.getText().trim();
            String priceText = priceField.getText().trim();
            String stockText = stockField.getText().trim();

            if (idText.isEmpty() || sku.isEmpty() || name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
                return;
            }

            int id = Integer.parseInt(idText);
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);

            ProductModel product = new ProductModel(id, sku, name, price, stock);
            productDAO.createProduct(product);

            JOptionPane.showMessageDialog(this, "Product added successfully!");
            loadProducts();
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateProduct() {
        try {
            if (selectedProductId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a product to update!");
                return;
            }

            String idText = idField.getText().trim();
            String sku = skuField.getText().trim();
            String name = nameField.getText().trim();
            String priceText = priceField.getText().trim();
            String stockText = stockField.getText().trim();

            if (idText.isEmpty() || sku.isEmpty() || name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
                return;
            }

            int id = Integer.parseInt(idText);
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);

            ProductModel product = new ProductModel(id, sku, name, price, stock);
            productDAO.updateProduct(product);

            JOptionPane.showMessageDialog(this, "Product updated successfully!");
            loadProducts();
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteProduct() {
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
        idField.setText("");
        skuField.setText("");
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
        selectedProductId = -1;
        productTable.clearSelection();
    }
}
