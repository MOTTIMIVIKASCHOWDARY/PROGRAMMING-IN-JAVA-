package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Product;
import model.StockTransaction;
import service.InventoryService;
import service.StockService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController extends BaseController implements HttpHandler {

    private final InventoryService inventoryService = new InventoryService();
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
            if (path.endsWith("/stats") && "GET".equalsIgnoreCase(method)) {
                handleStats(exchange);
            } else if (path.endsWith("/chart") && "GET".equalsIgnoreCase(method)) {
                handleChart(exchange);
            } else if (path.endsWith("/alerts") && "GET".equalsIgnoreCase(method)) {
                handleAlerts(exchange);
            } else if (path.endsWith("/activity") && "GET".equalsIgnoreCase(method)) {
                handleActivity(exchange);
            } else {
                sendErrorResponse(exchange, 404, "Endpoint not found: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void handleStats(HttpExchange exchange) throws IOException {
        Map<String, Object> stats = inventoryService.getDashboardStats();
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("stats", stats);
        sendJsonResponse(exchange, 200, resp);
    }

    private void handleChart(HttpExchange exchange) throws IOException {
        int days = Integer.parseInt(getQueryParam(exchange, "days", "14"));
        Map<String, Object> chartData = stockService.getStockMovementChartData(days);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("chartData", chartData);
        sendJsonResponse(exchange, 200, resp);
    }

    private void handleAlerts(HttpExchange exchange) throws IOException {
        List<Product> alerts = inventoryService.getLowStockAlerts();
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("alerts", alerts);
        sendJsonResponse(exchange, 200, resp);
    }

    private void handleActivity(HttpExchange exchange) throws IOException {
        int limit = Integer.parseInt(getQueryParam(exchange, "limit", "10"));
        List<StockTransaction> transactions = stockService.getRecentTransactions(limit);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("activities", transactions);
        sendJsonResponse(exchange, 200, resp);
    }
}
