package org.example.DAO;

import java.util.ArrayList;
import java.util.List;
import org.example.util.db;
import org.example.Model.ProductModel;
import java.sql.*;

public class ProductDAO {
    public int createProduct(ProductModel product) throws Exception {
        String sql = "INSERT INTO product (id, sku, name, price, stock) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, product.id());
            statement.setString(2, product.sku());
            statement.setString(3, product.name());
            statement.setDouble(4, product.price());
            statement.setInt(5, product.stock());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }

            throw new SQLException("Creating product failed, no ID obtained.");
        }
    }
    public boolean updateProduct(ProductModel product) throws Exception {
        String sql = "UPDATE product SET sku = ?, name = ?, price = ?, stock = ? " +
                "WHERE id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.sku());
            statement.setString(2, product.name());
            statement.setDouble(3, product.price());
            statement.setInt(4, product.stock());
            statement.setInt(5, product.id());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }
    public boolean deleteProduct(int productId) throws Exception {
        String sql = "DELETE FROM product WHERE id = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public List<ProductModel> getAllProducts() throws Exception {
        String sql = "SELECT id, sku, name, price, stock FROM product ORDER BY id";
        List<ProductModel> products = new ArrayList<>();

        try (Connection connection = db.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

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
        }

        return products;
    }
}
