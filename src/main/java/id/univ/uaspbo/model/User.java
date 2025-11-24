package id.univ.uaspbo.model;

/**
 * Model pengguna yang merepresentasikan user dalam sistem dengan berbagai peran,
 * seperti 'ADMIN' dan 'USER'. Kelas ini merupakan turunan dari kelas Entity,
 * sehingga memiliki atribut id unik. Model ini menyimpan informasi email,
 * password, dan role pengguna.
 *
 * Konsep Object Oriented Programming (OOP) yang dipakai:
 * - Inheritance (Pewarisan): Menggunakan pewarisan dari kelas abstrak Entity untuk atribut id.
 * - Enkapsulasi: Atribut email, password, dan role bersifat private dan diakses lewat getter dan setter.
 */

public class User extends Entity {
    private String email;               // Email pengguna
    private String password;            // Password pengguna
    private String role;                // Peran pengguna, misalnya "ADMIN" atau "USER"

    /**
     * Konstruktor default tanpa parameter.
     * Membuat objek pengguna dengan atribut default.
     */
    public User() {}

    /**
     * Konstruktor dengan parameter lengkap.
     * Menginisialisasi objek user dengan id, email, password, dan role sesuai input.
     *
     * @param id Identifier unik pengguna, diwariskan dari Entity
     * @param email Email pengguna
     * @param password Password pengguna
     * @param role Peran pengguna ("ADMIN" atau "USER")
     */
    public User(String id, String email, String password, String role) {
        super(id);
        this.email = email;
        this.password = password;
        this.role = role;
    }
    /**
     * Mengambil email pengguna.
     */
    public String getEmail() { return email; }

    /**
     * Mengatur email pengguna.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Mengambil password pengguna.
     */
    public String getPassword() { return password; }

    /**
     * Mengatur password pengguna.
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Mengambil peran pengguna.
     */
    public String getRole() { return role; }

    /**
     * Mengatur peran pengguna.
     */
    public void setRole(String role) { this.role = role; }
}
