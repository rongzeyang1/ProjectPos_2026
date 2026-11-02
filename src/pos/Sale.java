package pos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A completed or in-progress sale. Owns its line items and payments
 * (Creator: Sale aggregates SalesLineItem, so Sale creates them).
 */
public class Sale {

    private final List<SalesLineItem> lineItems = new ArrayList<SalesLineItem>();
    private final List<Payment> payments = new ArrayList<Payment>();
    private final Date time = new Date();
    private boolean complete;

    public void makeLineItem(ProductDescription description, int quantity) {
        // merge with an existing line for the same product (better receipts, fewer rows)
        for (SalesLineItem item : lineItems) {
            if (item.getDescription().getItemID().equals(description.getItemID())) {
                lineItems.remove(item);
                lineItems.add(new SalesLineItem(description, item.getQuantity() + quantity));
                return;
            }
        }
        lineItems.add(new SalesLineItem(description, quantity));
    }

    public void removeLastLineItem() {
        if (!lineItems.isEmpty()) {
            lineItems.remove(lineItems.size() - 1);
        }
    }

    /** Information Expert: Sale owns the line items, so it computes the total. */
    public double getTotal() {
        double total = 0.0;
        for (SalesLineItem item : lineItems) {
            total += item.getSubtotal();
        }
        return total;
    }

    public double getAmountTendered() {
        double paid = 0.0;
        for (Payment p : payments) {
            if (p instanceof CashPayment) {
                paid += ((CashPayment) p).getCashTendered();
            } else {
                paid += p.getAmount();
            }
        }
        return paid;
    }

    public double getChange() { return getAmountTendered() - getTotal(); }

    public void makePayment(Payment payment) {
        payments.add(payment);
        complete = true;
    }

    public List<SalesLineItem> getLineItems() { return lineItems; }
    public List<Payment> getPayments()        { return payments; }
    public boolean isComplete()               { return complete; }
    public Date getTime()                     { return time; }

    public String getTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }
}
