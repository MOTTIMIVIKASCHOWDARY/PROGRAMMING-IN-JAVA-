-- Smart Inventory, Supplier & Predictive Reorder Management Platform (SIMR)
-- Seed Demo Data

-- 1. Default Admin User
INSERT INTO users (username, password, full_name, role) VALUES 
('admin', 'admin123', 'System Administrator', 'Administrator');

-- 2. Suppliers
INSERT INTO suppliers (supplier_code, name, contact_person, phone, email, address, lead_time, status) VALUES
('SUP-001', 'ABC Electronics Ltd', 'Robert Miller', '+1-555-0192', 'robert@abcelectronics.com', '104 Industrial Ave, Silicon Bay, CA', 3, 'Active'),
('SUP-002', 'TechWorld Components', 'Sarah Jenkins', '+1-555-0144', 'sales@techworldcomp.com', '240 Tech Parkway, Austin, TX', 4, 'Active'),
('SUP-003', 'Global Traders Inc', 'David Zhang', '+1-555-0178', 'dzhang@globaltraders.io', '55 Trade Center Blvd, Seattle, WA', 5, 'Active'),
('SUP-004', 'Apex Networking Solutions', 'Elena Rostova', '+1-555-0129', 'elena@apexnet.com', '88 Connectivity Way, Boston, MA', 2, 'Active'),
('SUP-005', 'Nexus Office Supplies', 'Marcus Vance', '+1-555-0163', 'marcus@nexusoffices.com', '312 Corporate Plaza, Chicago, IL', 3, 'Active');

-- 3. Products
INSERT INTO products (product_code, name, category, supplier_id, unit_price, current_stock, minimum_stock, maximum_stock, reorder_quantity) VALUES
('PRD-1001', 'Pro Laptop 15.6" i7', 'Computer Equipment', 3, 62500.00, 25, 10, 50, 20),
('PRD-1002', 'Wireless Mechanical Keyboard', 'Accessories', 1, 3450.00, 7, 10, 60, 40),
('PRD-1003', 'Ergonomic Optical Mouse', 'Accessories', 2, 1250.00, 3, 12, 70, 50),
('PRD-1004', 'Ultra-Slim 27" 4K Monitor', 'Electronics', 1, 24999.00, 18, 8, 40, 15),
('PRD-1005', 'USB-C 7-in-1 Multiport Hub', 'Accessories', 1, 2199.00, 5, 10, 50, 30),
('PRD-1006', 'High-Speed HDMI 2.1 Cable (2m)', 'Accessories', 2, 499.00, 0, 15, 100, 60),
('PRD-1007', 'Cat6 Gigabit Ethernet Cable (10m)', 'Networking', 4, 349.00, 8, 15, 120, 50),
('PRD-1008', 'Gigabit 16-Port Network Switch', 'Networking', 4, 7850.00, 14, 6, 30, 12),
('PRD-1009', 'Wi-Fi 6 Dual Band Router', 'Networking', 4, 5490.00, 22, 10, 45, 20),
('PRD-1010', '16GB DDR4 3200MHz RAM', 'Computer Equipment', 2, 3200.00, 2, 12, 60, 35),
('PRD-1011', '1TB NVMe M.2 PCIe Gen4 SSD', 'Computer Equipment', 2, 6800.00, 30, 10, 50, 25),
('PRD-1012', 'Noise Cancelling Headset USB', 'Electronics', 1, 4200.00, 16, 8, 40, 20),
('PRD-1013', 'Full HD 1080p Web Camera', 'Electronics', 1, 2750.00, 19, 8, 50, 25),
('PRD-1014', 'Ergonomic Memory Foam Mousepad', 'Office Supplies', 5, 399.00, 6, 12, 80, 40),
('PRD-1015', 'Adjustable Aluminum Laptop Stand', 'Office Supplies', 5, 1650.00, 28, 10, 50, 20),
('PRD-1016', 'Thermal Paste Syringe (4g)', 'Computer Equipment', 2, 450.00, 4, 10, 50, 30),
('PRD-1017', 'Surge Protector 6-Socket Strip', 'Electronics', 3, 1150.00, 24, 10, 60, 25),
('PRD-1018', 'Heavy-Duty Cable Organizer Pack', 'Office Supplies', 5, 299.00, 45, 15, 100, 50);

