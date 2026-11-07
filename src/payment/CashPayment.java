
package payment;

public class CashPayment implements Payment {

    @Override
    public boolean processPayment(double amount) {

        System.out.println("Cash on Delivery selected.");
        System.out.println("Amount payable on delivery: Rs. " + amount);

        return true;
    }
}
