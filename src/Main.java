
import model.Customer;
import model.Order;
import model.Product;
import service.CartService;
import service.CustomerService;
import service.OrderService;
import service.PaymentService;
import service.ProductService;
import service.AdminService;
import service.DiscountService;

import java.util.Scanner;

public class Main {

    static AdminService adminService = new AdminService();
    static boolean isAdminLoggedIn = false;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ProductService productService = new ProductService();
        CustomerService customerService = new CustomerService();
        CartService cartService = new CartService();
        OrderService orderService = new OrderService();
        PaymentService paymentService = new PaymentService();
        DiscountService discountService = new DiscountService();

        Customer loggedInCustomer = null;
        boolean running = true;

        while (running) {

            System.out.println("\n===== E-COMMERCE MANAGEMENT SYSTEM =====");
            System.out.println("1. Register Customer");
            System.out.println("2. Customer Login");
            System.out.println("3. View Registered Customers");
            System.out.println("4. Product Management");
            System.out.println("5. Shopping Cart");
            System.out.println("6. Checkout and Payment");
            System.out.println("7. View My Orders");
            System.out.println("8. View All Payments");
            System.out.println("9. Cancel My Order");
            System.out.println("10. Admin Order Management");
            System.out.println("11. Logout");
            System.out.println("12. Admin Login");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {

                    case "1": {
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();

                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();

                        System.out.print("Enter address: ");
                        String address = scanner.nextLine();

                        customerService.registerCustomer(
                                name, email, password, address);
                        break;
                    }

                    case "2": {
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();

                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();

                        Customer customer = customerService.login(
                                email, password);

                        if (customer != null) {
                            if (isAdminLoggedIn) {
                                System.out.println(
                                        "Please log out of the admin account first.");
                            } else {
                                loggedInCustomer = customer;
                                System.out.println("You are now logged in.");
                            }
                        }
                        break;
                    }

                    case "3":
                        if (!isAdminLoggedIn) {
                            System.out.println(
                                    "Access denied. Admin login required.");
                        } else {
                            customerService.viewCustomers();
                        }
                        break;

                    case "4":
                        if (!isAdminLoggedIn) {
                            System.out.println(
                                    "Access denied. Please log in as admin first.");
                        } else {
                            manageProducts(scanner, productService);
                        }
                        break;

                    case "5":
                        if (loggedInCustomer == null) {
                            System.out.println(
                                    "Please log in as a customer to use the shopping cart.");
                        } else {
                            manageCart(
                                    scanner,
                                    productService,
                                    cartService,
                                    loggedInCustomer.getUserId());
                        }
                        break;

                    case "6": {
                        if (loggedInCustomer == null) {
                            System.out.println(
                                    "Please log in before checkout.");
                            break;
                        }

                        int customerId = loggedInCustomer.getUserId();

                        if (cartService.isEmpty(customerId)) {
                            System.out.println(
                                    "Your shopping cart is empty.");
                            break;
                        }

                        cartService.viewCart(customerId);

                        System.out.print(
                                "Proceed with checkout? (yes/no): ");
                        String confirmation =
                                scanner.nextLine().trim();

                        if (!confirmation.equalsIgnoreCase("yes")) {
                            System.out.println("Checkout cancelled.");
                            break;
                        }

                        // Calculate discount
                        double originalAmount =
                                cartService.getTotal(customerId);

                        double discountPercentage = 0;

                        System.out.print(
                                "Apply 10% discount? (yes/no): ");
                        String discountChoice =
                                scanner.nextLine().trim();

                        if (discountChoice.equalsIgnoreCase("yes")) {
                            discountPercentage = 10;
                        }

                        double finalAmount =
                                discountService.calculateFinalAmount(
                                        originalAmount,
                                        discountPercentage);

                        discountService.displayDiscount(
                                originalAmount,
                                discountPercentage);

                        // Select payment method
                        System.out.println("\n===== PAYMENT METHOD =====");
                        System.out.println("1. UPI");
                        System.out.println("2. Card");
                        System.out.println("3. Cash on Delivery");
                        System.out.print("Enter choice: ");

                        String paymentChoice =
                                scanner.nextLine().trim();

                        String paymentMethod;

                        if (paymentChoice.equals("1")) {
                            paymentMethod = "UPI";
                        } else if (paymentChoice.equals("2")) {
                            paymentMethod = "CARD";
                        } else if (paymentChoice.equals("3")) {
                            paymentMethod = "COD";
                        } else {
                            System.out.println(
                                    "Invalid payment choice. "
                                            + "Your cart has been kept.");
                            break;
                        }

                        // Create order
                        Order order = orderService.createOrder(
                                customerId,
                                cartService);

                        if (order == null) {
                            System.out.println(
                                    "Unable to create your order.");
                            break;
                        }

                        // Apply discounted amount to the order
                        order.setTotalAmount(finalAmount);

                        // Process payment
                        paymentService.makePayment(
                                order,
                                paymentMethod,
                                productService);

                        break;
                    }

                    case "7":
                        if (loggedInCustomer == null) {
                            System.out.println(
                                    "Please log in to view your orders.");
                        } else {
                            orderService.viewCustomerOrders(
                                    loggedInCustomer.getUserId());
                        }
                        break;

                    case "8":
                        if (!isAdminLoggedIn) {
                            System.out.println(
                                    "Access denied. Admin login required.");
                        } else {
                            paymentService.viewAllPayments();
                        }
                        break;

                    case "9": {
                        if (loggedInCustomer == null) {
                            System.out.println(
                                    "Please log in to cancel an order.");
                            break;
                        }

                        System.out.print(
                                "Enter Order ID to cancel: ");
                        int orderId = Integer.parseInt(
                                scanner.nextLine().trim());

                        orderService.cancelOrder(
                                orderId,
                                loggedInCustomer.getUserId(),
                                productService,
                                paymentService);
                        break;
                    }

                    case "10": {
                        if (!isAdminLoggedIn) {
                            System.out.println(
                                    "Access denied. Please log in as admin first.");
                            break;
                        }

                        System.out.println(
                                "\n===== ADMIN ORDER MANAGEMENT =====");
                        System.out.println("1. View All Orders");
                        System.out.println("2. Mark Order as Shipped");
                        System.out.println("3. Mark Order as Delivered");
                        System.out.println("0. Back to Main Menu");
                        System.out.print("Enter your choice: ");

                        String adminChoice =
                                scanner.nextLine().trim();

                        switch (adminChoice) {

                            case "1":
                                orderService.viewAllOrders();
                                break;

                            case "2": {
                                System.out.print(
                                        "Enter Order ID to ship: ");
                                int orderId = Integer.parseInt(
                                        scanner.nextLine().trim());

                                orderService.updateOrderStatus(
                                        orderId, "SHIPPED");
                                break;
                            }

                            case "3": {
                                System.out.print(
                                        "Enter Order ID to deliver: ");
                                int orderId = Integer.parseInt(
                                        scanner.nextLine().trim());

                                orderService.updateOrderStatus(
                                        orderId, "DELIVERED");
                                break;
                            }

                            case "0":
                                break;

                            default:
                                System.out.println(
                                        "Invalid admin menu choice.");
                        }
                        break;
                    }

                    case "11":
                        if (loggedInCustomer != null) {
                            loggedInCustomer = null;
                            System.out.println(
                                    "Customer logout successful.");
                        } else if (isAdminLoggedIn) {
                            isAdminLoggedIn = false;
                            System.out.println(
                                    "Admin logout successful.");
                        } else {
                            System.out.println(
                                    "No user is currently logged in.");
                        }
                        break;

                    case "12": {
                        if (isAdminLoggedIn) {
                            System.out.println(
                                    "Admin is already logged in.");
                            break;
                        }

                        if (loggedInCustomer != null) {
                            System.out.println(
                                    "Please log out of the customer account first.");
                            break;
                        }

                        System.out.print("Enter admin username: ");
                        String username = scanner.nextLine().trim();

                        System.out.print("Enter admin password: ");
                        String password = scanner.nextLine();

                        if (adminService.login(username, password)) {
                            isAdminLoggedIn = true;
                            System.out.println(
                                    "Admin login successful!");
                        } else {
                            System.out.println(
                                    "Invalid admin username or password.");
                        }
                        break;
                    }

                    case "0":
                        running = false;
                        System.out.println(
                                "Thank you for using the system!");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void manageProducts(
            Scanner scanner,
            ProductService productService) {

        boolean menuRunning = true;

        while (menuRunning) {

            System.out.println("\n===== PRODUCT MANAGEMENT =====");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Search Product");
            System.out.println("4. Update Product Price");
            System.out.println("5. Update Product Stock");
            System.out.println("6. Remove Product");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {

                    case "1": {
                        System.out.print("Enter product ID: ");
                        int id = Integer.parseInt(
                                scanner.nextLine().trim());

                        System.out.print("Enter product name: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter category: ");
                        String category = scanner.nextLine();

                        System.out.print("Enter price: ");
                        double price = Double.parseDouble(
                                scanner.nextLine().trim());

                        System.out.print("Enter stock quantity: ");
                        int quantity = Integer.parseInt(
                                scanner.nextLine().trim());

                        productService.addProduct(
                                new Product(
                                        id, name, category, price, quantity));
                        break;
                    }

                    case "2":
                        productService.viewProducts();
                        break;

                    case "3": {
                        System.out.print("Enter product ID: ");
                        int id = Integer.parseInt(
                                scanner.nextLine().trim());

                        Product product =
                                productService.searchProduct(id);

                        if (product != null) {
                            System.out.println(product);
                        } else {
                            System.out.println("Product not found.");
                        }
                        break;
                    }

                    case "4": {
                        System.out.print("Enter product ID: ");
                        int id = Integer.parseInt(
                                scanner.nextLine().trim());

                        System.out.print("Enter new price: ");
                        double price = Double.parseDouble(
                                scanner.nextLine().trim());

                        productService.updatePrice(id, price);
                        break;
                    }

                    case "5": {
                        System.out.print("Enter product ID: ");
                        int id = Integer.parseInt(
                                scanner.nextLine().trim());

                        System.out.print("Enter new stock quantity: ");
                        int quantity = Integer.parseInt(
                                scanner.nextLine().trim());

                        productService.updateStock(id, quantity);
                        break;
                    }

                    case "6": {
                        System.out.print(
                                "Enter product ID to remove: ");
                        int id = Integer.parseInt(
                                scanner.nextLine().trim());

                        productService.removeProduct(id);
                        break;
                    }

                    case "0":
                        menuRunning = false;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void manageCart(
            Scanner scanner,
            ProductService productService,
            CartService cartService,
            int customerId) {

        boolean menuRunning = true;

        while (menuRunning) {

            System.out.println("\n===== SHOPPING CART =====");
            System.out.println("1. Add Product to Cart");
            System.out.println("2. View Cart");
            System.out.println("3. Update Cart Quantity");
            System.out.println("4. Remove Product from Cart");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {

                    case "1": {
                        System.out.print("Enter product ID: ");
                        int productId = Integer.parseInt(
                                scanner.nextLine().trim());

                        System.out.print("Enter quantity: ");
                        int quantity = Integer.parseInt(
                                scanner.nextLine().trim());

                        Product product =
                                productService.searchProduct(productId);

                        if (product == null) {
                            System.out.println("Product not found.");
                        } else {
                            cartService.addToCart(
                                    customerId, product, quantity);
                        }
                        break;
                    }

                    case "2":
                        cartService.viewCart(customerId);
                        break;

                    case "3": {
                        System.out.print("Enter product ID: ");
                        int productId = Integer.parseInt(
                                scanner.nextLine().trim());

                        System.out.print("Enter new quantity: ");
                        int quantity = Integer.parseInt(
                                scanner.nextLine().trim());

                        cartService.updateCartQuantity(
                                customerId, productId, quantity);
                        break;
                    }

                    case "4": {
                        System.out.print(
                                "Enter product ID to remove: ");
                        int productId = Integer.parseInt(
                                scanner.nextLine().trim());

                        cartService.removeFromCart(
                                customerId, productId);
                        break;
                    }

                    case "0":
                        menuRunning = false;
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
