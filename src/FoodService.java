import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class FoodService {

    private MongoCollection<Document> collection;

    public FoodService() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        collection = db.getCollection("foods");
    }

    public void addFood(Food food) {

        Document document = new Document()
                .append("foodId", food.getId())
                .append("name", food.getName())
                .append("category", food.getCategory())
                .append("price", food.getPrice())
                .append("available", food.isAvailable());

        collection.insertOne(document);
    }

    public void addFood(String name,
                        String category,
                        double price,
                        boolean available) {

        String id = "F" + System.currentTimeMillis();

        addFood(new Food(
                id,
                name,
                category,
                price,
                available
        ));
    }

    public List<Food> getAllFoods() {

        List<Food> foods = new ArrayList<>();

        FindIterable<Document> documents = collection.find();

        for (Document document : documents) {

            Double price = document.getDouble("price");

            foods.add(new Food(
                    document.getString("foodId"),
                    document.getString("name"),
                    document.getString("category"),
                    price != null ? price : 0.0,
                    document.getBoolean("available", true)
            ));
        }

        return foods;
    }

    public boolean deleteFood(String id) {

        var result = collection.deleteOne(
                new Document("foodId", id)
        );

        return result.getDeletedCount() > 0;
    }

    public long getFoodCount() {
        return collection.countDocuments();
    }
}