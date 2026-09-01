public class Food {

    private String id;
    private String name;
    private String category;
    private double price;
    private boolean available;

    // Constructor used by Main.java
    public Food(String name,
                String category,
                double price,
                boolean available) {

        this.id = "F" + System.currentTimeMillis();
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    // Constructor used when reading from MongoDB
    public Food(String id,
                String name,
                String category,
                double price,
                boolean available) {

        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }
}