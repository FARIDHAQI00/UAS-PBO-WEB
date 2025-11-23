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
 * Komponen utilitas yang bertanggung jawab memuat data default
 * ke dalam aplikasi saat startup. Mengimplementasikan CommandLineRunner
 * sehingga proses inisialisasi data akan dijalankan otomatis ketika aplikasi dijalankan.
 *
 * Fungsi utama adalah memastikan bahwa data pengguna dan produk default
 * sudah tersedia di lingkungan aplikasi, membuat entri default jika belum ada.
 *
 * Konsep OOP yang digunakan:
 * - Implementasi Interface: Mengimplementasikan CommandLineRunner untuk mengatur proses inisialisasi saat runtime.
 * - Enkapsulasi: Menyimpan path file data sebagai atribut private dengan akses terkontrol melalui anotasi Spring.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Value("${uas.data.users}")
    private String usersPath; // Path ke file data pengguna (JSON)

    @Value("${uas.data.products}")
    private String productsPath; // Path ke file data produk (JSON)

    /**
     * Metode yang dijalankan pada saat aplikasi mulai berjalan,
     * bertugas untuk mengecek dan memuat data default jika belum tersedia.
     */
    @Override
    public void run(String... args) throws Exception {
        var userRepo = new FileRepository<User>(usersPath, User[].class);
        var prodRepo = new FileRepository<Product>(productsPath, Product[].class);

        // Membuat data pengguna default jika belum ada
        if (userRepo.readAll().isEmpty()) {
            User admin = new User(UUID.randomUUID().toString(), "admin@uas", "admin123", "ADMIN");
            User user = new User(UUID.randomUUID().toString(), "user@uas", "user123", "USER");
            userRepo.saveAll(List.of(admin, user).stream().toList());
            System.out.println("Created default users");
        }

        // Membuat data produk default jika belum ada
        if (prodRepo.readAll().isEmpty()) {
            Product p1 = new Product(UUID.randomUUID().toString(), "Nasi Goreng", 15000, 10);
            Product p2 = new Product(UUID.randomUUID().toString(), "Mie Goreng", 12000, 15);
            Product p3 = new Product(UUID.randomUUID().toString(), "Es Teh", 5000, 30);
            prodRepo.saveAll(List.of(p1,p2,p3).stream().toList());
            System.out.println("Created default products");
        }
    }
}
