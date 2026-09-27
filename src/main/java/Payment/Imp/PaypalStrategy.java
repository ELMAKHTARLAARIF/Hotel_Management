package Payment.Imp;

import Payment.PaymentStrategy;
import Payment.Imp.PricingCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

public class PaypalStrategy implements PaymentStrategy {

    @Override
    public HashMap calculate(double pricePerNight, LocalDate checkIn, LocalDate checkOut,
                             LocalDate bookingDate, double taxRate) {
        return PricingCalculator.calculateTotal(pricePerNight, checkIn, checkOut, bookingDate, taxRate);
    }

    @Override
    public void pay(BigDecimal amount) {
        // TODO: call PayPal SDK/API
    }
}