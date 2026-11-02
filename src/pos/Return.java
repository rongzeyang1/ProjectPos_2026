package pos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** A return transaction for goods brought back by a customer. */
public class Return {

    private final List<ReturnLineItem> lineItems = new ArrayList<ReturnLineItem>();
    private final Date time = new Date();
    private Payment refund;

    public void makeLineItem(ProductDescription description, int quantity) {
        for (ReturnLineItem item : lineItems) {
            if (item.getDescription().getItemID().equals(description.getItemID())) {
                lineItems.remove(item);
                lineItems.add(new ReturnLineItem(description, item.getQuantity() + quantity));
                return;
            }
        }
        lineItems.add(new ReturnLineItem(description, quantity));
    }

    public void removeLastLineItem() {
        if (!lineItems.isEmpty()) {
            lineItems.remove(lineItems.size() - 1);
        }
    }

    public double getTotal() {
        double total = 0.0;
        for (ReturnLineItem item : lineItems) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void makeRefund(Payment refund) {
        this.refund = refund;
    }

    public Payment getRefund()                    { return refund; }
    public List<ReturnLineItem> getLineItems()    { return lineItems; }
    public boolean isComplete()                   { return refund != null; }

    public String getTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }
}
