package pos;

/** One line of a return: a quantity of one returned product. */
public class ReturnLineItem {

    private final ProductDescription description;
    private final int quantity;

    public ReturnLineItem(ProductDescription description, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.description = description;
        this.quantity = quantity;
    }

    public ProductDescription getDescription() { return description; }
    public int getQuantity()                   { return quantity; }
    public double getSubtotal()                { return description.getPrice() * quantity; }
}
