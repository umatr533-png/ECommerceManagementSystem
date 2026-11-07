
package model;

public class Customer extends User {

    private String address;

    public Customer(int userId, String name, String email,
                    String password, String address) {

        super(userId, name, email, password, "CUSTOMER");

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException(
                    "Address cannot be empty.");
        }

        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException(
                    "Address cannot be empty.");
        }

        this.address = address;
    }
}
