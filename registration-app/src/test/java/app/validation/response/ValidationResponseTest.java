package app.validation.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ValidationResponseTest {

    @Test
    void testValidationResponseConstructor() throws Exception {
        ValidationResponse validationResponse = new ValidationResponse();

        assertFalse(validationResponse.isValid());
        assertEquals("", validationResponse.errorMessage());
    }
}
