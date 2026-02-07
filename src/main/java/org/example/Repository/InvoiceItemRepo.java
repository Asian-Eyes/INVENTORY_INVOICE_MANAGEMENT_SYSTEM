package org.example.Repository;

import org.example.Model.InvoiceItemModel;
import org.example.Model.QInvoiceItemModel;
import org.example.util.DB;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class InvoiceItemRepo {

    public int create(InvoiceItemModel item) throws Exception {
        try (Session session = DB.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(item);
            transaction.commit();
            return item.getId();
        }
    }

    public boolean deleteByProductIdAndInvoiceId(int productId, int invoiceId) throws Exception {
        try (Session session = DB.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            // QueryDSL Delete
            JPAQueryFactory queryFactory = new JPAQueryFactory(session);
            QInvoiceItemModel qItem = QInvoiceItemModel.invoiceItemModel;

            long updated = queryFactory.delete(qItem)
                    .where(qItem.product.id.eq(productId)
                            .and(qItem.invoice.id.eq(invoiceId)))
                    .execute();
            transaction.commit();
            return updated > 0;
        }
    }

    public List<InvoiceItemModel> getListByInvoiceId(int invoiceId) throws Exception {
        try (Session session = DB.getSessionFactory().openSession()) {
            JPAQueryFactory queryFactory = new JPAQueryFactory(session);
            QInvoiceItemModel qItem = QInvoiceItemModel.invoiceItemModel;

            return queryFactory.selectFrom(qItem)
                    .where(qItem.invoice.id.eq(invoiceId))
                    .fetch();
        }
    }
}