package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Supplier;
import service.SupplierService;
import util.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierController extends BaseController implements HttpHandler {

    private final SupplierService supplierService = new SupplierService();

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
                if (path.matches(".*/suppliers/\\d+$")) {
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
                if (path.matches(".*/suppliers/\\d+$")) {
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
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("count", suppliers.size());
        resp.put("suppliers", suppliers);
        sendJsonResponse(exchange, 200, resp);
    }

    private void handleGetById(HttpExchange exchange, int id) throws IOException {
        Supplier supplier = supplierService.getSupplierById(id);
        if (supplier != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("supplier", supplier);
            sendJsonResponse(exchange, 200, resp);
        } else {
            sendErrorResponse(exchange, 404, "Supplier not found.");
        }
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Map<String, Object> req = JsonUtil.parseJsonObject(body);

        Supplier s = new Supplier();
        s.setSupplierCode(JsonUtil.getString(req, "supplierCode", ""));
        s.setName(JsonUtil.getString(req, "name", ""));
        s.setContactPerson(JsonUtil.getString(req, "contactPerson", ""));
        s.setPhone(JsonUtil.getString(req, "phone", ""));
        s.setEmail(JsonUtil.getString(req, "email", ""));
        s.setAddress(JsonUtil.getString(req, "address", ""));
        s.setLeadTime(JsonUtil.getInt(req, "leadTime", 3));
        s.setStatus(JsonUtil.getString(req, "status", "Active"));

        Map<String, Object> result = supplierService.addSupplier(s);
        int statusCode = Boolean.TRUE.equals(result.get("success")) ? 201 : 400;
        sendJsonResponse(exchange, statusCode, result);
    }

    private void handleUpdate(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Map<String, Object> req = JsonUtil.parseJsonObject(body);

        Supplier s = new Supplier();
        s.setId(JsonUtil.getInt(req, "id", 0));
        s.setSupplierCode(JsonUtil.getString(req, "supplierCode", ""));
        s.setName(JsonUtil.getString(req, "name", ""));
        s.setContactPerson(JsonUtil.getString(req, "contactPerson", ""));
        s.setPhone(JsonUtil.getString(req, "phone", ""));
        s.setEmail(JsonUtil.getString(req, "email", ""));
        s.setAddress(JsonUtil.getString(req, "address", ""));
        s.setLeadTime(JsonUtil.getInt(req, "leadTime", 3));
        s.setStatus(JsonUtil.getString(req, "status", "Active"));

        Map<String, Object> result = supplierService.updateSupplier(s);
        int statusCode = Boolean.TRUE.equals(result.get("success")) ? 200 : 400;
        sendJsonResponse(exchange, statusCode, result);
    }

    private void handleDelete(HttpExchange exchange, int id) throws IOException {
        if (id <= 0) {
            sendErrorResponse(exchange, 400, "Invalid supplier ID.");
            return;
        }
        Map<String, Object> result = supplierService.deleteSupplier(id);
        int statusCode = Boolean.TRUE.equals(result.get("success")) ? 200 : 400;
        sendJsonResponse(exchange, statusCode, result);
    }
}
