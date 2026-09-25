package model;

import model.enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class ReservationDomain {
    private UUID id;
    private String reservationCode;
    private UUID userId;
    private UUID roomId;
    private int numberOfGuest;
    private LocalDate check_in;
    private LocalDate check_out;
    private ReservationStatus status;
    private Date created_at;
    private BigDecimal total_amount;

    public ReservationDomain(UUID id, String reservationCode, UUID userId, UUID roomId, int numberOfGuest, LocalDate check_in, LocalDate check_out, BigDecimal total_amount, ReservationStatus status, Date created_at) {
        this.id = UUID.randomUUID();
        this.reservationCode = reservationCode;
        this.userId = userId;
        this.roomId = roomId;
        this.numberOfGuest = numberOfGuest;
        this.check_in = check_in;
        this.check_out = check_out;
        this.total_amount = total_amount;
        this.status = status;
        this.created_at = created_at;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public void setRoomId(UUID roomId) {
        this.roomId = roomId;
    }

    public int getNumberOfGuest() {
        return numberOfGuest;
    }

    public void setNumberOfGuest(int numberOfGuest) {
        this.numberOfGuest = numberOfGuest;
    }

    public LocalDate getCheck_in() {
        return check_in;
    }

    public void setCheck_in(LocalDate check_in) {
        this.check_in = check_in;
    }

    public LocalDate getCheck_out() {
        return check_out;
    }

    public void setCheck_out(LocalDate check_out) {
        this.check_out = check_out;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }
}