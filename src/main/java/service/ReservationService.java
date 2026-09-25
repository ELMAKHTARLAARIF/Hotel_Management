package service;

import exception.InvalidReservationDateException;
import exception.RoomNotAvailableException;
import exception.RoomNotFoundException;
import model.ReservationDomain;
import model.RoomDomain;
import model.enums.ReservationStatus;
import model.enums.RoomStatus;
import repository.IReservationRepository;
import repository.IRoomRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ReservationService {
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

    public boolean makeReservation(String roomNumber, LocalDate checkIn, LocalDate checkOut, int numberOfGuests, UUID userId) {
        // Validate dates checkOut before CheckIn
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new InvalidReservationDateException("Check-out date must be after check-in date.");
        }
        // CheckIn before now date
        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidReservationDateException("Cannot book a room in the past.");
        }

        // Find the room
        RoomDomain targetRoom = null;
        for (RoomDomain room : roomRepository.findAll()) {
            if (room.getRoomNumber().equals(roomNumber)) {
                targetRoom = room;
                break;
            }
        }

        if (targetRoom == null) {
            throw new RoomNotFoundException("Room " + roomNumber + " does not exist.");
        }

        // Check capacity
        if (numberOfGuests > targetRoom.getCapacity()) {
            throw new IllegalArgumentException("Room capacity is " + targetRoom.getCapacity() + ", but you requested for " + numberOfGuests + " guests.");
        }

        for (ReservationDomain res : reservationRepository.getAll()) {
            if (res.getRoomId().equals(targetRoom.getId()) && res.getStatus() == ReservationStatus.CONFIRMED) {

                boolean isOverlapping = !(checkOut.isBefore(res.getCheck_in()) || checkOut.isEqual(res.getCheck_in()) || checkIn.isAfter(res.getCheck_out()) || checkIn.isEqual(res.getCheck_out()));
                if (isOverlapping) {
                    throw new RoomNotAvailableException("Room " + roomNumber + " is already booked from " + res.getCheck_in() + " to " + res.getCheck_out());
                }
            }
        }


        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        BigDecimal totalPrice = targetRoom.getPricePerNight().multiply(BigDecimal.valueOf(nights));
        
        Random random = new Random();
        String reservationCode = String.valueOf(100000 + random.nextInt(900000));

        ReservationDomain newReservation = new ReservationDomain(null, reservationCode, userId, targetRoom.getId(), numberOfGuests, checkIn, checkOut, totalPrice, ReservationStatus.PENDING, new Date());
        reservationRepository.create(newReservation);
        targetRoom.setRoomStatus(RoomStatus.OCCUPIED);
        System.out.println("Success! Reservation created. Total Price: $" + totalPrice + " for " + nights + " nights." + userId);
        return true;
    }

    public List<ReservationDomain> myReservations(UUID user_id) {
        try {
            return reservationRepository.findByUserID(user_id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
