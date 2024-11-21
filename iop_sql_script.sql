CREATE DATABASE iop;
USE iop;
CREATE TABLE warehouse (
    warehouse_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);
CREATE TABLE warehouse_connections (
    connection_id INT AUTO_INCREMENT PRIMARY KEY,
    from_warehouse_id INT,
    to_warehouse_id INT,
    edge_weight INT NOT NULL,
    FOREIGN KEY (from_warehouse_id) REFERENCES warehouse(warehouse_id) ON DELETE CASCADE,
    FOREIGN KEY (to_warehouse_id) REFERENCES warehouse(warehouse_id) ON DELETE CASCADE
);

CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    quantity INT DEFAULT 0,
    warehouse_id INT,  -- This foreign key references the warehouse table
    FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id) ON DELETE SET NULL
);
ALTER TABLE Product ADD COLUMN product_hash VARCHAR(64) AFTER quantity;
