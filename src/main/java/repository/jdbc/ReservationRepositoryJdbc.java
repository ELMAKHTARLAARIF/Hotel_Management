package repository.jdbc;

import db.DatabaseConnection;
import model.ReservationDomain;
import model.enums.ReservationStatus;
import repository.IReservationRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ReservationRepositoryJdbc implements IReservationRepository {
    private final DatabaseConnection databaseConnection;

    public ReservationRepositoryJdbc(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void create(ReservationDomain reservation) {

        String sql = "INSERT INTO reservations(id,reservationcode,user_id,room_id,numberofguests,check_in,check_out,reservationstatus,total_amount,created_at)";

        try (Connection connection = databaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql);

        ) {
            statement.setObject(1,reservation.getId());
            statement.setString(2,reservation.getReservationCode());
            statement.setObject(3,reservation.getUserId());
            statement.setObject(4,reservation.getRoomId());
            statement.setInt(5,reservation.getNumberOfGuest());
            statement.setObject(6,reservation.getCheck_in());
            statement.setObject(7,reservation.getCheck_out());
            statement.setObject(8,reservation.getStatus());
            statement.setBigDecimal(9,reservation.getTotal_amount());
            statement.setObject(10,reservation.getCreated_at());

            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException e) {

            throw new RuntimeException("Error creating Reservation: " + e.getMessage(), e);
        }

        throw new RuntimeException("Reservation could not be created.");
    }

    public Optional<ReservationDomain> findByCode(String reservationCode) {

        String sql = "SELECT * " + "FROM reservations " + "WHERE reservationcode = ?";

        try (Connection connection = databaseConnection.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, reservationCode);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return Optional.of(mapReservation(resultSet));
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException("Error finding reservation by code: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public void changeStatus(String reservationCode, ReservationStatus status) {

        String sql = "UPDATE reservations " + "SET status = ? " + "WHERE reservationcode = ?";

        try (Connection connection = databaseConnection.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status.name());

            statement.setString(2, reservationCode);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {

                throw new IllegalArgumentException("Reservation not found.");
            }

        } catch (SQLException e) {

            throw new RuntimeException("Error changing reservation status: " + e.getMessage(), e);
        }
    }

    public ReservationDomain mapReservation(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        UUID userId = resultSet.getObject("user_id", UUID.class);
        UUID roomId = resultSet.getObject("room_id", UUID.class);
        String reservationCode = resultSet.getNString("reservationcode");
        int numberOfGuest = resultSet.getInt("numberofguests");
        String Preparedstatus = resultSet.getString("reservationstatus");
        ReservationStatus status = ReservationStatus.valueOf(Preparedstatus);
        LocalDate check_in = resultSet.getObject("checkin", LocalDate.class);
        LocalDate check_out = resultSet.getObject("checkout", LocalDate.class);
        BigDecimal total_amount = resultSet.getBigDecimal("total_amount");
        Date created_at = resultSet.getDate("created_at");
        return new ReservationDomain(id, reservationCode, userId, roomId, numberOfGuest, check_in, check_out, total_amount, status, created_at);
    }

    public List<ReservationDomain> getAll() {
        List<ReservationDomain> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";

        try (Connection connection = databaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()) {
                reservations.add(mapReservation(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error get Reseration: " + e.getMessage());
        }
        return reservations;
    }


    public List<ReservationDomain> findByUserID(UUID userId) {
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        List<ReservationDomain> reservations = new ArrayList<>();

        try (
                Connection connection = databaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    reservations.add(mapReservation(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding reservations for user", e);
        }

        return reservations;
    }

}