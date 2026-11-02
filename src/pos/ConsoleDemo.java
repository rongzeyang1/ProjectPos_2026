package pos;

/**
 * Headless walk-through of both use cases.  It exercises exactly the same domain
 * objects as the GUI and prints the receipts, which is handy for the report and
 * for regression checking.
 */
public final class ConsoleDemo {

    private ConsoleDemo() { }

    public static void main(String[] args) {
        ProductCatalog catalog = new ProductCatalog();
        Register register = new Register(catalog);

        System.out.println("=== USE CASE 1: PROCESS SALE ===");
        register.makeNewSale();
        register.enterItem("1001", 2);
        register.enterItem("1003", 1);
        register.enterItem("1004", 1);
        register.enterItem("1009", 2);
        double total = register.endSale();
        System.out.printf("endSale() -> total %.2f%n", total);
        double change = register.makePayment(60.00);
        System.out.printf("makePayment(60.00) -> change %.2f%n%n", change);
        System.out.println(ReceiptPrinter.receipt(register.getSale()));

        System.out.println("=== USE CASE 2: HANDLE RETURNS ===");
        register.makeNewReturn();
        register.enterReturnItem("1002", 1);
        register.enterReturnItem("1009", 2);
        double refund = register.endReturn();
        System.out.printf("endReturn() -> refund %.2f%n", refund);
        register.makeRefund(true);
        System.out.println();
        System.out.println(ReceiptPrinter.refundReceipt(register.getCurrentReturn()));
    }
}
