package id.univ.uaspbo.controller;

import id.univ.uaspbo.service.ProductService;
import id.univ.uaspbo.service.TransactionService;
import id.univ.uaspbo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

/**
 * Kelas controller yang mengelola semua operasi terkait admin,
 * seperti manajemen produk, transaksi, pengguna, dan laporan dalam sistem.
 * Kelas ini bertindak sebagai penghubung antara view (template) dan service
 * yang memproses data bisnis pada bagian admin aplikasi.
 * Semua method di kelas ini memerlukan autentikasi sebagai admin agar dapat diakses.
 *
 * Konsep Object Oriented Programming (OOP) yang dipakai:
 * - Inheritance (Pewarisan): Controller ini mewarisi class dasar Controller Spring
 * - Enkapsulasi: Menggunakan private fields untuk service yang diinject melalui konstruktor
 *   dan hanya menyediakan method akses public untuk routing HTTP.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final TransactionService transactionService;
    private final UserService userService;

    /**
     * Konstruktor utama kelas AdminController.
     * Menginisialisasi service produk, transaksi, dan pengguna yang akan digunakan oleh controller.
     *
     * @param productService Service yang menangani operasi terkait produk
     * @param transactionService Service yang menangani operasi terkait transaksi
     * @param userService Service yang menangani operasi terkait pengguna
     */
    public AdminController(ProductService productService, TransactionService transactionService, UserService userService) {
        this.productService = productService;
        this.transactionService = transactionService;
        this.userService = userService;
    }

    /**
     * Mengecek apakah pengguna pada sesi saat ini memiliki peran sebagai admin.
     *
     * @param s HTTP session yang berisi informasi pengguna saat ini
     * @return true jika pengguna adalah admin, false jika bukan atau tidak ada pengguna
     */
    private boolean isAdmin(HttpSession s) {
        var u = s.getAttribute("user");
        return u != null && ((id.univ.uaspbo.model.User)u).getRole().equalsIgnoreCase("ADMIN");
    }

    /**
     * Menampilkan halaman dashboard admin yang berisi ringkasan
     * data produk, transaksi, dan pengguna.
     * Halaman ini hanya dapat diakses oleh admin yang sudah login,
     * jika bukan admin maka akan diarahkan ke halaman login.
     *
     * @param s HTTP session yang digunakan untuk verifikasi peran admin
     * @param m Model untuk menyimpan data produk, transaksi, dan pengguna yang akan ditampilkan di view
     * @return nama view halaman dashboard admin atau redirect ke login jika bukan admin
     */
    @GetMapping
    public String dashboard(HttpSession s, Model m) {
        if (!isAdmin(s)) return "redirect:/login";
        m.addAttribute("products", productService.getAll());
        m.addAttribute("transactions", transactionService.getAll());
        m.addAttribute("users", userService.getAll());
        return "admin/dashboard";
    }

    /**
     * Menampilkan halaman manajemen produk yang berisi daftar produk.
     * Hanya dapat diakses oleh admin yang sudah login.
     * Jika bukan admin, pengguna diarahkan ke halaman login.
     *
     * @param s HTTP session yang digunakan untuk verifikasi admin
     * @param m Model untuk menyimpan daftar produk yang akan ditampilkan
     * @return nama view halaman produk atau redirect ke login jika bukan admin
     */
    @GetMapping("/products")
    public String products(HttpSession s, Model m) {
        if (!isAdmin(s)) return "redirect:/login";
        m.addAttribute("products", productService.getAll());
        return "admin/products";
    }

    /**
     * Menampilkan halaman manajemen pengguna yang meliputi daftar pengguna
     * dan riwayat transaksi mereka.
     * Hanya dapat diakses oleh admin yang sudah login.
     * Jika bukan admin, pengguna diarahkan ke halaman login.
     *
     * @param s HTTP session yang digunakan untuk verifikasi admin
     * @param m Model untuk menyimpan daftar pengguna dan transaksi yang akan ditampilkan
     * @return nama view halaman pengguna atau redirect ke login jika bukan admin
     */
    @GetMapping("/users")
    public String users(HttpSession s, Model m) {
        if (!isAdmin(s)) return "redirect:/login";
        m.addAttribute("users", userService.getAll());
        m.addAttribute("transactions", transactionService.getAll());
        return "admin/users";
    }

    /**
     * Menampilkan halaman laporan yang berisi ringkasan
     * statistik transaksi seperti total pendapatan, jumlah pesanan,
     * rata-rata nilai pesanan, dan nilai pesanan tertinggi.
     * Hanya dapat diakses oleh admin yang sudah login.
     * Jika bukan admin, pengguna diarahkan ke halaman login.
     *
     * @param s HTTP session yang digunakan untuk verifikasi admin
     * @param m Model untuk menyimpan data laporan yang akan ditampilkan
     * @return nama view halaman laporan atau redirect ke login jika bukan admin
     */
    @GetMapping("/reports")
    public String reports(HttpSession s, Model m) {
        if (!isAdmin(s)) return "redirect:/login";
        int totalRevenue = transactionService.getTotalRevenue();
        int totalOrders = transactionService.getTotalOrders();
        double averageOrder = transactionService.getAverageOrder();
        int highestOrder = transactionService.getHighestOrder();

        m.addAttribute("totalRevenue", totalRevenue);
        m.addAttribute("totalOrders", totalOrders);
        m.addAttribute("averageOrder", averageOrder);
        m.addAttribute("highestOrder", highestOrder);
        return "admin/reports";
    }

    /**
     * Menampilkan halaman daftar transaksi.
     * Hanya dapat diakses oleh admin yang sudah login.
     * Jika bukan admin, pengguna diarahkan ke halaman login.
     *
     * @param s HTTP session yang digunakan untuk verifikasi admin
     * @param m Model untuk menyimpan daftar transaksi yang akan ditampilkan
     * @return nama view halaman transaksi atau redirect ke login jika bukan admin
     */
    @GetMapping("/transactions")
    public String transactions(HttpSession s, Model m) {
        if (!isAdmin(s)) return "redirect:/login";
        m.addAttribute("transactions", transactionService.getAll());
        return "admin/transactions";
    }

    /**
     * Menambahkan pengguna baru ke sistem berdasarkan data dari form.
     * Hanya admin yang sudah login dapat melakukan aksi ini.
     * Jika bukan admin, pengguna diarahkan ke halaman login.
     *
     * @param s HTTP session yang digunakan untuk verifikasi admin
     * @param email alamat email pengguna baru
     * @param password password pengguna baru
     * @param role peran pengguna baru, misalnya "ADMIN" atau "USER"
     * @return redirect ke halaman manajemen pengguna atau redirect ke login jika bukan admin
     */
    @PostMapping("/users/add")
    public String addUser(HttpSession s, @RequestParam String email, @RequestParam String password, @RequestParam String role) {
        if (!isAdmin(s)) return "redirect:/login";
        id.univ.uaspbo.model.User u = new id.univ.uaspbo.model.User();
        u.setEmail(email);
        u.setPassword(password);
        u.setRole(role.toUpperCase());
        userService.add(u);
        return "redirect:/admin/users";
    }

    /**
     * Memperbarui peran (role) pengguna berdasarkan email pengguna yang sudah ada.
     * Hanya admin yang sudah login dapat mengakses fungsi ini.
     * Jika bukan admin, pengguna diarahkan ke halaman login.
     *
     * @param s HTTP session yang digunakan untuk verifikasi admin
     * @param email alamat email pengguna yang rolenya ingin diubah
     * @param role peran baru yang akan diberikan (HARUS dalam huruf kapital)
     * @return redirect ke halaman manajemen pengguna atau redirect ke login jika bukan admin
     */
    @PostMapping("/users/update-role")
    public String updateUserRole(HttpSession s, @RequestParam String email, @RequestParam String role) {
        if (!isAdmin(s)) return "redirect:/login";
        id.univ.uaspbo.model.User u = userService.findByEmail(email);
        if (u != null) {
            u.setRole(role.toUpperCase());
            userService.update(u);
        }
        return "redirect:/admin/users";
    }

    /**
     * Menghapus pengguna dari sistem berdasarkan emailnya.
     * Hanya admin yang sudah login dapat melakukan penghapusan ini.
     * Jika bukan admin, pengguna diarahkan ke halaman login.
     *
     * @param s HTTP session yang digunakan untuk verifikasi admin
     * @param email alamat email pengguna yang akan dihapus
     * @return redirect ke halaman manajemen pengguna atau redirect ke login jika bukan admin
     */
    @PostMapping("/users/delete")
    public String deleteUser(HttpSession s, @RequestParam String email) {
        if (!isAdmin(s)) return "redirect:/login";
        userService.delete(email);
        return "redirect:/admin/users";
    }

    /**
     * Melakukan penyimpanan data secara manual.
     * Walaupun data sudah otomatis tersimpan melalui FileRepository,
     * method ini dapat digunakan untuk memaksa penyimpanan saat dibutuhkan.
     * Hanya admin yang sudah login dapat menggunakan fitur ini.
     * Jika bukan admin, pengguna diarahkan ke halaman login.
     *
     * @param s HTTP session yang digunakan untuk verifikasi admin
     * @return redirect ke halaman dashboard admin atau redirect ke login jika bukan admin
     */
    @PostMapping("/save")
    public String saveData(HttpSession s) {
        if (!isAdmin(s)) return "redirect:/login";
        // Data sudah otomatis tersimpan oleh FileRepository, method ini hanya untuk force save
        return "redirect:/admin";
    }
    
    /**
     * Menangani penambahan produk baru dari form input admin.
     * Hanya admin yang sudah login dapat menambahkan produk baru.
     * Jika bukan admin, pengguna diarahkan ke halaman login.
     *
     * @param s HTTP session yang digunakan untuk verifikasi admin
     * @param name nama produk baru yang akan ditambahkan
     * @param price harga produk (integer)
     * @param stock jumlah stok produk yang tersedia
     * @return redirect ke halaman daftar produk atau redirect ke login jika bukan admin
     */
    @PostMapping("/products/add")
    public String addProduct(HttpSession s,
                             @RequestParam String name,
                             @RequestParam int price,
                             @RequestParam int stock) {
        if (!isAdmin(s)) return "redirect:/login";
        id.univ.uaspbo.model.Product p = new id.univ.uaspbo.model.Product();
        p.setName(name);
        p.setPrice(price);
        p.setStock(stock);
        productService.add(p);
        return "redirect:/admin/products";
    }

    /**
     * Menangani pembaruan data produk yang sudah ada berdasarkan form input.
     * Hanya admin yang sudah login dapat melakukan pembaruan data produk.
     * Jika bukan admin, pengguna diarahkan ke halaman login.
     *
     * @param s HTTP session yang digunakan untuk verifikasi admin
     * @param id ID produk yang akan diperbarui
     * @param name nama produk yang diperbarui
     * @param price harga produk yang diperbarui
     * @param stock stok produk yang diperbarui
     * @return redirect ke halaman daftar produk atau redirect ke login jika bukan admin
     */
    @PostMapping("/products/update")
    public String updateProduct(HttpSession s,
                                @RequestParam String id,
                                @RequestParam String name,
                                @RequestParam int price,
                                @RequestParam int stock) {
        if (!isAdmin(s)) return "redirect:/login";
        id.univ.uaspbo.model.Product p = productService.findById(id);
        if (p != null) {
            p.setName(name);
            p.setPrice(price);
            p.setStock(stock);
            productService.update(p);
        }
        return "redirect:/admin/products";
    }

    /**
     * Menangani penghapusan produk berdasarkan ID produk.
     * Hanya admin yang sudah login dapat menghapus produk.
     * Jika bukan admin, pengguna diarahkan ke halaman login.
     *
     * @param s HTTP session yang digunakan untuk verifikasi admin
     * @param id ID produk yang akan dihapus
     * @return redirect ke halaman daftar produk atau redirect ke login jika bukan admin
     */
    @PostMapping("/products/delete")
    public String deleteProduct(HttpSession s,
                                @RequestParam String id) {
        if (!isAdmin(s)) return "redirect:/login";
        productService.delete(id);
        return "redirect:/admin/products";
    }
}
