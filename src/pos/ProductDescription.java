package pos;

/** A description of a product that can be sold. Conceptual class: ProductDescription. */
public class ProductDescription {

    private final String itemID;
    private final String description;
    private final double price;

    public ProductDescription(String itemID, String description, double price) {
        this.itemID = itemID;
        this.description = description;
        this.price = price;
    }

    public String getItemID()      { return itemID; }
    public String getDescription() { return description; }
    public double getPrice()       { return price; }

    @Override
    public String toString()       { return itemID + " - " + description; }
}
