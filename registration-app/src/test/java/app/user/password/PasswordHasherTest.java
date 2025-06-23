package app.user.password;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordHasherTest {

    @Test
    public void testHashConsistency() {
        String password = "Password";
        String hash1 = PasswordHasher.hash(password);
        String hash2 = PasswordHasher.hash(password);

        assertNotNull(hash1);
        assertEquals(hash1, hash2);
    }

    @Test
    public void testHashDifferentPasswords() {
        String hash1 = PasswordHasher.hash("Password1");
        String hash2 = PasswordHasher.hash("Password2");

        assertNotEquals(hash1, hash2);
    }

    @Test
    public void testHashPassword() {
        String expected = "e7cf3ef4f17c3999a94f2c6f612e8a888e5b1026878e4e19398b23bd38ec221a";
        assertEquals(expected, PasswordHasher.hash("Password"));
    }

    @Test
    void hashThrowsInvalidAlgorithm() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            PasswordHasher.hash("Password", "invalid");
        });
        assert(ex.getCause() instanceof java.security.NoSuchAlgorithmException);
    }
}
