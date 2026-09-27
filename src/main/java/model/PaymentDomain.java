package model;

import Payment.PaymentStrategy;

import java.beans.BeanInfo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PaymentDomain {

    private UUID id;
    private UUID reservation_id;
    private BigDecimal amount;
    private LocalDate payment_date;
    private String payment_method;

    public PaymentDomain(UUID id,UUID reservation_id,BigDecimal amount,LocalDate payment_date,String payment_method) {
        this.id = UUID.randomUUID();
        this.reservation_id = reservation_id;
        this.amount = amount;
        this.payment_date = payment_date;
        this.payment_method = payment_method;
    }

    public LocalDate getPayment_date() {
        return payment_date;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public void setPayment_date(LocalDate payment_date) {
        this.payment_date = payment_date;
    }
//

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(UUID reservation_id) {
        this.reservation_id = reservation_id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
//    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
//    reservation_id UUID NOT NULL,
//    amount NUMERIC(10, 2) NOT NULL,
//    payment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
//                          --status VARCHAR(30) NOT NULL,
//    payment_method  VARCHAR(30) NOT NULL,
}