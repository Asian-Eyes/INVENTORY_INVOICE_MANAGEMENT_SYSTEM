package org.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.example.Model.*;

public final class DB {
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure();
            String url = "jdbc:mysql://" + Config.getDbHost() + ":" + Config.getDbPort() + "/" + Config.getDbName()
                    + "?allowPublicKeyRetrieval=true&useSSL=false";
            configuration.setProperty("hibernate.connection.url", url);
            configuration.setProperty("hibernate.connection.username", Config.getDbUser());
            configuration.setProperty("hibernate.connection.password", Config.getDbPassword());
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("hibernate.show_sql", "true");

            configuration.addAnnotatedClass(ProductModel.class);
            configuration.addAnnotatedClass(InvoiceModel.class);
            configuration.addAnnotatedClass(InvoiceItemModel.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private DB() {
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}