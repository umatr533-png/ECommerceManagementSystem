
package service;

import model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException(
                    "Product cannot be null.");
        }

        if (searchProduct(product.getProductId()) != null) {
            throw new IllegalArgumentException(
                    "Product ID already exists.");
        }

        products.add(product);
        System.out.println("Product added successfully!");
    }

    public void viewProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        for (Product product : products) {
            System.out.println("--------------------");
            System.out.println(product);
        }
    }

    public Product searchProduct(int productId) {
        for (Product product : products) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }

    public boolean updatePrice(int productId, double newPrice) {
        Product product = searchProduct(productId);

        if (product == null) {
            System.out.println("Product not found.");
            return false;
        }

        product.setPrice(newPrice);
        System.out.println("Product price updated successfully!");
        return true;
    }

    public boolean updateStock(int productId, int newQuantity) {
        Product product = searchProduct(productId);

        if (product == null) {
            System.out.println("Product not found.");
            return false;
        }

        product.setQuantity(newQuantity);
        System.out.println("Product stock updated successfully!");
        return true;
    }

    public boolean removeProduct(int productId) {
        Product product = searchProduct(productId);

        if (product == null) {
            System.out.println("Product not found.");
            return false;
        }

        products.remove(product);
        System.out.println("Product removed successfully!");
        return true;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
}
