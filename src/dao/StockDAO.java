package dao;

import model.StockTransaction;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockDAO {

    private StockTransaction mapRow(ResultSet rs) throws SQLException {
        StockTransaction t = new StockTransaction();
        t.setId(rs.getInt("id"));
        t.setProductId(rs.getInt("product_id"));
        try {
            t.setProductName(rs.getString("product_name"));
            t.setProductCode(rs.getString("product_code"));
        } catch (SQLException ignored) {}
        t.setType(rs.getString("type"));
        t.setQuantity(rs.getInt("quantity"));
        t.setReason(rs.getString("reason"));
        t.setTransactionDate(rs.getTimestamp("transaction_date"));
        t.setPerformedBy(rs.getString("performed_by"));
        return t;
    }

    public boolean recordTransaction(StockTransaction t) {
        String sql = "INSERT INTO stock_transactions (product_id, type, quantity, reason, performed_by) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getProductId());
            ps.setString(2, t.getType());
            ps.setInt(3, t.getQuantity());
            ps.setString(4, t.getReason());
            ps.setString(5, t.getPerformedBy() != null ? t.getPerformedBy() : "admin");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        t.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[StockDAO] Error in recordTransaction: " + e.getMessage());
        }
        return false;
    }

    public List<StockTransaction> getRecentTransactions(int limit) {
        List<StockTransaction> list = new ArrayList<>();
        String sql = "SELECT st.*, p.name as product_name, p.product_code as product_code " +
                     "FROM stock_transactions st " +
                     "JOIN products p ON st.product_id = p.id " +
                     "ORDER BY st.transaction_date DESC, st.id DESC " +
                     "LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit > 0 ? limit : 20);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[StockDAO] Error in getRecentTransactions: " + e.getMessage());
        }
        return list;
    }

    public List<StockTransaction> getAllTransactions() {
        List<StockTransaction> list = new ArrayList<>();
        String sql = "SELECT st.*, p.name as product_name, p.product_code as product_code " +
                     "FROM stock_transactions st " +
                     "JOIN products p ON st.product_id = p.id " +
                     "ORDER BY st.transaction_date DESC, st.id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[StockDAO] Error in getAllTransactions: " + e.getMessage());
        }
        return list;
    }

    /**
     * Calculates the total stock-out quantity for a given product in the last N days.
     */
    public int getTotalStockOutQuantity(int productId, int days) {
        String isSqlite = "SQLITE".equals(DBConnection.getDatabaseType()) ? "true" : "false";
        String sql;
        if ("true".equals(isSqlite)) {
            sql = "SELECT COALESCE(SUM(quantity), 0) FROM stock_transactions " +
                  "WHERE product_id = ? AND type = 'OUT' AND transaction_date >= datetime('now', '-' || ? || ' days')";
        } else {
            sql = "SELECT COALESCE(SUM(quantity), 0) FROM stock_transactions " +
                  "WHERE product_id = ? AND type = 'OUT' AND transaction_date >= DATE_SUB(NOW(), INTERVAL ? DAY)";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, days);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[StockDAO] Error in getTotalStockOutQuantity: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Returns total Stock IN count and total Stock OUT count overall.
     */
    public Map<String, Integer> getStockMovementTotals() {
        Map<String, Integer> totals = new HashMap<>();
        totals.put("stockIn", 0);
        totals.put("stockOut", 0);
        String sql = "SELECT type, COALESCE(SUM(quantity), 0) as total FROM stock_transactions GROUP BY type";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String type = rs.getString("type");
                int sum = rs.getInt("total");
                if ("IN".equalsIgnoreCase(type)) {
                    totals.put("stockIn", sum);
                } else if ("OUT".equalsIgnoreCase(type)) {
                    totals.put("stockOut", sum);
                }
            }
        } catch (SQLException e) {
            System.err.println("[StockDAO] Error in getStockMovementTotals: " + e.getMessage());
        }
        return totals;
    }

    /**
     * Returns daily stock in and stock out movements for chart display over the past 7/14 days.
     */
    public List<Map<String, Object>> getDailyStockMovements(int days) {
        List<Map<String, Object>> list = new ArrayList<>();
        boolean isSqlite = "SQLITE".equals(DBConnection.getDatabaseType());
        String dateCol = isSqlite ? "substr(transaction_date, 1, 10)" : "DATE_FORMAT(transaction_date, '%Y-%m-%d')";
        String dateFilter = isSqlite ?
                "transaction_date >= datetime('now', '-' || ? || ' days')" :
                "transaction_date >= DATE_SUB(NOW(), INTERVAL ? DAY)";

        String sql = "SELECT " + dateCol + " as tx_date, type, SUM(quantity) as total_qty " +
                     "FROM stock_transactions " +
                     "WHERE " + dateFilter + " " +
                     "GROUP BY tx_date, type " +
                     "ORDER BY tx_date ASC";

        Map<String, Map<String, Object>> dateMap = new HashMap<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, days);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String d = rs.getString("tx_date");
                    String type = rs.getString("type");
                    int qty = rs.getInt("total_qty");

                    Map<String, Object> dayEntry = dateMap.computeIfAbsent(d, k -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("date", k);
                        map.put("stockIn", 0);
                        map.put("stockOut", 0);
                        return map;
                    });

                    if ("IN".equalsIgnoreCase(type)) {
                        dayEntry.put("stockIn", qty);
                    } else if ("OUT".equalsIgnoreCase(type)) {
                        dayEntry.put("stockOut", qty);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[StockDAO] Error in getDailyStockMovements: " + e.getMessage());
        }

        // Sort by date
        List<String> sortedKeys = new ArrayList<>(dateMap.keySet());
        java.util.Collections.sort(sortedKeys);
        for (String k : sortedKeys) {
            list.add(dateMap.get(k));
        }

        return list;
    }
}
