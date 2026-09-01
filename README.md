# 🍽️ Restaurant Management System

A **Java-based Restaurant Management System** integrated with **MongoDB** and a web-based frontend.

The system allows customers to view available food items, search and filter foods, place orders, and automatically store customer and order information in MongoDB. An **Admin Dashboard** is provided to monitor foods, customers, orders, and total revenue.

---

## 📌 Features

### 👤 User Features

* View available food items
* Search food by name or category
* Filter food by category
* View food price and availability
* Select food and quantity
* Enter customer details
* Place food orders
* Automatically generate Order ID
* Store customer details in MongoDB
* Store order details in MongoDB
* Display order confirmation

### 🔐 Admin Features

* Admin Dashboard
* View total number of foods
* View total customers
* View total orders
* View total revenue
* View all food items
* Add food items
* Delete food items
* View registered customers
* Delete customers
* View all orders
* Monitor restaurant data

### 🗄️ Database Features

MongoDB is used as the database.

Database:

```text
RestaurantManagementDB
```

Collections:

```text
foods
customers
orders
```

---

# 🛠️ Technologies Used

| Technology          | Purpose                   |
| ------------------- | ------------------------- |
| Java                | Backend                   |
| MongoDB             | Database                  |
| MongoDB Java Driver | Java-MongoDB connectivity |
| HTML                | Web page structure        |
| CSS                 | User interface            |
| JavaScript          | Frontend functionality    |
| Java HttpServer     | Web server                |
| VS Code             | Development environment   |

---

# 📂 Project Structure

```text
RestaurantManagementSystem/
│
├── lib/
│   ├── bson-*.jar
│   ├── mongodb-driver-core-*.jar
│   └── mongodb-driver-sync-*.jar
│
├── out/
│   └── Compiled Java .class files
│
├── src/
│   ├── Main.java
│   ├── MongoDBConnection.java
│   ├── Food.java
│   ├── FoodService.java
│   ├── Customer.java
│   ├── CustomerService.java
│   ├── Order.java
│   └── OrderService.java
│
├── web/
│   ├── index.html
│   ├── dashboard.html
│   ├── style.css
│   └── script.js
│
└── README.md
```

---

# ⚙️ Requirements

Before running the project, install the following:

### 1. Java JDK

Java JDK 17 or later is recommended.

Check Java:

```powershell
java -version
```

Check Java compiler:

```powershell
javac -version
```

---

### 2. MongoDB

Install MongoDB Community Server.

MongoDB must be running before starting the Java application.

You can check MongoDB using MongoDB Compass.

---

### 3. MongoDB Compass

MongoDB Compass is recommended for viewing the database and collections.

After running the application and inserting data, you should see:

```text
RestaurantManagementDB
│
├── foods
├── customers
└── orders
```

---

# 🗄️ MongoDB Configuration

The application connects to MongoDB using:

```text
mongodb://localhost:27017
```

The database name is:

```text
RestaurantManagementDB
```

This configuration is present in:

```text
src/MongoDBConnection.java
```

The connection code uses:

```java
private static final String URI =
        "mongodb://localhost:27017";

private static final String DATABASE_NAME =
        "RestaurantManagementDB";
```

No manual database creation is required.

MongoDB automatically creates the database and collections when data is inserted.

---

# 📊 Sample Data

The project can contain sample data such as:

```text
100 Foods
100 Customers
100 Orders
```

The food data can be viewed in MongoDB Compass under:

```text
RestaurantManagementDB
    └── foods
```

Customers:

```text
RestaurantManagementDB
    └── customers
```

Orders:

```text
RestaurantManagementDB
    └── orders
```

---

# 🚀 How to Run the Project

Follow these steps in order.

---

## Step 1: Start MongoDB

Open **MongoDB Compass** and connect to:

```text
mongodb://localhost:27017
```

Make sure the connection is successful.

Keep MongoDB running.

---

## Step 2: Open the Project

Open the project folder in VS Code.

Example:

