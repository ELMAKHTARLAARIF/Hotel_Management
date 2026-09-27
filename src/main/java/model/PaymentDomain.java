package model;

import java.beans.BeanInfo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

class PaymentDomain {

    private UUID id;
    private UUID reservation_id;
    private BigDecimal amount;
    private LocalDate payment_date;
    private String payment_method;

    public PaymentDomain(UUID id,UUID reservation_id,BigDecimal amount,LocalDate payment_date,String payment_method) {
        this.id = id;
        this.reservation_id = reservation_id;
        this.amount = amount;
        this.payment_date = payment_date;
        this.payment_method = payment_method;
    }
    //
//    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
//    reservation_id UUID NOT NULL,
//    amount NUMERIC(10, 2) NOT NULL,
//    payment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
//                          --status VARCHAR(30) NOT NULL,
//    payment_method  VARCHAR(30) NOT NULL,
}