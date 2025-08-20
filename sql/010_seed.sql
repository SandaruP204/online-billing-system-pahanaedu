-- 010_seed.sql  (MySQL 8+)
-- Minimal, safe demo data. Adjust usernames/passwords to match your DAO logic.

USE pahanaedu;

-- Users
INSERT INTO users (username, password, role) VALUES
  ('admin',    'admin123',    'ADMIN'),
  ('employer', 'employer123', 'EMPLOYER'),
  ('cashier',  'cashier123',  'CASHIER');

-- Customers
INSERT INTO customers (account_no, name, address, phone, email) VALUES
  (1001, 'Alice Fernando',   'Colombo 05', '0771234567', 'alice@example.com'),
  (1002, 'M. Perera',        'Galle',      '0715557788', 'perera@example.com'),
  (1003, 'Nuwan Jay',        'Kandy',      '0752223344', 'nuwan@example.com');

-- Products
INSERT INTO products (product_no, name, unit, price) VALUES
  (2001, 'A4 Exercise Book', 120, 180.00),
  (2002, 'Blue Pen',         350,  60.00),
  (2003, 'HB Pencil',        240,  40.00),
  (2004, 'Ruler 30cm',       180,  75.00);

-- One sample bill (admin sells to account_no=1001)
INSERT INTO bills (customer_no, created_by, total)
VALUES (1001, 'admin', 0.00);

-- Grab the new bill_id
SET @bill_id = LAST_INSERT_ID();

-- Add items to the bill (unit_price captured from products at time of sale)
INSERT INTO bill_items (bill_id, product_no, qty, unit_price) VALUES
  (@bill_id, 2001, 2, 180.00),
  (@bill_id, 2002, 3,  60.00);

-- Update the bill total from its items
UPDATE bills b
SET b.total = (SELECT COALESCE(SUM(qty * unit_price),0) FROM bill_items bi WHERE bi.bill_id = b.bill_id)
WHERE b.bill_id = @bill_id;
