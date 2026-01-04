package org.example.DAO;

import org.example.Model.InvoicesModel;
import org.example.Model.ProductModel;
import org.example.util.db;
import java.time.LocalDate;
import java.sql.*;

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
}
