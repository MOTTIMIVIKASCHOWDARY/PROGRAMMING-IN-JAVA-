package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Product;
import service.InventoryService;
import util.JsonUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController extends BaseController implements HttpHandler {

    private final InventoryService inventoryService = new InventoryService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            handleOptions(exchange);
            return;
        }

        String path = exchange.getRequestURI().getPath();

        try {
            if ("GET".equalsIgnoreCase(method)) {
                if (path.matches(".*/(products|inventory)/\\d+$")) {
                    int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                    handleGetById(exchange, id);
                } else {
                    handleList(exchange);
                }
            } else if ("POST".equalsIgnoreCase(method)) {
                handleCreate(exchange);
            } else if ("PUT".equalsIgnoreCase(method)) {
                handleUpdate(exchange);
            } else if ("DELETE".equalsIgnoreCase(method)) {
                int id = 0;
                if (path.matches(".*/(products|inventory)/\\d+$")) {
                    id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                } else {
                    id = Integer.parseInt(getQueryParam(exchange, "id", "0"));
                }
                handleDelete(exchange, id);
            } else {
                sendErrorResponse(exchange, 405, "Method not allowed: " + method);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void handleList(HttpExchange exchange) throws IOException {
        String query = getQueryParam(exchange, "query", "");
        String category = getQueryParam(exchange, "category", "");
        String status = getQueryParam(exchange, "status", "");

        List<Product> products = inventoryService.searchAndFilterProducts(query, category, status);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("count", products.size());
        resp.put("products", products);
        sendJsonResponse(exchange, 200, resp);
    }

    private void handleGetById(HttpExchange exchange, int id) throws IOException {
        Product product = inventoryService.getProductById(id);
        if (product != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("product", product);
            sendJsonResponse(exchange, 200, resp);
        } else {
            sendErrorResponse(exchange, 404, "Product not found.");
        }
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Map<String, Object> req = JsonUtil.parseJsonObject(body);

        Product p = new Product();
        p.setProductCode(JsonUtil.getString(req, "productCode", ""));
        p.setName(JsonUtil.getString(req, "name", ""));
        p.setCategory(JsonUtil.getString(req, "category", ""));
        int supId = JsonUtil.getInt(req, "supplierId", 0);
        if (supId > 0) p.setSupplierId(supId);
        p.setUnitPrice(JsonUtil.getBigDecimal(req, "unitPrice", BigDecimal.ZERO));
        p.setCurrentStock(JsonUtil.getInt(req, "currentStock", 0));
        p.setMinimumStock(JsonUtil.getInt(req, "minimumStock", 10));
        p.setMaximumStock(JsonUtil.getInt(req, "maximumStock", 100));
        p.setReorderQuantity(JsonUtil.getInt(req, "reorderQuantity", 30));

        Map<String, Object> result = inventoryService.addProduct(p);
        int statusCode = Boolean.TRUE.equals(result.get("success")) ? 201 : 400;
        sendJsonResponse(exchange, statusCode, result);
    }

    private void handleUpdate(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Map<String, Object> req = JsonUtil.parseJsonObject(body);

        Product p = new Product();
        p.setId(JsonUtil.getInt(req, "id", 0));
        p.setProductCode(JsonUtil.getString(req, "productCode", ""));
        p.setName(JsonUtil.getString(req, "name", ""));
        p.setCategory(JsonUtil.getString(req, "category", ""));
        int supId = JsonUtil.getInt(req, "supplierId", 0);
        if (supId > 0) p.setSupplierId(supId);
        p.setUnitPrice(JsonUtil.getBigDecimal(req, "unitPrice", BigDecimal.ZERO));
        p.setCurrentStock(JsonUtil.getInt(req, "currentStock", 0));
        p.setMinimumStock(JsonUtil.getInt(req, "minimumStock", 10));
        p.setMaximumStock(JsonUtil.getInt(req, "maximumStock", 100));
        p.setReorderQuantity(JsonUtil.getInt(req, "reorderQuantity", 30));

        Map<String, Object> result = inventoryService.updateProduct(p);
        int statusCode = Boolean.TRUE.equals(result.get("success")) ? 200 : 400;
        sendJsonResponse(exchange, statusCode, result);
    }

    private void handleDelete(HttpExchange exchange, int id) throws IOException {
        if (id <= 0) {
            sendErrorResponse(exchange, 400, "Invalid product ID.");
            return;
        }
        Map<String, Object> result = inventoryService.deleteProduct(id);
        int statusCode = Boolean.TRUE.equals(result.get("success")) ? 200 : 400;
        sendJsonResponse(exchange, statusCode, result);
    }
}
