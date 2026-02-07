package org.example.Service;

import org.example.Model.InvoiceModel;
import org.example.Model.InvoiceItemModel;
import org.example.Repository.InvoiceRepo;
import org.example.Repository.InvoiceItemRepo;
import java.util.List;

public class InvoiceService {
    private final InvoiceRepo invoiceRepo = new InvoiceRepo();
    private final InvoiceItemRepo invoiceItemRepo = new InvoiceItemRepo();

    public int createInvoice(InvoiceModel invoice) throws Exception {
        return invoiceRepo.create(invoice);
    }

    public int createInvoiceItem(InvoiceItemModel item) throws Exception {
        return invoiceItemRepo.create(item);
    }

    public List<InvoiceModel> getAllInvoices() throws Exception {
        return invoiceRepo.getAll();
    }

    public InvoiceModel getInvoiceById(int id) throws Exception {
        return invoiceRepo.getByInvoiceId(id);
    }

    public void recalculateInvoiceTotal(int invoiceId) throws Exception {
        // Business logic: Calculate total from items and update invoice
        List<InvoiceItemModel> items = invoiceItemRepo.getListByInvoiceId(invoiceId);
        double total = items.stream().mapToDouble(InvoiceItemModel::getLineTotal).sum();

        InvoiceModel invoice = invoiceRepo.getByInvoiceId(invoiceId);
        if (invoice != null) {
            invoice.setTotal(total);
            invoiceRepo.update(invoice);
        }
    }
}