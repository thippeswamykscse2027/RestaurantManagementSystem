public class Order {

    private String id;
    private String customerName;
    private String foodName;
    private int quantity;
    private double total;
    private String date;

    public Order(String id,
                 String customerName,
                 String foodName,
                 int quantity,
                 double total,
                 String date) {

        this.id = id;
        this.customerName = customerName;
        this.foodName = foodName;
        this.quantity = quantity;
        this.total = total;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }
}