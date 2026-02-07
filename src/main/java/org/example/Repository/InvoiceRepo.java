package org.example.Repository;

import org.example.Model.InvoiceModel;
import org.example.Model.QInvoiceModel;
import org.example.util.DB;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class InvoiceRepo {
    public int create(InvoiceModel invoice) throws Exception {
        try (Session session = DB.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(invoice);
            transaction.commit();
            return invoice.getId();
        }
    }

    public void update(InvoiceModel invoice) throws Exception {
        try (Session session = DB.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(invoice);
            transaction.commit();
        }
    }

    public List<InvoiceModel> getAll() throws Exception {
        try (Session session = DB.getSessionFactory().openSession()) {
            return new JPAQueryFactory(session)
                    .selectFrom(QInvoiceModel.invoiceModel)
                    .orderBy(QInvoiceModel.invoiceModel.createdAt.desc())
                    .fetch();
        }
    }

    public InvoiceModel getByInvoiceId(int invoiceId) throws Exception {
        try (Session session = DB.getSessionFactory().openSession()) {
            JPAQueryFactory queryFactory = new JPAQueryFactory(session);
            InvoiceModel invoice = queryFactory.selectFrom(QInvoiceModel.invoiceModel)
                    .where(QInvoiceModel.invoiceModel.id.eq(invoiceId))
                    .fetchOne();

            if (invoice == null) {
                throw new Exception("Invoice not found with ID: " + invoiceId);
            }
            return invoice;
        }
    }
}