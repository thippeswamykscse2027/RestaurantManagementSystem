public class Customer {

    private String id;
    private String name;
    private String phone;
    private String email;

    // Constructor used by Main.java
    public Customer(String name, String phone, String email) {
        this.id = "C" + System.currentTimeMillis();
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // Constructor used when reading from MongoDB
    public Customer(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}