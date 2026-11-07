
package model;

public class CartItem {

    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Cart quantity must be greater than zero.");
        }

        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Cart quantity must be greater than zero.");
        }

        this.quantity = quantity;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "Product: " + product.getProductName()
                + "\nPrice: Rs. " + product.getPrice()
                + "\nQuantity: " + quantity
                + "\nSubtotal: Rs. " + getSubtotal();
    }
}