```text
C:\Users\YourName\Desktop\RestaurantManagementSystem
```

Open a PowerShell terminal inside the project folder.

You should see:

```powershell
PS C:\Users\YourName\Desktop\RestaurantManagementSystem>
```

---

## Step 3: Check the Project Structure

Make sure these folders exist:

```text
src
web
lib
out
```

And make sure the Java files are inside `src`.

---

# 🔨 Step 4: Compile the Java Files

Run:

```powershell
javac -cp "lib/*" -d out src/*.java
```

If compilation is successful, there should be no error message.

The compiled `.class` files will be created inside:

```text
out/
```

For example:

```text
out/
├── Main.class
├── Food.class
├── FoodService.class
├── Customer.class
├── CustomerService.class
├── Order.class
├── OrderService.class
└── MongoDBConnection.class
```

---

# ▶️ Step 5: Start the Java Server

Run:

```powershell
java -cp "out;lib/*" Main
```

You should see:

```text
Starting Restaurant Management System...

======================================
 RESTAURANT MANAGEMENT SYSTEM
======================================

Server running at:
http://localhost:8080

Admin Dashboard:
http://localhost:8080/dashboard.html

Keep this terminal running.
```

Do **not close this terminal** while using the application.

---

# 🌐 Step 6: Open the User Website

Open your browser and go to:

```text
http://localhost:8080
```

You should see the Restaurant Management System homepage.

The website loads food data using:

```text
/api/foods
```

The data comes from:

```text
MongoDB
   ↓
FoodService.java
   ↓
Main.java
   ↓
Java API
   ↓
script.js
   ↓
index.html
```

---

# 🍕 Step 7: View Available Foods

The homepage automatically loads food data from MongoDB.

The food items are displayed as cards containing:

```text
Food Category
Food Name
Price
Availability
Order button
```

Example:

```text
🍛
Biryani

Chicken Biryani

₹250

[ Order ]
```

---

# 🔍 Step 8: Search Food

Use the search box on the homepage.

For example:

```text
pizza
```

The system displays matching foods.

You can also search by category.

---

# 🗂️ Step 9: Filter Food

Food can be filtered using categories such as:

```text
All
South Indian
North Indian
Fast Food
Main Course
Desserts
Beverages
```

---

# 🛒 Step 10: Place an Order

Click the:

```text
Order
```

button for any available food.

The order window will open.

Enter:

```text
Full Name
Phone Number
Email Address
Quantity
```

The total price is automatically calculated.

For example:

```text
Food:
Chicken Biryani

Price:
₹250

Quantity:
2

Total:
₹500
```

Click:

```text
🛒 Place Order
```

---

# 💾 Step 11: Order Storage

When the user places an order, the Java backend receives the request.

The system creates:

### Customer

A new customer is stored in:

```text
RestaurantManagementDB
    └── customers
```

Example:

```json
{
    "customerId": "C123456",
    "name": "Rahul",
    "phone": "9876543210",
    "email": "rahul@gmail.com"
}
```

### Order

The order is stored in:

```text
RestaurantManagementDB
    └── orders
```

Example:

```json
{
    "orderId": "ORD123456",
    "customerName": "Rahul",
    "foodName": "Chicken Biryani",
    "quantity": 2,
    "total": 500,
    "date": "2026-09-01T22:30:00"
}
```

---

# 📋 Step 12: Check Data in MongoDB Compass

Open MongoDB Compass.

Select:

```text
RestaurantManagementDB
```

You should see:

```text
RestaurantManagementDB
│
├── foods
├── customers
└── orders
```

Click:

```text
foods
```

to see food records.

Click:

```text
customers
```

to see customers.

Click:

```text
orders
```

to see orders.

---

# 🔐 Step 13: Open Admin Dashboard

Open:

```text
http://localhost:8080/dashboard.html
```

The Admin Dashboard gets information from:

```text
/api/dashboard
/api/foods
/api/customers
/api/orders
```

The dashboard can display:

```text
Total Foods
Total Customers
Total Orders
Total Revenue
```

