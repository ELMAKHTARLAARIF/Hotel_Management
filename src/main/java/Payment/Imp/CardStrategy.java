package Payment.Imp;

import Payment.PaymentStrategy;

public class CardStrategy implements PaymentStrategy {

    @Override
    public double calculate(double pricePerNight, long numberOfNights, double taxRate) {

        double subtotal = pricePerNight * numberOfNights;
        double tax = subtotal * taxRate;

        return subtotal + tax;
    }

    @Override
    public void pay(double amount) {


    }
}