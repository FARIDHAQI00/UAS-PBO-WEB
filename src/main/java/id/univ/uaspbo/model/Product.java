package id.univ.uaspbo.model;

/**
 * Product model representing items available for purchase.
 */
public class Product extends Entity {
    private String name;
    private int price;
    private int stock;

    public Product() {}

    public Product(String id, String name, int price, int stock) {
        super(id);
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
