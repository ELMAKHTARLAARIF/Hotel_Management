package repository.jdbc;

import db.DatabaseConnection;
import model.InvoiceDomain;
import repository.IInvoiceRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InvoiceRepositoryJdbc implements IInvoiceRepository {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();
    public InvoiceRepositoryJdbc(){
    }
    @Override
    public void create(InvoiceDomain invoice) throws SQLException {
            String sql = "Insert Into invoices (id,reservation_id,invoice_number,subtotal,vat_amount,total_ttc,created_at";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                 statement.setObject(1,invoice.getId());
                 statement.setObject(2,invoice.getReservation_id());
                 statement.setString(3,invoice.getInvoice_code());
                 statement.setBigDecimal(4,invoice.getSubtotal_ht());
                 statement.setBigDecimal(5,invoice.getSubtotal_ht());
                 statement.setBigDecimal(6,invoice.getTotal_ttc());
                 statement.setObject(7,invoice.getCreated_at());
                 ResultSet resultSet = statement.executeQuery();
            } catch (SQLException e) {
                throw new SQLException("Error Creationg Invoice ! ",e.getMessage());
            }
            throw new RuntimeException("Invoice could not be Created");

    }
}
