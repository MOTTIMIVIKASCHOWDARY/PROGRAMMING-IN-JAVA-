import com.sun.net.httpserver.HttpServer;
import controller.*;
import util.DBConnection;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        } else if (System.getProperty("server.port") != null) {
            try {
                port = Integer.parseInt(System.getProperty("server.port"));
            } catch (NumberFormatException ignored) {}
        }

        System.out.println("================================================================================");
        System.out.println("   SIMR - Smart Inventory, Supplier & Predictive Reorder Management Platform   ");
        System.out.println("   College Java Capstone Project (Java 21 / JDBC / Modern Web Frontend)        ");
        System.out.println("================================================================================");

        // 1. Initialize Database
        System.out.println("[Startup] Initializing Database Connection & Schema...");
        DBConnection.initDatabase();

        // 2. Start Embedded HTTP Server
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            // API Handlers
            server.createContext("/api/auth", new LoginController());
            server.createContext("/api/dashboard", new DashboardController());
            server.createContext("/api/products", new ProductController());
            server.createContext("/api/inventory", new ProductController());
            server.createContext("/api/suppliers", new SupplierController());
            server.createContext("/api/stock", new StockController());
            server.createContext("/api/reorder", new ReorderController());
            server.createContext("/api/reports", new ReportController());

            // Static Files Handler (Frontend Web Assets)
            File webDir = new File("web");
            if (!webDir.exists()) {
                webDir.mkdirs();
            }
            server.createContext("/", new StaticFileController("web"));

            server.setExecutor(Executors.newFixedThreadPool(10));
            server.start();

            System.out.println("\n[Server Status] Application successfully started!");
            System.out.println("[Database Mode] " + DBConnection.getDatabaseType());
            System.out.println("[Web URL]       http://localhost:" + port + "/");
            System.out.println("[Demo Login]    Username: admin  |  Password: admin123");
            System.out.println("================================================================================");
            System.out.println("Press Ctrl+C to stop the server.\n");

        } catch (Exception e) {
            System.err.println("[Server Fatal Error] Failed to start HTTP server on port " + port + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
