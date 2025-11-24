package id.univ.uaspbo.model;

/**
 * Model produk yang merepresentasikan item yang tersedia untuk dibeli.
 * Kelas ini mewarisi kelas Entity yang menyediakan atribut dan metode id unik.
 * Model ini menyimpan informasi nama, harga, dan stok produk.
 *
 * Konsep Object Oriented Programming (OOP) yang dipakai:
 * - Inheritance (Pewarisan): Kelas ini mewarisi kelas abstrak Entity,
 *   sehingga atribut id dapat dipakai ulang oleh semua entitas produk.
 * - Enkapsulasi: Atribut name, price, dan stock bersifat private dan diakses lewat getter dan setter,
 *   untuk melindungi atribut dari akses langsung dan menjaga integritas data.
 */

public class Product extends Entity {      //Kelas ini mewarisi kelas abstrak Entity,

    private String name;        //Nama produk
    private int price;          //Harga produk dalam satuan integer (misalnya rupiah).
    private int stock;          //Jumlah stok produk yang tersedia.

    /**
     * Konstruktor default tanpa parameter.
     * Membuat objek produk dengan nilai atribut default.
     */
    public Product() {}

    /**
     * Konstruktor dengan parameter lengkap.
     * Menginisialisasi produk dengan id, nama, harga, dan stok sesuai input.
     */
    public Product(String id, String name, int price, int stock) {
        super(id);
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Mengambil nama produk.
     */
    public String getName() { return name; }

    /**
     * Mengatur nama produk.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Mengambil harga produk.
     */
    public int getPrice() { return price; }

    /**
     * Mengatur harga produk.
     */
    public void setPrice(int price) { this.price = price; }

    /**
     * Mengambil jumlah stok produk.
     */
    public int getStock() { return stock; }

    /**
     * Mengatur jumlah stok produk.
     */
    public void setStock(int stock) { this.stock = stock; }
}
