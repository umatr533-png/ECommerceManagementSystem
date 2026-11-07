
package service;

import model.Order;
import model.OrderItem;
import model.Payment;
import model.Product;

import payment.UpiPayment;
import payment.CardPayment;
import payment.CashPayment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentService {

    private final List<Payment> payments = new ArrayList<>();
    private int nextPaymentId = 1;

    public Payment makePayment(
            Order order,
            String paymentMethod,
            ProductService productService) {

        // 1. Validate the order
        if (order == null) {
            throw new IllegalArgumentException(
                    "Order not found.");
        }

        if (productService == null) {
            throw new IllegalArgumentException(
                    "Product service is required.");
        }

        if (!"PLACED".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalArgumentException(
                    "Payment is allowed only for a placed order.");
        }

        // 2. Validate payment method
        if (paymentMethod == null) {
            throw new IllegalArgumentException(
                    "Choose UPI, CARD, or COD.");
        }

        paymentMethod = paymentMethod.trim().toUpperCase();

        if (!paymentMethod.equals("UPI")
                && !paymentMethod.equals("CARD")
                && !paymentMethod.equals("COD")) {

            throw new IllegalArgumentException(
                    "Choose UPI, CARD, or COD.");
        }

        // 3. Prevent duplicate payments
        if (findPaymentByOrderId(order.getOrderId()) != null) {
            throw new IllegalArgumentException(
                    "A payment record already exists for this order.");
        }

        // 4. Collect required product quantities
        Map<Integer, Integer> requiredStock = new HashMap<>();

        for (OrderItem item : order.getOrderItems()) {

            int productId = item.getProductId();
            int quantity = item.getQuantity();

            if (quantity <= 0) {
                throw new IllegalArgumentException(
                        "Order quantity must be greater than zero.");
            }

            requiredStock.put(
                    productId,
                    requiredStock.getOrDefault(productId, 0) + quantity
            );
        }

        // 5. Validate product availability and stock
        for (Map.Entry<Integer, Integer> entry
                : requiredStock.entrySet()) {

            Product product =
                    productService.searchProduct(entry.getKey());

            if (product == null) {
                throw new IllegalArgumentException(
                        "Product ID " + entry.getKey()
                                + " no longer exists.");
            }

            if (product.getQuantity() < entry.getValue()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for "
                                + product.getProductName()
                                + ". Available: "
                                + product.getQuantity()
                                + ", Required: "
                                + entry.getValue());
            }
        }

        // 6. Select payment implementation
        payment.Payment paymentProcessor;

        switch (paymentMethod) {

            case "UPI":
                paymentProcessor = new UpiPayment();
                break;

            case "CARD":
                paymentProcessor = new CardPayment();
                break;

            case "COD":
                paymentProcessor = new CashPayment();
                break;

            default:
                throw new IllegalArgumentException(
                        "Invalid payment method.");
        }

        // 7. Process payment
        boolean paymentSuccessful =
                paymentProcessor.processPayment(
                        order.getTotalAmount());

        // 8. Create payment record
        Payment payment = new Payment(
                nextPaymentId,
                order.getOrderId(),
                order.getTotalAmount(),
                paymentMethod
        );

        // 9. Handle failed payment
        if (!paymentSuccessful) {

            payment.setPaymentStatus("FAILED");

            payments.add(payment);
            nextPaymentId++;

            System.out.println("Payment failed.");
            System.out.println("Order not confirmed.");

            return payment;
        }

        // 10. Handle successful payment
        if (paymentMethod.equals("COD")) {

            payment.setPaymentStatus("PENDING");

            order.setStatus("PLACED");

            System.out.println(
                    "Cash on Delivery selected.");

            System.out.println(
                    "Payment is due on delivery.");

        } else {

            payment.setPaymentStatus("SUCCESS");

            // Reduce stock after successful online payment
            for (Map.Entry<Integer, Integer> entry
                    : requiredStock.entrySet()) {

                Product product =
                        productService.searchProduct(entry.getKey());

                product.setQuantity(
                        product.getQuantity() - entry.getValue());
            }

            order.setStatus("CONFIRMED");

            System.out.println("Payment successful!");
            System.out.println("Order confirmed.");
        }

        // 11. Save payment record
        payments.add(payment);
        nextPaymentId++;

        System.out.println(payment);

        return payment;
    }

    // Display all payment records
    public void viewAllPayments() {

        if (payments.isEmpty()) {
            System.out.println("No payment records found.");
            return;
        }

        for (Payment payment : payments) {
            System.out.println(payment);
            System.out.println("--------------------");
        }
    }

    // Find payment using payment ID
    public Payment findPayment(int paymentId) {

        for (Payment payment : payments) {

            if (payment.getPaymentId() == paymentId) {
                return payment;
            }
        }

        return null;
    }

    // Find payment using order ID
    public Payment findPaymentByOrderId(int orderId) {

        for (Payment payment : payments) {

            if (payment.getOrderId() == orderId) {
                return payment;
            }
        }

        return null;
    }
}
