package id.univ.uaspbo.controller;

import id.univ.uaspbo.service.ProductService;
import id.univ.uaspbo.service.TransactionService;
import id.univ.uaspbo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

/**
 * Controller class handling admin operations including product management,
 * transaction viewing, user management, and reports.
 * All methods require admin authentication.
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
     * Displays the admin dashboard with overview of products, transactions, and users.
     *
     * @param s HTTP session
     * @param m Model for view attributes
     * @return Admin dashboard view or redirect to login if not admin
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
     * Displays the product management page.
     *
     * @param s HTTP session
     * @param m Model for view attributes
     * @return Products view or redirect to login if not admin
     */
    @GetMapping("/products")
    public String products(HttpSession s, Model m) {
        if (!isAdmin(s)) return "redirect:/login";
        m.addAttribute("products", productService.getAll());
        return "admin/products";
    }

    /**
     * Displays the users page.
     *
     * @param s HTTP session
     * @param m Model for view attributes
     * @return Users view or redirect to login if not admin
     */
    @GetMapping("/users")
    public String users(HttpSession s, Model m) {
        if (!isAdmin(s)) return "redirect:/login";
        m.addAttribute("users", userService.getAll());
        m.addAttribute("transactions", transactionService.getAll());
        return "admin/users";
    }

    /**
     * Displays the reports page.
     *
     * @param s HTTP session
     * @param m Model for view attributes
     * @return Reports view or redirect to login if not admin
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
     * Displays the transactions page.
     *
     * @param s HTTP session
     * @param m Model for view attributes
     * @return Transactions view or redirect to login if not admin
     */
    @GetMapping("/transactions")
    public String transactions(HttpSession s, Model m) {
        if (!isAdmin(s)) return "redirect:/login";
        m.addAttribute("transactions", transactionService.getAll());
        return "admin/transactions";
    }

    /**
     * Adds a new user.
     *
     * @param s HTTP session
     * @param email User email
     * @param password User password
     * @param role User role
     * @return Redirect to users page or login if not admin
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
     * Updates a user's role.
     *
     * @param s HTTP session
     * @param email User email
     * @param role New role
     * @return Redirect to users page or login if not admin
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
     * Deletes a user.
     *
     * @param s HTTP session
     * @param email User email to delete
     * @return Redirect to users page or login if not admin
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
}
