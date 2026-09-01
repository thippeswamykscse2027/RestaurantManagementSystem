import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Random;

public class GenerateData {

    public static void main(String[] args) {

        MongoDatabase db =
                MongoDBConnection.getDatabase();

        MongoCollection<Document> foods =
                db.getCollection("foods");

        MongoCollection<Document> customers =
                db.getCollection("customers");

        MongoCollection<Document> orders =
                db.getCollection("orders");

        // Clear old data
        foods.deleteMany(new Document());
        customers.deleteMany(new Document());
        orders.deleteMany(new Document());

        System.out.println("Old data cleared.");

        // -------------------------
        // FOOD DATA
        // -------------------------

        String[] foodNames = {
                "Chicken Biryani",
                "Veg Biryani",
                "Cheese Pizza",
                "Margherita Pizza",
                "Classic Burger",
                "Veg Burger",
                "Masala Dosa",
                "Idli Sambar",
                "Paneer Butter Masala",
                "Butter Naan",
                "Chicken Fried Rice",
                "Veg Fried Rice",
                "Chicken Noodles",
                "Veg Noodles",
                "Paneer Tikka",
                "Chicken Tikka",
                "Gobi Manchurian",
                "French Fries",
                "Masala Puri",
                "Pav Bhaji"
        };

        String[] categories = {
                "Main Course",
                "Fast Food",
                "South Indian",
                "North Indian",
                "Chinese"
        };

        Random random = new Random();

        for (int i = 1; i <= 100; i++) {

            String foodName =
                    foodNames[(i - 1) % foodNames.length];

            String category =
                    categories[(i - 1) % categories.length];

            double price =
                    100 + random.nextInt(401);

            boolean available = random.nextInt(10) != 0;

            Document food = new Document()
                    .append("foodId", "F" + String.format("%03d", i))
                    .append("name", foodName)
                    .append("category", category)
                    .append("price", price)
                    .append("available", available);

            foods.insertOne(food);
        }

        System.out.println("100 food records inserted.");

        // -------------------------
        // CUSTOMER DATA
        // -------------------------

        String[] firstNames = {
                "Rahul",
                "Priya",
                "Arjun",
                "Sneha",
                "Kiran",
                "Ananya",
                "Rohit",
                "Pooja",
                "Vikas",
                "Divya",
                "Amit",
                "Neha",
                "Sanjay",
                "Kavya",
                "Nikhil",
                "Swathi",
                "Akash",
                "Meghana",
                "Varun",
                "Shreya"
        };

        for (int i = 1; i <= 100; i++) {

            String name =
                    firstNames[(i - 1) % firstNames.length]
                    + " "
                    + (i);

            String phone =
                    "98" + String.format("%08d", i);

            String email =
                    "customer" + i + "@gmail.com";

            Document customer = new Document()
                    .append("customerId",
                            "C" + String.format("%03d", i))
                    .append("name", name)
                    .append("phone", phone)
                    .append("email", email);

            customers.insertOne(customer);
        }

        System.out.println("100 customer records inserted.");

        // -------------------------
        // ORDER DATA
        // -------------------------

        for (int i = 1; i <= 100; i++) {

            int foodNumber =
                    ((i - 1) % 100) + 1;

            int customerNumber =
                    ((i - 1) % 100) + 1;

            int quantity =
                    1 + random.nextInt(4);

            double foodPrice =
                    100 + random.nextInt(401);

            double total =
                    quantity * foodPrice;

            Document order = new Document()
                    .append("orderId",
                            "ORD" + String.format("%03d", i))
                    .append("customerId",
                            "C" + String.format("%03d",
                                    customerNumber))
                    .append("foodId",
                            "F" + String.format("%03d",
                                    foodNumber))
                    .append("quantity", quantity)
                    .append("total", total)
                    .append("status", "Completed");

            orders.insertOne(order);
        }

        System.out.println("100 order records inserted.");

        System.out.println();
        System.out.println("==================================");
        System.out.println(" RESTAURANT DATA GENERATED");
        System.out.println("==================================");
        System.out.println("Foods      : "
                + foods.countDocuments());
        System.out.println("Customers  : "
                + customers.countDocuments());
        System.out.println("Orders     : "
                + orders.countDocuments());
        System.out.println("==================================");
    }
}
