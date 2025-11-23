package id.univ.uaspbo.controller;

import id.univ.uaspbo.model.Product;
import id.univ.uaspbo.model.Transaction;
import id.univ.uaspbo.model.User;
import id.univ.uaspbo.service.ProductService;
import id.univ.uaspbo.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas controller yang mengelola operasi pengguna,
 * termasuk penjelajahan produk, pemrosesan checkout,
 * dan peninjauan riwayat transaksi.
 * Semua method dalam kelas ini memerlukan autentikasi pengguna.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final ProductService productService;
    private final TransactionService transactionService;

    /**
     * Konstruktor utama UserController yang menerima service produk
     * dan transaksi untuk melayani permintaan pengguna.
     *
     * @param productService Service yang menangani operasi produk
     * @param transactionService Service yang menangani operasi transaksi
     */
    public UserController(ProductService productService, TransactionService transactionService) {
        this.productService = productService;
        this.transactionService = transactionService;
    }

    /**
     * Memeriksa apakah pengguna pada sesi saat ini memiliki peran USER.
     *
     * @param s HTTP session yang menyimpan data pengguna aktif
     * @return true jika pengguna memiliki role USER, false jika tidak atau tidak login
     */
    private boolean isUser(HttpSession s) {
        var u = s.getAttribute("user");
        return u != null && ((User) u).getRole().equalsIgnoreCase("USER");
    }

    /**
     * Menampilkan halaman dashboard pengguna dengan daftar produk
     * yang dapat dicari dan diurutkan sesuai kriteria.
     * Jika pengguna belum login, diarahkan ke halaman login.
     *
     * @param s HTTP session untuk validasi sesi pengguna
     * @param m Model untuk menyimpan atribut data produk dan filter
     * @param search parameter opsional untuk pencarian nama produk
     * @param sort parameter opsional untuk pengurutan produk
     * @return nama view halaman dashboard pengguna atau redirect ke login
     */
    @GetMapping
    public String dashboard(HttpSession s, Model m,
                            @RequestParam(required = false) String search,
                            @RequestParam(required = false) String sort) {
        if (s.getAttribute("user") == null) return "redirect:/login";
        List<Product> products = productService.searchProducts(search);
        products = productService.sortProducts(products, sort);
        m.addAttribute("products", products);
        m.addAttribute("search", search);
        m.addAttribute("sort", sort);
        return "user/dashboard";
    }

    /**
     * Memproses checkout produk yang dipilih oleh pengguna.
     * Membuat daftar item transaksi berdasarkan produk dan jumlah yang dipilih,
     * menghitung total pembayaran, dan mencatat transaksi.
     * Jika tidak ada item yang dipilih, menampilkan pesan error pada dashboard.
     * Jika sesi pengguna tidak valid, diarahkan ke login.
     *
     * @param s HTTP session untuk verifikasi pengguna
     * @param productIds array ID produk yang dipilih
     * @param qtys array jumlah masing-masing produk yang dipilih
     * @param m Model untuk menampilkan pesan error jika ada
     * @return redirect ke halaman riwayat transaksi jika sukses, atau kembali ke dashboard jika gagal
     */
    @PostMapping("/checkout")
    public String checkout(HttpSession s,
                           @RequestParam(name = "productId") String[] productIds,
                           @RequestParam(name = "qty") int[] qtys,
                           Model m) {
        var u = (User) s.getAttribute("user");
        if (u == null) return "redirect:/login";

        List<Transaction.TransactionItem> items = new ArrayList<>();
        int total = 0;
        for (int i = 0; i < productIds.length; i++) {
            String pid = productIds[i];
            int q = qtys[i];
            if (q <= 0) continue;
            Product p = productService.findById(pid);
            if (p == null) continue;
            Transaction.TransactionItem it = new Transaction.TransactionItem(p.getId(), p.getName(), q, p.getPrice());
            items.add(it);
            total += p.getPrice() * q;
        }
        if (items.isEmpty()) {
            m.addAttribute("error", "Tidak ada item yang dipilih");
            m.addAttribute("products", productService.getAll());
            return "user/dashboard";
        }

        Transaction t = new Transaction();
        t.setUserId(u.getId());
        t.setItems(items);
        t.setTotal(total);
        transactionService.createTransaction(t);

        return "redirect:/user/history";
    }


    /**
     * Menampilkan riwayat transaksi dari pengguna saat ini.
     * Jika sesi tidak valid, diarahkan ke halaman login.
     *
     * @param s HTTP session untuk verifikasi pengguna
     * @param m Model untuk menyimpan data transaksi pengguna
     * @return nama view halaman riwayat transaksi atau redirect ke login
    **/
    
    @GetMapping("/history")
    public String history(HttpSession s, Model m) {
        var u = (User) s.getAttribute("user");
        if (u == null) return "redirect:/login";
        m.addAttribute("transactions", transactionService.getByUserId(u.getId()));
        return "user/history";
    }
 }
