package pos;

/**
 * The POS terminal. It is the facade controller for the system operations
 * makeNewSale / enterItem / endSale / makePayment and their return counterparts.
 */
public class Register {

    private final ProductCatalog catalog;
    private Sale sale;
    private Return currentReturn;

    public Register(ProductCatalog catalog) { this.catalog = catalog; }

    public void makeNewSale()        { sale = new Sale(); }
    public void makeNewReturn()      { currentReturn = new Return(); }

    public void enterItem(String itemID, int quantity) {
        ProductDescription description = catalog.getDescription(itemID);
        sale.makeLineItem(description, quantity);
    }

    public void enterReturnItem(String itemID, int quantity) {
        ProductDescription description = catalog.getDescription(itemID);
        currentReturn.makeLineItem(description, quantity);
    }

    public double endSale()              { return sale.getTotal(); }
    public double endReturn()            { return currentReturn.getTotal(); }

    public double makePayment(double cashTendered) {
        CashPayment payment = new CashPayment(sale.getTotal(), cashTendered);
        sale.makePayment(payment);
        return payment.getChange();
    }

    public boolean makeCardPayment(String cardNumber) {
        CardPayment payment = new CardPayment(sale.getTotal(), cardNumber);
        if (!payment.authorize()) {
            return false;
        }
        sale.makePayment(payment);
        return true;
    }

    public double makeRefund(boolean cash) {
        Payment refund = cash
                ? new CashPayment(currentReturn.getTotal(), currentReturn.getTotal())
                : new CardPayment(currentReturn.getTotal(), "****");
        currentReturn.makeRefund(refund);
        return refund.getAmount();
    }

    public Sale getSale()            { return sale; }
    public Return getCurrentReturn() { return currentReturn; }
    public ProductCatalog getCatalog() { return catalog; }
}
