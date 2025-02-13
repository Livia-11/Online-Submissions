package com.submission.mis.onlinesubmissions.servlet;

import com.submission.mis.onlinesubmissions.model.Student;
import com.submission.mis.onlinesubmissions.model.Submission;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@WebServlet("/submission")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class SubmissionServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Student student = (Student) request.getSession().getAttribute("student");
        if (student == null) {
            response.sendRedirect("login.jsp?error=session_expired");
            return;
        }

        Long assignmentId = Long.parseLong(request.getParameter("assignmentId"));
        Part filePart = request.getPart("file");

        if (filePart == null || filePart.getSize() == 0) {
            response.sendRedirect("submission.jsp?error=no_file");
            return;
        }

        String fileName = new File(filePart.getSubmittedFileName()).getName();
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        com.submission.mis.onlinesubmissions.model.Assignment assignment = session.get(com.submission.mis.onlinesubmissions.model.Assignment.class, assignmentId);
        if (assignment == null) {
            response.sendRedirect("submission.jsp?error=invalid_assignment");
            return;
        }

        Submission submission = new Submission();
        submission.setStudent(student);
        submission.setAssignment(assignment);
        submission.setFilePath(filePath);
        submission.setSubmissionDate(new Date());

        session.save(submission);
        tx.commit();
        session.close();

        response.sendRedirect("dashboard.jsp?success=submission_uploaded");
    }
}
