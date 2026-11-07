
package service;

import model.CartItem;
import model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartService {

    // Each customer has a separate shopping cart.
    private final Map<Integer, List<CartItem>> customerCarts =
            new HashMap<>();

    // Get the cart belonging to a specific customer.
    private List<CartItem> getCustomerCart(int customerId) {

        if (customerId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid customer ID.");
        }

        return customerCarts.computeIfAbsent(
                customerId, id -> new ArrayList<>());
    }

    // Add a product to a customer's cart.
    public void addToCart(
            int customerId, Product product, int quantity) {

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product not found.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero.");
        }

        List<CartItem> cartItems = getCustomerCart(customerId);

        for (CartItem item : cartItems) {

            if (item.getProduct().getProductId()
                    == product.getProductId()) {

                int newQuantity;

                try {
                    newQuantity = Math.addExact(
                            item.getQuantity(), quantity);
                } catch (ArithmeticException e) {
                    throw new IllegalArgumentException(
                            "Requested quantity is too large.");
                }

                if (newQuantity > product.getQuantity()) {
                    throw new IllegalArgumentException(
                            "Requested quantity exceeds available stock.");
                }

                item.setQuantity(newQuantity);

                System.out.println(
                        "Cart quantity updated successfully!");
                return;
            }
        }

        if (quantity > product.getQuantity()) {
            throw new IllegalArgumentException(
                    "Not enough stock available.");
        }

        cartItems.add(new CartItem(product, quantity));

        System.out.println(
                "Product added to cart successfully!");
    }

    // Display the logged-in customer's cart.
    public void viewCart(int customerId) {

        List<CartItem> cartItems = getCustomerCart(customerId);

        if (cartItems.isEmpty()) {
            System.out.println(
                    "Your shopping cart is empty.");
            return;
        }

        System.out.println("\n===== YOUR SHOPPING CART =====");

        for (CartItem item : cartItems) {
            System.out.println("--------------------");
            System.out.println(item);
        }

        System.out.println("--------------------");
        System.out.println(
                "Total: Rs. " + getTotal(customerId));
    }

    // Calculate the total for a specific customer's cart.
    public double getTotal(int customerId) {

        double total = 0;

        for (CartItem item : getCustomerCart(customerId)) {
            total += item.getSubtotal();
        }

        return total;
    }

    // Update the quantity of a product in a customer's cart.
    public void updateCartQuantity(
            int customerId, int productId, int newQuantity) {

        if (newQuantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero.");
        }

        for (CartItem item : getCustomerCart(customerId)) {

            if (item.getProduct().getProductId() == productId) {

                if (newQuantity > item.getProduct().getQuantity()) {
                    throw new IllegalArgumentException(
                            "Requested quantity exceeds available stock.");
                }

                item.setQuantity(newQuantity);

                System.out.println(
                        "Cart quantity updated successfully!");
                return;
            }
        }

        System.out.println(
                "Product not found in your cart.");
    }

    // Remove a product from a specific customer's cart.
    public void removeFromCart(int customerId, int productId) {

        List<CartItem> cartItems = getCustomerCart(customerId);

        boolean removed = cartItems.removeIf(
                item -> item.getProduct().getProductId()
                        == productId);

        if (removed) {
            System.out.println(
                    "Product removed from cart.");
        } else {
            System.out.println(
                    "Product not found in your cart.");
        }
    }

    // Return a copy of the customer's cart items.
    public List<CartItem> getCartItems(int customerId) {

        return new ArrayList<>(
                getCustomerCart(customerId));
    }

    // Check whether a specific customer's cart is empty.
    public boolean isEmpty(int customerId) {

        return getCustomerCart(customerId).isEmpty();
    }

    // Clear only the specified customer's cart.
    public void clearCart(int customerId) {

        getCustomerCart(customerId).clear();
    }
}
