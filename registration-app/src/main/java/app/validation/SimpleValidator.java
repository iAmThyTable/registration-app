package app.validation;

import app.database.DatabaseManager;
import app.validation.response.ValidationResponse;

public class SimpleValidator implements Validator {
    private static final int MIN_PASS_LENGTH = 7;

    private static final String INVALID_EMAIL_MESSAGE =
            "Invalid email! Must contain '@' and '.'!\n";
    private static final String INVALID_NAME_MESSAGE =
            "Invalid name! Must contain latin letters only!\n";
    private static final String INVALID_PASSWORD_MESSAGE =
            "Invalid password! Must contain at least " + MIN_PASS_LENGTH +
                    " symbols, at least one lowercase and one uppercase among them!\n";
    private static final String USER_ALREADY_EXISTS_MESSAGE =
            "User is already registered with this email, please use another one or login instead\n";

    @Override
    public boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    @Override
    public boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\-\\s]+");
    }

    @Override
    public boolean isValidPassword(String password) {
        return password.length() >= MIN_PASS_LENGTH
                && password.matches(".*[a-z].*")
                && password.matches(".*[A-Z].*");
    }

    @Override
    public ValidationResponse getRegisterValidity(String email, String name,
                                                  String password,
                                                  DatabaseManager databaseManager) {
        boolean isValid = true;
        String message = "";

        if (!isValidEmail(email)) {
            isValid = false;
            message += INVALID_EMAIL_MESSAGE;
        }

        if (!isValidName(name)) {
            isValid = false;
            message += INVALID_NAME_MESSAGE;
        }

        if (!isValidPassword(password)) {
            isValid = false;
            message += INVALID_PASSWORD_MESSAGE;
        }

        if (databaseManager.userExists(email)) {
            isValid = false;
            message += USER_ALREADY_EXISTS_MESSAGE;
        }

        return new ValidationResponse(isValid, message);
    }

    @Override
    public ValidationResponse getLoginValidity(String email, String password,
                                               DatabaseManager databaseManager) {
        boolean isValid = databaseManager.checkLogin(email, password);
        String message = isValid ?
                "Successful login!" :
                "Incorrect email or password!";

        return new ValidationResponse(isValid, message);
    }

    @Override
    public ValidationResponse getUpdateValidity(String newName,
                                                String newPassword) {
        boolean isValid = true;
        String message = "";

        if (newName != null && !newName.isBlank() && !isValidName(newName)) {
            isValid = false;
            message += INVALID_NAME_MESSAGE;
        }

        if (newPassword != null && !newPassword.isBlank() && !isValidPassword(newPassword)) {
            isValid = false;
            message += INVALID_PASSWORD_MESSAGE;
        }

        return new ValidationResponse(isValid, message);
    }
}
