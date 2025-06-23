package app.validation;

import app.database.DatabaseManager;
import app.validation.response.ValidationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SimpleValidatorTest {

    private SimpleValidator validator;
    private DatabaseManager mockDb;

    @BeforeEach
    public void setUp() {
        validator = new SimpleValidator();
        mockDb = mock(DatabaseManager.class);
    }

    @Test
    public void testValidEmail() {
        assertTrue(validator.isValidEmail("a@b.com"));
    }

    @Test
    public void testInvalidEmail() {
        assertFalse(validator.isValidEmail("ab.com"));
        assertFalse(validator.isValidEmail("a@bcom"));
    }

    @Test
    public void testValidName() {
        assertTrue(validator.isValidName("Han Solo"));
        assertTrue(validator.isValidName("Darth-Vader"));
    }

    @Test
    public void testInvalidName() {
        assertFalse(validator.isValidName("Da4th"));
        assertFalse(validator.isValidName("123"));
        assertFalse(validator.isValidName("!@#"));
    }

    @Test
    public void testValidPassword() {
        assertTrue(validator.isValidPassword("Password"));
        assertTrue(validator.isValidPassword("St4@ngPass"));
    }

    @Test
    public void testInvalidPassword() {
        assertFalse(validator.isValidPassword("Short"));
        assertFalse(validator.isValidPassword("nocapital"));
        assertFalse(validator.isValidPassword("NOLOWERCASE"));
    }

    @Test
    public void testGetRegisterValidity() {
        when(mockDb.userExists("a@b.com")).thenReturn(false);

        ValidationResponse res = validator.getRegisterValidity(
                "a@b.com", "Alice Smith", "GoodPass1", mockDb
        );

        assertTrue(res.isValid());
        assertEquals("", res.errorMessage());
    }

    @Test
    public void testGetRegisterValidityFail() {
        when(mockDb.userExists("bademail")).thenReturn(true);

        ValidationResponse res = validator.getRegisterValidity(
                "bademail", "123", "weak", mockDb
        );

        assertFalse(res.isValid());
        assertTrue(res.errorMessage().contains("Invalid email"));
        assertTrue(res.errorMessage().contains("Invalid name"));
        assertTrue(res.errorMessage().contains("Invalid password"));
        assertTrue(res.errorMessage().contains("User is already registered"));
    }

    @Test
    public void testGetLoginValiditySuccess() {
        when(mockDb.checkLogin("a@b.com", "Password")).thenReturn(true);

        ValidationResponse res = validator.getLoginValidity("a@b.com",
                "Password", mockDb);
        assertTrue(res.isValid());
        assertEquals("Successful login!", res.errorMessage());
    }

    @Test
    public void testGetLoginValidityFail() {
        when(mockDb.checkLogin("a@b.com", "Password")).thenReturn(false);

        ValidationResponse res = validator.getLoginValidity("a@b.com",
                "Password", mockDb);
        assertFalse(res.isValid());
        assertEquals("Incorrect email or password!", res.errorMessage());
    }

    @Test
    public void testGetUpdateValiditySuccess() {
        ValidationResponse res = validator.getUpdateValidity("Alice",
                "St4@ngPass");
        assertTrue(res.isValid());
    }

    @Test
    public void testGetUpdateValidityInvalidNameAndPassword() {
        ValidationResponse res = validator.getUpdateValidity("!!!",
                "123");

        assertFalse(res.isValid());
        assertTrue(res.errorMessage().contains("Invalid name"));
        assertTrue(res.errorMessage().contains("Invalid password"));
    }

    @Test
    public void testGetUpdateValidityEmptyFields() {
        ValidationResponse res = validator.getUpdateValidity("", "");
        assertTrue(res.isValid());
    }
}
