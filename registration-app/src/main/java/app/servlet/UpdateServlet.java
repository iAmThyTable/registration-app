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
import java.net.URLEncoder;

public class UpdateServlet extends HttpServlet {
    DatabaseManager databaseManager;
    SimpleValidator validator;

    public UpdateServlet() {
        databaseManager = new DatabaseManager();
        validator = new SimpleValidator();
    }

    public UpdateServlet(DatabaseManager db, SimpleValidator val) {
        this.databaseManager = db;
        this.validator = val;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        HttpSession session = request.getSession(false);
        String email = (session != null) ?
                (String) session.getAttribute("userEmail") : null;

        if (email == null) {
            session = request.getSession(true);
            session.setAttribute("message", "Please login before changing your data!");
            response.sendRedirect("login.html");
            return;
        }

        ValidationResponse validationResponse = validator.getUpdateValidity(name, password);

        if (validationResponse.isValid()) {
            databaseManager.updateUserInfo(email, name, password);
            response.sendRedirect("logged");
        } else {
            String message = "Updagte failed: " + validationResponse.errorMessage();
            String encodedMessage = URLEncoder.encode(message, "UTF-8");

            response.sendRedirect("failure.html?error=" + encodedMessage);
        }
    }
}
