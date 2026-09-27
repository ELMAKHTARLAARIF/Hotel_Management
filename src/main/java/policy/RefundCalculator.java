package Payment.Imp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class RefundCalculator {

    private RefundCalculator() {
    }

    public static BigDecimal calculateRefund(BigDecimal totalAmount, LocalDate checkIn) {

        long hoursUntilCheckIn = ChronoUnit.HOURS.between(LocalDateTime.now(), checkIn.atStartOfDay());

        double refundRate;
        if (hoursUntilCheckIn > 14 * 24) {
            refundRate = 1.00; // Plus de 14 jours -> remboursement intégral
        } else if (hoursUntilCheckIn >= 7 * 24) {
            refundRate = 0.70; // Entre 7 et 14 jours -> pénalité 30%
        } else if (hoursUntilCheckIn >= 48) {
            refundRate = 0.50; // Entre 48h et 7 jours -> pénalité 50%
        } else {
            refundRate = 0.00; // Moins de 48h -> aucun remboursement
        }

        return totalAmount.multiply(BigDecimal.valueOf(refundRate)).setScale(2, RoundingMode.HALF_UP);
    }
}