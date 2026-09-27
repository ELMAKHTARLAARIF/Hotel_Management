package repository;

import model.InvoiceDomain;

import java.sql.SQLException;

public interface IInvoiceRepository {

    public void create(InvoiceDomain invoice) throws SQLException;
}
