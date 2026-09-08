package dao;

import model.Product;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setProductCode(rs.getString("product_code"));
        p.setName(rs.getString("name"));
        p.setCategory(rs.getString("category"));
        int supId = rs.getInt("supplier_id");
        if (!rs.wasNull()) {
            p.setSupplierId(supId);
        }
        try {
            p.setSupplierName(rs.getString("supplier_name"));
        } catch (SQLException ignored) {}
        p.setUnitPrice(rs.getBigDecimal("unit_price"));
        p.setCurrentStock(rs.getInt("current_stock"));
        p.setMinimumStock(rs.getInt("minimum_stock"));
        p.setMaximumStock(rs.getInt("maximum_stock"));
        p.setReorderQuantity(rs.getInt("reorder_quantity"));
        try {
            p.setCreatedAt(rs.getTimestamp("created_at"));
        } catch (SQLException ignored) {}
        return p;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, s.name as supplier_name FROM products p " +
                     "LEFT JOIN suppliers s ON p.supplier_id = s.id " +
                     "ORDER BY p.id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in getAllProducts: " + e.getMessage());
        }
        return list;
    }

    public Product getProductById(int id) {
        String sql = "SELECT p.*, s.name as supplier_name FROM products p " +
                     "LEFT JOIN suppliers s ON p.supplier_id = s.id WHERE p.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in getProductById: " + e.getMessage());
        }
        return null;
    }

    public Product getProductByCode(String code) {
        String sql = "SELECT p.*, s.name as supplier_name FROM products p " +
                     "LEFT JOIN suppliers s ON p.supplier_id = s.id WHERE p.product_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in getProductByCode: " + e.getMessage());
        }
        return null;
    }

    public boolean insertProduct(Product p) {
        String sql = "INSERT INTO products (product_code, name, category, supplier_id, unit_price, current_stock, minimum_stock, maximum_stock, reorder_quantity) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getProductCode());
            ps.setString(2, p.getName());
            ps.setString(3, p.getCategory());
            if (p.getSupplierId() != null && p.getSupplierId() > 0) {
                ps.setInt(4, p.getSupplierId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setBigDecimal(5, p.getUnitPrice());
            ps.setInt(6, p.getCurrentStock());
            ps.setInt(7, p.getMinimumStock());
            ps.setInt(8, p.getMaximumStock());
            ps.setInt(9, p.getReorderQuantity());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        p.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in insertProduct: " + e.getMessage());
        }
        return false;
    }

    public boolean updateProduct(Product p) {
        String sql = "UPDATE products SET product_code = ?, name = ?, category = ?, supplier_id = ?, unit_price = ?, current_stock = ?, minimum_stock = ?, maximum_stock = ?, reorder_quantity = ? " +
                     "WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getProductCode());
            ps.setString(2, p.getName());
            ps.setString(3, p.getCategory());
            if (p.getSupplierId() != null && p.getSupplierId() > 0) {
                ps.setInt(4, p.getSupplierId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setBigDecimal(5, p.getUnitPrice());
            ps.setInt(6, p.getCurrentStock());
            ps.setInt(7, p.getMinimumStock());
            ps.setInt(8, p.getMaximumStock());
            ps.setInt(9, p.getReorderQuantity());
            ps.setInt(10, p.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in updateProduct: " + e.getMessage());
        }
        return false;
    }

    public boolean updateStock(int id, int newStock) {
        String sql = "UPDATE products SET current_stock = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in updateStock: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in deleteProduct: " + e.getMessage());
        }
        return false;
    }

    public List<Product> getLowStockProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, s.name as supplier_name FROM products p " +
                     "LEFT JOIN suppliers s ON p.supplier_id = s.id " +
                     "WHERE p.current_stock <= p.minimum_stock " +
                     "ORDER BY p.current_stock ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in getLowStockProducts: " + e.getMessage());
        }
        return list;
    }

    public int getTotalProductsCount() {
        String sql = "SELECT count(*) FROM products";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in getTotalProductsCount: " + e.getMessage());
        }
        return 0;
    }

    public int getLowStockCount() {
        String sql = "SELECT count(*) FROM products WHERE current_stock <= minimum_stock AND current_stock > 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in getLowStockCount: " + e.getMessage());
        }
        return 0;
    }

    public int getOutOfStockCount() {
        String sql = "SELECT count(*) FROM products WHERE current_stock <= 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in getOutOfStockCount: " + e.getMessage());
        }
        return 0;
    }

    public BigDecimal getTotalInventoryValue() {
        String sql = "SELECT SUM(unit_price * current_stock) FROM products";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal val = rs.getBigDecimal(1);
                return val != null ? val : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            System.err.println("[ProductDAO] Error in getTotalInventoryValue: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
}
