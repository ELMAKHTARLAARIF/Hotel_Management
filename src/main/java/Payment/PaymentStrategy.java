package Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

public interface PaymentStrategy {

    HashMap calculate(double pricePerNight, LocalDate checkIn, LocalDate checkOut, LocalDate bookingDate, double taxRate);

    void pay(BigDecimal amount);
}