package com.submission.mis.onlinesubmissions.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import com.submission.mis.onlinesubmissions.model.Instructor;
import com.submission.mis.onlinesubmissions.model.Student;
import com.submission.mis.onlinesubmissions.model.Assignment;
import com.submission.mis.onlinesubmissions.model.Submission;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

            // Explicitly add annotated classes (Entities)
            configuration.addAnnotatedClass(Instructor.class);
            configuration.addAnnotatedClass(Student.class);
            configuration.addAnnotatedClass(Assignment.class);
            configuration.addAnnotatedClass(Submission.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
