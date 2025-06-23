package app.servlet;

import app.database.DatabaseManager;
import app.validation.SimpleValidator;
import app.validation.response.ValidationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UpdateServletTest {

    private UpdateServlet servlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;
    private DatabaseManager mockDb;
    private SimpleValidator mockValidator;

    @BeforeEach
    public void setUp() throws Exception {
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
        mockDb = mock(DatabaseManager.class);
        mockValidator = mock(SimpleValidator.class);

        servlet = new UpdateServlet(mockDb, mockValidator);
    }

    @Test
    void testDefaultConstructor() {
        UpdateServlet registerServlet = new UpdateServlet();

        assertNotNull(registerServlet);
        assertNotNull(registerServlet.databaseManager);
        assertNotNull(registerServlet.validator);
    }

    @Test
    public void testDoPostSuccess() throws Exception {
        when(mockRequest.getParameter("name")).thenReturn("New Name");
        when(mockRequest.getParameter("password")).thenReturn("NewPassword");
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("userEmail")).thenReturn("user@gmail.com");

        when(mockValidator.getUpdateValidity("New Name", "NewPassword"))
                .thenReturn(new ValidationResponse(true, ""));

        servlet.doPost(mockRequest, mockResponse);
        verify(mockDb).updateUserInfo("user@gmail.com",
                "New Name", "NewPassword");
        verify(mockResponse).sendRedirect("logged");
    }

    @Test
    public void testDoPostUserNotLogged() throws Exception {
        when(mockRequest.getSession(false)).thenReturn(null);
        when(mockRequest.getSession(true)).thenReturn(mockSession);

        servlet.doPost(mockRequest, mockResponse);

        verify(mockSession).setAttribute(eq("message"), contains("Please login"));
        verify(mockResponse).sendRedirect("login.html");
        verify(mockDb, never()).updateUserInfo(any(), any(), any());
    }

    @Test
    public void testDoPostValidationFail() throws Exception {
        when(mockRequest.getParameter("name")).thenReturn("Invalid! @@Name");
        when(mockRequest.getParameter("password")).thenReturn("123");
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute("userEmail")).thenReturn("user@gmail.com");

        when(mockValidator.getUpdateValidity("Invalid! @@Name", "123"))
                .thenReturn(new ValidationResponse(false,
                        "Incorrect name or password"));

        servlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect(contains("Incorrect+name+or+password"));
        verify(mockDb, never()).updateUserInfo(any(), any(), any());
    }
}
