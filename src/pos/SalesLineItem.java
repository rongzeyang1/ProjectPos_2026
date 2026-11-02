package pos;

/** One line of a sale: a quantity of one product. */
public class SalesLineItem {

    private final ProductDescription description;
    private final int quantity;

    public SalesLineItem(ProductDescription description, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.description = description;
        this.quantity = quantity;
    }

    public ProductDescription getDescription() { return description; }
    public int getQuantity()                   { return quantity; }

    /** Information Expert: the line item owns the data needed for its own subtotal. */
    public double getSubtotal() { return description.getPrice() * quantity; }
}
