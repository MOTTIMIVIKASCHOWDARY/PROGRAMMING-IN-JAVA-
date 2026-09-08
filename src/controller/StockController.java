package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.StockTransaction;
import service.StockService;
import util.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockController extends BaseController implements HttpHandler {

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
            if (path.endsWith("/in") && "POST".equalsIgnoreCase(method)) {
                handleStockIn(exchange);
            } else if (path.endsWith("/out") && "POST".equalsIgnoreCase(method)) {
                handleStockOut(exchange);
            } else if ((path.endsWith("/transactions") || path.equals("/api/stock") || path.equals("/api/stock/")) && "GET".equalsIgnoreCase(method)) {
                handleTransactions(exchange);
            } else {
                sendErrorResponse(exchange, 404, "Endpoint not found: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void handleStockIn(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Map<String, Object> req = JsonUtil.parseJsonObject(body);

        int productId = JsonUtil.getInt(req, "productId", 0);
        int quantity = JsonUtil.getInt(req, "quantity", 0);
        String reason = JsonUtil.getString(req, "reason", "Stock Intake");
        String performedBy = JsonUtil.getString(req, "performedBy", "admin");

        Map<String, Object> result = stockService.addStock(productId, quantity, reason, performedBy);
        int statusCode = Boolean.TRUE.equals(result.get("success")) ? 200 : 400;
        sendJsonResponse(exchange, statusCode, result);
    }

    private void handleStockOut(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Map<String, Object> req = JsonUtil.parseJsonObject(body);

        int productId = JsonUtil.getInt(req, "productId", 0);
        int quantity = JsonUtil.getInt(req, "quantity", 0);
        String reason = JsonUtil.getString(req, "reason", "Dispatched / Sold");
        String performedBy = JsonUtil.getString(req, "performedBy", "admin");

        Map<String, Object> result = stockService.removeStock(productId, quantity, reason, performedBy);
        int statusCode = Boolean.TRUE.equals(result.get("success")) ? 200 : 400;
        sendJsonResponse(exchange, statusCode, result);
    }

    private void handleTransactions(HttpExchange exchange) throws IOException {
        int limit = Integer.parseInt(getQueryParam(exchange, "limit", "100"));
        List<StockTransaction> list;
        if (limit > 0 && limit < 100) {
            list = stockService.getRecentTransactions(limit);
        } else {
            list = stockService.getAllTransactions();
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("count", list.size());
        resp.put("transactions", list);
        sendJsonResponse(exchange, 200, resp);
    }
}
