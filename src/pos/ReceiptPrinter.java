package pos;

import java.util.List;

/** Formats a sale (or a return) as plain-text receipts for the thermal printer. */
public final class ReceiptPrinter {

    private static final String RULE = "----------------------------------------";

    private ReceiptPrinter() { }

    public static String receipt(Sale sale) {
        StringBuilder sb = new StringBuilder();
        sb.append("     RIVERSIDE SUPERMARKET\n");
        sb.append("     POS Terminal 01\n");
        sb.append(RULE).append("\n");
        sb.append("Date:    ").append(sale.getTimeStamp()).append("\n");
        sb.append("Cashier: A. Chen\n");
        sb.append(RULE).append("\n");
        List<SalesLineItem> items = sale.getLineItems();
        for (SalesLineItem item : items) {
            sb.append(String.format("%-2d %-22s%10.2f%n",
                    item.getQuantity(), trim(item.getDescription().getDescription(), 22),
                    item.getSubtotal()));
        }
        sb.append(RULE).append("\n");
        sb.append(String.format("%-29s%11s%n", "TOTAL", money(sale.getTotal())));
        for (Payment p : sale.getPayments()) {
            double tendered = (p instanceof CashPayment) ? ((CashPayment) p).getCashTendered() : p.getAmount();
            sb.append(String.format("%-29s%11s%n", "PAID (" + p.getMethod() + ")", money(tendered)));
        }
        if (sale.getChange() > 0) {
            sb.append(String.format("%-29s%11s%n", "CHANGE", money(sale.getChange())));
        }
        sb.append(RULE).append("\n");
        sb.append("   Thank you for shopping with us\n");
        return sb.toString();
    }

    public static String refundReceipt(Return ret) {
        StringBuilder sb = new StringBuilder();
        sb.append("     RIVERSIDE SUPERMARKET\n");
        sb.append("     REFUND / RETURN\n");
        sb.append(RULE).append("\n");
        sb.append("Date:    ").append(ret.getTimeStamp()).append("\n");
        sb.append("Cashier: A. Chen\n");
        sb.append(RULE).append("\n");
        for (ReturnLineItem item : ret.getLineItems()) {
            sb.append(String.format("%-2d %-22s%10.2f%n",
                    item.getQuantity(), trim(item.getDescription().getDescription(), 22),
                    item.getSubtotal()));
        }
        sb.append(RULE).append("\n");
        sb.append(String.format("%-29s%11s%n", "REFUND TOTAL", money(ret.getTotal())));
        sb.append(String.format("%-29s%11s%n", "METHOD",
                ret.getRefund() == null ? "-" : ret.getRefund().getMethod()));
        sb.append(RULE).append("\n");
        sb.append("   Refund authorised by A. Chen\n");
        return sb.toString();
    }

    private static String money(double v) { return String.format("\u00A5%.2f", v); }

    private static String trim(String s, int n) { return s.length() <= n ? s : s.substring(0, n - 1) + "."; }
}
