package id.univ.uaspbo.service;

import id.univ.uaspbo.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Kelas service yang bertanggung jawab mengelola seluruh operasi yang berhubungan dengan entitas User.
 * Layanan ini menyediakan fungsi autentikasi pengguna berdasarkan email dan password,
 * pencarian pengguna berdasarkan email, serta pendaftaran pengguna baru dengan peran standar USER.
 * 
 * Data pengguna disimpan dan diambil dari file JSON yang path-nya dikonfigurasi melalui properti aplikasi.
 * 
 * Konsep OOP yang digunakan:
 * - Pewarisan (Inheritance): Memperluas AbstractService untuk mengimplementasikan CRUD dasar secara generik.
 * - Enkapsulasi: Melindungi akses ke data pengguna melalui fungsi service ini dan mengatur manipulasi data melalui method.
 * - Polimorfisme: Meng-override method abstrak dari AbstractService untuk spesifikasi tipe dan identitas user.
 */
@Service
public class UserService extends AbstractService<User> {

    @Value("${uas.data.users}")
    private String usersPath;  // Path file data pengguna

    /**
     * Mendapatkan path file data pengguna untuk repository.
     *
     * @return Path file pengguna
     */
    @Override
    protected String getDataPath() {
        return usersPath;
    }

    /**
     * Mendapatkan kelas array tipe User untuk deserialisasi JSON.
     *
     * @return Kelas array pengguna
     */
    @Override
    protected Class<User[]> getTypeClass() {
        return User[].class;
    }

    /**
     * Mendapatkan ID dari objek pengguna.
     *
     * @param user Pengguna
     * @return ID pengguna
     */
    @Override
    protected String getEntityId(User user) {
        return user.getId();
    }

    /**
     * Mengatur ID pada objek pengguna.
     *
     * @param user Pengguna
     * @param id ID yang akan diset
     */
    @Override
    protected void setEntityId(User user, String id) {
        user.setId(id);
    }

    /**
     * Melakukan autentikasi pengguna berdasarkan email dan password.
     *
     * @param email Email pengguna
     * @param password Password pengguna
     * @return Objek User jika autentikasi berhasil, atau null jika gagal
     */
    public User authenticate(String email, String password) {
        for (User u : getAll()) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) return u;
        }
        return null;
    }

    /**
     * Mencari pengguna berdasarkan email.
     *
     * @param email Email pengguna
     * @return Objek User jika ditemukan, atau null jika tidak ada
     */
    public User findByEmail(String email) {
        for (User u : getAll()) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    /**
     * Mendaftarkan pengguna baru dengan peran USER.
     *
     * @param email Email pengguna baru
     * @param password Password pengguna baru
     * @return true jika pendaftaran berhasil, false jika email sudah terdaftar
     */
    public boolean registerUser(String email, String password) {
        if (findByEmail(email) != null) {
            return false; // Email sudah terdaftar
        }
        User newUser = new User(null, email, password, "USER");
        add(newUser);
        return true;
    }
}
