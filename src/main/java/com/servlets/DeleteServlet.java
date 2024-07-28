package com.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.entities.Note;
import com.helper.FactoryProvider;

public class DeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DeleteServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = null;
        Transaction transaction = null;

        try {
            int noteId = Integer.parseInt(request.getParameter("note_id").trim());

            session = FactoryProvider.getFactory().openSession();
            transaction = session.beginTransaction();

            Note note = (Note) session.get(Note.class, noteId);
            if (note != null) {
                session.delete(note);
                transaction.commit();
            } else {
                transaction.rollback();
                // Handle the case where the note was not found
                response.sendRedirect("error_page.jsp?message=Note%20not%20found");
                return;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            response.sendRedirect("error_page.jsp?message=An%20error%20occurred");
        } finally {
            if (session != null) {
                session.close();
            }
        }

        response.sendRedirect("all_notes.jsp");
    }
}
