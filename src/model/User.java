
package model;

public class User {

    private int userId;
    private String name;
    private String email;
    private String password;
    private String role;

    public User(int userId, String name, String email,
                String password, String role) {

        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be positive.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Name cannot be empty.");
        }

        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException(
                    "Enter a valid email address.");
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException(
                    "Password must contain at least 6 characters.");
        }

        if (!"CUSTOMER".equals(role) && !"ADMIN".equals(role)) {
            throw new IllegalArgumentException(
                    "Role must be CUSTOMER or ADMIN.");
        }

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
