
package model;

public class OrderItem {

    private int productId;
    private String productName;
    private double price;
    private int quantity;

    public OrderItem(int productId, String productName,
                     double price, int quantity) {

        if (productId <= 0) {
            throw new IllegalArgumentException(
                    "Product ID must be positive.");
        }

        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException(
                    "Product name cannot be empty.");
        }

        if (price < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero.");
        }

        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return "Product: " + productName
                + "\nProduct ID: " + productId
                + "\nPrice: Rs. " + price
                + "\nQuantity: " + quantity
                + "\nSubtotal: Rs. " + getSubtotal();
    }
}
