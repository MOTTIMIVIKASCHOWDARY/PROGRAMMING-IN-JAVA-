package service;

import dao.SupplierDAO;
import model.Supplier;
import util.ValidationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierService {

    private final SupplierDAO supplierDAO = new SupplierDAO();

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    public Supplier getSupplierById(int id) {
        return supplierDAO.getSupplierById(id);
    }

    public Map<String, Object> addSupplier(Supplier supplier) {
        Map<String, Object> result = new HashMap<>();

        if (!ValidationUtil.isNotEmpty(supplier.getName())) {
            result.put("success", false);
            result.put("message", "Supplier name is required.");
            return result;
        }
        if (!ValidationUtil.isNotEmpty(supplier.getSupplierCode())) {
            result.put("success", false);
            result.put("message", "Supplier code is required.");
            return result;
        }
        if (!ValidationUtil.isValidEmail(supplier.getEmail())) {
            result.put("success", false);
            result.put("message", "Please provide a valid email address.");
            return result;
        }
        if (supplier.getLeadTime() <= 0) {
            supplier.setLeadTime(3); // default lead time
        }

        Supplier existing = supplierDAO.getSupplierByCode(supplier.getSupplierCode());
        if (existing != null) {
            result.put("success", false);
            result.put("message", "Supplier code '" + supplier.getSupplierCode() + "' already exists.");
            return result;
        }

        boolean saved = supplierDAO.insertSupplier(supplier);
        if (saved) {
            result.put("success", true);
            result.put("message", "Supplier added successfully.");
            result.put("supplier", supplier);
        } else {
            result.put("success", false);
            result.put("message", "Unable to save supplier. Please try again.");
        }
        return result;
    }

    public Map<String, Object> updateSupplier(Supplier supplier) {
        Map<String, Object> result = new HashMap<>();

        if (supplier.getId() <= 0) {
            result.put("success", false);
            result.put("message", "Invalid supplier ID.");
            return result;
        }
        if (!ValidationUtil.isNotEmpty(supplier.getName())) {
            result.put("success", false);
            result.put("message", "Supplier name is required.");
            return result;
        }
        if (!ValidationUtil.isValidEmail(supplier.getEmail())) {
            result.put("success", false);
            result.put("message", "Please provide a valid email address.");
            return result;
        }

        Supplier existing = supplierDAO.getSupplierByCode(supplier.getSupplierCode());
        if (existing != null && existing.getId() != supplier.getId()) {
            result.put("success", false);
            result.put("message", "Supplier code '" + supplier.getSupplierCode() + "' is already in use.");
            return result;
        }

        boolean updated = supplierDAO.updateSupplier(supplier);
        if (updated) {
            result.put("success", true);
            result.put("message", "Supplier updated successfully.");
            result.put("supplier", supplier);
        } else {
            result.put("success", false);
            result.put("message", "Unable to update supplier. Please try again.");
        }
        return result;
    }

    public Map<String, Object> deleteSupplier(int id) {
        Map<String, Object> result = new HashMap<>();
        boolean deleted = supplierDAO.deleteSupplier(id);
        if (deleted) {
            result.put("success", true);
            result.put("message", "Supplier deleted successfully.");
        } else {
            result.put("success", false);
            result.put("message", "Unable to delete supplier. Please ensure no products are linked.");
        }
        return result;
    }
}
