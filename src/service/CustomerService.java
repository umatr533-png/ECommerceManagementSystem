
package service;

import model.Customer;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    private final List<Customer> customers = new ArrayList<>();
    private int nextCustomerId = 1;

    // Register a new customer
    public Customer registerCustomer(String name, String email,
                                     String password, String address) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        email = email.trim();

        // Check whether the email is already registered
        for (Customer customer : customers) {
            if (customer.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException(
                        "This email is already registered.");
            }
        }

        // Create and store the customer
        Customer customer = new Customer(
                nextCustomerId, name, email, password, address);

        customers.add(customer);
        nextCustomerId++;

        System.out.println("Registration successful!");
        System.out.println("Your Customer ID is: " + customer.getUserId());

        return customer;
    }

    // Login an existing customer
    public Customer login(String email, String password) {

        if (email == null || password == null) {
            System.out.println("Invalid email or password.");
            return null;
        }

        for (Customer customer : customers) {
            if (customer.getEmail().equalsIgnoreCase(email.trim())
                    && customer.getPassword().equals(password)) {

                System.out.println("Login successful!");
                System.out.println("Welcome, " + customer.getName() + "!");

                return customer;
            }
        }

        System.out.println("Invalid email or password.");
        return null;
    }

    // View all registered customers
    public void viewCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers registered yet.");
            return;
        }

        for (Customer customer : customers) {
            System.out.println("--------------------");
            System.out.println("Customer ID: " + customer.getUserId());
            System.out.println("Name: " + customer.getName());
            System.out.println("Email: " + customer.getEmail());
            System.out.println("Address: " + customer.getAddress());
        }
    }
}
