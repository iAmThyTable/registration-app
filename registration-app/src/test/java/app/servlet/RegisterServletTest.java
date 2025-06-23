package app.servlet;

import app.captcha.Captcha;
import app.database.DatabaseManager;
import app.validation.SimpleValidator;
import app.validation.response.ValidationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class RegisterServletTest {

    private RegisterServlet servlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;
    private SimpleValidator mockValidator;
    private DatabaseManager mockDb;
    private Captcha mockCaptcha;
    ValidationResponse successResponse;
    ValidationResponse failResponse;

    @BeforeEach
    public void setUp() throws Exception {
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
        mockDb = mock(DatabaseManager.class);
        mockCaptcha = mock(Captcha.class);
        mockValidator = mock(SimpleValidator.class);
        successResponse = new ValidationResponse(true, "");
        failResponse = new ValidationResponse(false, "Invalid input");

        //servlet = new RegisterServlet(mockDb, mockValidator);
    }

    @Test
    void testDefaultConstructor() {
        RegisterServlet registerServlet = new RegisterServlet();

        assertNotNull(registerServlet);
        assertNotNull(registerServlet.databaseManager);
        assertNotNull(registerServlet.validator);
    }

    @Test
    void testDoGet() throws Exception {
        servlet = new RegisterServlet(mockDb, mockValidator);

        when(mockRequest.getSession(true)).thenReturn(mockSession);

        RequestDispatcher mockDispatcher = mock(RequestDispatcher.class);
        when(mockRequest.getRequestDispatcher("register.jsp")).
                thenReturn(mockDispatcher);

        servlet.doGet(mockRequest, mockResponse);
        verify(mockSession).setAttribute(eq("captcha"), any(Captcha.class));

        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void doPostNullSession() throws Exception {
        servlet = new RegisterServlet(mockDb, mockValidator);

        when(mockRequest.getSession(false)).thenReturn(null);

        servlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect(argThat(url ->
                url.contains("failure.html") && url.contains("Session+expired+or+invalid")
        ));
    }

    @Test
    void doPostSuccess() throws Exception {
        servlet = new RegisterServlet(mockDb, mockValidator);

        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("captcha")).thenReturn(mockCaptcha);
        when(mockRequest.getParameter("captchaAnswer")).thenReturn("correctAnswer");
        when(mockCaptcha.validate("correctAnswer")).thenReturn(true);

        when(mockRequest.getParameter("email")).thenReturn("user@gmail.com");
        when(mockRequest.getParameter("name")).thenReturn("User Name");
        when(mockRequest.getParameter("password")).thenReturn("Password");

        when(mockValidator.getRegisterValidity("user@gmail.com",
                "User Name", "Password", mockDb))
                .thenReturn(successResponse);

        servlet.doPost(mockRequest, mockResponse);

        verify(mockDb).addUser(any());
        verify(mockSession).setAttribute("userEmail", "user@gmail.com");
        verify(mockResponse).sendRedirect("logged");
    }

    @Test
    void doPostCaptchaFail() throws Exception {
        servlet = new RegisterServlet(mockDb, mockValidator);

        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("captcha")).thenReturn(mockCaptcha);
        when(mockRequest.getParameter("captchaAnswer")).thenReturn("wrongAnswer");
        when(mockCaptcha.validate("wrongAnswer")).thenReturn(false);

        servlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect(contains("failure.html?error="));
    }

    @Test
    void doPostValidationFail() throws Exception {
        servlet = new RegisterServlet(mockDb, mockValidator);

        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("captcha")).thenReturn(mockCaptcha);
        when(mockRequest.getParameter("captchaAnswer")).thenReturn("correctAnswer");
        when(mockCaptcha.validate("correctAnswer")).thenReturn(true);

        when(mockRequest.getParameter("email")).thenReturn("user@gmail.com");
        when(mockRequest.getParameter("name")).thenReturn("Name");
        when(mockRequest.getParameter("password")).thenReturn("Password");

        when(mockValidator.getRegisterValidity("user@gmail.com",
                "Name", "Password", mockDb))
                .thenReturn(failResponse);

        servlet.doPost(mockRequest, mockResponse);
        verify(mockResponse).sendRedirect(contains("failure.html?error="));
    }
}
