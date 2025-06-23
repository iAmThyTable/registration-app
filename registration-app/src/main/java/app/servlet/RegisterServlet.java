package app.servlet;

import app.captcha.Captcha;
import app.captcha.MathCaptcha;
import app.database.DatabaseManager;
import app.user.User;
import app.validation.SimpleValidator;
import app.validation.response.ValidationResponse;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;

public class RegisterServlet extends HttpServlet {
    final DatabaseManager databaseManager;
    final SimpleValidator validator;

    public RegisterServlet() {
        this.databaseManager = new DatabaseManager();
        this.validator = new SimpleValidator();
    }

    public RegisterServlet(DatabaseManager db, SimpleValidator val) {
        this.databaseManager = db;
        this.validator = val;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        Captcha captcha = new MathCaptcha();
        session.setAttribute("captcha", captcha);

        request.getRequestDispatcher("register.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            fail("Session expired or invalid.", response);
            return;
        }

        Captcha captcha = (Captcha) session.getAttribute("captcha");
        String captchaAnswer = request.getParameter("captchaAnswer");

        if (captcha == null || captchaAnswer == null ||
                !captcha.validate(captchaAnswer.trim())) {
            fail("Captcha answer is incorrect.", response);
            return;
        }

        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        ValidationResponse validationResponse = validator.getRegisterValidity(email,
                name, password, databaseManager);

        if (validationResponse.isValid()) {
            User newUser = new User(email, name, password);
            databaseManager.addUser(newUser);
            session.setAttribute("userEmail", email);

            response.sendRedirect("logged");
        } else {
            fail("Registration failed: " +
                    validationResponse.errorMessage(), response);
        }
    }

    private void fail(String message, HttpServletResponse response) throws IOException {
        String encodedMessage = URLEncoder.encode(message, "UTF-8");
        response.sendRedirect("failure.html?error=" + encodedMessage);
    }
}
