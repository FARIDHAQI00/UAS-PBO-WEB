package id.univ.uaspbo.util;

import id.univ.uaspbo.model.Product;
import id.univ.uaspbo.model.User;
import id.univ.uaspbo.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Utility component that loads default data into the application on startup.
 * Implements CommandLineRunner to execute data initialization when the application starts.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Value("${uas.data.users}")
    private String usersPath;

    @Value("${uas.data.products}")
    private String productsPath;

    /**
     * Runs the data loading process on application startup.
     * Creates default users and products if they don't exist.
     *
     * @param args Command line arguments (not used)
     * @throws Exception If data loading fails
     */
    @Override
    public void run(String... args) throws Exception {
        var userRepo = new FileRepository<User>(usersPath, User[].class);
        var prodRepo = new FileRepository<Product>(productsPath, Product[].class);

        // create default users if none
        if (userRepo.readAll().isEmpty()) {
            User admin = new User(UUID.randomUUID().toString(), "admin@uas", "admin123", "ADMIN");
            User user = new User(UUID.randomUUID().toString(), "user@uas", "user123", "USER");
            userRepo.saveAll(List.of(admin, user).stream().toList());
            System.out.println("Created default users");
        }

        if (prodRepo.readAll().isEmpty()) {
            Product p1 = new Product(UUID.randomUUID().toString(), "Nasi Goreng", 15000, 10);
            Product p2 = new Product(UUID.randomUUID().toString(), "Mie Goreng", 12000, 15);
            Product p3 = new Product(UUID.randomUUID().toString(), "Es Teh", 5000, 30);
            prodRepo.saveAll(List.of(p1,p2,p3).stream().toList());
            System.out.println("Created default products");
        }
    }
}
