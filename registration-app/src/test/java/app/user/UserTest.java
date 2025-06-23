package app.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    User user;

    @BeforeEach
    void setUp() throws Exception {
        user = new User("user@gmail.com", "Name", "Password");
    }

    @Test
    public void testConstructorWithHashing() {
        assertEquals("user@gmail.com", user.getEmail());
        assertEquals("Name", user.getName());
        assertNotEquals("Password", user.getPassword());
        assertEquals(user.getPassword(),
                app.user.password.PasswordHasher.hash("Password"));
    }

    @Test
    public void testConstructorWithHashedPassword() {
        String hashedPass = app.user.password.PasswordHasher.hash("Password");
        User user2 = new User("user@gmail.com", "Name", hashedPass, true);

        assertEquals("user@gmail.com", user.getEmail());
        assertEquals("Name", user.getName());
        assertEquals(hashedPass, user.getPassword());
    }

    @Test
    public void testChangeName() {
        user.changeName("Name2");
        assertEquals("Name2", user.getName());
    }

    @Test
    public void testChangePasswordWithHashing() {
        String newPassword = "Password2";
        user.changePassword(newPassword, false);
        assertEquals(app.user.password.PasswordHasher.hash(newPassword), user.getPassword());
    }

    @Test
    public void testChangePasswordAlreadyHashed() {
        String hashed = app.user.password.PasswordHasher.hash("Password2");

        user.changePassword(hashed, true);
        assertEquals(hashed, user.getPassword());
    }
}
