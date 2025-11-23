package id.univ.uaspbo.service;

import id.univ.uaspbo.model.Transaction;
import id.univ.uaspbo.model.Product;
import id.univ.uaspbo.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Kelas service yang bertanggung jawab mengelola seluruh operasi yang berkaitan dengan entitas Transaction.
 * Layanan ini mengelola pembuatan transaksi baru, pengurangan stok produk yang terkait, serta penghitungan
 * berbagai metrik transaksi seperti total pendapatan, jumlah total pesanan, rata-rata nilai pesanan, dan nilai pesanan tertinggi.
 *
 * Kelas ini menggunakan pendekatan komposisi dengan menyertakan service produk (ProductService) sebagai salah satu dependensi
 * untuk mengatur stok produk, sehingga memastikan integritas data terkait stok produk saat transaksi dibuat.
 *
 * Konsep OOP yang digunakan:
 * - Enkapsulasi: Menyimpan dependensi ProductService sebagai atribut private dan mengontrol akses terhadapnya melalui konstruktor.
 * - Komposisi: Menggabungkan layanan produk untuk mengelola hubungan antar domain bisnis yang terkait.
 */
@Service
public class TransactionService {
    private FileRepository<Transaction> repo;

    @Value("${uas.data.transactions}")
    private String transactionsPath;  // Path file data transaksi

    private final ProductService productService;  // Service produk untuk update stok

    /**
     * Konstruktor TransactionService, menerima service produk sebagai dependensi.
     *
     * @param productService Service untuk operasi produk terkait transaksi
     */
    public TransactionService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Inisialisasi repository file setelah konstruktor.
     * Dilakukan otomatis setelah injeksi dependensi selesai.
     */
    @PostConstruct
    private void init() {
        repo = new FileRepository<>(transactionsPath, Transaction[].class);
    }

    /**
     * Mendapatkan semua transaksi yang tersimpan.
     *
     * @return Daftar semua transaksi
     */
    public List<Transaction> getAll() { return repo.readAll(); }

    /**
     * Mendapatkan transaksi yang dilakukan oleh pengguna tertentu.
     *
     * @param userId ID pengguna untuk pencarian transaksi
     * @return Daftar transaksi milik pengguna dengan ID tersebut
     */
    public List<Transaction> getByUserId(String userId) {
        return repo.readAll().stream().filter(t -> t.getUserId().equals(userId)).toList();
    }

    /**
     * Membuat transaksi baru dan mengurangi stok produk terkait.
     *
     * @param t Objek transaksi yang akan dibuat
     */
    public void createTransaction(Transaction t) {
        // mengurangi stok produk sesuai jumlah yang dibeli
        for (Transaction.TransactionItem it : t.getItems()) {
            Product p = productService.findById(it.getProductId());
            if (p != null) {
                p.setStock(Math.max(0, p.getStock() - it.getQty()));
                productService.update(p);
            }
        }
        t.setId(UUID.randomUUID().toString());
        t.setTimestamp(LocalDateTime.now());
        var all = repo.readAll();
        all.add(t);
        repo.saveAll(all);
    }

    /**
     * Menghitung total pendapatan berdasarkan semua transaksi.
     *
     * @return Total pendapatan dalam bentuk integer
     */
    public int getTotalRevenue() {
        return repo.readAll().stream().mapToInt(Transaction::getTotal).sum();
    }

    /**
     * Mendapatkan total jumlah transaksi/pesanan.
     *
     * @return Jumlah total transaksi
     */
    public int getTotalOrders() {
        return repo.readAll().size();
    }

    /**
     * Menghitung rata-rata nilai transaksi.
     *
     * @return Rata-rata nilai transaksi, 0 jika tidak ada transaksi
     */
    public double getAverageOrder() {
        var transactions = repo.readAll();
        if (transactions.isEmpty()) return 0;
        return transactions.stream().mapToInt(Transaction::getTotal).average().orElse(0);
    }

    /**
     * Mendapatkan nilai transaksi tertinggi dari semua transaksi.
     *
     * @return Nilai transaksi tertinggi, 0 jika tidak ada transaksi
     */
    public int getHighestOrder() {
        var transactions = repo.readAll();
        return transactions.stream()
                .mapToInt(Transaction::getTotal)
                .max()
                .orElse(0);
    }
}
