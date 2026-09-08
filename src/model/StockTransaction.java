package model;

import java.sql.Timestamp;

public class StockTransaction {
    private int id;
    private int productId;
    private String productName;
    private String productCode;
    private String type; // 'IN' or 'OUT'
    private int quantity;
    private String reason;
    private Timestamp transactionDate;
    private String performedBy;

    public StockTransaction() {}

    public StockTransaction(int id, int productId, String type, int quantity, String reason, Timestamp transactionDate, String performedBy) {
        this.id = id;
        this.productId = productId;
        this.type = type;
        this.quantity = quantity;
        this.reason = reason;
        this.transactionDate = transactionDate;
        this.performedBy = performedBy;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }
}
