package com.submission.mis.onlinesubmissions.servlet;

import com.submission.mis.onlinesubmissions.model.Student;
import com.submission.mis.onlinesubmissions.util.HibernateUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

@WebServlet("/student")
public class StudentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("register".equals(action)) {
            registerStudent(request, response);
        } else if ("login".equals(action)) {
            loginStudent(request, response);
        }
    }

    private void registerStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Student student = new Student(email, password, name);
        session.save(student);

        tx.commit();
        session.close();

        response.sendRedirect("login.jsp?success=registered");
    }

    private void loginStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Session session = HibernateUtil.getSessionFactory().openSession();
        Student student = session.createQuery("FROM Student WHERE email = :email AND password = :password", Student.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .uniqueResult();
        session.close();

        if (student != null) {
            request.getSession().setAttribute("student", student);
            response.sendRedirect("dashboard.jsp");
        } else {
            response.sendRedirect("login.jsp?error=invalid_credentials");
        }
    }
}
