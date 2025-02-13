package com.submission.mis.onlinesubmissions.servlet;

import com.submission.mis.onlinesubmissions.model.Assignment;
import com.submission.mis.onlinesubmissions.model.Instructor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.Date;

@WebServlet("/instructor")
public class InstructorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            loginInstructor(request, response);
        } else if ("createAssignment".equals(action)) {
            createAssignment(request, response);
        }
    }

    private void loginInstructor(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Session session = HibernateUtil.getSessionFactory().openSession();
        Instructor instructor = session.createQuery("FROM Instructor WHERE email = :email AND password = :password", Instructor.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .uniqueResult();
        session.close();

        if (instructor != null) {
            request.getSession().setAttribute("instructor", instructor);
            response.sendRedirect("instructor_dashboard.jsp");
        } else {
            response.sendRedirect("instructor_login.jsp?error=invalid_credentials");
        }
    }

    private void createAssignment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Instructor instructor = (Instructor) request.getSession().getAttribute("instructor");
        if (instructor == null) {
            response.sendRedirect("instructor_login.jsp?error=session_expired");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        Date deadline = java.sql.Date.valueOf(request.getParameter("deadline"));

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDeadline(deadline);
        assignment.setInstructor(instructor);
        session.save(assignment);

        tx.commit();
        session.close();

        response.sendRedirect("instructor_dashboard.jsp?success=assignment_created");
    }
}
