package com.inventory.util;

import com.inventory.config.DatabaseConfig;
import com.inventory.model.Product;
import com.inventory.dao.ProductDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenerateProductHashes {

    public static void main(String[] args) {
        try {
            // Create ProductDAO instance
            ProductDAO productDAO = new ProductDAO();
            productDAO.generateAndUpdateProductHashes(); // Call the method to generate and update hashes
        } catch (ClassNotFoundException e) {
        }
    }
}
