
package model;

public class Grocery extends Product {

    public Grocery(int productId, String productName,
                   double price, int quantity) {

        super(productId, productName, "Grocery", price, quantity);
    }
}
