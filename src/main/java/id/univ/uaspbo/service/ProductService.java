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
 * Konsep OOP yang diterapkan dalam kelas ini meliputi:
 * - Pewarisan (Inheritance): Memanfaatkan implementasi dasar CRUD dari AbstractService agar tidak perlu mengulang kode.
 * - Enkapsulasi: Memproteksi variabel produkPath dan pengaksesan data produk melalui method yang disediakan.
 * - Polimorfisme: Meng-override method abstrak untuk mengatur spesifikasi tipe data produk dan identitas produk secara spesifik.
 */
@Service
public class ProductService extends AbstractService<Product> {

    @Value("${uas.data.products}")
    private String productsPath;  // Path file data produk

    /**
     * Mendapatkan path data produk untuk repository file.
     *
     * @return Path file produk
     */
    @Override
    protected String getDataPath() {
        return productsPath;
    }

    /**
     * Mendapatkan kelas array tipe Product untuk deserialisasi JSON.
     *
     * @return Kelas array produk
     */
    @Override
    protected Class<Product[]> getTypeClass() {
        return Product[].class;
    }

    /**
     * Mendapatkan ID dari objek produk.
     *
     * @param product Produk
     * @return ID produk
     */
    @Override
    protected String getEntityId(Product product) {
        return product.getId();
    }

    /**
     * Mengatur ID pada objek produk.
     *
     * @param product Produk
     * @param id ID yang akan diset
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