-- 4. Stock Transactions (Realistic history of Stock In and Stock Out)
-- Stock In records (initial & replenishment)
INSERT INTO stock_transactions (product_id, type, quantity, reason, transaction_date, performed_by) VALUES
(1, 'IN', 30, 'Initial Stock Intake', datetime('now', '-28 days'), 'admin'),
(2, 'IN', 40, 'Initial Stock Intake', datetime('now', '-28 days'), 'admin'),
(3, 'IN', 50, 'Initial Stock Intake', datetime('now', '-28 days'), 'admin'),
(4, 'IN', 25, 'Initial Stock Intake', datetime('now', '-27 days'), 'admin'),
(5, 'IN', 35, 'Initial Stock Intake', datetime('now', '-27 days'), 'admin'),
(6, 'IN', 60, 'Initial Stock Intake', datetime('now', '-26 days'), 'admin'),
(7, 'IN', 50, 'Initial Stock Intake', datetime('now', '-26 days'), 'admin'),
(8, 'IN', 20, 'Initial Stock Intake', datetime('now', '-25 days'), 'admin'),
(9, 'IN', 30, 'Initial Stock Intake', datetime('now', '-25 days'), 'admin'),
(10, 'IN', 35, 'Initial Stock Intake', datetime('now', '-24 days'), 'admin'),
(11, 'IN', 45, 'Initial Stock Intake', datetime('now', '-24 days'), 'admin'),
(12, 'IN', 25, 'Initial Stock Intake', datetime('now', '-23 days'), 'admin'),
(13, 'IN', 30, 'Initial Stock Intake', datetime('now', '-23 days'), 'admin'),
(14, 'IN', 40, 'Initial Stock Intake', datetime('now', '-22 days'), 'admin'),
(15, 'IN', 35, 'Initial Stock Intake', datetime('now', '-22 days'), 'admin'),
(16, 'IN', 30, 'Initial Stock Intake', datetime('now', '-21 days'), 'admin'),
(17, 'IN', 35, 'Initial Stock Intake', datetime('now', '-21 days'), 'admin'),
(18, 'IN', 60, 'Initial Stock Intake', datetime('now', '-20 days'), 'admin');

-- Stock Out records across past weeks simulating customer demand
INSERT INTO stock_transactions (product_id, type, quantity, reason, transaction_date, performed_by) VALUES
(1, 'OUT', 5, 'Sales Order #SO-8821', datetime('now', '-18 days'), 'admin'),
(2, 'OUT', 12, 'Corporate Bulk Order', datetime('now', '-17 days'), 'admin'),
(3, 'OUT', 18, 'Retail Dispatch', datetime('now', '-16 days'), 'admin'),
(6, 'OUT', 25, 'Customer Sales', datetime('now', '-15 days'), 'admin'),
(7, 'OUT', 20, 'Office Cabling Project', datetime('now', '-14 days'), 'admin'),
(10, 'OUT', 15, 'Workstation Upgrades', datetime('now', '-13 days'), 'admin'),
(14, 'OUT', 18, 'Retail Order #SO-8910', datetime('now', '-12 days'), 'admin'),
(16, 'OUT', 14, 'Service Lab Usage', datetime('now', '-11 days'), 'admin'),
(2, 'OUT', 11, 'Sales Order #SO-8940', datetime('now', '-10 days'), 'admin'),
(3, 'OUT', 15, 'Bulk Sales Dispatch', datetime('now', '-9 days'), 'admin'),
(5, 'OUT', 14, 'Customer Orders', datetime('now', '-8 days'), 'admin'),
(6, 'OUT', 20, 'Sales Order #SO-9012', datetime('now', '-7 days'), 'admin'),
(7, 'OUT', 12, 'Network Deployment', datetime('now', '-6 days'), 'admin'),
(10, 'OUT', 10, 'Lab Maintenance', datetime('now', '-5 days'), 'admin'),
(2, 'OUT', 10, 'Retail Order #SO-9102', datetime('now', '-4 days'), 'admin'),
(3, 'OUT', 14, 'Customer Sales', datetime('now', '-3 days'), 'admin'),
(5, 'OUT', 16, 'Corporate Procurement', datetime('now', '-2 days'), 'admin'),
(6, 'OUT', 15, 'Retail Order #SO-9215', datetime('now', '-1 days'), 'admin'),
(7, 'OUT', 10, 'Customer Orders', datetime('now', '-1 days'), 'admin'),
(10, 'OUT', 8, 'Upgrade Request', datetime('now', '-12 hours'), 'admin'),
(14, 'OUT', 16, 'Sales Order #SO-9340', datetime('now', '-6 hours'), 'admin'),
(16, 'OUT', 12, 'Repair Center', datetime('now', '-2 hours'), 'admin');

-- 5. Purchase Orders
INSERT INTO purchase_orders (po_number, supplier_id, product_id, quantity, unit_price, total_amount, order_date, expected_delivery, status) VALUES
('PO-2026-001', 2, 6, 60, 499.00, 29940.00, datetime('now', '-2 days'), date('now', '+2 days'), 'Pending'),
('PO-2026-002', 2, 3, 50, 1250.00, 62500.00, datetime('now', '-1 days'), date('now', '+3 days'), 'Pending'),
('PO-2026-003', 1, 2, 40, 3450.00, 138000.00, datetime('now', '-1 days'), date('now', '+2 days'), 'Pending');
