
package service;

import model.CartItem;
import model.Order;
import model.OrderItem;
import model.Payment;
import model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private int nextOrderId = 1;

    // Create an order using only the specified customer's cart.
    public Order createOrder(int customerId, CartService cartService) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID.");
        }

        if (cartService == null || cartService.isEmpty(customerId)) {
            throw new IllegalArgumentException(
                    "Cannot place an order with an empty cart.");
        }

        List<CartItem> cartItems = cartService.getCartItems(customerId);
        List<OrderItem> orderItems = new ArrayList<>();
        Map<Integer, Integer> requestedQuantities = new HashMap<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            if (product == null || quantity <= 0) {
                throw new IllegalArgumentException(
                        "Invalid product or quantity in cart.");
            }

            requestedQuantities.merge(
                    product.getProductId(), quantity, Integer::sum);

            orderItems.add(new OrderItem(
                    product.getProductId(),
                    product.getProductName(),
                    product.getPrice(),
                    quantity));
        }

        // Validate stock before creating the order.
        for (Map.Entry<Integer, Integer> entry
                : requestedQuantities.entrySet()) {

            Product product = null;

            for (CartItem cartItem : cartItems) {
                if (cartItem.getProduct().getProductId()
                        == entry.getKey()) {
                    product = cartItem.getProduct();
                    break;
                }
            }

            if (product == null
                    || product.getQuantity() < entry.getValue()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product ID "
                                + entry.getKey());
            }
        }

        Order order = new Order(nextOrderId, customerId, orderItems);
        orders.add(order);
        nextOrderId++;

        cartService.clearCart(customerId);

        System.out.println("Order placed successfully!");
        System.out.println(order);

        return order;
    }

    // View all orders.
    public void viewAllOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        for (Order order : orders) {
            System.out.println(order);
            System.out.println("--------------------");
        }
    }

    // View orders belonging to a specific customer.
    public void viewCustomerOrders(int customerId) {
        boolean found = false;

        for (Order order : orders) {
            if (order.getCustomerId() == customerId) {
                System.out.println(order);
                System.out.println("--------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No orders found for this customer.");
        }
    }

    // Find an order by ID.
    public Order findOrder(int orderId) {
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }

        return null;
    }

    // Cancel an order belonging to the logged-in customer.
    public boolean cancelOrder(
            int orderId,
            int customerId,
            ProductService productService,
            PaymentService paymentService) {

        Order order = findOrder(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return false;
        }

        if (order.getCustomerId() != customerId) {
            System.out.println(
                    "You cannot cancel another customer's order.");
            return false;
        }

        String status = order.getStatus();

        if ("CANCELLED".equalsIgnoreCase(status)) {
            System.out.println("This order is already cancelled.");
            return false;
        }

        if ("DELIVERED".equalsIgnoreCase(status)) {
            System.out.println("A delivered order cannot be cancelled.");
            return false;
        }

        if ("SHIPPED".equalsIgnoreCase(status)) {
            System.out.println("A shipped order cannot be cancelled.");
            return false;
        }

        if (!"PLACED".equalsIgnoreCase(status)
                && !"CONFIRMED".equalsIgnoreCase(status)) {
            System.out.println(
                    "This order cannot be cancelled in its current status.");
            return false;
        }

        Payment payment = paymentService == null
                ? null
                : paymentService.findPaymentByOrderId(orderId);

        boolean onlinePaymentSucceeded = payment != null
                && "SUCCESS".equalsIgnoreCase(payment.getPaymentStatus())
                && ("UPI".equalsIgnoreCase(payment.getPaymentMethod())
                || "CARD".equalsIgnoreCase(payment.getPaymentMethod()));

        // Validate every product before restoring stock.
        if (onlinePaymentSucceeded) {
            if (productService == null) {
                System.out.println(
                        "Cannot restore stock: ProductService is missing.");
                return false;
            }

            for (OrderItem item : order.getOrderItems()) {
                Product product =
                        productService.searchProduct(item.getProductId());

                if (product == null) {
                    System.out.println(
                            "Cannot cancel safely: product ID "
                                    + item.getProductId() + " was not found.");
                    return false;
                }
            }

            // Restore stock deducted by successful online payment.
            for (OrderItem item : order.getOrderItems()) {
                Product product =
                        productService.searchProduct(item.getProductId());

                product.setQuantity(
                        product.getQuantity() + item.getQuantity());
            }
        }

        order.setStatus("CANCELLED");

        System.out.println("Order cancelled successfully!");

        if (onlinePaymentSucceeded) {
            System.out.println("Stock restored for the cancelled order.");
            System.out.println(
                    "Note: This prototype does not process real refunds.");
        }

        return true;
    }

    // Update order status with transition checks.
    public boolean updateOrderStatus(int orderId, String newStatus) {
        Order order = findOrder(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return false;
        }

        if (newStatus == null || newStatus.isBlank()) {
            throw new IllegalArgumentException(
                    "Order status cannot be empty.");
        }

        String currentStatus = order.getStatus().toUpperCase();
        String requestedStatus = newStatus.trim().toUpperCase();

        if (currentStatus.equals("CANCELLED")) {
            System.out.println(
                    "A cancelled order cannot be shipped or delivered.");
            return false;
        }

        if (currentStatus.equals("DELIVERED")) {
            System.out.println(
                    "A delivered order cannot change status.");
            return false;
        }

        if (requestedStatus.equals("CANCELLED")) {
            System.out.println(
                    "Use cancelOrder() to cancel an order.");
            return false;
        }

        // COD orders remain PLACED; online-paid orders are CONFIRMED.
        // Both can be shipped. Only SHIPPED orders can be delivered.
        boolean validTransition =
                ((currentStatus.equals("PLACED")
                        || currentStatus.equals("CONFIRMED"))
                        && requestedStatus.equals("SHIPPED"))
                        || (currentStatus.equals("SHIPPED")
                        && requestedStatus.equals("DELIVERED"));

        if (!validTransition) {
            System.out.println(
                    "Invalid status transition: "
                            + currentStatus + " to " + requestedStatus);
            return false;
        }

        order.setStatus(requestedStatus);

        System.out.println("Order status updated successfully!");
        return true;
    }
}
