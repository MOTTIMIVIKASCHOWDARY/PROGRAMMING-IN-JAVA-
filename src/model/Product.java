package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {
    private int id;
    private String productCode;
    private String name;
    private String category;
    private Integer supplierId;
    private String supplierName;
    private BigDecimal unitPrice;
    private int currentStock;
    private int minimumStock; // Reorder Level
    private int maximumStock;
    private int reorderQuantity;
    private Timestamp createdAt;

    public Product() {}

    public Product(int id, String productCode, String name, String category, Integer supplierId,
                   BigDecimal unitPrice, int currentStock, int minimumStock, int maximumStock, int reorderQuantity) {
        this.id = id;
        this.productCode = productCode;
        this.name = name;
        this.category = category;
        this.supplierId = supplierId;
        this.unitPrice = unitPrice;
        this.currentStock = currentStock;
        this.minimumStock = minimumStock;
        this.maximumStock = maximumStock;
        this.reorderQuantity = reorderQuantity;
    }

    public String getStatus() {
        if (currentStock <= 0) {
            return "Out of Stock";
        } else if (currentStock <= minimumStock / 2) {
            return "Critical";
        } else if (currentStock <= minimumStock) {
            return "Low Stock";
        }
        return "Normal";
    }

    public BigDecimal getTotalValue() {
        if (unitPrice == null) return BigDecimal.ZERO;
        return unitPrice.multiply(BigDecimal.valueOf(currentStock));
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
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

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    public int getMaximumStock() {
        return maximumStock;
    }

    public void setMaximumStock(int maximumStock) {
        this.maximumStock = maximumStock;
    }

    public int getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(int reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
