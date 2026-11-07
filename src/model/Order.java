
package model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int orderId;
    private int customerId;
    private List<OrderItem> orderItems;
    private double totalAmount;
    private String status;

    public Order(int orderId, int customerId,
                 List<OrderItem> orderItems) {

        if (orderId <= 0) {
            throw new IllegalArgumentException(
                    "Order ID must be positive.");
        }

        if (customerId <= 0) {
            throw new IllegalArgumentException(
                    "Customer ID must be positive.");
        }

        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "An order must contain at least one item.");
        }

        this.orderId = orderId;
        this.customerId = customerId;
        this.orderItems = new ArrayList<>(orderItems);
        this.status = "PLACED";

        calculateTotal();
    }

    private void calculateTotal() {
        totalAmount = 0;

        for (OrderItem item : orderItems) {
            totalAmount += item.getSubtotal();
        }
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    // Updates the order amount after applying a discount
    public void setTotalAmount(double totalAmount) {
        if (totalAmount < 0 || Double.isNaN(totalAmount)
                || Double.isInfinite(totalAmount)) {
            throw new IllegalArgumentException(
                    "Order amount must be a valid non-negative value.");
        }

        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException(
                    "Order status cannot be empty.");
        }

        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder details = new StringBuilder();

        details.append("\n===== ORDER DETAILS =====\n");
        details.append("Order ID: ").append(orderId).append("\n");
        details.append("Customer ID: ").append(customerId).append("\n");

        for (OrderItem item : orderItems) {
            details.append("--------------------\n");
            details.append(item).append("\n");
        }

        details.append("--------------------\n");
        details.append("Total Amount: Rs. ")
                .append(totalAmount).append("\n");
        details.append("Order Status: ").append(status);

        return details.toString();
    }
}
