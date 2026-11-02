package pos;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/** In-memory catalog of product descriptions (stands in for the persistence service). */
public class ProductCatalog {

    private final Map<String, ProductDescription> descriptions = new LinkedHashMap<String, ProductDescription>();

    public ProductCatalog() {
        add(new ProductDescription("1001", "Cola 330ml",        3.50));
        add(new ProductDescription("1002", "Spring Water 550ml", 2.00));
        add(new ProductDescription("1003", "White Bread",        8.00));
        add(new ProductDescription("1004", "Milk 1L",           12.50));
        add(new ProductDescription("1005", "Eggs (10 pack)",    15.80));
        add(new ProductDescription("1006", "Rice 5kg",          42.00));
        add(new ProductDescription("1007", "Instant Noodles",    4.50));
        add(new ProductDescription("1008", "Green Tea 500ml",    5.50));
        add(new ProductDescription("1009", "Chocolate Bar",      9.90));
        add(new ProductDescription("1010", "Paper Towels",      13.00));
    }

    private void add(ProductDescription d) { descriptions.put(d.getItemID(), d); }

    public ProductDescription getDescription(String itemID) {
        ProductDescription d = descriptions.get(itemID);
        if (d == null) {
            throw new IllegalArgumentException("Unknown item ID: " + itemID);
        }
        return d;
    }

    public Collection<ProductDescription> getAll() { return descriptions.values(); }
}
