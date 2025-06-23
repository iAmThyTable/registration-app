package app.servlet;

import app.database.DatabaseManager;
import app.validation.SimpleValidator;
import app.validation.response.ValidationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

public class LoginServletTest {

    private LoginServlet servlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;
    private SimpleValidator mockValidator;
    private DatabaseManager mockDb;

    private StringWriter responseWriter;

    @BeforeEach
    public void setUp() throws Exception {
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
        mockValidator = mock(SimpleValidator.class);
        mockDb = mock(DatabaseManager.class);
        responseWriter = new StringWriter();

        servlet = new LoginServlet() {
            {
                this.validator = mockValidator;
                this.databaseManager = mockDb;
            }
        };

        when(mockRequest.getSession(true)).thenReturn(mockSession);
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    public void testDoPostSuccess() throws Exception {
        when(mockRequest.getParameter("email")).thenReturn("user@example.com");
        when(mockRequest.getParameter("password")).thenReturn("Password");

        ValidationResponse mockResponseObj = new ValidationResponse(true, "");
        when(mockValidator.getLoginValidity(anyString(), anyString(), eq(mockDb)))
                .thenReturn(mockResponseObj);

        servlet.doPost(mockRequest, mockResponse);

        verify(mockSession).setAttribute("userEmail", "user@example.com");
        verify(mockResponse).sendRedirect("logged");
    }

    @Test
    public void testDoPost_FailedLogin_RedirectsToFailurePage() throws Exception {
        when(mockRequest.getParameter("email")).thenReturn("user@example.com");
        when(mockRequest.getParameter("password")).thenReturn("Password");

        ValidationResponse mockResponseObj = new ValidationResponse(false,
                "Invalid credentials");
        when(mockValidator.getLoginValidity(anyString(), anyString(), eq(mockDb)))
                .thenReturn(mockResponseObj);

        servlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect(
                argThat(url -> url.startsWith("failure.html?error=") &&
                        url.contains("Login+failed"))
        );
    }
}
