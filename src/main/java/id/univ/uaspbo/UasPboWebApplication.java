package id.univ.uaspbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Kelas utama untuk aplikasi web UAS PBO ini.
 * Kelas ini menggunakan anotasi @SpringBootApplication yang menandakan kelas
 * ini sebagai titik awal aplikasi Spring Boot berjalan.
 * 
 * Fungsi utama kelas ini adalah menjalankan aplikasi dengan memicu metode main,
 * yang memulai konteks Spring dan melakukan konfigurasi otomatis.
 */
@SpringBootApplication
public class UasPboWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(UasPboWebApplication.class, args);
    }
}
