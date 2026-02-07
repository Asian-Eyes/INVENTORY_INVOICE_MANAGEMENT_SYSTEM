package org.example.GUI.panels;

import org.example.Model.ProductModel;
import org.example.Model.InvoiceModel;
import org.example.Model.InvoiceItemModel;
import org.example.Service.ProductService;
import org.example.Service.InvoiceService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceCartPanel extends JPanel {
    private JComboBox<ProductModel> productComboBox;
    private JSpinner quantitySpinner;
    private JButton addToCartButton;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel subtotalLabel;
    private JLabel totalLabel;
    private JButton checkoutButton;
    private JButton clearCartButton;
    private JTextField invoiceNoField;

    private List<CartItem> cartItems;
    private ProductService productService;
    private InvoiceService invoiceService;

    public InvoiceCartPanel() {
        cartItems = new ArrayList<>();
        productService = new ProductService();
        invoiceService = new InvoiceService();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initializeComponents();
        layoutComponents();
        loadProducts();
    }

    private void initializeComponents() {
        invoiceNoField = new JTextField("INV-" + System.currentTimeMillis());
        invoiceNoField.setEditable(false);
        invoiceNoField.setFont(new Font("Arial", Font.BOLD, 14));
        invoiceNoField.setPreferredSize(new Dimension(200, 30));

        productComboBox = new JComboBox<>();
        productComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        productComboBox.setPreferredSize(new Dimension(250, 30));
        productComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ProductModel) {
                    ProductModel product = (ProductModel) value;
                    setText(product.getName() + " - $" + String.format("%.2f", product.getPrice()));
                }
                return this;
            }
        });

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setColumns(5);

        addToCartButton = new JButton("Add to Cart");
        addToCartButton.setFont(new Font("Arial", Font.BOLD, 14));
        addToCartButton.setPreferredSize(new Dimension(120, 30));
        addToCartButton.addActionListener(e -> addToCart());

        String[] columnNames = { "Product", "SKU", "Price", "Quantity", "Total" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        cartTable = new JTable(tableModel);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 14));
        cartTable.setRowHeight(35);
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        cartTable.getTableHeader().setBackground(new Color(240, 240, 240));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cartTable.getColumnCount(); i++) {
            cartTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableModel.addTableModelListener(e -> {
            if (e.getColumn() == 3) {
                updateQuantity(e.getFirstRow());
            }
        });

        subtotalLabel = new JLabel("$0.00");
        subtotalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        totalLabel = new JLabel("0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 24));
        totalLabel.setForeground(new Color(0, 120, 215));

        checkoutButton = new JButton("Checkout");
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutButton.setPreferredSize(new Dimension(150, 40));
        checkoutButton.addActionListener(e -> checkout());

        clearCartButton = new JButton("Clear Cart");
        clearCartButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearCartButton.setPreferredSize(new Dimension(120, 40));
        clearCartButton.addActionListener(e -> clearCart());
    }

    private void layoutComponents() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        "Create Invoice",
                        0, 0,
                        new Font("Arial", Font.BOLD, 18)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JPanel invoicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        invoicePanel.setBackground(Color.WHITE);
        invoicePanel.add(new JLabel("Invoice No:"));
        invoicePanel.add(invoiceNoField);
        invoicePanel.add(Box.createHorizontalStrut(20));
        invoicePanel.add(new JLabel("Date: " + LocalDate.now()));

        JPanel productPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        productPanel.setBackground(Color.WHITE);
        productPanel.add(new JLabel("Product:"));
        productPanel.add(productComboBox);
        productPanel.add(Box.createHorizontalStrut(10));
        productPanel.add(new JLabel("Quantity:"));
        productPanel.add(quantitySpinner);
        productPanel.add(Box.createHorizontalStrut(10));
        productPanel.add(addToCartButton);

        topPanel.add(invoicePanel);
        topPanel.add(productPanel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        "Invoice Items",
                        0, 0,
                        new Font("Arial", Font.BOLD, 16)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setPreferredSize(new Dimension(650, 250));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel subtotalPanel = new JPanel(new BorderLayout());
        subtotalPanel.setBackground(Color.WHITE);
        subtotalPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel subtotalTitleLabel = new JLabel("Subtotal");
        subtotalTitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtotalPanel.add(subtotalTitleLabel, BorderLayout.WEST);
        subtotalPanel.add(subtotalLabel, BorderLayout.EAST);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        JLabel totalTitleLabel = new JLabel("Total");
        totalTitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalPanel.add(totalTitleLabel, BorderLayout.WEST);
        totalPanel.add(totalLabel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(clearCartButton);
        buttonPanel.add(checkoutButton);

        bottomPanel.add(subtotalPanel);
        bottomPanel.add(separator);
        bottomPanel.add(totalPanel);
        bottomPanel.add(buttonPanel);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadProducts() {
        try {
            var products = productService.getAllProducts();
            productComboBox.removeAllItems();
            for (ProductModel product : products) {
                productComboBox.addItem(product);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading products: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addToCart() {
        ProductModel selectedProduct = (ProductModel) productComboBox.getSelectedItem();
        int quantity = (Integer) quantitySpinner.getValue();

        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a product",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (quantity > selectedProduct.getStock()) {
            JOptionPane.showMessageDialog(this,
                    "Insufficient stock! Available: " + selectedProduct.getStock(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == selectedProduct.getId()) {
                int newQuantity = item.getQuantity() + quantity;
                if (newQuantity > selectedProduct.getStock()) {
                    JOptionPane.showMessageDialog(this,
                            "Cannot add more! Total would exceed available stock: " + selectedProduct.getStock(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                item.setQuantity(newQuantity);
                found = true;
                break;
            }
        }

        if (!found) {
            cartItems.add(new CartItem(selectedProduct, quantity));
        }

        updateCartTable();
        quantitySpinner.setValue(1);
    }

    private void updateQuantity(int row) {
        try {
            int newQuantity = Integer.parseInt(tableModel.getValueAt(row, 3).toString());
            CartItem item = cartItems.get(row);

            if (newQuantity <= 0) {
                cartItems.remove(row);
                updateCartTable();
            } else if (newQuantity > item.getProduct().getStock()) {
                JOptionPane.showMessageDialog(this,
                        "Quantity exceeds available stock: " + item.getProduct().getStock(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                updateCartTable();
            } else {
                item.setQuantity(newQuantity);
                updateCartTable();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid quantity",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            updateCartTable();
        }
    }

    private void updateCartTable() {
        tableModel.setRowCount(0);

        for (CartItem item : cartItems) {
            Object[] row = {
                    item.getProduct().getName(),
                    item.getProduct().getSku(),
                    String.format("$%.2f", item.getProduct().getPrice()),
                    item.getQuantity(),
                    String.format("$%.2f", item.getTotalPrice())
            };
            tableModel.addRow(row);
        }

        updateTotals();
    }

    private void updateTotals() {
        double subtotal = 0.0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotalPrice();
        }

        subtotalLabel.setText(String.format("$%.2f", subtotal));
        totalLabel.setText(String.format("%.2f", subtotal));
    }

    private void clearCart() {
        if (cartItems.isEmpty()) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Clear all items from cart?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            cartItems.clear();
            updateCartTable();
        }
    }

    private void checkout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Your cart is empty!",
                    "Checkout",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder message = new StringBuilder("Invoice Summary:\n\n");
        for (CartItem item : cartItems) {
            message.append(String.format("%s x%d - $%.2f\n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()));
        }
        message.append(String.format("\nTotal: $%.2f",
                cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum()));

        int result = JOptionPane.showConfirmDialog(this,
                message.toString(),
                "Confirm Checkout",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double total = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();

                InvoiceModel invoice = new InvoiceModel(
                        0,
                        invoiceNoField.getText(),
                        LocalDate.now(),
                        total,
                        null);
                int invoiceId = invoiceService.createInvoice(invoice);

                for (CartItem item : cartItems) {
                    double lineTotal = item.getProduct().getPrice() * item.getQuantity();
                    InvoiceItemModel invoiceItem = new InvoiceItemModel(
                            0,
                            invoice,
                            item.getProduct(),
                            item.getQuantity(),
                            item.getProduct().getPrice(),
                            lineTotal);
                    invoiceService.createInvoiceItem(invoiceItem);

                    productService.deductStock(item.getProduct().getId(), item.getQuantity());
                }

                invoiceService.recalculateInvoiceTotal(invoiceId);

                JOptionPane.showMessageDialog(this,
                        "Invoice created successfully!\nInvoice No: " + invoiceNoField.getText(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                cartItems.clear();
                updateCartTable();
                invoiceNoField.setText("INV-" + System.currentTimeMillis());
                loadProducts();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error creating invoice: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private static class CartItem {
        private ProductModel product;
        private int quantity;

        public CartItem(ProductModel product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public ProductModel getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getTotalPrice() {
            return product.getPrice() * quantity;
        }
    }
}