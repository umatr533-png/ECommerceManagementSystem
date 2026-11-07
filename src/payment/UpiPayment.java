
package payment;

public class UpiPayment implements Payment {

    @Override
    public boolean processPayment(double amount) {

        System.out.println("Processing UPI payment...");
        System.out.println("Amount: Rs. " + amount);
        System.out.println("UPI payment successful!");

        return true;
    }
}
