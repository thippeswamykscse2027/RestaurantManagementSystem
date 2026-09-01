import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class Main {

    static FoodService foodService;
    static CustomerService customerService;
    static OrderService orderService;

    public static void main(String[] args) throws Exception {

        System.out.println("Starting Restaurant Management System...");

        foodService = new FoodService();
        customerService = new CustomerService();
        orderService = new OrderService();

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0
        );

        // USER WEBSITE
        server.createContext("/", Main::serveIndex);
        server.createContext("/dashboard.html", Main::serveDashboard);
        server.createContext("/style.css", Main::serveCSS);
        server.createContext("/script.js", Main::serveJS);

        // APIs
        server.createContext("/api/dashboard", Main::dashboardAPI);
        server.createContext("/api/foods", Main::foodsAPI);
        server.createContext("/api/customers", Main::customersAPI);
        server.createContext("/api/orders", Main::ordersAPI);

        server.setExecutor(null);
        server.start();

        System.out.println();
        System.out.println("======================================");
        System.out.println(" RESTAURANT MANAGEMENT SYSTEM");
        System.out.println("======================================");
        System.out.println("Server running at:");
        System.out.println("http://localhost:8080");
        System.out.println();
        System.out.println("Admin Dashboard:");
        System.out.println("http://localhost:8080/dashboard.html");
        System.out.println();
        System.out.println("Keep this terminal running.");
    }

    // =====================================================
    // USER HOME PAGE
    // =====================================================

    static void serveIndex(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(exchange, 405,
                    "Method Not Allowed", "text/plain");
            return;
        }

        serveFile(exchange, "web/index.html", "text/html");
    }

    // =====================================================
    // ADMIN DASHBOARD
    // =====================================================

    static void serveDashboard(HttpExchange exchange)
            throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(exchange, 405,
                    "Method Not Allowed", "text/plain");
            return;
        }

        serveFile(exchange, "web/dashboard.html", "text/html");
    }

    // =====================================================
    // CSS
    // =====================================================

    static void serveCSS(HttpExchange exchange)
            throws IOException {

        serveFile(exchange, "web/style.css", "text/css");
    }

    // =====================================================
    // JAVASCRIPT
    // =====================================================

    static void serveJS(HttpExchange exchange)
            throws IOException {

        serveFile(exchange, "web/script.js",
                "application/javascript");
    }

    // =====================================================
    // SERVE FILE
    // =====================================================

    static void serveFile(
            HttpExchange exchange,
            String fileName,
            String contentType) throws IOException {

        File file = new File(fileName);

        if (!file.exists()) {
            sendResponse(
                    exchange,
                    404,
                    fileName + " not found",
                    "text/plain"
            );
            return;
        }

        byte[] data = Files.readAllBytes(file.toPath());

        exchange.getResponseHeaders().set(
                "Content-Type",
                contentType + "; charset=UTF-8"
        );

        exchange.sendResponseHeaders(200, data.length);

        OutputStream output = exchange.getResponseBody();
        output.write(data);
        output.close();
    }

    // =====================================================
    // DASHBOARD API
    // =====================================================

    static void dashboardAPI(HttpExchange exchange)
            throws IOException {

        if (handleOptions(exchange)) return;

        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("GET")) {

            sendResponse(
                    exchange,
                    405,
                    "Method Not Allowed",
                    "text/plain"
            );
            return;
        }

        long foods = foodService.getFoodCount();
        long customers = customerService.getCustomerCount();
        long orders = orderService.getOrderCount();
        double revenue = orderService.getTotalRevenue();

        String json =
                "{"
                + "\"foods\":" + foods + ","
                + "\"customers\":" + customers + ","
                + "\"orders\":" + orders + ","
                + "\"revenue\":" + revenue
                + "}";

        sendResponse(
                exchange,
                200,
                json,
                "application/json"
        );
    }

    // =====================================================
    // FOOD API
    // =====================================================

    static void foodsAPI(HttpExchange exchange)
            throws IOException {

        if (handleOptions(exchange)) return;

        String method = exchange.getRequestMethod();

        // ---------------- GET FOODS ----------------

        if (method.equalsIgnoreCase("GET")) {

            List<Food> foods = foodService.getAllFoods();

            StringBuilder json = new StringBuilder("[");

            for (int i = 0; i < foods.size(); i++) {

                Food food = foods.get(i);

                json.append("{")
                        .append("\"id\":\"")
                        .append(escape(food.getId()))
                        .append("\",")

                        .append("\"name\":\"")
                        .append(escape(food.getName()))
                        .append("\",")

                        .append("\"category\":\"")
                        .append(escape(food.getCategory()))
                        .append("\",")

                        .append("\"price\":")
                        .append(food.getPrice())
                        .append(",")

                        .append("\"available\":")
                        .append(food.isAvailable())

                        .append("}");

                if (i < foods.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");

            sendResponse(
                    exchange,
                    200,
                    json.toString(),
                    "application/json"
            );

            return;
        }

        // ---------------- ADD FOOD ----------------

        if (method.equalsIgnoreCase("POST")) {

            String body = readBody(exchange);

            String name = getJsonValue(body, "name");
            String category = getJsonValue(body, "category");
            String priceText = getJsonValue(body, "price");
            String availableText =
                    getJsonValue(body, "available");

            if (name == null ||
                    category == null ||
                    priceText == null) {

                sendResponse(
                        exchange,
                        400,
                        "{\"error\":\"Invalid food data\"}",
                        "application/json"
                );

                return;
            }

            double price = Double.parseDouble(priceText);

            boolean available =
                    availableText == null ||
                    Boolean.parseBoolean(availableText);

            Food food = new Food(
                    name,
                    category,
                    price,
                    available
            );

            foodService.addFood(food);

            sendResponse(
                    exchange,
                    200,
                    "{\"success\":true}",
                    "application/json"
            );

            return;
        }

        // ---------------- DELETE FOOD ----------------

        if (method.equalsIgnoreCase("DELETE")) {

            String id =
                    getQueryParameter(exchange, "id");

            if (id == null) {

                sendResponse(
                        exchange,
                        400,
                        "{\"error\":\"Food ID required\"}",
                        "application/json"
                );

                return;
            }

            boolean deleted =
                    foodService.deleteFood(id);

            sendResponse(
                    exchange,
                    200,
                    "{\"success\":" + deleted + "}",
                    "application/json"
            );

            return;
        }

        sendResponse(
                exchange,
                405,
                "Method Not Allowed",
                "text/plain"
        );
    }

    // =====================================================
    // CUSTOMER API
    // =====================================================

    static void customersAPI(HttpExchange exchange)
            throws IOException {

        if (handleOptions(exchange)) return;

        String method = exchange.getRequestMethod();

        // ---------------- GET CUSTOMERS ----------------

        if (method.equalsIgnoreCase("GET")) {

            List<Customer> customers =
                    customerService.getAllCustomers();

            StringBuilder json =
                    new StringBuilder("[");

            for (int i = 0; i < customers.size(); i++) {

                Customer customer = customers.get(i);

                json.append("{")
                        .append("\"id\":\"")
                        .append(escape(customer.getId()))
                        .append("\",")

                        .append("\"name\":\"")
                        .append(escape(customer.getName()))
                        .append("\",")

                        .append("\"phone\":\"")
                        .append(escape(customer.getPhone()))
                        .append("\",")

                        .append("\"email\":\"")
                        .append(escape(customer.getEmail()))
                        .append("\"")
                        .append("}");

                if (i < customers.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");

            sendResponse(
                    exchange,
                    200,
                    json.toString(),
                    "application/json"
            );

            return;
        }

        // ---------------- ADD CUSTOMER ----------------

        if (method.equalsIgnoreCase("POST")) {

            String body = readBody(exchange);

            String name = getJsonValue(body, "name");
            String phone = getJsonValue(body, "phone");
            String email = getJsonValue(body, "email");

            if (name == null ||
                    phone == null ||
                    email == null) {

                sendResponse(
                        exchange,
                        400,
                        "{\"error\":\"Invalid customer data\"}",
                        "application/json"
                );

                return;
            }

            Customer customer =
                    new Customer(name, phone, email);

            customerService.addCustomer(customer);

            sendResponse(
                    exchange,
                    200,
                    "{\"success\":true,\"customerId\":\""
                            + escape(customer.getId())
                            + "\"}",
                    "application/json"
            );

            return;
        }

        // ---------------- DELETE CUSTOMER ----------------

        if (method.equalsIgnoreCase("DELETE")) {

            String id =
                    getQueryParameter(exchange, "id");

            if (id == null) {

                sendResponse(
                        exchange,
                        400,
                        "{\"error\":\"Customer ID required\"}",
                        "application/json"
                );

                return;
            }

            boolean deleted =
                    customerService.deleteCustomer(id);

            sendResponse(
                    exchange,
                    200,
                    "{\"success\":" + deleted + "}",
                    "application/json"
            );

            return;
        }

        sendResponse(
                exchange,
                405,
                "Method Not Allowed",
                "text/plain"
        );
    }

    // =====================================================
    // ORDER API
    // =====================================================

    static void ordersAPI(HttpExchange exchange)
            throws IOException {

        if (handleOptions(exchange)) return;

        String method = exchange.getRequestMethod();

        // ---------------- GET ORDERS ----------------

        if (method.equalsIgnoreCase("GET")) {

            List<Order> orders =
                    orderService.getAllOrders();

            StringBuilder json =
                    new StringBuilder("[");

            for (int i = 0; i < orders.size(); i++) {

                Order order = orders.get(i);

                json.append("{")
                        .append("\"id\":\"")
                        .append(escape(order.getId()))
                        .append("\",")

                        .append("\"customerName\":\"")
                        .append(escape(order.getCustomerName()))
                        .append("\",")

                        .append("\"foodName\":\"")
                        .append(escape(order.getFoodName()))
                        .append("\",")

                        .append("\"quantity\":")
                        .append(order.getQuantity())
                        .append(",")

                        .append("\"total\":")
                        .append(order.getTotal())
                        .append(",")

                        .append("\"date\":\"")
                        .append(escape(order.getDate()))
                        .append("\"")
                        .append("}");

                if (i < orders.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");

            sendResponse(
                    exchange,
                    200,
                    json.toString(),
                    "application/json"
            );

            return;
        }

        // =================================================
        // PLACE ORDER
        // This creates BOTH customer and order
        // =================================================

        if (method.equalsIgnoreCase("POST")) {

            String body = readBody(exchange);

            String customerName =
                    getJsonValue(body, "customerName");

            String phone =
                    getJsonValue(body, "phone");

            String email =
                    getJsonValue(body, "email");

            String foodName =
                    getJsonValue(body, "foodName");

            String quantityText =
                    getJsonValue(body, "quantity");

            String totalText =
                    getJsonValue(body, "total");

            if (customerName == null ||
                    phone == null ||
                    email == null ||
                    foodName == null ||
                    quantityText == null ||
                    totalText == null) {

                sendResponse(
                        exchange,
                        400,
                        "{\"error\":\"Missing order details\"}",
                        "application/json"
                );

                return;
            }

            try {

                int quantity =
                        Integer.parseInt(quantityText);

                double total =
                        Double.parseDouble(totalText);

                // -----------------------------------------
                // 1. SAVE CUSTOMER
                // -----------------------------------------

                Customer customer =
                        new Customer(
                                customerName,
                                phone,
                                email
                        );

                customerService.addCustomer(customer);

                // -----------------------------------------
                // 2. CREATE ORDER ID
                // -----------------------------------------

                String orderId =
                        "ORD" + System.currentTimeMillis();

                // -----------------------------------------
                // 3. DATE
                // -----------------------------------------

                String date =
                        java.time.LocalDateTime.now()
                                .toString();

                // -----------------------------------------
                // 4. CREATE ORDER
                // -----------------------------------------

                Order order =
                        new Order(
                                orderId,
                                customerName,
                                foodName,
                                quantity,
                                total,
                                date
                        );

                // -----------------------------------------
                // 5. SAVE ORDER
                // -----------------------------------------

                orderService.addOrder(order);

                // -----------------------------------------
                // 6. RESPONSE
                // -----------------------------------------

                String response =
                        "{"
                        + "\"success\":true,"
                        + "\"message\":\"Order placed successfully\","
                        + "\"customerId\":\""
                        + escape(customer.getId())
                        + "\","
                        + "\"orderId\":\""
                        + escape(orderId)
                        + "\","
                        + "\"total\":"
                        + total
                        + "}";

                sendResponse(
                        exchange,
                        200,
                        response,
                        "application/json"
                );

            } catch (Exception e) {

                e.printStackTrace();

                sendResponse(
                        exchange,
                        500,
                        "{\"error\":\"Unable to place order\"}",
                        "application/json"
                );
            }

            return;
        }

        sendResponse(
                exchange,
                405,
                "Method Not Allowed",
                "text/plain"
        );
    }

    // =====================================================
    // READ REQUEST BODY
    // =====================================================

    static String readBody(
            HttpExchange exchange) throws IOException {

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                exchange.getRequestBody(),
                                StandardCharsets.UTF_8
                        )
                );

        StringBuilder body =
                new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        return body.toString();
    }

    // =====================================================
    // SIMPLE JSON READER
    // =====================================================

    static String getJsonValue(
            String json,
            String key) {

        if (json == null) {
            return null;
        }

        String search =
                "\"" + key + "\"";

        int keyPosition =
                json.indexOf(search);

        if (keyPosition == -1) {
            return null;
        }

        int colon =
                json.indexOf(
                        ":",
                        keyPosition
                );

        if (colon == -1) {
            return null;
        }

        int start = colon + 1;

        while (start < json.length() &&
                Character.isWhitespace(
                        json.charAt(start))) {

            start++;
        }

        if (start >= json.length()) {
            return null;
        }

        // String
        if (json.charAt(start) == '"') {

            start++;

            StringBuilder value =
                    new StringBuilder();

            boolean escaped = false;

            for (int i = start;
                    i < json.length();
                    i++) {

                char c = json.charAt(i);

                if (escaped) {
                    value.append(c);
                    escaped = false;
                    continue;
                }

                if (c == '\\') {
                    escaped = true;
                    continue;
                }

                if (c == '"') {
                    return value.toString();
                }

                value.append(c);
            }

            return null;
        }

        // Number / boolean
        int end = start;

        while (end < json.length() &&
                json.charAt(end) != ',' &&
                json.charAt(end) != '}') {

            end++;
        }

        return json.substring(
                start,
                end
        ).trim();
    }

    // =====================================================
    // QUERY PARAMETER
    // =====================================================

    static String getQueryParameter(
            HttpExchange exchange,
            String parameter) {

        String query =
                exchange.getRequestURI()
                        .getQuery();

        if (query == null) {
            return null;
        }

        String[] parameters =
                query.split("&");

        for (String param : parameters) {

            String[] pair =
                    param.split("=", 2);

            if (pair.length == 2 &&
                    pair[0].equals(parameter)) {

                return pair[1];
            }
        }

        return null;
    }

    // =====================================================
    // ESCAPE JSON
    // =====================================================

    static String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    // =====================================================
    // OPTIONS / CORS
    // =====================================================

    static boolean handleOptions(
            HttpExchange exchange) throws IOException {

        if (exchange.getRequestMethod()
                .equalsIgnoreCase("OPTIONS")) {

            exchange.getResponseHeaders().set(
                    "Access-Control-Allow-Origin",
                    "*"
            );

            exchange.getResponseHeaders().set(
                    "Access-Control-Allow-Methods",
                    "GET, POST, DELETE, OPTIONS"
            );

            exchange.getResponseHeaders().set(
                    "Access-Control-Allow-Headers",
                    "Content-Type"
            );

            exchange.sendResponseHeaders(204, -1);
            exchange.close();

            return true;
        }

        return false;
    }

    // =====================================================
    // SEND RESPONSE
    // =====================================================

    static void sendResponse(
            HttpExchange exchange,
            int status,
            String response,
            String contentType)
            throws IOException {

        byte[] data =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange.getResponseHeaders().set(
                "Content-Type",
                contentType + "; charset=UTF-8"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin",
                "*"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, POST, DELETE, OPTIONS"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Headers",
                "Content-Type"
        );

        exchange.sendResponseHeaders(
                status,
                data.length
        );

        OutputStream output =
                exchange.getResponseBody();

        output.write(data);
        output.close();
    }
}