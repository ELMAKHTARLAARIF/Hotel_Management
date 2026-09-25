package repository;

import model.ReservationDomain;
import model.enums.ReservationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IReservationRepository {

    Optional<ReservationDomain> findByCode(String Code);

    void create(ReservationDomain reservation);

    void changeStatus(String reservationCode, ReservationStatus status);

    List<ReservationDomain> getAll();

    List<ReservationDomain> findByUserID(UUID user_id);

}