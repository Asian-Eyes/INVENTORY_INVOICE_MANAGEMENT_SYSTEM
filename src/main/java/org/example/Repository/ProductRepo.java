package org.example.Repository;

import org.example.Model.ProductModel;
import org.example.Model.QProductModel;
import org.example.util.DB;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ProductRepo {

    public int create(ProductModel product) throws Exception {
        try (Session session = DB.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(product);
            transaction.commit();
            return product.getId();
        }
    }

    public boolean update(ProductModel product) throws Exception {
        try (Session session = DB.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(product);
            transaction.commit();
            return true;
        }
    }

    public boolean delete(int productId) throws Exception {
        try (Session session = DB.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            JPAQueryFactory queryFactory = new JPAQueryFactory(session);
            QProductModel qProduct = QProductModel.productModel;

            long deleted = queryFactory.delete(qProduct)
                    .where(qProduct.id.eq(productId))
                    .execute();

            transaction.commit();
            return deleted > 0;
        }
    }

    public List<ProductModel> getAll() throws Exception {
        try (Session session = DB.getSessionFactory().openSession()) {
            return new JPAQueryFactory(session)
                    .selectFrom(QProductModel.productModel)
                    .fetch();
        }
    }

}
