import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private MongoCollection<Document> collection;

    public CustomerService() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        collection = db.getCollection("customers");
    }

    public void addCustomer(Customer customer) {

        Document document = new Document()
                .append("customerId", customer.getId())
                .append("name", customer.getName())
                .append("phone", customer.getPhone())
                .append("email", customer.getEmail());

        collection.insertOne(document);
    }

    public void addCustomer(String name,
                            String phone,
                            String email) {

        addCustomer(new Customer(
                name,
                phone,
                email
        ));
    }

    public List<Customer> getAllCustomers() {

        List<Customer> customers = new ArrayList<>();

        FindIterable<Document> documents = collection.find();

        for (Document document : documents) {

            customers.add(new Customer(
                    document.getString("customerId"),
                    document.getString("name"),
                    document.getString("phone"),
                    document.getString("email")
            ));
        }

        return customers;
    }

    public boolean deleteCustomer(String id) {

        var result = collection.deleteOne(
                new Document("customerId", id)
        );

        return result.getDeletedCount() > 0;
    }

    public long getCustomerCount() {
        return collection.countDocuments();
    }
}