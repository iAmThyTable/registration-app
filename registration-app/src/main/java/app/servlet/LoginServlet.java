package app.servlet;

import app.database.DatabaseManager;
import app.validation.SimpleValidator;
import app.validation.response.ValidationResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

public class LoginServlet extends HttpServlet {
    DatabaseManager databaseManager = new DatabaseManager();
    SimpleValidator validator = new SimpleValidator();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        ValidationResponse validationResponse = validator.getLoginValidity(
                email, password, databaseManager);

        response.setContentType("text/html");
        HttpSession session = request.getSession(true);

        if (validationResponse.isValid()) {
            session.setAttribute("userEmail", email);
            response.sendRedirect("logged");
        } else {
            String message = "Login failed: " + validationResponse.errorMessage();
            String encodedMessage = URLEncoder.encode(message, "UTF-8");

            response.sendRedirect("failure.html?error=" + encodedMessage);
        }
    }
}
