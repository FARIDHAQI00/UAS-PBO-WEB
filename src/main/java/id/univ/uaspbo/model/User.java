package id.univ.uaspbo.model;

/**
 * User model representing system users with different roles.
 */
public class User extends Entity {
    private String email;
    private String password;
    private String role; // "ADMIN" or "USER"

    public User() {}

    public User(String id, String email, String password, String role) {
        super(id);
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
