package app.servlet;

import app.database.DatabaseManager;
import app.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

public class LoggedServletTest {

    private LoggedServlet servlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;
    private DatabaseManager mockDb;

    private StringWriter responseWriter;

    @BeforeEach
    public void setUp() throws Exception {

        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
        mockDb = mock(DatabaseManager.class);
        responseWriter = new StringWriter();

        servlet = new LoggedServlet() {
            {
                this.databaseManager = mockDb;
            }
        };

        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    public void testDoGet() throws Exception {

        when(mockSession.getAttribute("userEmail")).thenReturn("test@example.com");
        when(mockDb.getUserByEmail("test@example.com"))
                .thenReturn(new User("test@example.com",
                        "Test User", "hashed", true));

        servlet.doGet(mockRequest, mockResponse);

        String output = responseWriter.toString();

        assert output.contains("Welcome, Test User!");
        assert output.contains("Update Profile");
        assert output.contains("Logout");
    }

    @Test
    public void testDoGetUserNotLogged() throws Exception {

        when(mockRequest.getSession(false)).thenReturn(null);
        servlet.doGet(mockRequest, mockResponse);
        verify(mockResponse).sendRedirect("login.html");
    }
}
