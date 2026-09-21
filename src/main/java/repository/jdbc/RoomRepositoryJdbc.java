package repository.jdbc;

import db.DatabaseConnection;
import model.RoomDomain;
import model.enums.RoomStatus;
import model.enums.RoomType;
import repository.IRoomRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomRepositoryJdbc implements IRoomRepository {

    private final DatabaseConnection databaseConnection;

    public RoomRepositoryJdbc(
            DatabaseConnection databaseConnection
    ) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public RoomDomain create(RoomDomain room) {

        String sql =
                "INSERT INTO rooms " +
                        "(roomnumber, user_id, roomtype, pricepernight, roomstatus, capacity) " +
                        "VALUES (?, ?, ?, ?, ?, ?) " +
                        "RETURNING id";

        try (
                Connection connection =
                        databaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    room.getRoomNumber()
            );

            statement.setObject(
                    2,
                    room.getUserId()
            );

            statement.setString(
                    3,
                    room.getRoomType().name()
            );

            statement.setBigDecimal(
                    4,
                    room.getPricePerNight()
            );

            statement.setString(
                    5,
                    room.getRoomStatus().name()
            );

            statement.setInt(
                    6,
                    room.getCapacity()
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    UUID id =
                            resultSet.getObject(
                                    "id",
                                    UUID.class
                            );

                    room.setId(id);

                    return room;
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error creating room: "
                            + e.getMessage(),
                    e
            );
        }

        throw new RuntimeException(
                "Room could not be created."
        );
    }

    @Override
    public RoomDomain findById(UUID id) {

        String sql =
                "SELECT id, user_id, roomnumber, roomtype, " +
                        "pricepernight, roomstatus, capacity " +
                        "FROM rooms " +
                        "WHERE id = ?";

        try (
                Connection connection =
                        databaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setObject(
                    1,
                    id
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {
                    return mapRoom(resultSet);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding room: "
                            + e.getMessage(),
                    e
            );
        }

        return null;
    }

    @Override
    public RoomDomain findByRoomNumber(
            String roomNumber
    ) {

        String sql =
                "SELECT id, user_id, roomnumber, roomtype, " +
                        "pricepernight, roomstatus, capacity " +
                        "FROM rooms " +
                        "WHERE LOWER(roomnumber) = LOWER(?)";

        try (
                Connection connection =
                        databaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    roomNumber
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {
                    return mapRoom(resultSet);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error finding room by number: "
                            + e.getMessage(),
                    e
            );
        }

        return null;
    }

    @Override
    public List<RoomDomain> findAll() {

        List<RoomDomain> rooms =
                new ArrayList<>();

        String sql =
                "SELECT id, user_id, roomnumber, roomtype, " +
                        "pricepernight, roomstatus, capacity " +
                        "FROM rooms " +
                        "ORDER BY roomnumber";

        try (
                Connection connection =
                        databaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                rooms.add(
                        mapRoom(resultSet)
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error getting rooms: "
                            + e.getMessage(),
                    e
            );
        }

        return rooms;
    }

    @Override
    public void update(RoomDomain room) {

        String sql =
                "UPDATE rooms " +
                        "SET roomnumber = ?, " +
                        "user_id = ?, " +
                        "roomtype = ?, " +
                        "pricepernight = ?, " +
                        "roomstatus = ?, " +
                        "capacity = ? " +
                        "WHERE id = ?";

        try (
                Connection connection =
                        databaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    room.getRoomNumber()
            );

            statement.setObject(
                    2,
                    room.getUserId()
            );

            statement.setString(
                    3,
                    room.getRoomType().name()
            );

            statement.setBigDecimal(
                    4,
                    room.getPricePerNight()
            );

            statement.setString(
                    5,
                    room.getRoomStatus().name()
            );

            statement.setInt(
                    6,
                    room.getCapacity()
            );

            statement.setObject(
                    7,
                    room.getId()
            );

            int rowsUpdated =
                    statement.executeUpdate();

            if (rowsUpdated == 0) {

                throw new RuntimeException(
                        "Room not found."
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error updating room: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public void delete(UUID id) {

        String sql =
                "DELETE FROM rooms " +
                        "WHERE id = ?";

        try (
                Connection connection =
                        databaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setObject(
                    1,
                    id
            );

            int rowsDeleted =
                    statement.executeUpdate();

            if (rowsDeleted == 0) {

                throw new RuntimeException(
                        "Room not found."
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error deleting room: "
                            + e.getMessage(),
                    e
            );
        }
    }

    private RoomDomain mapRoom(
            ResultSet resultSet
    ) throws SQLException {

        UUID id =
                resultSet.getObject(
                        "id",
                        UUID.class
                );

        UUID userId =
                resultSet.getObject(
                        "user_id",
                        UUID.class
                );

        String roomNumber =
                resultSet.getString(
                        "roomnumber"
                );

        String roomType =
                resultSet.getString(
                        "roomtype"
                );

        var pricePerNight =
                resultSet.getBigDecimal(
                        "pricepernight"
                );

        String roomStatus =
                resultSet.getString(
                        "roomstatus"
                );

        int capacity =
                resultSet.getInt(
                        "capacity"
                );

        RoomType type =
                RoomType.valueOf(
                        roomType
                );

        RoomStatus status =
                RoomStatus.valueOf(
                        roomStatus
                );

        return new RoomDomain(
                id,
                userId,
                roomNumber,
                type,
                pricePerNight,
                status,
                capacity
        );
    }
}