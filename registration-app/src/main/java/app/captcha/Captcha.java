package app.captcha;

public interface Captcha {
    String getQuestion();
    boolean validate(String answer);
}
