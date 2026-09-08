package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.UserDAO;
import model.User;
import util.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginController extends BaseController implements HttpHandler {

    private final UserDAO userDAO = new UserDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            handleOptions(exchange);
            return;
        }

        String path = exchange.getRequestURI().getPath();

        try {
            if (path.endsWith("/login") && "POST".equalsIgnoreCase(method)) {
                handleLogin(exchange);
            } else if (path.endsWith("/logout") && "POST".equalsIgnoreCase(method)) {
                handleLogout(exchange);
            } else if (path.endsWith("/me") && "GET".equalsIgnoreCase(method)) {
                handleMe(exchange);
            } else {
                sendErrorResponse(exchange, 404, "Endpoint not found: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Map<String, Object> req = JsonUtil.parseJsonObject(body);

        String username = JsonUtil.getString(req, "username", "");
        String password = JsonUtil.getString(req, "password", "");

        if (username.isEmpty() || password.isEmpty()) {
            sendErrorResponse(exchange, 400, "Username and password are required.");
            return;
        }

        User user = userDAO.authenticate(username, password);
        if (user != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("message", "Login successful.");
            resp.put("user", user);
            resp.put("token", "simr-demo-session-token-" + System.currentTimeMillis());
            sendJsonResponse(exchange, 200, resp);
        } else {
            sendErrorResponse(exchange, 401, "Invalid username or password. Please try again.");
        }
    }

    private void handleLogout(HttpExchange exchange) throws IOException {
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("message", "Logged out successfully.");
        sendJsonResponse(exchange, 200, resp);
    }

    private void handleMe(HttpExchange exchange) throws IOException {
        User user = userDAO.getUserById(1);
        if (user != null) {
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("user", user);
            sendJsonResponse(exchange, 200, resp);
        } else {
            sendErrorResponse(exchange, 401, "Not authenticated.");
        }
    }
}
