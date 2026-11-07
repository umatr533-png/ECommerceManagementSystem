
package payment;

public class CardPayment implements Payment {

    @Override
    public boolean processPayment(double amount) {

        System.out.println("Processing Card payment...");
        System.out.println("Amount: Rs. " + amount);
        System.out.println("Card payment successful!");

        return true;
    }
}
