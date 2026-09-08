package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.PurchaseOrder;
import model.ReorderRecommendation;
import service.ReorderService;
import util.JsonUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReorderController extends BaseController implements HttpHandler {

    private final ReorderService reorderService = new ReorderService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            handleOptions(exchange);
            return;
        }

        String path = exchange.getRequestURI().getPath();

        try {
            if ((path.endsWith("/recommendations") || path.equals("/api/reorder") || path.equals("/api/reorder/")) && "GET".equalsIgnoreCase(method)) {
                handleRecommendations(exchange);
            } else if (path.endsWith("/create-po") && "POST".equalsIgnoreCase(method)) {
                handleCreatePO(exchange);
            } else if (path.endsWith("/purchase-orders") && "GET".equalsIgnoreCase(method)) {
                handlePurchaseOrders(exchange);
            } else if (path.endsWith("/simulate") && ("POST".equalsIgnoreCase(method) || "GET".equalsIgnoreCase(method))) {
                handleSimulate(exchange);
            } else {
                sendErrorResponse(exchange, 404, "Endpoint not found: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void handleSimulate(HttpExchange exchange) throws IOException {
        double demandMultiplier = 1.0;
        int leadTimeDelay = 0;
        double safetyBufferFactor = 1.0;

        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String body = readRequestBody(exchange);
            if (body != null && !body.trim().isEmpty()) {
                Map<String, Object> req = JsonUtil.parseJsonObject(body);
                Object dm = req.get("demandMultiplier");
                if (dm instanceof Number) demandMultiplier = ((Number) dm).doubleValue();
                Object ltd = req.get("leadTimeDelay");
                if (ltd instanceof Number) leadTimeDelay = ((Number) ltd).intValue();
                Object sbf = req.get("safetyBufferFactor");
                if (sbf instanceof Number) safetyBufferFactor = ((Number) sbf).doubleValue();
            }
        } else {
            try {
                demandMultiplier = Double.parseDouble(getQueryParam(exchange, "demandMultiplier", "1.0"));
                leadTimeDelay = Integer.parseInt(getQueryParam(exchange, "leadTimeDelay", "0"));
                safetyBufferFactor = Double.parseDouble(getQueryParam(exchange, "safetyBufferFactor", "1.0"));
            } catch (Exception ignored) {}
        }

        List<Map<String, Object>> results = reorderService.simulateReorderScenarios(demandMultiplier, leadTimeDelay, safetyBufferFactor);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("count", results.size());
        resp.put("parameters", Map.of("demandMultiplier", demandMultiplier, "leadTimeDelay", leadTimeDelay, "safetyBufferFactor", safetyBufferFactor));
        resp.put("simulation", results);
        sendJsonResponse(exchange, 200, resp);
    }

    private void handleRecommendations(HttpExchange exchange) throws IOException {
        List<ReorderRecommendation> recs = reorderService.generateReorderRecommendations();
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("count", recs.size());
        resp.put("recommendations", recs);
        sendJsonResponse(exchange, 200, resp);
    }

    private void handleCreatePO(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Map<String, Object> req = JsonUtil.parseJsonObject(body);

        int supplierId = JsonUtil.getInt(req, "supplierId", 0);
        int productId = JsonUtil.getInt(req, "productId", 0);
        int quantity = JsonUtil.getInt(req, "quantity", 0);
        BigDecimal unitPrice = JsonUtil.getBigDecimal(req, "unitPrice", null);

        Map<String, Object> result = reorderService.createPurchaseOrder(supplierId, productId, quantity, unitPrice);
        int statusCode = Boolean.TRUE.equals(result.get("success")) ? 201 : 400;
        sendJsonResponse(exchange, statusCode, result);
    }

    private void handlePurchaseOrders(HttpExchange exchange) throws IOException {
        List<PurchaseOrder> orders = reorderService.getAllPurchaseOrders();
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("count", orders.size());
        resp.put("purchaseOrders", orders);
        sendJsonResponse(exchange, 200, resp);
    }
}
