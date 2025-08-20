-- 001_schema.sql  (MySQL 8+)
-- Creates the database + tables for the online billing system (no sample data).

-- Create DB (rename if you use a different name)
CREATE DATABASE IF NOT EXISTS pahanaedu
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE pahanaedu;

-- Drop tables in FK-safe order (idempotent for local dev)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS bill_items;
DROP TABLE IF EXISTS bills;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

-- Users (for login/roles)
CREATE TABLE users (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  username      VARCHAR(64) NOT NULL UNIQUE,
  password      VARCHAR(255) NOT NULL,      -- store plain for demo OR hash if your DAO hashes
  role          VARCHAR(20)  NOT NULL,      -- e.g., 'ADMIN', 'EMPLOYER', 'CASHIER'
  created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Customers
CREATE TABLE customers (
  account_no    INT NOT NULL PRIMARY KEY,
  name          VARCHAR(120) NOT NULL,
  address       VARCHAR(255),
  phone         VARCHAR(32),
  email         VARCHAR(120),
  created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Products
CREATE TABLE products (
  product_no    INT NOT NULL PRIMARY KEY,
  name          VARCHAR(160) NOT NULL,
  unit          INT NOT NULL DEFAULT 0,        -- stock on hand
  price         DECIMAL(10,2) NOT NULL,        -- per-unit price
  created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_products_unit CHECK (unit >= 0),
  CONSTRAINT chk_products_price CHECK (price >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bills (header)
CREATE TABLE bills (
  bill_id       INT AUTO_INCREMENT PRIMARY KEY,
  customer_no   INT NOT NULL,
  created_by    VARCHAR(64) NOT NULL,          -- users.username
  created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total         DECIMAL(12,2) DEFAULT 0,       -- denormalized for convenience
  CONSTRAINT fk_bills_customer   FOREIGN KEY (customer_no) REFERENCES customers(account_no)
      ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT fk_bills_user       FOREIGN KEY (created_by)  REFERENCES users(username)
      ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Bill line-items
CREATE TABLE bill_items (
  bill_id       INT NOT NULL,
  product_no    INT NOT NULL,
  qty           INT NOT NULL,
  unit_price    DECIMAL(10,2) NOT NULL,        -- captured at time of sale
  line_total    DECIMAL(12,2) GENERATED ALWAYS AS (qty * unit_price) STORED,
  PRIMARY KEY (bill_id, product_no),
  CONSTRAINT fk_items_bill     FOREIGN KEY (bill_id)    REFERENCES bills(bill_id)
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_items_product  FOREIGN KEY (product_no) REFERENCES products(product_no)
      ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT chk_items_qty     CHECK (qty > 0),
  CONSTRAINT chk_items_price   CHECK (unit_price >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Helpful indexes (optional; FKs already indexed)
CREATE INDEX idx_customers_name ON customers(name);
CREATE INDEX idx_products_name  ON products(name);
