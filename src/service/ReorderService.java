package service;

import dao.ProductDAO;
import dao.PurchaseOrderDAO;
import dao.StockDAO;
import dao.SupplierDAO;
import model.Product;
import model.PurchaseOrder;
import model.ReorderRecommendation;
import model.Supplier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReorderService {

    private final ProductDAO productDAO = new ProductDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final StockDAO stockDAO = new StockDAO();
    private final PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();

    private static final int ANALYSIS_PERIOD_DAYS = 30;

    /**
     * Generates predictive reorder analysis and recommendations for all inventory items.
     */
    public List<ReorderRecommendation> generateReorderRecommendations() {
        List<Product> products = productDAO.getAllProducts();
        List<ReorderRecommendation> recommendations = new ArrayList<>();

        for (Product product : products) {
            ReorderRecommendation rec = new ReorderRecommendation();
            rec.setProductId(product.getId());
            rec.setProductCode(product.getProductCode());
            rec.setProductName(product.getName());
            rec.setCategory(product.getCategory());
            rec.setCurrentStock(product.getCurrentStock());
            rec.setReorderLevel(product.getMinimumStock());
            rec.setSupplierId(product.getSupplierId() != null ? product.getSupplierId() : 0);
            rec.setSupplierName(product.getSupplierName() != null ? product.getSupplierName() : "No Supplier Assigned");
            rec.setUnitPrice(product.getUnitPrice());

            // 1. Fetch Lead Time from Supplier (default 3 days)
            int leadTime = 3;
            if (product.getSupplierId() != null && product.getSupplierId() > 0) {
                Supplier sup = supplierDAO.getSupplierById(product.getSupplierId());
                if (sup != null && sup.getLeadTime() > 0) {
                    leadTime = sup.getLeadTime();
                }
            }
            rec.setLeadTime(leadTime);

            // 2. Calculate Average Daily Demand from stock-out transactions
            int totalStockOut = stockDAO.getTotalStockOutQuantity(product.getId(), ANALYSIS_PERIOD_DAYS);
            double dailyDemand;
            if (totalStockOut > 0) {
                dailyDemand = (double) totalStockOut / (double) ANALYSIS_PERIOD_DAYS;
            } else {
                // Baseline heuristic if no transaction history yet
                dailyDemand = Math.max(1.0, (double) product.getMinimumStock() / 10.0);
            }
            // Round daily demand to 1 decimal place
            dailyDemand = Math.round(dailyDemand * 10.0) / 10.0;
            rec.setDailyDemand(dailyDemand);

            // 3. Calculate Safety Stock (buffer)
            int safetyStock = Math.max(2, (int) Math.ceil(product.getMinimumStock() * 0.4));
            rec.setSafetyStock(safetyStock);

            // 4. Calculate Reorder Point = (Average Daily Demand * Lead Time) + Safety Stock
            int reorderPoint = (int) Math.ceil((dailyDemand * leadTime) + safetyStock);
            // Reorder point should also consider the user-configured minimum stock threshold
            reorderPoint = Math.max(reorderPoint, product.getMinimumStock());
            rec.setReorderPoint(reorderPoint);

            // 5. Determine Reorder Urgency Status & Suggested Quantity
            int currentStock = product.getCurrentStock();
            if (currentStock <= 0) {
                rec.setUrgencyStatus("Critical");
                rec.setReorderRequired(true);
                int suggested = Math.max(product.getReorderQuantity(), product.getMaximumStock() - currentStock);
                rec.setSuggestedQuantity(suggested > 0 ? suggested : 30);
            } else if (currentStock <= safetyStock) {
                rec.setUrgencyStatus("Critical");
                rec.setReorderRequired(true);
                int suggested = Math.max(product.getReorderQuantity(), product.getMaximumStock() - currentStock);
                rec.setSuggestedQuantity(suggested > 0 ? suggested : 30);
            } else if (currentStock <= reorderPoint) {
                rec.setUrgencyStatus("Low Stock");
                rec.setReorderRequired(true);
                int suggested = Math.max(product.getReorderQuantity(), product.getMaximumStock() - currentStock);
                rec.setSuggestedQuantity(suggested > 0 ? suggested : 25);
            } else {
                rec.setUrgencyStatus("Sufficient");
                rec.setReorderRequired(false);
                rec.setSuggestedQuantity(0);
            }

            // Calculate estimated total cost
            if (rec.isReorderRequired() && rec.getUnitPrice() != null) {
                rec.setEstimatedCost(rec.getUnitPrice().multiply(BigDecimal.valueOf(rec.getSuggestedQuantity())));
            } else {
                rec.setEstimatedCost(BigDecimal.ZERO);
            }

            recommendations.add(rec);
        }

        // Sort: Critical first, then Low Stock, then Sufficient
        recommendations.sort((a, b) -> {
            int scoreA = "Critical".equals(a.getUrgencyStatus()) ? 0 : ("Low Stock".equals(a.getUrgencyStatus()) ? 1 : 2);
            int scoreB = "Critical".equals(b.getUrgencyStatus()) ? 0 : ("Low Stock".equals(b.getUrgencyStatus()) ? 1 : 2);
            if (scoreA != scoreB) return Integer.compare(scoreA, scoreB);
            return Integer.compare(a.getCurrentStock(), b.getCurrentStock());
        });

        return recommendations;
    }

    /**
     * Creates a Purchase Order from a reorder recommendation or custom PO form.
     */
    public Map<String, Object> createPurchaseOrder(int supplierId, int productId, int quantity, BigDecimal unitPrice) {
        Map<String, Object> result = new HashMap<>();

        if (supplierId <= 0) {
            result.put("success", false);
            result.put("message", "A valid supplier is required for reordering.");
            return result;
        }
        if (productId <= 0) {
            result.put("success", false);
            result.put("message", "A valid product is required.");
            return result;
        }
        if (quantity <= 0) {
            result.put("success", false);
            result.put("message", "Reorder quantity must be greater than 0.");
            return result;
        }

        Product product = productDAO.getProductById(productId);
        if (product == null) {
            result.put("success", false);
            result.put("message", "Product not found.");
            return result;
        }

        Supplier supplier = supplierDAO.getSupplierById(supplierId);
        if (supplier == null) {
            result.put("success", false);
            result.put("message", "Supplier not found.");
            return result;
        }

        BigDecimal price = (unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) > 0) ? unitPrice : product.getUnitPrice();
        BigDecimal total = price.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);

        // Generate PO Number
        String poNumber = "PO-" + LocalDate.now().getYear() + "-" + String.format("%04d", (int)(Math.random() * 9000) + 1000);

        int leadDays = supplier.getLeadTime() > 0 ? supplier.getLeadTime() : 3;
        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(leadDays);

        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber(poNumber);
        po.setSupplierId(supplierId);
        po.setProductId(productId);
        po.setQuantity(quantity);
        po.setUnitPrice(price);
        po.setTotalAmount(total);
        po.setExpectedDelivery(Date.valueOf(expectedDeliveryDate));
        po.setStatus("Pending");

        boolean created = purchaseOrderDAO.createPurchaseOrder(po);
        if (created) {
            result.put("success", true);
            result.put("message", "Purchase Order #" + poNumber + " created successfully for " + quantity + " units.");
            result.put("purchaseOrder", po);
        } else {
            result.put("success", false);
            result.put("message", "Unable to create Purchase Order. Please try again.");
        }

        return result;
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderDAO.getAllPurchaseOrders();
    }

    /**
     * AI Simulation Engine: Simulates market demand spikes, supplier lead-time delays,
     * and safety buffer adjustments to calculate dynamic risk scores and projected stockout days.
     */
    public List<Map<String, Object>> simulateReorderScenarios(double demandMultiplier, int leadTimeDelay, double safetyBufferFactor) {
        List<Product> products = productDAO.getAllProducts();
        List<Map<String, Object>> simulationResults = new ArrayList<>();

        if (demandMultiplier <= 0) demandMultiplier = 1.0;
        if (safetyBufferFactor <= 0) safetyBufferFactor = 1.0;

        for (Product product : products) {
            Map<String, Object> item = new HashMap<>();
            item.put("productId", product.getId());
            item.put("productCode", product.getProductCode());
            item.put("productName", product.getName());
            item.put("category", product.getCategory());
            item.put("currentStock", product.getCurrentStock());
            item.put("unitPrice", product.getUnitPrice());
            item.put("supplierName", product.getSupplierName() != null ? product.getSupplierName() : "General Supplier");

            int baseLeadTime = 3;
            if (product.getSupplierId() != null && product.getSupplierId() > 0) {
                Supplier sup = supplierDAO.getSupplierById(product.getSupplierId());
                if (sup != null && sup.getLeadTime() > 0) {
                    baseLeadTime = sup.getLeadTime();
                }
            }
            int simulatedLeadTime = Math.max(1, baseLeadTime + leadTimeDelay);

            int totalStockOut = stockDAO.getTotalStockOutQuantity(product.getId(), ANALYSIS_PERIOD_DAYS);
            double baseDailyDemand = totalStockOut > 0 ? ((double) totalStockOut / ANALYSIS_PERIOD_DAYS) : Math.max(1.0, (double) product.getMinimumStock() / 10.0);
            double simulatedDailyDemand = Math.round(baseDailyDemand * demandMultiplier * 10.0) / 10.0;

            int simulatedSafetyStock = (int) Math.ceil(product.getMinimumStock() * 0.4 * safetyBufferFactor);
            simulatedSafetyStock = Math.max(1, simulatedSafetyStock);

            int simulatedROP = (int) Math.ceil((simulatedDailyDemand * simulatedLeadTime) + simulatedSafetyStock);
            simulatedROP = Math.max(simulatedROP, product.getMinimumStock());

            double daysUntilDepletion = simulatedDailyDemand > 0 ? (double) product.getCurrentStock() / simulatedDailyDemand : 999.0;
            daysUntilDepletion = Math.round(daysUntilDepletion * 10.0) / 10.0;

            // Risk index 0-100
            int riskScore = 0;
            String urgency;
            if (product.getCurrentStock() <= 0) {
                riskScore = 100;
                urgency = "Stockout";
            } else if (daysUntilDepletion <= simulatedLeadTime) {
                riskScore = Math.min(99, (int) (85 + (15 * (1.0 - (daysUntilDepletion / simulatedLeadTime)))));
                urgency = "Critical Risk";
            } else if (product.getCurrentStock() <= simulatedROP) {
                riskScore = Math.min(80, (int) (50 + (30 * (1.0 - (daysUntilDepletion / (simulatedLeadTime * 2.0))))));
                urgency = "High Warning";
            } else {
                riskScore = Math.max(5, (int) (30 * (1.0 - Math.min(1.0, daysUntilDepletion / 30.0))));
                urgency = "Stable Buffer";
            }

            int suggestedQty = Math.max(product.getReorderQuantity(), (int) (product.getMaximumStock() - product.getCurrentStock() + (simulatedDailyDemand * simulatedLeadTime)));

            item.put("baseDailyDemand", Math.round(baseDailyDemand * 10.0) / 10.0);
            item.put("simulatedDailyDemand", simulatedDailyDemand);
            item.put("baseLeadTime", baseLeadTime);
            item.put("simulatedLeadTime", simulatedLeadTime);
            item.put("simulatedSafetyStock", simulatedSafetyStock);
            item.put("simulatedROP", simulatedROP);
            item.put("daysUntilDepletion", daysUntilDepletion);
            item.put("riskScore", riskScore);
            item.put("urgency", urgency);
            item.put("suggestedQuantity", suggestedQty);
            item.put("estimatedCost", product.getUnitPrice() != null ? product.getUnitPrice().multiply(BigDecimal.valueOf(suggestedQty)) : BigDecimal.ZERO);

            simulationResults.add(item);
        }

        simulationResults.sort((a, b) -> Integer.compare((Integer) b.get("riskScore"), (Integer) a.get("riskScore")));
        return simulationResults;
    }
}
