package repository;

import model.PaymentDomain;

import java.sql.SQLException;

public interface IPaymentRepository {
    static void create(PaymentDomain payment) throws SQLException {};
}
