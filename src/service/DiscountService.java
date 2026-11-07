
package service;

public class DiscountService {

    // Calculate discount amount
    public double calculateDiscount(
            double totalAmount, double discountPercentage) {

        if (totalAmount < 0) {
            throw new IllegalArgumentException(
                    "Total amount cannot be negative.");
        }

        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException(
                    "Discount must be between 0 and 100 percent.");
        }

        return totalAmount * discountPercentage / 100;
    }

    // Calculate final amount after discount
    public double calculateFinalAmount(
            double totalAmount, double discountPercentage) {

        double discountAmount =
                calculateDiscount(totalAmount, discountPercentage);

        return totalAmount - discountAmount;
    }

    // Display discount details
    public void displayDiscount(
            double totalAmount, double discountPercentage) {

        double discountAmount =
                calculateDiscount(totalAmount, discountPercentage);

        double finalAmount =
                calculateFinalAmount(totalAmount, discountPercentage);

        System.out.println("\n===== DISCOUNT DETAILS =====");
        System.out.println("Original Amount: Rs. " + totalAmount);
        System.out.println("Discount: " + discountPercentage + "%");
        System.out.println("Discount Amount: Rs. " + discountAmount);
        System.out.println("Final Amount: Rs. " + finalAmount);
    }
}
