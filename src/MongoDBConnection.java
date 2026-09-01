import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static final String URI =
            "mongodb://localhost:27017";

    private static final String DATABASE_NAME =
            "RestaurantManagementDB";

    public static MongoDatabase getDatabase() {

        MongoClient client = MongoClients.create(URI);

        return client.getDatabase(DATABASE_NAME);
    }
}