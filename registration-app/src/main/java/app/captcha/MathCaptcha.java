package app.captcha;

import java.io.IOException;
import java.util.Random;

public class MathCaptcha implements Captcha {
    private final String question;
    private final int answer;

    public MathCaptcha() {
        Random rand = new Random();
        int a = rand.nextInt(10) + 1;
        int b = rand.nextInt(10) + 1;
        this.answer = a + b;
        this.question = a + " + " + b + " = ?";
    }

    public String getQuestion() {
        return question;
    }

    public boolean validate(String userAnswer) {
        try {
            int ans = Integer.parseInt(userAnswer.trim());
            return ans == answer;
        } catch (Exception e) {
            return false;
        }
    }
}
