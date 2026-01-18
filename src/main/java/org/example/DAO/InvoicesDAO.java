package org.example.DAO;

import org.example.Model.InvoicesModel;
import org.example.Model.ProductModel;
import org.example.util.db;
import java.time.LocalDate;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

public class InvoicesDAO {
    public int createInvoice(InvoicesModel invoice) throws Exception {
        String sql = "INSERT INTO invoice (id, invoice_no, created_at, total) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, invoice.id());
            statement.setString(2, invoice.invoice_no());
            statement.setDate(3, Date.valueOf(invoice.created_at()));
            statement.setDouble(4, invoice.total());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }

            throw new SQLException("Creating invoice failed, no ID obtained.");
        }
    }

    public boolean updateInvoiceTotal(int invoiceId) throws Exception {
        String sql = """
        UPDATE invoice SET total = (SELECT COALESCE(SUM(line_total), 0) FROM invoice_items WHERE invoice_id = ?) WHERE id = ?""";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, invoiceId);
            statement.setInt(2, invoiceId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<ProductModel> findListProductsByInvoiceId(int invoiceId) throws Exception {
        String sql = "SELECT p.id, p.sku, p.name, p.price, p.stock " +
                "FROM product p " +
                "JOIN invoice_items ii ON p.id = ii.product_id " +
                "WHERE ii.invoice_id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, invoiceId);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<ProductModel> products = new java.util.ArrayList<>();

                while (resultSet.next()) {
                    ProductModel product = new ProductModel(
                            resultSet.getInt("id"),
                            resultSet.getString("sku"),
                            resultSet.getString("name"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("stock")
                    );
                    products.add(product);
                }

                return products;
            }
        }
    }

    public List<InvoicesModel> getAllInvoices() throws Exception {
        String sql = "SELECT * FROM invoice ORDER BY created_at DESC";
        List<InvoicesModel> invoices = new ArrayList<>();

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                invoices.add(new InvoicesModel(
                        rs.getInt("id"),
                        rs.getString("invoice_no"),
                        rs.getDate("created_at").toLocalDate(),
                        rs.getDouble("total")
                ));
            }
        }
        return invoices;
    }

    public InvoicesModel getInvoiceById(int invoiceId) throws Exception {
        String sql = "SELECT id, invoice_no, created_at, total FROM invoice WHERE id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, invoiceId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new InvoicesModel(
                            rs.getInt("id"),
                            rs.getString("invoice_no"),
                            rs.getDate("created_at").toLocalDate(),
                            rs.getDouble("total")
                    );
                } else {
                    throw new SQLException("Invoice not found with ID: " + invoiceId);
                }
            }
        }
    }

}