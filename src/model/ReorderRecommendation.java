package model;

import java.math.BigDecimal;

public class ReorderRecommendation {
    private int productId;
    private String productCode;
    private String productName;
    private String category;
    private int currentStock;
    private int reorderLevel;
    private double dailyDemand;
    private int leadTime;
    private int safetyStock;
    private int reorderPoint;
    private int suggestedQuantity;
    private int supplierId;
    private String supplierName;
    private BigDecimal unitPrice;
    private BigDecimal estimatedCost;
    private String urgencyStatus; // 'Critical', 'Low Stock', 'Sufficient'
    private boolean reorderRequired;

    public ReorderRecommendation() {}

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public double getDailyDemand() {
        return dailyDemand;
    }

    public void setDailyDemand(double dailyDemand) {
        this.dailyDemand = dailyDemand;
    }

    public int getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(int leadTime) {
        this.leadTime = leadTime;
    }

    public int getSafetyStock() {
        return safetyStock;
    }

    public void setSafetyStock(int safetyStock) {
        this.safetyStock = safetyStock;
    }

    public int getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(int reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public int getSuggestedQuantity() {
        return suggestedQuantity;
    }

    public void setSuggestedQuantity(int suggestedQuantity) {
        this.suggestedQuantity = suggestedQuantity;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public String getUrgencyStatus() {
        return urgencyStatus;
    }

    public void setUrgencyStatus(String urgencyStatus) {
        this.urgencyStatus = urgencyStatus;
    }

    public boolean isReorderRequired() {
        return reorderRequired;
    }

    public void setReorderRequired(boolean reorderRequired) {
        this.reorderRequired = reorderRequired;
    }
}
