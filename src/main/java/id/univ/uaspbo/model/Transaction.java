package id.univ.uaspbo.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Transaction model representing purchase transactions.
 */
public class Transaction extends Entity {
    private String userId;
    private List<TransactionItem> items;
    private LocalDateTime timestamp;
    private int total;

    public Transaction() {}

    public Transaction(String id, String userId, List<TransactionItem> items, LocalDateTime timestamp, int total) {
        super(id);
        this.userId = userId;
        this.items = items;
        this.timestamp = timestamp;
        this.total = total;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<TransactionItem> getItems() { return items; }
    public void setItems(List<TransactionItem> items) { this.items = items; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public static class TransactionItem {
        private String productId;
        private String productName;
        private int qty;
        private int price; // unit price

        public TransactionItem() {}

        public TransactionItem(String productId, String productName, int qty, int price) {
            this.productId = productId;
            this.productName = productName;
            this.qty = qty;
            this.price = price;
        }

        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public int getQty() { return qty; }
        public void setQty(int qty) { this.qty = qty; }
        public int getPrice() { return price; }
        public void setPrice(int price) { this.price = price; }
    }
}
