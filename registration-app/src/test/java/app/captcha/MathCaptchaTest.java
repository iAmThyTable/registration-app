package app.captcha;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MathCaptchaTest {

    @Test
    public void testValidate() {
        MathCaptcha captcha = new MathCaptcha();

        String[] parts = captcha.getQuestion().split(" ");
        int a = Integer.parseInt(parts[0]);
        int b = Integer.parseInt(parts[2]);
        int expectedAnswer = a + b;

        assertTrue(captcha.validate(String.valueOf(expectedAnswer)),
                "Correct answer should pass validation");
    }

    @Test
    public void testValidateFail() {
        MathCaptcha captcha = new MathCaptcha();

        assertFalse(captcha.validate("-999"),
                "Incorrect answer should fail validation");
    }

    @Test
    public void testValidateNonNumericFail() {
        MathCaptcha captcha = new MathCaptcha();

        assertFalse(captcha.validate("not a number"));
    }

    @Test
    public void testValidateWhitespaceFail() {
        MathCaptcha captcha = new MathCaptcha();

        String[] parts = captcha.getQuestion().split(" ");
        int a = Integer.parseInt(parts[0]);
        int b = Integer.parseInt(parts[2]);
        int expectedAnswer = a + b;

        assertTrue(captcha.validate("  " + expectedAnswer + "  "),
                "Answer with whitespace should still validate");
    }
}
