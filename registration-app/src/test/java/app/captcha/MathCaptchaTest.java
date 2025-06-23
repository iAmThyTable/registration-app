package app.captcha;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MathCaptchaTest {

    @Test
    public void testGetQuestion_NotNullAndFormatted() {
        MathCaptcha captcha = new MathCaptcha();
        String question = captcha.getQuestion();

        assertNotNull(question, "Question should not be null");
        assertTrue(question.matches("\\d+ \\+ \\d+ = \\?"), "Question should be in the format 'a + b = ?'");
    }

    @Test
    public void testValidate_CorrectAnswer() {
        MathCaptcha captcha = new MathCaptcha();

        // Extract numbers from the question
        String[] parts = captcha.getQuestion().split(" ");
        int a = Integer.parseInt(parts[0]);
        int b = Integer.parseInt(parts[2]);
        int expectedAnswer = a + b;

        assertTrue(captcha.validate(String.valueOf(expectedAnswer)), "Correct answer should pass validation");
    }

    @Test
    public void testValidate_IncorrectAnswer() {
        MathCaptcha captcha = new MathCaptcha();

        // Just choose an intentionally incorrect answer
        assertFalse(captcha.validate("-999"), "Incorrect answer should fail validation");
    }

    @Test
    public void testValidate_NonNumericAnswer() {
        MathCaptcha captcha = new MathCaptcha();

        assertFalse(captcha.validate("not a number"), "Non-numeric input should fail validation");
    }

    @Test
    public void testValidate_AnswerWithWhitespace() {
        MathCaptcha captcha = new MathCaptcha();

        String[] parts = captcha.getQuestion().split(" ");
        int a = Integer.parseInt(parts[0]);
        int b = Integer.parseInt(parts[2]);
        int expectedAnswer = a + b;

        assertTrue(captcha.validate("  " + expectedAnswer + "  "), "Answer with whitespace should still validate");
    }
}
