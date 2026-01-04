package org.example.Service;

import org.example.DAO.InvoicesDAO;
import org.example.Model.InvoicesModel;

public class InvoiceService {
    private final InvoicesDAO InvoicesDAO = new InvoicesDAO();

    public int createInvoice(InvoicesModel invoice) throws Exception{
        return InvoicesDAO.createInvoice(invoice);
    }
}