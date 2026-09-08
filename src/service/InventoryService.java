package service;

import dao.ProductDAO;
import dao.SupplierDAO;
import model.Product;
import util.ValidationUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryService {

    private final ProductDAO productDAO = new ProductDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }

    public List<Product> searchAndFilterProducts(String query, String category, String status) {
        List<Product> products = productDAO.getAllProducts();

        return products.stream().filter(p -> {
            boolean matchQuery = true;
            if (ValidationUtil.isNotEmpty(query)) {
                String q = query.toLowerCase().trim();
                matchQuery = (p.getName() != null && p.getName().toLowerCase().contains(q)) ||
                             (p.getProductCode() != null && p.getProductCode().toLowerCase().contains(q)) ||
                             (p.getCategory() != null && p.getCategory().toLowerCase().contains(q)) ||
                             (p.getSupplierName() != null && p.getSupplierName().toLowerCase().contains(q));
            }

            boolean matchCategory = true;
            if (ValidationUtil.isNotEmpty(category) && !"ALL".equalsIgnoreCase(category)) {
                matchCategory = p.getCategory() != null && p.getCategory().equalsIgnoreCase(category.trim());
            }

            boolean matchStatus = true;
            if (ValidationUtil.isNotEmpty(status) && !"ALL".equalsIgnoreCase(status)) {
                matchStatus = p.getStatus() != null && p.getStatus().equalsIgnoreCase(status.trim());
            }

            return matchQuery && matchCategory && matchStatus;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> addProduct(Product product) {
        Map<String, Object> result = new HashMap<>();

        // Validation
        if (!ValidationUtil.isNotEmpty(product.getName())) {
            result.put("success", false);
            result.put("message", "Product name is required.");
            return result;
        }
        if (!ValidationUtil.isNotEmpty(product.getProductCode())) {
            result.put("success", false);
            result.put("message", "Product code is required.");
            return result;
        }
        if (!ValidationUtil.isValidPrice(product.getUnitPrice())) {
            result.put("success", false);
            result.put("message", "Unit price must be greater than 0.");
            return result;
        }
        if (product.getCurrentStock() < 0) {
            result.put("success", false);
            result.put("message", "Current stock cannot be negative.");
            return result;
        }

        // Duplicate code check
        Product existing = productDAO.getProductByCode(product.getProductCode());
        if (existing != null) {
            result.put("success", false);
            result.put("message", "Product code '" + product.getProductCode() + "' already exists.");
            return result;
        }

        boolean saved = productDAO.insertProduct(product);
        if (saved) {
            result.put("success", true);
            result.put("message", "Product added successfully.");
            result.put("product", product);
        } else {
            result.put("success", false);
            result.put("message", "Unable to save product. Please try again.");
        }
        return result;
    }

    public Map<String, Object> updateProduct(Product product) {
        Map<String, Object> result = new HashMap<>();

        if (product.getId() <= 0) {
            result.put("success", false);
            result.put("message", "Invalid product ID.");
            return result;
        }
        if (!ValidationUtil.isNotEmpty(product.getName())) {
            result.put("success", false);
            result.put("message", "Product name is required.");
            return result;
        }
        if (!ValidationUtil.isValidPrice(product.getUnitPrice())) {
            result.put("success", false);
            result.put("message", "Unit price must be greater than 0.");
            return result;
        }
        if (product.getCurrentStock() < 0) {
            result.put("success", false);
            result.put("message", "Current stock cannot be negative.");
            return result;
        }

        // Duplicate code check on other products
        Product existing = productDAO.getProductByCode(product.getProductCode());
        if (existing != null && existing.getId() != product.getId()) {
            result.put("success", false);
            result.put("message", "Product code '" + product.getProductCode() + "' is already in use.");
            return result;
        }

        boolean updated = productDAO.updateProduct(product);
        if (updated) {
            result.put("success", true);
            result.put("message", "Product updated successfully.");
            result.put("product", product);
        } else {
            result.put("success", false);
            result.put("message", "Unable to update product. Please try again.");
        }
        return result;
    }

    public Map<String, Object> deleteProduct(int id) {
        Map<String, Object> result = new HashMap<>();
        boolean deleted = productDAO.deleteProduct(id);
        if (deleted) {
            result.put("success", true);
            result.put("message", "Product deleted successfully.");
        } else {
            result.put("success", false);
            result.put("message", "Unable to delete product. Please try again.");
        }
        return result;
    }

    public List<Product> getLowStockAlerts() {
        return productDAO.getLowStockProducts();
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        int totalProducts = productDAO.getTotalProductsCount();
        int lowStock = productDAO.getLowStockCount();
        int outOfStock = productDAO.getOutOfStockCount();
        int totalSuppliers = supplierDAO.getTotalSuppliersCount();
        BigDecimal totalValue = productDAO.getTotalInventoryValue();

        stats.put("totalProducts", totalProducts);
        stats.put("lowStock", lowStock);
        stats.put("outOfStock", outOfStock);
        stats.put("totalSuppliers", totalSuppliers);
        stats.put("inventoryValue", totalValue);
        return stats;
    }
}
