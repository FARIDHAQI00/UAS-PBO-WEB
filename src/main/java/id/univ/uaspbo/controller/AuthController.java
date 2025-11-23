package id.univ.uaspbo.controller;

import id.univ.uaspbo.model.User;
import id.univ.uaspbo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

/**
 * Kelas controller yang mengelola operasi autentikasi pengguna,
 * termasuk proses login, logout, dan registrasi.
 * Kelas ini mengatur sesi pengguna dan mengalihkan pengguna ke halaman
 * yang sesuai berdasarkan peran mereka (ADMIN atau USER).
 *
 * Konsep OOP yang digunakan:
 * - Enkapsulasi: privatisasi UserService dan pengaksesan melalui konstruktor.
 */
@Controller
public class AuthController {

    private final UserService userService;

    /**
     * Konstruktor utama AuthController yang menerima service pengguna
     * untuk melakukan autentikasi dan registrasi.
     */
    public AuthController(UserService userService) { this.userService = userService; }

    /**
     * Menampilkan halaman login kepada pengguna.
     */
    @GetMapping({"/", "/login"})
    public String loginPage() { return "login"; }

    /**
     * Memproses permintaan login dari pengguna.
     * Melakukan autentikasi berdasarkan email dan password.
     * Jika gagal, menampilkan halaman login dengan pesan error.
     * Jika berhasil, menyimpan data pengguna di sesi dan mengalihkan
     * ke dashboard sesuai dengan peran pengguna.
     */
    @PostMapping("/login")
    public String doLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model m) {
        User u = userService.authenticate(email, password);
        if (u == null) {
            m.addAttribute("error", "Login gagal: email atau password salah");
            return "login";
        }
        session.setAttribute("user", u);
        if ("ADMIN".equalsIgnoreCase(u.getRole())) return "redirect:/admin";
        return "redirect:/user";
    }

    /**
     * Melakukan logout pengguna dengan menghapus sesi yang aktif.
     */
    @PostMapping("/logout")
    public String logout(HttpSession s) {
        s.invalidate();
        return "redirect:/login";
    }

    /**
     * Menampilkan halaman registrasi pengguna baru.
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    /**
     * Memproses pendaftaran pengguna baru.
     * Melakukan validasi kecocokan password dan konfirmasi password.
     * Jika valid, mendaftarkan pengguna melalui UserService.
     * Menampilkan pesan sukses atau error sesuai hasil proses pendaftaran.
     */
    @PostMapping("/register")
    public String doRegister(@RequestParam String email, @RequestParam String password,
                           @RequestParam String confirmPassword, Model m) {
        if (!password.equals(confirmPassword)) {
            m.addAttribute("error", "Password dan konfirmasi password tidak cocok");
            return "register";
        }

        if (userService.registerUser(email, password)) {
            m.addAttribute("success", "Registrasi berhasil! Silakan login.");
            return "login";
        } else {
            m.addAttribute("error", "Email sudah terdaftar");
            return "register";
        }
    }
}
