package Payment.Imp;

import Payment.PaymentStrategy;
import Payment.Imp.PricingCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

public class CashStrategy implements PaymentStrategy {

    @Override
    public HashMap calculate(double pricePerNight, LocalDate checkIn, LocalDate checkOut,
                             LocalDate bookingDate, double taxRate) {
        return PricingCalculator.calculateTotal(pricePerNight, checkIn, checkOut, bookingDate, taxRate);
    }

    @Override
    public void pay(BigDecimal amount) {
        // Cash is settled in person at check-in/check-out — typically just
        // record the payment, no external gateway call needed.
        System.out.println("Cash payment of $" + amount + " recorded. Collect at front desk.");
    }
}