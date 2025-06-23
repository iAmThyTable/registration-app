package app.servlet;

import app.database.DatabaseManager;
import app.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class LoggedServlet extends HttpServlet {
    DatabaseManager databaseManager = new DatabaseManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String email = (session != null) ?
                (String) session.getAttribute("userEmail") : null;

        if (email == null) {
            response.sendRedirect("login.html");
            return;
        }
        User user = databaseManager.getUserByEmail(email);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Logged In</title></head><body>");
        out.println("<h1>Welcome, " + user.getName() + "!</h1>");
        out.println("<p><a href=\"update.html\">Update Profile</a></p>");
        out.println("<p><a href=\"logout\">Logout</a></p></body></html>");
    }
}
