package Payment.Imp;

import Payment.PaymentStrategy;
import Payment.Imp.PricingCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

public class CardStrategy implements PaymentStrategy {

    @Override
    public HashMap calculate(double pricePerNight, LocalDate checkIn, LocalDate checkOut,
                             LocalDate bookingDate, double taxRate) {
        return PricingCalculator.calculateTotal(pricePerNight, checkIn, checkOut, bookingDate, taxRate);
    }

    @Override
    public void pay(BigDecimal amount) {
        // TODO: integrate with card processor (Stripe, etc.)
    }
}