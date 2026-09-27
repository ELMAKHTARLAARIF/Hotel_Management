package repository.jdbc;

import db.DatabaseConnection;
import model.PaymentDomain;
import repository.IPaymentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentRepositoryJdbc implements IPaymentRepository {
    private static final Connection connection = DatabaseConnection.getInstance().getConnection();

    public PaymentRepositoryJdbc() {
    }

    public static void create(PaymentDomain payment) throws SQLException {
        String sql = "INSERT INTO payments (id,reservation_id,amount,payment_date,payment_method) VALUES (?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, payment.getId());
            statement.setObject(2, payment.getReservation_id());
            statement.setBigDecimal(3, payment.getAmount());
            statement.setObject(4, payment.getPayment_date());
            statement.setString(5, payment.getPayment_method());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error creating Payment: " + e.getMessage(), e);
        }
    }
}
