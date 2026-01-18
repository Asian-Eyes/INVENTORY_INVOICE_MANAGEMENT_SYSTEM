package org.example.DAO;

import org.example.Model.InvoiceItemsModel;
import org.example.Model.ProductModel;
import org.example.util.db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceItemDAO {

    public int createInvoiceItem(InvoiceItemsModel item) throws Exception {
        String sql = "INSERT INTO invoice_items (invoice_id, product_id, quantity, unit_price, line_total) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, item.invoice_id());
            statement.setInt(2, item.product_id());
            statement.setInt(3, item.quantity());
            statement.setDouble(4, item.unit_price());
            statement.setDouble(5, item.line_total());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }

            throw new SQLException("Creating invoice item failed, no ID obtained.");
        }
    }

    public boolean deleteInvoiceItem(int product_id, int invoice_id) throws Exception {
        String sql = "DELETE FROM invoice_items ii WHERE ii.product_id = ? and ii.invoice_id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, product_id);
            statement.setInt(2, invoice_id);
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }
}