package service;

import dao.ProductDAO;
import dao.StockDAO;
import model.Product;
import model.StockTransaction;
import util.ValidationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockService {

    private final StockDAO stockDAO = new StockDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public synchronized Map<String, Object> addStock(int productId, int quantity, String reason, String performedBy) {
        Map<String, Object> result = new HashMap<>();

        if (productId <= 0) {
            result.put("success", false);
            result.put("message", "Please select a valid product.");
            return result;
        }
        if (quantity <= 0) {
            result.put("success", false);
            result.put("message", "Quantity to add must be greater than 0.");
            return result;
        }

        Product product = productDAO.getProductById(productId);
        if (product == null) {
            result.put("success", false);
            result.put("message", "Selected product not found in inventory.");
            return result;
        }

        int updatedStock = product.getCurrentStock() + quantity;
        boolean stockUpdated = productDAO.updateStock(productId, updatedStock);
        if (!stockUpdated) {
            result.put("success", false);
            result.put("message", "Failed to update product stock.");
            return result;
        }

        StockTransaction tx = new StockTransaction();
        tx.setProductId(productId);
        tx.setType("IN");
        tx.setQuantity(quantity);
        tx.setReason(ValidationUtil.isNotEmpty(reason) ? reason : "Stock Intake / Replenishment");
        tx.setPerformedBy(ValidationUtil.isNotEmpty(performedBy) ? performedBy : "admin");

        stockDAO.recordTransaction(tx);

        result.put("success", true);
        result.put("message", "Successfully added " + quantity + " units to " + product.getName() + ".");
        result.put("newStock", updatedStock);
        return result;
    }

    public synchronized Map<String, Object> removeStock(int productId, int quantity, String reason, String performedBy) {
        Map<String, Object> result = new HashMap<>();

        if (productId <= 0) {
            result.put("success", false);
            result.put("message", "Please select a valid product.");
            return result;
        }
        if (quantity <= 0) {
            result.put("success", false);
            result.put("message", "Quantity to remove must be greater than 0.");
            return result;
        }

        Product product = productDAO.getProductById(productId);
        if (product == null) {
            result.put("success", false);
            result.put("message", "Selected product not found in inventory.");
            return result;
        }

        if (product.getCurrentStock() < quantity) {
            result.put("success", false);
            result.put("message", "Insufficient stock! Current stock is only " + product.getCurrentStock() + " units.");
            return result;
        }

        int updatedStock = product.getCurrentStock() - quantity;
        boolean stockUpdated = productDAO.updateStock(productId, updatedStock);
        if (!stockUpdated) {
            result.put("success", false);
            result.put("message", "Failed to update product stock.");
            return result;
        }

        StockTransaction tx = new StockTransaction();
        tx.setProductId(productId);
        tx.setType("OUT");
        tx.setQuantity(quantity);
        tx.setReason(ValidationUtil.isNotEmpty(reason) ? reason : "Dispatched / Sold");
        tx.setPerformedBy(ValidationUtil.isNotEmpty(performedBy) ? performedBy : "admin");

        stockDAO.recordTransaction(tx);

        result.put("success", true);
        result.put("message", "Successfully issued " + quantity + " units from " + product.getName() + ".");
        result.put("newStock", updatedStock);
        return result;
    }

    public List<StockTransaction> getRecentTransactions(int limit) {
        return stockDAO.getRecentTransactions(limit);
    }

    public List<StockTransaction> getAllTransactions() {
        return stockDAO.getAllTransactions();
    }

    public Map<String, Object> getStockMovementChartData(int days) {
        Map<String, Object> data = new HashMap<>();
        data.put("totals", stockDAO.getStockMovementTotals());
        data.put("dailyMovements", stockDAO.getDailyStockMovements(days > 0 ? days : 14));
        return data;
    }
}
