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
 * Controller class handling user operations including product browsing,
 * checkout, and transaction history viewing.
 * All methods require user authentication.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final ProductService productService;
    private final TransactionService transactionService;

    /**
     * Constructor for UserController.
     *
     * @param productService Service for product operations
     * @param transactionService Service for transaction operations
     */
    public UserController(ProductService productService, TransactionService transactionService) {
        this.productService = productService;
        this.transactionService = transactionService;
    }

    /**
     * Checks if the current session user has user role.
     *
     * @param s HTTP session
     * @return true if user has USER role, false otherwise
     */
    private boolean isUser(HttpSession s) {
        var u = s.getAttribute("user");
        return u != null && ((User) u).getRole().equalsIgnoreCase("USER");
    }

    /**
     * Displays the user dashboard with searchable and sortable product list.
     *
     * @param s HTTP session
     * @param m Model for view attributes
     * @param search Optional search query for filtering products
     * @param sort Optional sort criteria for products
     * @return User dashboard view or redirect to login if not authenticated
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
     * Processes checkout for selected products.
     *
     * @param s HTTP session
     * @param productIds Array of product IDs to purchase
     * @param qtys Array of quantities corresponding to product IDs
     * @param m Model for view attributes
     * @return Redirect to history page on success or dashboard with error
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
     * Displays the transaction history for the current user.
     *
     * @param s HTTP session
     * @param m Model for view attributes
     * @return User history view or redirect to login if not authenticated
     */
    @GetMapping("/history")
    public String history(HttpSession s, Model m) {
        var u = (User) s.getAttribute("user");
        if (u == null) return "redirect:/login";
        m.addAttribute("transactions", transactionService.getByUserId(u.getId()));
        return "user/history";
    }
}
