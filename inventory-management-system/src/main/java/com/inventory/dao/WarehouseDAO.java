package com.inventory.dao;

import com.inventory.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class WarehouseDAO {

    // Method to retrieve all warehouses with their IDs and names
    public Map<Integer, String> getAllWarehouseNames() throws ClassNotFoundException {
        Map<Integer, String> warehouseNames = new HashMap<>();
        String sql = "SELECT warehouse_id, name FROM warehouse";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                warehouseNames.put(resultSet.getInt("warehouse_id"), resultSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warehouseNames;
    }
}
