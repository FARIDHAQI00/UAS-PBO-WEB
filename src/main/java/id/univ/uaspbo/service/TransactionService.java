package id.univ.uaspbo.service;

import id.univ.uaspbo.model.Transaction;
import id.univ.uaspbo.model.Product;
import id.univ.uaspbo.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service class for managing Transaction entities with stock management and revenue calculation.
 */
@Service
public class TransactionService {
    private FileRepository<Transaction> repo;

    @Value("${uas.data.transactions}")
    private String transactionsPath;

    private final ProductService productService;

    /**
     * Constructor for TransactionService.
     *
     * @param productService Service for product operations
     */
    public TransactionService(ProductService productService) {
        this.productService = productService;
    }

    @PostConstruct
    private void init() {
        repo = new FileRepository<>(transactionsPath, Transaction[].class);
    }

    /**
     * Retrieves all transactions.
     *
     * @return List of all transactions
     */
    public List<Transaction> getAll() { return repo.readAll(); }

    /**
     * Retrieves transactions for a specific user.
     *
     * @param userId The user ID to filter transactions
     * @return List of transactions for the specified user
     */
    public List<Transaction> getByUserId(String userId) {
        return repo.readAll().stream().filter(t -> t.getUserId().equals(userId)).toList();
    }

    /**
     * Creates a new transaction and updates product stock.
     *
     * @param t The transaction to create
     */
    public void createTransaction(Transaction t) {
        // reduce stock
        for (Transaction.TransactionItem it : t.getItems()) {
            Product p = productService.findById(it.getProductId());
            if (p != null) {
                p.setStock(Math.max(0, p.getStock() - it.getQty()));
                productService.update(p);
            }
        }
        t.setId(UUID.randomUUID().toString());
        t.setTimestamp(LocalDateTime.now());
        var all = repo.readAll();
        all.add(t);
        repo.saveAll(all);
    }

    /**
     * Calculates total revenue from all transactions.
     *
     * @return Total revenue as integer
     */
    public int getTotalRevenue() {
        return repo.readAll().stream().mapToInt(Transaction::getTotal).sum();
    }

    public int getTotalOrders() {
        return repo.readAll().size();
    }

    public double getAverageOrder() {
        var transactions = repo.readAll();
        if (transactions.isEmpty()) return 0;
        return transactions.stream().mapToInt(Transaction::getTotal).average().orElse(0);
    }

    public int getHighestOrder() {
        var transactions = repo.readAll();
        return transactions.stream()
                .mapToInt(Transaction::getTotal)
                .max()
                .orElse(0);
    }
}
