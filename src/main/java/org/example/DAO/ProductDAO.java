package org.example.DAO;

import org.example.Model.ProductModel;
import org.example.util.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public int createProduct(ProductModel product) throws Exception {
        String sql = """
                INSERT INTO Products (sku, name, price, stock)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, product.sku());
            statement.setString(2, product.name());
            statement.setDouble(3, product.price());
            statement.setInt(4, product.stock());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new SQLException("Failed to create product, no ID returned.");
        }
    }

    public boolean updateProduct(ProductModel product) throws Exception {
        String sql = """
                UPDATE Products
                SET sku = ?, name = ?, price = ?, stock = ?
                WHERE id = ?
                """;

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.sku());
            statement.setString(2, product.name());
            statement.setDouble(3, product.price());
            statement.setInt(4, product.stock());
            statement.setInt(5, product.id());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteProduct(int productId) throws Exception {
        String sql = "DELETE FROM Products WHERE id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
            return statement.executeUpdate() > 0;
        }
    }

    public List<ProductModel> getAllProducts() throws Exception {
        String sql = """
                SELECT id, sku, name, price, stock
                FROM Products
                ORDER BY id
                """;

        List<ProductModel> products = new ArrayList<>();

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                products.add(new ProductModel(
                        rs.getInt("id"),
                        rs.getString("sku"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
        }

        return products;
    }

    public void deductStock(int productId, int quantity) throws Exception {
        String sql = """
                UPDATE Products
                SET stock = stock - ?
                WHERE id = ? AND stock >= ?
                """;

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Insufficient stock or product not found.");
            }
        }
    }
}
