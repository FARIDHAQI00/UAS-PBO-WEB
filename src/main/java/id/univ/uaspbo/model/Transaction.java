package id.univ.uaspbo.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Model transaksi yang merepresentasikan transaksi pembelian.
 * Kelas ini mewarisi kelas Entity yang menyediakan atribut dan metode id unik.
 * Model ini menyimpan informasi userId (pengguna pembeli), daftar item transaksi,
 * waktu transaksi, dan total harga keseluruhan.
 *
 * Konsep Object Oriented Programming (OOP) yang dipakai:
 * - Inheritance (Pewarisan): Mewarisi kelas Entity untuk atribut id unik.
 * - Enkapsulasi: Atribut disembunyikan dengan akses private dan disediakan getter-setter.
 * - Nested Class (Inner Class Static): TransactionItem merepresentasikan detail item dalam transaksi.
 */
public class Transaction extends Entity {
    private String userId;                  // ID pengguna yang melakukan transaksi
    private List<TransactionItem> items;    // Daftar item dalam transaksi, objek TransactionItem
    private LocalDateTime timestamp;        // Waktu dan tanggal transaksi dilakukan
    private int total;                      // Total harga keseluruhan transaksi

    /**
     * Konstruktor default tanpa parameter.
     * Membuat objek transaksi dengan atribut default.
     */
    public Transaction() {}

    /**
     * Konstruktor dengan parameter lengkap.
     * Inisialisasi objek transaksi dengan semua atribut.
     *
     * @param id Identifier unik transaksi, diwariskan dari Entity
     * @param userId ID pengguna yang melakukan transaksi
     * @param items Daftar item dalam transaksi
     * @param timestamp Waktu transaksi terjadi
     * @param total Total harga transaksi
     */
    public Transaction(String id, String userId, List<TransactionItem> items, LocalDateTime timestamp, int total) {
        super(id);
        this.userId = userId;
        this.items = items;
        this.timestamp = timestamp;
        this.total = total;
    }

    /**
     * Mengambil ID pengguna yang melakukan transaksi.
     */
    public String getUserId() { return userId; }

    /**
     * Mengatur ID pengguna yang melakukan transaksi.
     */
    public void setUserId(String userId) { this.userId = userId; }

    /**
     * Mengambil daftar item dalam transaksi.
     */
    public List<TransactionItem> getItems() { return items; }

    /**
     * Mengatur daftar item dalam transaksi.
     */
    public void setItems(List<TransactionItem> items) { this.items = items; }

    /**
     * Mengambil waktu dan tanggal transaksi dilakukan.
     */
    public LocalDateTime getTimestamp() { return timestamp; }

    /**
     * Mengatur waktu dan tanggal transaksi.
     */
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    /**
     * Mengambil total harga transaksi.
     */
    public int getTotal() { return total; }

    /**
     * Mengatur total harga transaksi.
     */
    public void setTotal(int total) { this.total = total; }



    /**
     * Kelas inner statis yang merepresentasikan detail item dalam transaksi.
     */
    public static class TransactionItem {
        private String productId;           // ID produk
        private String productName;         // Nama produk
        private int qty;                    // Jumlah produk yang dibeli
        private int price;                  // Harga satuan produk (unit price)

        /**
         * Konstruktor default tanpa parameter.
         */
        public TransactionItem() {}

        /**
         * Konstruktor dengan parameter lengkap.
         *
         * @param productId ID produk
         * @param productName Nama produk
         * @param qty Jumlah produk yang dibeli
         * @param price Harga satuan produk
         */
        public TransactionItem(String productId, String productName, int qty, int price) {
            this.productId = productId;
            this.productName = productName;
            this.qty = qty;
            this.price = price;
        }

        /**
         * Mengambil ID produk.
         */
        public String getProductId() { return productId; }

        /**
         * Mengatur ID produk.
         */
        public void setProductId(String productId) { this.productId = productId; }

        /**
         * Mengambil nama produk.
         */
        public String getProductName() { return productName; }

        /**
         * Mengatur nama produk.
         */
        public void setProductName(String productName) { this.productName = productName; }

        /**
         * Mengambil jumlah produk yang dibeli.
         */
        public int getQty() { return qty; }

        /**
         * Mengatur jumlah produk yang dibeli.
         */
        public void setQty(int qty) { this.qty = qty; }

        /**
         * Mengambil harga satuan produk.
         */
        public int getPrice() { return price; }

        /**
         * Mengatur harga satuan produk.
         */
        public void setPrice(int price) { this.price = price; }
    }
}
