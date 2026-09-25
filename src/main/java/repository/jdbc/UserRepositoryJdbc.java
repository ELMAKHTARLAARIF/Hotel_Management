package repository.jdbc;

import db.DatabaseConnection;
import model.UserDomain;
import model.enums.UserRole;
import repository.IUserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepositoryJdbc implements IUserRepository {

    private final DatabaseConnection databaseConnection;

    public UserRepositoryJdbc(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public UserDomain create(UserDomain user) {

        String sql = "INSERT INTO users " + "(full_name, email, password_hash, role) " + "VALUES (?, ?, ?, ?) " + "RETURNING id";

        try (Connection connection = databaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getRole().name().toLowerCase());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    UUID id = resultSet.getObject("id", UUID.class);

                    user.setId(id);

                    return user;
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }

        throw new RuntimeException("User could not be created.");
    }

    @Override
    public UserDomain findById(UUID id) {

        String sql = "SELECT id, full_name, email, " + "password_hash, role " + "FROM users " + "WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException("Error finding user: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public UserDomain findByEmail(String email) {

        String sql = "SELECT id, full_name, email, " + "password_hash, role " + "FROM users " + "WHERE LOWER(email) = LOWER(?)";

        try (Connection connection = databaseConnection.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException("Error finding user by email: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<UserDomain> findAll() {

        List<UserDomain> users = new ArrayList<>();

        String sql = "SELECT id, full_name, email, " + "password_hash, role " + "FROM users";

        try (Connection connection = databaseConnection.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql);

             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }

        } catch (SQLException e) {

            throw new RuntimeException("Error getting users: " + e.getMessage(), e);
        }

        return users;
    }

    @Override
    public void update(UserDomain user) {

        String sql = "UPDATE users " + "SET full_name = ?, " + "password_hash = ?, " + "WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getPasswordHash());
            statement.setObject(3, user.getId());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("User not found.");
            }

        } catch (SQLException e) {

            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(UUID id) {

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = databaseConnection.getConnection();

             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted == 0) {
                throw new RuntimeException("User not found.");
            }

        } catch (SQLException e) {

            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }

    private UserDomain mapUser(ResultSet resultSet) throws SQLException {

        UUID id = resultSet.getObject("id", UUID.class);

        String fullName = resultSet.getString("full_name");

        String email = resultSet.getString("email");

        String passwordHash = resultSet.getString("password_hash");

        String role = resultSet.getString("role");

        UserRole userRole = UserRole.valueOf(role.toUpperCase());

        return new UserDomain(id, fullName, email, passwordHash, userRole);
    }
}