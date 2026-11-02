package pos;

public class CashPayment extends Payment {

    private final double cashTendered;

    public CashPayment(double amount, double cashTendered) {
        super(amount);
        this.cashTendered = cashTendered;
    }

    public double getCashTendered() { return cashTendered; }

    public double getChange() { return cashTendered - amount; }

    @Override
    public String getMethod() { return "CASH"; }
}
