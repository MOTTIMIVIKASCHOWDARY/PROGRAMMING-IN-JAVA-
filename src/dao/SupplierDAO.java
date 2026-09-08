package dao;

import model.Supplier;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    private Supplier mapRow(ResultSet rs) throws SQLException {
        Supplier s = new Supplier();
        s.setId(rs.getInt("id"));
        s.setSupplierCode(rs.getString("supplier_code"));
        s.setName(rs.getString("name"));
        s.setContactPerson(rs.getString("contact_person"));
        s.setPhone(rs.getString("phone"));
        s.setEmail(rs.getString("email"));
        s.setAddress(rs.getString("address"));
        s.setLeadTime(rs.getInt("lead_time"));
        s.setStatus(rs.getString("status"));
        try {
            s.setProductsCount(rs.getInt("products_count"));
        } catch (SQLException ignored) {}
        try {
            s.setCreatedAt(rs.getTimestamp("created_at"));
        } catch (SQLException ignored) {}
        return s;
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT s.*, (SELECT count(*) FROM products p WHERE p.supplier_id = s.id) as products_count " +
                     "FROM suppliers s ORDER BY s.id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] Error in getAllSuppliers: " + e.getMessage());
        }
        return list;
    }

    public Supplier getSupplierById(int id) {
        String sql = "SELECT s.*, (SELECT count(*) FROM products p WHERE p.supplier_id = s.id) as products_count " +
                     "FROM suppliers s WHERE s.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] Error in getSupplierById: " + e.getMessage());
        }
        return null;
    }

    public Supplier getSupplierByCode(String code) {
        String sql = "SELECT s.*, (SELECT count(*) FROM products p WHERE p.supplier_id = s.id) as products_count " +
                     "FROM suppliers s WHERE s.supplier_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] Error in getSupplierByCode: " + e.getMessage());
        }
        return null;
    }

    public boolean insertSupplier(Supplier s) {
        String sql = "INSERT INTO suppliers (supplier_code, name, contact_person, phone, email, address, lead_time, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, s.getSupplierCode());
            ps.setString(2, s.getName());
            ps.setString(3, s.getContactPerson());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getEmail());
            ps.setString(6, s.getAddress());
            ps.setInt(7, s.getLeadTime() > 0 ? s.getLeadTime() : 3);
            ps.setString(8, s.getStatus() != null ? s.getStatus() : "Active");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        s.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] Error in insertSupplier: " + e.getMessage());
        }
        return false;
    }

    public boolean updateSupplier(Supplier s) {
        String sql = "UPDATE suppliers SET supplier_code = ?, name = ?, contact_person = ?, phone = ?, email = ?, address = ?, lead_time = ?, status = ? " +
                     "WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getSupplierCode());
            ps.setString(2, s.getName());
            ps.setString(3, s.getContactPerson());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getEmail());
            ps.setString(6, s.getAddress());
            ps.setInt(7, s.getLeadTime() > 0 ? s.getLeadTime() : 3);
            ps.setString(8, s.getStatus() != null ? s.getStatus() : "Active");
            ps.setInt(9, s.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] Error in updateSupplier: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteSupplier(int id) {
        String sql = "DELETE FROM suppliers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] Error in deleteSupplier: " + e.getMessage());
        }
        return false;
    }

    public int getTotalSuppliersCount() {
        String sql = "SELECT count(*) FROM suppliers";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[SupplierDAO] Error in getTotalSuppliersCount: " + e.getMessage());
        }
        return 0;
    }
}
