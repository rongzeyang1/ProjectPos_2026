package pos;

/** Abstract payment; CashPayment and CardPayment specialise it. */
public abstract class Payment {

    protected final double amount;

    protected Payment(double amount) { this.amount = amount; }

    public double getAmount() { return amount; }

    public abstract String getMethod();
}
