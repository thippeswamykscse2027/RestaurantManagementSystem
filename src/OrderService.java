import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private MongoCollection<Document> collection;

    public OrderService() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        collection = db.getCollection("orders");
    }

    public void addOrder(Order order) {

        Document document = new Document()
                .append("orderId", order.getId())
                .append("customerName", order.getCustomerName())
                .append("foodName", order.getFoodName())
                .append("quantity", order.getQuantity())
                .append("total", order.getTotal())
                .append("date", order.getDate());

        collection.insertOne(document);
    }

    public List<Order> getAllOrders() {

        List<Order> orders = new ArrayList<>();

        FindIterable<Document> documents = collection.find();

        for (Document document : documents) {

            orders.add(new Order(
                    document.getString("orderId"),
                    document.getString("customerName"),
                    document.getString("foodName"),
                    document.getInteger("quantity", 1),
                    document.getDouble("total"),
                    document.getString("date")
            ));
        }

        return orders;
    }

    public double getTotalRevenue() {

        double revenue = 0;

        for (Order order : getAllOrders()) {
            revenue += order.getTotal();
        }

        return revenue;
    }

    public long getOrderCount() {
        return collection.countDocuments();
    }
}