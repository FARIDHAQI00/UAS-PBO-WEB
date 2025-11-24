package id.univ.uaspbo.service;

import id.univ.uaspbo.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Kelas service yang bertanggung jawab mengelola semua operasi yang berhubungan dengan entitas Product.
 * Kelas ini mewarisi AbstractService untuk mendapatkan fungsi dasar CRUD (Create, Read, Update, Delete)
 * secara otomatis, serta memperluasnya dengan fitur tambahan seperti pencarian dan pengurutan produk.
 *
 * Kelas ini mengatur pengambilan data produk dari file JSON yang path-nya dikonfigurasi
 * melalui properti aplikasi, memastikan konsistensi penyimpanan dan pengambilan data.
 *
 * Konsep Object Oriented Programming (OOP) yang dipakai:
 * - Inheritance (Pewarisan): Memanfaatkan implementasi CRUD dasar dari AbstractService.
 * - Encapsulation (Enkapsulasi): Variabel productsPath bersifat private dan diakses lewat method.
 * - Polymorphism (Polimorfisme): Meng-override method abstrak dari AbstractService.
 */
@Service
public class ProductService extends AbstractService<Product> {

    @Value("${uas.data.products}")
    private String productsPath;  // Path file data produk

    /**
     * Mendapatkan path data produk untuk repository file.
     */
    @Override
    protected String getDataPath() {
        return productsPath;
    }

    /**
     * Mendapatkan kelas array tipe Product untuk deserialisasi JSON.
     */
    @Override
    protected Class<Product[]> getTypeClass() {
        return Product[].class;
    }

    /**
     * Mendapatkan ID dari objek produk.
     */
    @Override
    protected String getEntityId(Product product) {
        return product.getId();
    }

    /**
     * Mengatur ID pada objek produk.
     */
    @Override
    protected void setEntityId(Product product, String id) {
        product.setId(id);
    }

    /**
     * Melakukan pencarian produk berdasarkan nama produk (case-insensitive).
     *
     * @param query Kata kunci pencarian
     * @return Daftar produk yang memenuhi kriteria pencarian
     */
    public List<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAll();
        }
        String lowerQuery = query.toLowerCase();
        return getAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerQuery))
                .toList();
    }

    /**
     * Mengurutkan daftar produk berdasarkan kriteria tertentu.
     *
     * @param products Daftar produk yang akan diurutkan
     * @param sortBy Kriteria pengurutan ("name_asc", "name_desc", "price_asc", "price_desc")
     * @return Daftar produk yang sudah diurutkan
     */
    public List<Product> sortProducts(List<Product> products, String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return products;
        }
        return switch (sortBy) {
            case "name_asc" -> products.stream().sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).toList();
            case "name_desc" -> products.stream().sorted((a, b) -> b.getName().compareToIgnoreCase(a.getName())).toList();
            case "price_asc" -> products.stream().sorted((a, b) -> Integer.compare(a.getPrice(), b.getPrice())).toList();
            case "price_desc" -> products.stream().sorted((a, b) -> Integer.compare(b.getPrice(), a.getPrice())).toList();
            default -> products;
        };
    }
}
