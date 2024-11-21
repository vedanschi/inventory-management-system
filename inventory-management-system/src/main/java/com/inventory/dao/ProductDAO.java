package com.inventory.dao;

import com.inventory.config.DatabaseConfig;
import com.inventory.model.Product;
import com.inventory.util.HashUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Method to add a product with hashing
    public void addProduct(Product product) throws ClassNotFoundException {
        String sql = "INSERT INTO Product (name, warehouse_id, type, quantity, product_hash) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Generate hash based on product details
            String productHash = HashUtil.generateHash(product.getProductName() + product.getType() + product.getWarehouseId() + product.getQuantity());

            statement.setString(1, product.getProductName());
            statement.setInt(2, product.getWarehouseId());
            statement.setString(3, product.getType());
            statement.setInt(4, product.getQuantity());
            statement.setString(5, productHash); // Store the generated hash
            statement.executeUpdate();

        } catch (SQLException e) {
        }
    }

    // Method to retrieve all products with their hash
    public List<Product> getAllProducts() throws ClassNotFoundException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT product_id, name, warehouse_id, type, quantity, product_hash FROM Product";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Product product = new Product();
                product.setProductId(resultSet.getInt("product_id"));
                product.setProductName(resultSet.getString("name"));
                product.setWarehouseId(resultSet.getInt("warehouse_id"));
                product.setType(resultSet.getString("type"));
                product.setQuantity(resultSet.getInt("quantity"));
                product.setProductHash(resultSet.getString("product_hash")); // Retrieve the stored hash

                products.add(product);
            }

        } catch (SQLException e) {
        }
        return products;
    }
    public void generateAndUpdateProductHashes() throws ClassNotFoundException {
        String selectQuery = "SELECT product_id, name, warehouse_id, type, quantity FROM Product WHERE product_hash IS NULL";
        String updateQuery = "UPDATE Product SET product_hash = ? WHERE product_id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            ResultSet resultSet = selectStatement.executeQuery();
            
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("name");
                int warehouseId = resultSet.getInt("warehouse_id");
                String type = resultSet.getString("type");
                int quantity = resultSet.getInt("quantity");

                // Generate hash based on product details
                String productHash = HashUtil.generateHash(productName + type + warehouseId + quantity);

                // Update the product's hash in the database
                updateStatement.setString(1, productHash);
                updateStatement.setInt(2, productId);
                updateStatement.executeUpdate();
            }
            
            System.out.println("Product hashes updated successfully.");
            
        } catch (SQLException e) {
        }
    }
    // Method to get product by ID
    public Product getProductById(int productId) throws ClassNotFoundException {
        Product product = null;
        String sql = "SELECT * FROM Product WHERE product_id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    product = new Product();
                    product.setProductId(resultSet.getInt("product_id"));
                    product.setProductName(resultSet.getString("name"));
                    product.setWarehouseId(resultSet.getInt("warehouse_id"));
                    product.setType(resultSet.getString("type"));
                    product.setQuantity(resultSet.getInt("quantity"));
                    product.setProductHash(resultSet.getString("product_hash"));
                }
            }

        } catch (SQLException e) {
        }
        return product;
    }
// Method to get product by name
    public Product getProductByName(String productName) throws ClassNotFoundException {
        Product product = null;
        String sql = "SELECT * FROM Product WHERE name = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, productName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    product = new Product();
                    product.setProductId(resultSet.getInt("product_id"));
                    product.setProductName(resultSet.getString("name"));
                    product.setWarehouseId(resultSet.getInt("warehouse_id"));
                    product.setType(resultSet.getString("type"));
                    product.setQuantity(resultSet.getInt("quantity"));
                    product.setProductHash(resultSet.getString("product_hash"));
                }
            }

        } catch (SQLException e) {
        }
        return product;
    }

    public void updateQuantity(int productId, int quantityToAdd) throws ClassNotFoundException {
    if (quantityToAdd <= 0) {
        throw new IllegalArgumentException("Quantity to add must be positive.");
    }

    String sql = "UPDATE Product SET quantity = quantity + ? WHERE product_id = ?";

    try (Connection connection = DatabaseConfig.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, quantityToAdd); // Adding to the existing stock
        statement.setInt(2, productId);

        int rowsUpdated = statement.executeUpdate();
        
        if (rowsUpdated == 0) {
            throw new SQLException("No product found with the given product ID.");
        }

    } catch (SQLException e) {
        e.printStackTrace();  // Handle SQL exceptions here
    }
}


    // Method to transfer product to a new warehouse
    public void transferProduct(int productId, int newWarehouseId) throws ClassNotFoundException {
        String sql = "UPDATE Product SET warehouse_id = ? WHERE product_id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, newWarehouseId);
            statement.setInt(2, productId);
            statement.executeUpdate();

        } catch (SQLException e) {
        }
    }
}

