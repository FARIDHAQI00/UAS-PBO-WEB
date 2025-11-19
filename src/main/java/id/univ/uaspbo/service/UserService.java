package id.univ.uaspbo.service;

import id.univ.uaspbo.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing User entities with authentication functionality.
 */
@Service
public class UserService extends AbstractService<User> {

    @Value("${uas.data.users}")
    private String usersPath;

    @Override
    protected String getDataPath() {
        return usersPath;
    }

    @Override
    protected Class<User[]> getTypeClass() {
        return User[].class;
    }

    @Override
    protected String getEntityId(User user) {
        return user.getId();
    }

    @Override
    protected void setEntityId(User user, String id) {
        user.setId(id);
    }

    /**
     * Authenticate user by email and password.
     * @param email User email
     * @param password User password
     * @return User if authenticated, null otherwise
     */
    public User authenticate(String email, String password) {
        for (User u : getAll()) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) return u;
        }
        return null;
    }

    /**
     * Find user by email.
     * @param email User email
     * @return User if found, null otherwise
     */
    public User findByEmail(String email) {
        for (User u : getAll()) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    /**
     * Register a new user with USER role.
     * @param email User email
     * @param password User password
     * @return true if registration successful, false if email already exists
     */
    public boolean registerUser(String email, String password) {
        if (findByEmail(email) != null) {
            return false; // Email already exists
        }
        User newUser = new User(null, email, password, "USER");
        add(newUser);
        return true;
    }
}
