
package service;

public class AdminService {

    private final String adminUsername = "admin";
    private final String adminPassword = "Admin@123";

    public boolean login(String username, String password) {

        return adminUsername.equals(username)
                && adminPassword.equals(password);
    }
}
