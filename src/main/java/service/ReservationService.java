package service;

import Payment.Imp.CardStrategy;
import Payment.Imp.CashStrategy;
import Payment.Imp.PaypalStrategy;
import Payment.Imp.RefundCalculator;
import Payment.PaymentStrategy;
import exception.InvalidReservationDateException;
import exception.ReservationNotFoundException;
import exception.RoomNotAvailableException;
import exception.RoomNotFoundException;
import model.InvoiceDomain;
import model.PaymentDomain;
import model.ReservationDomain;
import model.RoomDomain;
import model.enums.ReservationStatus;
import model.enums.RoomStatus;
import repository.IInvoiceRepository;
import repository.IReservationRepository;
import repository.IRoomRepository;
import repository.jdbc.InvoiceRepositoryJdbc;
import repository.jdbc.PaymentRepositoryJdbc;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ReservationService {
    private static final double TAX_RATE = 0.20; // move to config/constant as needed
    private final IReservationRepository reservationRepository;
    private final IRoomRepository roomRepository;


    public ReservationService(IReservationRepository reservationRepositoryJdbc, IRoomRepository roomRepositoryJdbc) {
        this.reservationRepository = reservationRepositoryJdbc;
        this.roomRepository = roomRepositoryJdbc;
    }

    public void accept(String reservationCode) {

        Optional<ReservationDomain> reservation = reservationRepository.findByCode(reservationCode);

        if (reservation.isEmpty()) {

            throw new IllegalArgumentException("Reservation not found!");
        }

        reservationRepository.changeStatus(reservationCode, ReservationStatus.CONFIRMED);
    }

    public boolean makeReservation(String roomNumber, LocalDate checkIn, LocalDate checkOut, int numberOfGuests, UUID userId, String paymentMethod) throws SQLException {

        // Validate dates    CheckOut before Checkin
        if (checkOut.isBefore(checkIn) || checkOut.equals(checkIn)) {
            throw new InvalidReservationDateException("\"Check-out date must be after check-in date.\"");
        }
        // Checkin in the past problem
        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidReservationDateException("Cannot book a room in the past.");
        }

        // Find the room if not exist return optional  throw exception
        RoomDomain targetRoom = roomRepository.findByRoomNumber(roomNumber).orElseThrow(() -> new RoomNotFoundException("Room " + roomNumber + " does not exist."));

        // Check capacity
        if (numberOfGuests > targetRoom.getCapacity()) {
            throw new IllegalArgumentException("Room capacity is " + targetRoom.getCapacity() + ", but you requested for " + numberOfGuests + " guests.");
        }

        // Check overlap
        for (ReservationDomain res : reservationRepository.getAll()) {
            if (res.getRoomId().equals(targetRoom.getId()) && res.getStatus().equals(ReservationStatus.CONFIRMED)) {
                if (!(checkOut.isBefore(res.getCheck_in()) || checkOut.isEqual(res.getCheck_in()) || checkIn.isAfter(res.getCheck_out()) || checkIn.isEqual(res.getCheck_out()))) {
                    throw new RoomNotAvailableException("Room " + roomNumber + " is already booked from " + res.getCheck_in() + " to " + res.getCheck_out());
                }
            }
        }
        // Resolve the payment strategy from the raw string
        PaymentStrategy paymentStrategy;
        switch (paymentMethod.trim().toUpperCase()) {
            case "CASH":
                paymentStrategy = new CashStrategy();
                break;
            case "CARD":
                paymentStrategy = new CardStrategy();
                break;
            case "PAYPAL":
                paymentStrategy = new PaypalStrategy();
                break;
            default:
                throw new IllegalArgumentException("Unknown payment method: " + paymentMethod + ". Expected CASH, CARD, or PAYPAL.");
        }
        // Price the stay using the resolved payment strategy (bookingDate = today)
        LocalDate bookingDate = LocalDate.now();
        double pricePerNight = targetRoom.getPricePerNight().doubleValue();
        HashMap calculateDetails = paymentStrategy.calculate(pricePerNight, checkIn, checkOut, bookingDate, TAX_RATE);
        BigDecimal totalPrice = (BigDecimal) calculateDetails.get("totalPrice");
        Random random = new Random();
        String reservationCode = String.valueOf(100000 + random.nextInt(900000));

        ReservationDomain newReservation = new ReservationDomain(null, reservationCode, userId, targetRoom.getId(), numberOfGuests, checkIn, checkOut, totalPrice, ReservationStatus.PENDING, new Date());
        reservationRepository.create(newReservation);

        // Charge the customer
        paymentStrategy.pay(totalPrice);
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        targetRoom.setRoomStatus(RoomStatus.OCCUPIED);
        System.out.println("Success! Reservation created. Total Price: $" + totalPrice + " for " + nights + " nights." + userId);
//        create Payment table;
        PaymentDomain payment = new PaymentDomain(null, newReservation.getId(), (BigDecimal) calculateDetails.get("totalPrice"), LocalDate.now(), paymentMethod);
        PaymentRepositoryJdbc.create(payment);
//        create Invoice table;
        createInvoice(calculateDetails, newReservation);

        return true;
    }

    public void createInvoice(HashMap calculateDetails, ReservationDomain newReservation) throws SQLException {
        Random random = new Random();
        String invoiceCode = String.valueOf(100000 + random.nextInt(900000));
        InvoiceDomain invoice = new InvoiceDomain(null, newReservation.getId(), invoiceCode, (BigDecimal) calculateDetails.get("subtotal"), (BigDecimal) calculateDetails.get("tax"), (BigDecimal) calculateDetails.get("totalPrice"), null);
        IInvoiceRepository invoiceRepo = new InvoiceRepositoryJdbc();
        invoiceRepo.create(invoice);
    }

    public List<ReservationDomain> myReservations(UUID user_id) {
        try {
            return reservationRepository.findByUserID(user_id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public BigDecimal cancelReservation(String reservationCode, UUID currentUserId) throws SQLException, ReservationNotFoundException {

        ReservationDomain reservation = reservationRepository.findByCode(reservationCode)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation " + reservationCode + " does not exist."));

        if (!reservation.getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("This reservation does not belong to the current user.");
        }

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException("Only confirmed reservations can be cancelled. Current status: "
                    + reservation.getStatus());
        }

        BigDecimal refundAmount = RefundCalculator.calculateRefund(reservation.getTotal_amount(), reservation.getCheck_in());

        reservationRepository.updateStatus(reservation.getId(), ReservationStatus.CANCELLED);

        return refundAmount;
    }
}
