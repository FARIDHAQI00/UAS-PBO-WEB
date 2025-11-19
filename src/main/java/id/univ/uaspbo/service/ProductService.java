package id.univ.uaspbo.service;

import id.univ.uaspbo.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Product entities with search and sort functionality.
 */
@Service
public class ProductService extends AbstractService<Product> {

    @Value("${uas.data.products}")
    private String productsPath;

    @Override
    protected String getDataPath() {
        return productsPath;
    }

    @Override
    protected Class<Product[]> getTypeClass() {
        return Product[].class;
    }

    @Override
    protected String getEntityId(Product product) {
        return product.getId();
    }

    @Override
    protected void setEntityId(Product product, String id) {
        product.setId(id);
    }

    /**
     * Search products by name.
     * @param query Search query (case-insensitive)
     * @return Filtered list of products
     */
    public List<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAll();
        }
        String lowerQuery = query.toLowerCase();
        return getAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerQuery))
                .toList();
    }

    /**
     * Sort products by specified criteria.
     * @param products List to sort
     * @param sortBy Sort criteria (name_asc, name_desc, price_asc, price_desc)
     * @return Sorted list of products
     */
    public List<Product> sortProducts(List<Product> products, String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return products;
        }
        return switch (sortBy) {
            case "name_asc" -> products.stream().sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).toList();
            case "name_desc" -> products.stream().sorted((a, b) -> b.getName().compareToIgnoreCase(a.getName())).toList();
            case "price_asc" -> products.stream().sorted((a, b) -> Integer.compare(a.getPrice(), b.getPrice())).toList();
            case "price_desc" -> products.stream().sorted((a, b) -> Integer.compare(b.getPrice(), a.getPrice())).toList();
            default -> products;
        };
    }
}
