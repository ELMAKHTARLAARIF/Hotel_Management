package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class InvoiceDomain {
     private UUID id;
     private UUID reservation_id;
     private String invoice_code;
     private BigDecimal subtotal_ht;
     private BigDecimal vat_amount;
     private BigDecimal total_ttc;
     private LocalDate Created_at;


    public InvoiceDomain(UUID id, UUID reservation_id, String invoice_code, BigDecimal subtotal_ht, BigDecimal vat_amount, BigDecimal total_ttc, LocalDate created_at) {
        this.id = UUID.randomUUID();
        this.reservation_id = reservation_id;
        this.invoice_code = invoice_code;
        this.subtotal_ht = subtotal_ht;
        this.vat_amount = vat_amount;
        this.total_ttc = total_ttc;
        this.Created_at = created_at;
    }
    public String getInvoice_code() {
        return invoice_code;
    }

    public LocalDate getCreated_at() {
        return Created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        Created_at = created_at;
    }

    public BigDecimal getTotal_ttc() {
        return total_ttc;
    }

    public void setTotal_ttc(BigDecimal total_ttc) {
        this.total_ttc = total_ttc;
    }

    public BigDecimal getVat_amount() {
        return vat_amount;
    }

    public void setVat_amount(BigDecimal vat_amount) {
        this.vat_amount = vat_amount;
    }

    public BigDecimal getSubtotal_ht() {
        return subtotal_ht;
    }

    public void setSubtotal_ht(BigDecimal subtotal_ht) {
        this.subtotal_ht = subtotal_ht;
    }

    public void setInvoice_code(String invoice_code) {
        this.invoice_code = invoice_code;
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
}
