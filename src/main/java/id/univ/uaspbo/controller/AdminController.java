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
 * seperti manajemen produk, transaksi, pengguna, dan laporan.
 * Semua method di kelas ini memerlukan autentikasi sebagai admin
 * untuk dapat mengaksesnya. Kelas ini berfungsi sebagai penghubung
 * antara view (template) dan service yang bertugas memproses data
 * bisnis yang terkait dengan bagian admin dari aplikasi.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final TransactionService transactionService;
    private final UserService userService;

    /**
     * Constructor for AdminController.
     *
     * @param productService Service for product operations
     * @param transactionService Service for transaction operations
     * @param userService Service for user operations
     */
    public AdminController(ProductService productService, TransactionService transactionService, UserService userService) {
        this.productService = productService;
        this.transactionService = transactionService;
        this.userService = userService;
    }

    /**
     * Checks if the current session user has admin role.
     *
     * @param s HTTP session
     * @return true if user is admin, false otherwise
     */
    private boolean isAdmin(HttpSession s) {
        var u = s.getAttribute("user");
        return u != null && ((id.univ.uaspbo.model.User)u).getRole().equalsIgnoreCase("ADMIN");
    }

    /**
     * Menampilkan halaman dashboard admin yang berisi ringkasan
     * data produk, transaksi, dan pengguna.
     * Jika pengguna bukan admin, akan diarahkan ke halaman login.
     *
     * @param s HTTP session untuk verifikasi peran admin
     * @param m Model untuk menyimpan data yang akan ditampilkan di view
     * @return nama view halaman dashboard admin atau redirect ke login
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
     * Hanya dapat diakses oleh admin.
     *
     * @param s HTTP session untuk verifikasi admin
     * @param m Model untuk menyimpan atribut data produk
     * @return nama view halaman produk atau redirect ke login
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
     * Hanya dapat diakses oleh admin.
     *
     * @param s HTTP session untuk verifikasi admin
     * @param m Model untuk menyimpan atribut data pengguna dan transaksi
     * @return nama view halaman pengguna atau redirect ke login
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
     * Hanya dapat diakses oleh admin.
     *
     * @param s HTTP session untuk verifikasi admin
     * @param m Model untuk menyimpan data laporan yang akan ditampilkan
     * @return nama view halaman laporan atau redirect ke login
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
     *
     * @param s HTTP session untuk verifikasi admin
     * @param m Model untuk menyimpan daftar transaksi yang akan ditampilkan
     * @return nama view halaman transaksi atau redirect ke login
     */
    @GetMapping("/transactions")
    public String transactions(HttpSession s, Model m) {
        if (!isAdmin(s)) return "redirect:/login";
        m.addAttribute("transactions", transactionService.getAll());
        return "admin/transactions";
    }

    /**
     * Menambahkan pengguna baru ke sistem berdasarkan data dari form.
     * Hanya admin yang dapat melakukan aksi ini.
     *
     * @param s HTTP session untuk verifikasi peran admin
     * @param email alamat email pengguna yang akan ditambahkan
     * @param password password pengguna baru
     * @param role peran pengguna baru (contoh: ADMIN, USER)
     * @return redirect ke halaman manajemen pengguna atau ke login jika bukan admin
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
     * Hanya admin yang dapat mengakses fungsi ini.
     *
     * @param s HTTP session untuk verifikasi peran admin
     * @param email alamat email pengguna yang rolenya ingin diubah
     * @param role peran baru yang akan diberikan (HARUS dalam huruf kapital)
     * @return redirect ke halaman manajemen pengguna atau ke login jika bukan admin
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
     * Hanya admin yang dapat melakukan penghapusan ini.
     *
     * @param s HTTP session untuk verifikasi admin
     * @param email alamat email pengguna yang akan dihapus
     * @return redirect ke halaman manajemen pengguna atau ke login
     */
    @PostMapping("/users/delete")
    public String deleteUser(HttpSession s, @RequestParam String email) {
        if (!isAdmin(s)) return "redirect:/login";
        userService.delete(email);
        return "redirect:/admin/users";
    }

    /**
     * Manually saves data (though automatic saving is handled by FileRepository).
     *
     * @param s HTTP session
     * @return Redirect to admin dashboard or login if not admin
     */
    @PostMapping("/save")
    public String saveData(HttpSession s) {
        if (!isAdmin(s)) return "redirect:/login";
        // Data is already saved automatically via FileRepository, but we can force save
        // For manual save, we can reload or just redirect
        return "redirect:/admin";
    }
    
    /**
     * Menangani penambahan produk baru dari form input admin.
     * Hanya admin yang dapat menambahkan produk baru.
     *
     * @param s HTTP session untuk verifikasi admin
     * @param name nama produk baru yang akan ditambahkan
     * @param price harga produk (integer)
     * @param stock jumlah stok produk yang tersedia
     * @return redirect ke halaman daftar produk atau ke login jika bukan admin
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
     * Hanya admin yang boleh melakukan pembaruan data produk.
     *
     * @param s HTTP session untuk verifikasi admin
     * @param id ID produk yang akan diperbarui
     * @param name nama produk yang diperbarui
     * @param price harga produk yang diperbarui
     * @param stock stok produk yang diperbarui
     * @return redirect ke halaman daftar produk atau ke login jika bukan admin
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
     * Hanya admin yang dapat melakukan penghapusan produk.
     *
     * @param s HTTP session untuk verifikasi admin
     * @param id ID produk yang akan dihapus
     * @return redirect ke halaman daftar produk atau ke login jika bukan admin
     */
    @PostMapping("/products/delete")
    public String deleteProduct(HttpSession s,
                                @RequestParam String id) {
        if (!isAdmin(s)) return "redirect:/login";
        productService.delete(id);
        return "redirect:/admin/products";
    }
}
