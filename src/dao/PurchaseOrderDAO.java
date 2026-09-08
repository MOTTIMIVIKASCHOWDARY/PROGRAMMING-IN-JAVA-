package dao;

import model.PurchaseOrder;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderDAO {

    private PurchaseOrder mapRow(ResultSet rs) throws SQLException {
        PurchaseOrder po = new PurchaseOrder();
        po.setId(rs.getInt("id"));
        po.setPoNumber(rs.getString("po_number"));
        po.setSupplierId(rs.getInt("supplier_id"));
        try {
            po.setSupplierName(rs.getString("supplier_name"));
        } catch (SQLException ignored) {}
        po.setProductId(rs.getInt("product_id"));
        try {
            po.setProductName(rs.getString("product_name"));
        } catch (SQLException ignored) {}
        po.setQuantity(rs.getInt("quantity"));
        po.setUnitPrice(rs.getBigDecimal("unit_price"));
        po.setTotalAmount(rs.getBigDecimal("total_amount"));
        try {
            po.setOrderDate(rs.getTimestamp("order_date"));
        } catch (SQLException ignored) {}
        try {
            po.setExpectedDelivery(rs.getDate("expected_delivery"));
        } catch (SQLException ignored) {}
        po.setStatus(rs.getString("status"));
        return po;
    }

    public boolean createPurchaseOrder(PurchaseOrder po) {
        String sql = "INSERT INTO purchase_orders (po_number, supplier_id, product_id, quantity, unit_price, total_amount, expected_delivery, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, po.getPoNumber());
            ps.setInt(2, po.getSupplierId());
            ps.setInt(3, po.getProductId());
            ps.setInt(4, po.getQuantity());
            ps.setBigDecimal(5, po.getUnitPrice());
            ps.setBigDecimal(6, po.getTotalAmount());
            if (po.getExpectedDelivery() != null) {
                ps.setDate(7, po.getExpectedDelivery());
            } else {
                ps.setNull(7, Types.DATE);
            }
            ps.setString(8, po.getStatus() != null ? po.getStatus() : "Pending");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        po.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[PurchaseOrderDAO] Error in createPurchaseOrder: " + e.getMessage());
        }
        return false;
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        List<PurchaseOrder> list = new ArrayList<>();
        String sql = "SELECT po.*, s.name as supplier_name, p.name as product_name " +
                     "FROM purchase_orders po " +
                     "JOIN suppliers s ON po.supplier_id = s.id " +
                     "JOIN products p ON po.product_id = p.id " +
                     "ORDER BY po.order_date DESC, po.id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[PurchaseOrderDAO] Error in getAllPurchaseOrders: " + e.getMessage());
        }
        return list;
    }

    public PurchaseOrder getPurchaseOrderById(int id) {
        String sql = "SELECT po.*, s.name as supplier_name, p.name as product_name " +
                     "FROM purchase_orders po " +
                     "JOIN suppliers s ON po.supplier_id = s.id " +
                     "JOIN products p ON po.product_id = p.id " +
                     "WHERE po.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[PurchaseOrderDAO] Error in getPurchaseOrderById: " + e.getMessage());
        }
        return null;
    }
}
