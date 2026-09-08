package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Product;
import model.StockTransaction;
import model.Supplier;
import service.InventoryService;
import service.StockService;
import service.SupplierService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportController extends BaseController implements HttpHandler {

    private final InventoryService inventoryService = new InventoryService();
    private final SupplierService supplierService = new SupplierService();
    private final StockService stockService = new StockService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            handleOptions(exchange);
            return;
        }

        String path = exchange.getRequestURI().getPath();

        try {
            if (path.endsWith("/inventory") && "GET".equalsIgnoreCase(method)) {
                handleInventoryReport(exchange);
            } else if (path.endsWith("/low-stock") && "GET".equalsIgnoreCase(method)) {
                handleLowStockReport(exchange);
            } else if (path.endsWith("/suppliers") && "GET".equalsIgnoreCase(method)) {
                handleSupplierReport(exchange);
            } else if (path.endsWith("/stock-movement") && "GET".equalsIgnoreCase(method)) {
                handleStockMovementReport(exchange);
            } else if (path.endsWith("/export-csv") && "GET".equalsIgnoreCase(method)) {
                handleExportCsv(exchange);
            } else {
                sendErrorResponse(exchange, 404, "Endpoint not found: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void handleInventoryReport(HttpExchange exchange) throws IOException {
        List<Product> list = inventoryService.getAllProducts();
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("reportName", "Inventory Valuation & Status Report");
        resp.put("generatedAt", new java.util.Date().toString());
        resp.put("data", list);
        sendJsonResponse(exchange, 200, resp);
    }

    private void handleLowStockReport(HttpExchange exchange) throws IOException {
        List<Product> list = inventoryService.getLowStockAlerts();
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("reportName", "Low Stock & Reorder Level Report");
        resp.put("generatedAt", new java.util.Date().toString());
        resp.put("data", list);
        sendJsonResponse(exchange, 200, resp);
    }

    private void handleSupplierReport(HttpExchange exchange) throws IOException {
        List<Supplier> list = supplierService.getAllSuppliers();
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("reportName", "Supplier Directory & Lead Time Report");
        resp.put("generatedAt", new java.util.Date().toString());
        resp.put("data", list);
        sendJsonResponse(exchange, 200, resp);
    }

    private void handleStockMovementReport(HttpExchange exchange) throws IOException {
        List<StockTransaction> list = stockService.getAllTransactions();
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("reportName", "Stock In & Stock Out Movement Audit Log");
        resp.put("generatedAt", new java.util.Date().toString());
        resp.put("data", list);
        sendJsonResponse(exchange, 200, resp);
    }

    private void handleExportCsv(HttpExchange exchange) throws IOException {
        String type = getQueryParam(exchange, "type", "inventory").toLowerCase();
        StringBuilder csv = new StringBuilder();
        String filename = "simr_report_" + type + ".csv";

        switch (type) {
            case "low-stock":
                filename = "simr_low_stock_report.csv";
                csv.append("Product Code,Product Name,Category,Supplier,Unit Price,Current Stock,Reorder Level,Status\n");
                for (Product p : inventoryService.getLowStockAlerts()) {
                    csv.append(escapeCsv(p.getProductCode())).append(",")
                       .append(escapeCsv(p.getName())).append(",")
                       .append(escapeCsv(p.getCategory())).append(",")
                       .append(escapeCsv(p.getSupplierName())).append(",")
                       .append(p.getUnitPrice()).append(",")
                       .append(p.getCurrentStock()).append(",")
                       .append(p.getMinimumStock()).append(",")
                       .append(escapeCsv(p.getStatus())).append("\n");
                }
                break;

            case "suppliers":
                filename = "simr_suppliers_report.csv";
                csv.append("Supplier Code,Supplier Name,Contact Person,Phone,Email,Lead Time (Days),Status,Products Supplied\n");
                for (Supplier s : supplierService.getAllSuppliers()) {
                    csv.append(escapeCsv(s.getSupplierCode())).append(",")
                       .append(escapeCsv(s.getName())).append(",")
                       .append(escapeCsv(s.getContactPerson())).append(",")
                       .append(escapeCsv(s.getPhone())).append(",")
                       .append(escapeCsv(s.getEmail())).append(",")
                       .append(s.getLeadTime()).append(",")
                       .append(escapeCsv(s.getStatus())).append(",")
                       .append(s.getProductsCount()).append("\n");
                }
                break;

            case "stock-movement":
                filename = "simr_stock_movement_report.csv";
                csv.append("Transaction ID,Product Code,Product Name,Type,Quantity,Reason,Performed By,Date\n");
                for (StockTransaction t : stockService.getAllTransactions()) {
                    csv.append(t.getId()).append(",")
                       .append(escapeCsv(t.getProductCode())).append(",")
                       .append(escapeCsv(t.getProductName())).append(",")
                       .append(escapeCsv(t.getType())).append(",")
                       .append(t.getQuantity()).append(",")
                       .append(escapeCsv(t.getReason())).append(",")
                       .append(escapeCsv(t.getPerformedBy())).append(",")
                       .append(t.getTransactionDate()).append("\n");
                }
                break;

            case "inventory":
            default:
                filename = "simr_inventory_report.csv";
                csv.append("Product Code,Product Name,Category,Supplier,Unit Price,Current Stock,Reorder Level,Max Stock,Total Value,Status\n");
                for (Product p : inventoryService.getAllProducts()) {
                    csv.append(escapeCsv(p.getProductCode())).append(",")
                       .append(escapeCsv(p.getName())).append(",")
                       .append(escapeCsv(p.getCategory())).append(",")
                       .append(escapeCsv(p.getSupplierName())).append(",")
                       .append(p.getUnitPrice()).append(",")
                       .append(p.getCurrentStock()).append(",")
                       .append(p.getMinimumStock()).append(",")
                       .append(p.getMaximumStock()).append(",")
                       .append(p.getTotalValue()).append(",")
                       .append(escapeCsv(p.getStatus())).append("\n");
                }
                break;
        }

        sendCsvResponse(exchange, filename, csv.toString());
    }

    private String escapeCsv(String str) {
        if (str == null) return "\"\"";
        return "\"" + str.replace("\"", "\"\"") + "\"";
    }
}
