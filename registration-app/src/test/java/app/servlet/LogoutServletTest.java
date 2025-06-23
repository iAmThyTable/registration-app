package app.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.*;

import static org.mockito.Mockito.*;

public class LogoutServletTest {

    private LogoutServlet servlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = new LogoutServlet();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
    }

    @Test
    public void testDoGetLogsOut() throws Exception {
        when(mockRequest.getSession(false)).thenReturn(mockSession);

        servlet.doGet(mockRequest, mockResponse);

        verify(mockSession).invalidate();
        verify(mockResponse).sendRedirect("index.html");
    }
}
