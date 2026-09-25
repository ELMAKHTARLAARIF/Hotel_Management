package Payment;

public interface PaymentStrategy {

    double calculate(double pricePerNight, long numberOfNights, double taxRate);

    void pay(double amount);
}