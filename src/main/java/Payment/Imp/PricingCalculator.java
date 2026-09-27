package Payment.Imp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public final class PricingCalculator {

    private PricingCalculator() {
    }

    public static HashMap calculateTotal(double pricePerNight, LocalDate checkIn, LocalDate checkOut, LocalDate bookingDate, double taxRate) {

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
//        exemple nights = 5
        // 1) Price each night, applying season + weekend modifiers per-night
        double subtotal = 0.0;

        for (int i = 0; i < nights; i++) {
            LocalDate night = checkIn.plusDays(i);
            double rate = pricePerNight;

            Month month = night.getMonth();
            if (month == Month.JULY || month == Month.AUGUST) {
                rate *= 1.30;
            } else if (month == Month.NOVEMBER || month == Month.DECEMBER || month == Month.JANUARY || month == Month.FEBRUARY) {
                rate *= 0.85;
            }
            DayOfWeek dow = night.getDayOfWeek();
            if (dow == DayOfWeek.FRIDAY || dow == DayOfWeek.SATURDAY) {
                rate *= 1.15;
            }
            subtotal += rate;
        }

        if (nights >= 7) {
            subtotal *= 0.90;
        }
        if (nights >= 14) {
            subtotal *= 0.85;
        }
        long DaysBeforeCheckin = ChronoUnit.DAYS.between(bookingDate, checkIn);
        if (DaysBeforeCheckin >= 30) {
            subtotal *= 0.95;
        } else if (DaysBeforeCheckin <= 3) {
            subtotal *= 1.10;
        }

        double tax = subtotal * taxRate;
        double totalPrice = subtotal + tax;
        HashMap calculateDetails = new HashMap();
        calculateDetails.put("taxRate", taxRate);
        calculateDetails.put("subtotal", subtotal);
        calculateDetails.put("tax", tax);
        calculateDetails.put("totalPrice",totalPrice);
        return calculateDetails;
    }
}