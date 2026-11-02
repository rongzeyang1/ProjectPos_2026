package pos;

public class CardPayment extends Payment {

    private final String cardNumber;
    private boolean authorised;

    public CardPayment(double amount, String cardNumber) {
        super(amount);
        this.cardNumber = cardNumber;
    }

    /** Simulated call to an external payment authorisation service (Adapter target). */
    public boolean authorize() {
        authorised = cardNumber != null && cardNumber.replaceAll("\\s", "").length() >= 4;
        return authorised;
    }

    public boolean isAuthorised() { return authorised; }

    public String getCardNumber() { return cardNumber; }

    @Override
    public String getMethod() { return "CARD"; }
}
