
package model;

public class Payment {

    private int paymentId;
    private int orderId;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;

    public Payment(int paymentId, int orderId, double amount,
                   String paymentMethod) {

        if (paymentId <= 0) {
            throw new IllegalArgumentException(
                    "Payment ID must be positive.");
        }

        if (orderId <= 0) {
            throw new IllegalArgumentException(
                    "Order ID must be positive.");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero.");
        }

        if (paymentMethod == null
                || !(paymentMethod.equalsIgnoreCase("UPI")
                || paymentMethod.equalsIgnoreCase("CARD")
                || paymentMethod.equalsIgnoreCase("COD"))) {
            throw new IllegalArgumentException(
                    "Payment method must be UPI, CARD, or COD.");
        }

        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod.toUpperCase();
        this.paymentStatus = "PENDING";
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        if (paymentStatus == null || paymentStatus.isBlank()) {
            throw new IllegalArgumentException(
                    "Payment status cannot be empty.");
        }

        this.paymentStatus = paymentStatus.toUpperCase();
    }

    @Override
    public String toString() {
        return "\n===== PAYMENT DETAILS ====="
                + "\nPayment ID: " + paymentId
                + "\nOrder ID: " + orderId
                + "\nAmount: Rs. " + amount
                + "\nPayment Method: " + paymentMethod
                + "\nPayment Status: " + paymentStatus;
    }
}