It can also display food, customer and order information.

---

# 📊 Admin Dashboard Data Flow

```text
                 MongoDB
                    │
       ┌────────────┼────────────┐
       │            │            │
     foods      customers      orders
       │            │            │
       ▼            ▼            ▼
 FoodService  CustomerService OrderService
       │            │            │
       └────────────┼────────────┘
                    │
                    ▼
                 Main.java
                    │
                    ▼
             REST-like APIs
                    │
                    ▼
            Admin Dashboard
```

---

# 🔗 Available API Endpoints

### Get Dashboard Information

```text
GET /api/dashboard
```

Returns:

```json
{
    "foods": 100,
    "customers": 100,
    "orders": 100,
    "revenue": 25000
}
```

---

### Get All Foods

```text
GET /api/foods
```

---

### Add Food

```text
POST /api/foods
```

---

### Delete Food

```text
DELETE /api/foods?id=F123
```

---

### Get All Customers

```text
GET /api/customers
```

---

### Add Customer

```text
POST /api/customers
```

---

### Delete Customer

```text
DELETE /api/customers?id=C123
```

---

### Get All Orders

```text
GET /api/orders
```

---

### Place Order

```text
POST /api/orders
```

---

# 🧪 Testing the Application

Use the following sequence to test the complete project:

```text
1. Start MongoDB
        ↓
2. Open project in VS Code
        ↓
3. Compile Java
        ↓
4. Start Main.java
        ↓
5. Open localhost:8080
        ↓
6. Check foods
        ↓
7. Select food
        ↓
8. Enter customer details
        ↓
9. Place order
        ↓
10. Check MongoDB
        ↓
11. Open Admin Dashboard
        ↓
12. Verify customer/order/revenue
```

---

# ❗ Troubleshooting

## Problem: `mvn is not recognized`

This project does **not require Maven**.

Use:

```powershell
javac -cp "lib/*" -d out src/*.java
```

and:

```powershell
java -cp "out;lib/*" Main
```

---

## Problem: Foods are not loading

Check:

### MongoDB

Make sure MongoDB is running.

### Java server

Make sure the terminal shows:

```text
Server running at:
http://localhost:8080
```

### Browser

Open:

```text
http://localhost:8080
```

Do not open `index.html` directly from the file system.

---

## Problem: `404 index.html not found`

Make sure the structure is:

```text
RestaurantManagementSystem/
└── web/
    └── index.html
```

The Java server must be started from the main project directory:

```text
RestaurantManagementSystem
```

---

## Problem: Admin Dashboard does not open

Make sure:

```text
web/dashboard.html
```

exists.

Then open:

```text
http://localhost:8080/dashboard.html
```

---

## Problem: MongoDB database is not visible

The database may not appear until data is inserted.

Open MongoDB Compass and refresh the databases.

Look for:

```text
RestaurantManagementDB
```

Then:

```text
foods
customers
orders
```

---

# 🔄 Restarting the Application

If you modify Java code:

### Stop server

Press:

```text
Ctrl + C
```

### Compile again

```powershell
javac -cp "lib/*" -d out src/*.java
```

### Start again

```powershell
java -cp "out;lib/*" Main
```

If you modify only HTML, CSS or JavaScript, usually you only need to refresh the browser.

Use:

```text
Ctrl + F5
```

for a hard refresh.

---

# 🎯 Project Objective

The objective of this project is to develop a simple restaurant management platform using **Java and MongoDB**, providing an interactive web interface for customers and an administrative dashboard for restaurant management.

The system demonstrates:

* Java backend development
* MongoDB database integration
* CRUD operations
* REST-style APIs
* HTML/CSS/JavaScript frontend
* Customer management
* Food management
* Order management
* Revenue calculation
* Real-time communication between frontend, backend and database

---

# 👩‍💻 Author

**Thippeswamy K S**

Restaurant Management System
Java + MongoDB + HTML + CSS + JavaScript

---

# 📜 License

This project is developed for **educational and academic purposes**.
