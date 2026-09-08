package util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static String dbType = "UNKNOWN"; // "MYSQL" or "SQLITE"
    private static String dbUrl;
    private static String dbUser = "root";
    private static String dbPassword = "";

    // Default MySQL configuration
    private static final String MYSQL_HOST = System.getProperty("db.host", "localhost");
    private static final String MYSQL_PORT = System.getProperty("db.port", "3306");
    private static final String MYSQL_DB = System.getProperty("db.name", "simr_db");
    private static final String MYSQL_USER = System.getProperty("db.user", "root");
    private static final String MYSQL_PASSWORD = System.getProperty("db.password", "");

    // Default SQLite fallback
    private static final String SQLITE_DB_PATH = "db/simr.db";

    static {
        initDatabase();
    }

    public static synchronized void initDatabase() {
        if (!"UNKNOWN".equals(dbType)) {
            return;
        }

        // Try MySQL first
        boolean mysqlSuccess = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String testUrl = "jdbc:mysql://" + MYSQL_HOST + ":" + MYSQL_PORT + "/?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
            try (Connection conn = DriverManager.getConnection(testUrl, MYSQL_USER, MYSQL_PASSWORD);
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + MYSQL_DB);
                dbUrl = "jdbc:mysql://" + MYSQL_HOST + ":" + MYSQL_PORT + "/" + MYSQL_DB + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
                dbUser = MYSQL_USER;
                dbPassword = MYSQL_PASSWORD;
                dbType = "MYSQL";
                mysqlSuccess = true;
                System.out.println("[DBConnection] Connected successfully to MySQL Database: " + dbUrl);
            }
        } catch (Throwable t) {
            System.out.println("[DBConnection] MySQL not available (" + t.getMessage() + "). Falling back to embedded SQLite database.");
        }

        // Fallback to SQLite
        if (!mysqlSuccess) {
            try {
                Class.forName("org.sqlite.JDBC");
                File dbDir = new File("db");
                if (!dbDir.exists()) dbDir.mkdirs();
                dbUrl = "jdbc:sqlite:" + SQLITE_DB_PATH;
                dbUser = "";
                dbPassword = "";
                dbType = "SQLITE";
                System.out.println("[DBConnection] Initialized SQLite Database at: " + SQLITE_DB_PATH);
            } catch (Throwable t) {
                System.err.println("[DBConnection] Fatal: Failed to initialize any JDBC driver: " + t.getMessage());
                t.printStackTrace();
            }
        }

        // Create tables and seed data if not existing
        createTablesAndSeedData();
    }

    public static Connection getConnection() throws SQLException {
        if ("SQLITE".equals(dbType)) {
            return DriverManager.getConnection(dbUrl);
        } else {
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        }
    }

    public static String getDatabaseType() {
        return dbType;
    }

    private static void createTablesAndSeedData() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            boolean tablesExist = false;
            try {
                ResultSet rs = stmt.executeQuery("SELECT count(*) FROM users");
                if (rs.next()) {
                    tablesExist = true;
                }
            } catch (SQLException e) {
                tablesExist = false;
            }

            if (!tablesExist) {
                System.out.println("[DBConnection] Creating schema and inserting initial sample data...");
                if ("SQLITE".equals(dbType)) {
                    createSQLiteSchema(stmt);
                    seedSQLiteData(stmt);
                } else {
                    createMySQLSchema(stmt);
                    seedMySQLData(stmt);
                }
                System.out.println("[DBConnection] Database schema and sample data initialized successfully!");
            } else {
                System.out.println("[DBConnection] Database tables found and ready.");
            }
        } catch (SQLException e) {
            System.err.println("[DBConnection] Error during schema/data setup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createSQLiteSchema(Statement stmt) throws SQLException {
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "full_name TEXT NOT NULL, " +
                "role TEXT NOT NULL DEFAULT 'Administrator', " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS suppliers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "supplier_code TEXT NOT NULL UNIQUE, " +
                "name TEXT NOT NULL, " +
                "contact_person TEXT NOT NULL, " +
                "phone TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "lead_time INTEGER NOT NULL DEFAULT 3, " +
                "status TEXT NOT NULL DEFAULT 'Active', " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "product_code TEXT NOT NULL UNIQUE, " +
                "name TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "supplier_id INTEGER, " +
                "unit_price DECIMAL(10, 2) NOT NULL, " +
                "current_stock INTEGER NOT NULL DEFAULT 0, " +
                "minimum_stock INTEGER NOT NULL DEFAULT 10, " +
                "maximum_stock INTEGER NOT NULL DEFAULT 100, " +
                "reorder_quantity INTEGER NOT NULL DEFAULT 30, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS stock_transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "product_id INTEGER NOT NULL, " +
                "type TEXT NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "reason TEXT, " +
                "transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "performed_by TEXT DEFAULT 'admin', " +
                "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS purchase_orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "po_number TEXT NOT NULL UNIQUE, " +
                "supplier_id INTEGER NOT NULL, " +
                "product_id INTEGER NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "unit_price DECIMAL(10, 2) NOT NULL, " +
                "total_amount DECIMAL(12, 2) NOT NULL, " +
                "order_date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "expected_delivery DATE, " +
                "status TEXT NOT NULL DEFAULT 'Pending', " +
                "FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE)");
    }

    private static void createMySQLSchema(Statement stmt) throws SQLException {
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(100) NOT NULL, " +
                "full_name VARCHAR(100) NOT NULL, " +
                "role VARCHAR(30) NOT NULL DEFAULT 'Administrator', " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS suppliers (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "supplier_code VARCHAR(20) NOT NULL UNIQUE, " +
                "name VARCHAR(100) NOT NULL, " +
                "contact_person VARCHAR(100) NOT NULL, " +
                "phone VARCHAR(25) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "address VARCHAR(255) NOT NULL, " +
                "lead_time INT NOT NULL DEFAULT 3, " +
                "status VARCHAR(20) NOT NULL DEFAULT 'Active', " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS products (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "product_code VARCHAR(20) NOT NULL UNIQUE, " +
                "name VARCHAR(120) NOT NULL, " +
                "category VARCHAR(50) NOT NULL, " +
                "supplier_id INT, " +
                "unit_price DECIMAL(10, 2) NOT NULL, " +
                "current_stock INT NOT NULL DEFAULT 0, " +
                "minimum_stock INT NOT NULL DEFAULT 10, " +
                "maximum_stock INT NOT NULL DEFAULT 100, " +
                "reorder_quantity INT NOT NULL DEFAULT 30, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE SET NULL)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS stock_transactions (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "product_id INT NOT NULL, " +
                "type VARCHAR(10) NOT NULL, " +
                "quantity INT NOT NULL, " +
                "reason VARCHAR(150), " +
                "transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "performed_by VARCHAR(50) DEFAULT 'admin', " +
                "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE)");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS purchase_orders (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "po_number VARCHAR(30) NOT NULL UNIQUE, " +
                "supplier_id INT NOT NULL, " +
                "product_id INT NOT NULL, " +
                "quantity INT NOT NULL, " +
                "unit_price DECIMAL(10, 2) NOT NULL, " +
                "total_amount DECIMAL(12, 2) NOT NULL, " +
                "order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "expected_delivery DATE, " +
                "status VARCHAR(20) NOT NULL DEFAULT 'Pending', " +
                "FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE)");
    }

    private static void seedSQLiteData(Statement stmt) throws SQLException {
        stmt.executeUpdate("INSERT INTO users (username, password, full_name, role) VALUES ('admin', 'admin123', 'System Administrator', 'Administrator')");

        stmt.executeUpdate("INSERT INTO suppliers (supplier_code, name, contact_person, phone, email, address, lead_time, status) VALUES " +
                "('SUP-001', 'ABC Electronics Ltd', 'Robert Miller', '+1-555-0192', 'robert@abcelectronics.com', '104 Industrial Ave, Silicon Bay, CA', 3, 'Active')," +
                "('SUP-002', 'TechWorld Components', 'Sarah Jenkins', '+1-555-0144', 'sales@techworldcomp.com', '240 Tech Parkway, Austin, TX', 4, 'Active')," +
                "('SUP-003', 'Global Traders Inc', 'David Zhang', '+1-555-0178', 'dzhang@globaltraders.io', '55 Trade Center Blvd, Seattle, WA', 5, 'Active')," +
                "('SUP-004', 'Apex Networking Solutions', 'Elena Rostova', '+1-555-0129', 'elena@apexnet.com', '88 Connectivity Way, Boston, MA', 2, 'Active')," +
                "('SUP-005', 'Nexus Office Supplies', 'Marcus Vance', '+1-555-0163', 'marcus@nexusoffices.com', '312 Corporate Plaza, Chicago, IL', 3, 'Active')");

        stmt.executeUpdate("INSERT INTO products (product_code, name, category, supplier_id, unit_price, current_stock, minimum_stock, maximum_stock, reorder_quantity) VALUES " +
                "('PRD-1001', 'Pro Laptop 15.6\" i7', 'Computer Equipment', 3, 62500.00, 25, 10, 50, 20)," +
                "('PRD-1002', 'Wireless Mechanical Keyboard', 'Accessories', 1, 3450.00, 7, 10, 60, 40)," +
                "('PRD-1003', 'Ergonomic Optical Mouse', 'Accessories', 2, 1250.00, 3, 12, 70, 50)," +
                "('PRD-1004', 'Ultra-Slim 27\" 4K Monitor', 'Electronics', 1, 24999.00, 18, 8, 40, 15)," +
                "('PRD-1005', 'USB-C 7-in-1 Multiport Hub', 'Accessories', 1, 2199.00, 5, 10, 50, 30)," +
                "('PRD-1006', 'High-Speed HDMI 2.1 Cable (2m)', 'Accessories', 2, 499.00, 0, 15, 100, 60)," +
                "('PRD-1007', 'Cat6 Gigabit Ethernet Cable (10m)', 'Networking', 4, 349.00, 8, 15, 120, 50)," +
                "('PRD-1008', 'Gigabit 16-Port Network Switch', 'Networking', 4, 7850.00, 14, 6, 30, 12)," +
                "('PRD-1009', 'Wi-Fi 6 Dual Band Router', 'Networking', 4, 5490.00, 22, 10, 45, 20)," +
                "('PRD-1010', '16GB DDR4 3200MHz RAM', 'Computer Equipment', 2, 3200.00, 2, 12, 60, 35)," +
                "('PRD-1011', '1TB NVMe M.2 PCIe Gen4 SSD', 'Computer Equipment', 2, 6800.00, 30, 10, 50, 25)," +
                "('PRD-1012', 'Noise Cancelling Headset USB', 'Electronics', 1, 4200.00, 16, 8, 40, 20)," +
                "('PRD-1013', 'Full HD 1080p Web Camera', 'Electronics', 1, 2750.00, 19, 8, 50, 25)," +
                "('PRD-1014', 'Ergonomic Memory Foam Mousepad', 'Office Supplies', 5, 399.00, 6, 12, 80, 40)," +
                "('PRD-1015', 'Adjustable Aluminum Laptop Stand', 'Office Supplies', 5, 1650.00, 28, 10, 50, 20)," +
                "('PRD-1016', 'Thermal Paste Syringe (4g)', 'Computer Equipment', 2, 450.00, 4, 10, 50, 30)," +
                "('PRD-1017', 'Surge Protector 6-Socket Strip', 'Electronics', 3, 1150.00, 24, 10, 60, 25)," +
                "('PRD-1018', 'Heavy-Duty Cable Organizer Pack', 'Office Supplies', 5, 299.00, 45, 15, 100, 50)");

        // Initial transactions
        stmt.executeUpdate("INSERT INTO stock_transactions (product_id, type, quantity, reason, transaction_date, performed_by) VALUES " +
                "(1, 'IN', 30, 'Initial Stock Intake', datetime('now', '-25 days'), 'admin')," +
                "(2, 'IN', 40, 'Initial Stock Intake', datetime('now', '-25 days'), 'admin')," +
                "(3, 'IN', 50, 'Initial Stock Intake', datetime('now', '-25 days'), 'admin')," +
                "(4, 'IN', 25, 'Initial Stock Intake', datetime('now', '-24 days'), 'admin')," +
                "(5, 'IN', 35, 'Initial Stock Intake', datetime('now', '-24 days'), 'admin')," +
                "(6, 'IN', 60, 'Initial Stock Intake', datetime('now', '-23 days'), 'admin')," +
                "(7, 'IN', 50, 'Initial Stock Intake', datetime('now', '-23 days'), 'admin')," +
                "(8, 'IN', 20, 'Initial Stock Intake', datetime('now', '-22 days'), 'admin')," +
                "(9, 'IN', 30, 'Initial Stock Intake', datetime('now', '-22 days'), 'admin')," +
                "(10, 'IN', 35, 'Initial Stock Intake', datetime('now', '-21 days'), 'admin')," +
                "(1, 'OUT', 5, 'Sales Order #SO-8821', datetime('now', '-18 days'), 'admin')," +
                "(2, 'OUT', 12, 'Corporate Bulk Order', datetime('now', '-16 days'), 'admin')," +
                "(3, 'OUT', 18, 'Retail Dispatch', datetime('now', '-15 days'), 'admin')," +
                "(6, 'OUT', 25, 'Customer Sales', datetime('now', '-14 days'), 'admin')," +
                "(7, 'OUT', 20, 'Office Cabling Project', datetime('now', '-12 days'), 'admin')," +
                "(10, 'OUT', 15, 'Workstation Upgrades', datetime('now', '-11 days'), 'admin')," +
                "(14, 'OUT', 18, 'Retail Order #SO-8910', datetime('now', '-10 days'), 'admin')," +
                "(16, 'OUT', 14, 'Service Lab Usage', datetime('now', '-9 days'), 'admin')," +
                "(2, 'OUT', 11, 'Sales Order #SO-8940', datetime('now', '-7 days'), 'admin')," +
                "(3, 'OUT', 15, 'Bulk Sales Dispatch', datetime('now', '-6 days'), 'admin')," +
                "(5, 'OUT', 14, 'Customer Orders', datetime('now', '-5 days'), 'admin')," +
                "(6, 'OUT', 20, 'Sales Order #SO-9012', datetime('now', '-4 days'), 'admin')," +
                "(7, 'OUT', 12, 'Network Deployment', datetime('now', '-3 days'), 'admin')," +
                "(10, 'OUT', 10, 'Lab Maintenance', datetime('now', '-2 days'), 'admin')," +
                "(2, 'OUT', 10, 'Retail Order #SO-9102', datetime('now', '-1 days'), 'admin')," +
                "(3, 'OUT', 14, 'Customer Sales', datetime('now', '-1 days'), 'admin')," +
                "(5, 'OUT', 16, 'Corporate Procurement', datetime('now', '-12 hours'), 'admin')," +
                "(6, 'OUT', 15, 'Retail Order #SO-9215', datetime('now', '-6 hours'), 'admin')," +
                "(7, 'OUT', 10, 'Customer Orders', datetime('now', '-4 hours'), 'admin')," +
                "(10, 'OUT', 8, 'Upgrade Request', datetime('now', '-2 hours'), 'admin')," +
                "(14, 'OUT', 16, 'Sales Order #SO-9340', datetime('now', '-1 hours'), 'admin')," +
                "(16, 'OUT', 12, 'Repair Center', datetime('now', '-30 minutes'), 'admin')");

        stmt.executeUpdate("INSERT INTO purchase_orders (po_number, supplier_id, product_id, quantity, unit_price, total_amount, order_date, expected_delivery, status) VALUES " +
                "('PO-2026-001', 2, 6, 60, 499.00, 29940.00, datetime('now', '-2 days'), date('now', '+2 days'), 'Pending')," +
                "('PO-2026-002', 2, 3, 50, 1250.00, 62500.00, datetime('now', '-1 days'), date('now', '+3 days'), 'Pending')," +
                "('PO-2026-003', 1, 2, 40, 3450.00, 138000.00, datetime('now', '-1 days'), date('now', '+2 days'), 'Pending')");
    }

    private static void seedMySQLData(Statement stmt) throws SQLException {
        stmt.executeUpdate("INSERT INTO users (username, password, full_name, role) VALUES ('admin', 'admin123', 'System Administrator', 'Administrator')");

        stmt.executeUpdate("INSERT INTO suppliers (supplier_code, name, contact_person, phone, email, address, lead_time, status) VALUES " +
                "('SUP-001', 'ABC Electronics Ltd', 'Robert Miller', '+1-555-0192', 'robert@abcelectronics.com', '104 Industrial Ave, Silicon Bay, CA', 3, 'Active')," +
                "('SUP-002', 'TechWorld Components', 'Sarah Jenkins', '+1-555-0144', 'sales@techworldcomp.com', '240 Tech Parkway, Austin, TX', 4, 'Active')," +
                "('SUP-003', 'Global Traders Inc', 'David Zhang', '+1-555-0178', 'dzhang@globaltraders.io', '55 Trade Center Blvd, Seattle, WA', 5, 'Active')," +
                "('SUP-004', 'Apex Networking Solutions', 'Elena Rostova', '+1-555-0129', 'elena@apexnet.com', '88 Connectivity Way, Boston, MA', 2, 'Active')," +
                "('SUP-005', 'Nexus Office Supplies', 'Marcus Vance', '+1-555-0163', 'marcus@nexusoffices.com', '312 Corporate Plaza, Chicago, IL', 3, 'Active')");

        stmt.executeUpdate("INSERT INTO products (product_code, name, category, supplier_id, unit_price, current_stock, minimum_stock, maximum_stock, reorder_quantity) VALUES " +
                "('PRD-1001', 'Pro Laptop 15.6\" i7', 'Computer Equipment', 3, 62500.00, 25, 10, 50, 20)," +
                "('PRD-1002', 'Wireless Mechanical Keyboard', 'Accessories', 1, 3450.00, 7, 10, 60, 40)," +
                "('PRD-1003', 'Ergonomic Optical Mouse', 'Accessories', 2, 1250.00, 3, 12, 70, 50)," +
                "('PRD-1004', 'Ultra-Slim 27\" 4K Monitor', 'Electronics', 1, 24999.00, 18, 8, 40, 15)," +
                "('PRD-1005', 'USB-C 7-in-1 Multiport Hub', 'Accessories', 1, 2199.00, 5, 10, 50, 30)," +
                "('PRD-1006', 'High-Speed HDMI 2.1 Cable (2m)', 'Accessories', 2, 499.00, 0, 15, 100, 60)," +
                "('PRD-1007', 'Cat6 Gigabit Ethernet Cable (10m)', 'Networking', 4, 349.00, 8, 15, 120, 50)," +
                "('PRD-1008', 'Gigabit 16-Port Network Switch', 'Networking', 4, 7850.00, 14, 6, 30, 12)," +
                "('PRD-1009', 'Wi-Fi 6 Dual Band Router', 'Networking', 4, 5490.00, 22, 10, 45, 20)," +
                "('PRD-1010', '16GB DDR4 3200MHz RAM', 'Computer Equipment', 2, 3200.00, 2, 12, 60, 35)," +
                "('PRD-1011', '1TB NVMe M.2 PCIe Gen4 SSD', 'Computer Equipment', 2, 6800.00, 30, 10, 50, 25)," +
                "('PRD-1012', 'Noise Cancelling Headset USB', 'Electronics', 1, 4200.00, 16, 8, 40, 20)," +
                "('PRD-1013', 'Full HD 1080p Web Camera', 'Electronics', 1, 2750.00, 19, 8, 50, 25)," +
                "('PRD-1014', 'Ergonomic Memory Foam Mousepad', 'Office Supplies', 5, 399.00, 6, 12, 80, 40)," +
                "('PRD-1015', 'Adjustable Aluminum Laptop Stand', 'Office Supplies', 5, 1650.00, 28, 10, 50, 20)," +
                "('PRD-1016', 'Thermal Paste Syringe (4g)', 'Computer Equipment', 2, 450.00, 4, 10, 50, 30)," +
                "('PRD-1017', 'Surge Protector 6-Socket Strip', 'Electronics', 3, 1150.00, 24, 10, 60, 25)," +
                "('PRD-1018', 'Heavy-Duty Cable Organizer Pack', 'Office Supplies', 5, 299.00, 45, 15, 100, 50)");

        stmt.executeUpdate("INSERT INTO stock_transactions (product_id, type, quantity, reason, transaction_date, performed_by) VALUES " +
                "(1, 'IN', 30, 'Initial Stock Intake', DATE_SUB(NOW(), INTERVAL 25 DAY), 'admin')," +
                "(2, 'IN', 40, 'Initial Stock Intake', DATE_SUB(NOW(), INTERVAL 25 DAY), 'admin')," +
                "(3, 'IN', 50, 'Initial Stock Intake', DATE_SUB(NOW(), INTERVAL 25 DAY), 'admin')," +
                "(4, 'IN', 25, 'Initial Stock Intake', DATE_SUB(NOW(), INTERVAL 24 DAY), 'admin')," +
                "(5, 'IN', 35, 'Initial Stock Intake', DATE_SUB(NOW(), INTERVAL 24 DAY), 'admin')," +
                "(6, 'IN', 60, 'Initial Stock Intake', DATE_SUB(NOW(), INTERVAL 23 DAY), 'admin')," +
                "(7, 'IN', 50, 'Initial Stock Intake', DATE_SUB(NOW(), INTERVAL 23 DAY), 'admin')," +
                "(8, 'IN', 20, 'Initial Stock Intake', DATE_SUB(NOW(), INTERVAL 22 DAY), 'admin')," +
                "(9, 'IN', 30, 'Initial Stock Intake', DATE_SUB(NOW(), INTERVAL 22 DAY), 'admin')," +
                "(10, 'IN', 35, 'Initial Stock Intake', DATE_SUB(NOW(), INTERVAL 21 DAY), 'admin')," +
                "(1, 'OUT', 5, 'Sales Order #SO-8821', DATE_SUB(NOW(), INTERVAL 18 DAY), 'admin')," +
                "(2, 'OUT', 12, 'Corporate Bulk Order', DATE_SUB(NOW(), INTERVAL 16 DAY), 'admin')," +
                "(3, 'OUT', 18, 'Retail Dispatch', DATE_SUB(NOW(), INTERVAL 15 DAY), 'admin')," +
                "(6, 'OUT', 25, 'Customer Sales', DATE_SUB(NOW(), INTERVAL 14 DAY), 'admin')," +
                "(7, 'OUT', 20, 'Office Cabling Project', DATE_SUB(NOW(), INTERVAL 12 DAY), 'admin')," +
                "(10, 'OUT', 15, 'Workstation Upgrades', DATE_SUB(NOW(), INTERVAL 11 DAY), 'admin')," +
                "(14, 'OUT', 18, 'Retail Order #SO-8910', DATE_SUB(NOW(), INTERVAL 10 DAY), 'admin')," +
                "(16, 'OUT', 14, 'Service Lab Usage', DATE_SUB(NOW(), INTERVAL 9 DAY), 'admin')," +
                "(2, 'OUT', 11, 'Sales Order #SO-8940', DATE_SUB(NOW(), INTERVAL 7 DAY), 'admin')," +
                "(3, 'OUT', 15, 'Bulk Sales Dispatch', DATE_SUB(NOW(), INTERVAL 6 DAY), 'admin')," +
                "(5, 'OUT', 14, 'Customer Orders', DATE_SUB(NOW(), INTERVAL 5 DAY), 'admin')," +
                "(6, 'OUT', 20, 'Sales Order #SO-9012', DATE_SUB(NOW(), INTERVAL 4 DAY), 'admin')," +
                "(7, 'OUT', 12, 'Network Deployment', DATE_SUB(NOW(), INTERVAL 3 DAY), 'admin')," +
                "(10, 'OUT', 10, 'Lab Maintenance', DATE_SUB(NOW(), INTERVAL 2 DAY), 'admin')," +
                "(2, 'OUT', 10, 'Retail Order #SO-9102', DATE_SUB(NOW(), INTERVAL 1 DAY), 'admin')," +
                "(3, 'OUT', 14, 'Customer Sales', DATE_SUB(NOW(), INTERVAL 1 DAY), 'admin')," +
                "(5, 'OUT', 16, 'Corporate Procurement', DATE_SUB(NOW(), INTERVAL 12 HOUR), 'admin')," +
                "(6, 'OUT', 15, 'Retail Order #SO-9215', DATE_SUB(NOW(), INTERVAL 6 HOUR), 'admin')," +
                "(7, 'OUT', 10, 'Customer Orders', DATE_SUB(NOW(), INTERVAL 4 HOUR), 'admin')," +
                "(10, 'OUT', 8, 'Upgrade Request', DATE_SUB(NOW(), INTERVAL 2 HOUR), 'admin')," +
                "(14, 'OUT', 16, 'Sales Order #SO-9340', DATE_SUB(NOW(), INTERVAL 1 HOUR), 'admin')," +
                "(16, 'OUT', 12, 'Repair Center', DATE_SUB(NOW(), INTERVAL 30 MINUTE), 'admin')");

        stmt.executeUpdate("INSERT INTO purchase_orders (po_number, supplier_id, product_id, quantity, unit_price, total_amount, order_date, expected_delivery, status) VALUES " +
                "('PO-2026-001', 2, 6, 60, 499.00, 29940.00, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'Pending')," +
                "('PO-2026-002', 2, 3, 50, 1250.00, 62500.00, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'Pending')," +
                "('PO-2026-003', 1, 2, 40, 3450.00, 138000.00, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'Pending')");
    }
}
