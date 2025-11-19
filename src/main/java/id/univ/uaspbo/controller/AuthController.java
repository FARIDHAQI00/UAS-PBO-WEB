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
 * Controller class handling authentication operations including login and logout.
 * Manages user sessions and redirects based on user roles.
 */
@Controller
public class AuthController {

    private final UserService userService;

    /**
     * Constructor for AuthController.
     *
     * @param userService The user service for authentication
     */
    public AuthController(UserService userService) { this.userService = userService; }

    /**
     * Displays the login page.
     *
     * @return The login view name
     */
    @GetMapping({"/", "/login"})
    public String loginPage() { return "login"; }

    /**
     * Processes user login attempt.
     *
     * @param email User email
     * @param password User password
     * @param session HTTP session
     * @param m Model for view attributes
     * @return Redirect to appropriate dashboard or back to login on failure
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
     * Logs out the current user by invalidating the session.
     *
     * @param s HTTP session to invalidate
     * @return Redirect to login page
     */
    @PostMapping("/logout")
    public String logout(HttpSession s) {
        s.invalidate();
        return "redirect:/login";
    }

    /**
     * Displays the registration page.
     *
     * @return The registration view name
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    /**
     * Processes user registration attempt.
     *
     * @param email User email
     * @param password User password
     * @param confirmPassword Confirmation password
     * @param m Model for view attributes
     * @return Redirect to login page on success or back to register on failure
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
