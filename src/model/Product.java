
package model;

public class Product {

    private int productId;
    private String productName;
    private String category;
    private double price;
    private int quantity;

    public Product(int productId, String productName,
                   String category, double price, int quantity) {

        if (productId <= 0) {
            throw new IllegalArgumentException(
                    "Product ID must be positive.");
        }

        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException(
                    "Product name cannot be empty.");
        }

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "Category cannot be empty.");
        }

        this.productId = productId;
        this.productName = productName;
        this.category = category;
        setPrice(price);
        setQuantity(quantity);
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductName(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException(
                    "Product name cannot be empty.");
        }
        this.productName = productName;
    }

    public void setCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "Category cannot be empty.");
        }
        this.category = category;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative.");
        }
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product ID: " + productId
                + "\nName: " + productName
                + "\nCategory: " + category
                + "\nPrice: Rs. " + price
                + "\nAvailable Stock: " + quantity;
    }
}
