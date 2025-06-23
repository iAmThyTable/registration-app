package app.validation;

import app.database.DatabaseManager;
import app.validation.response.ValidationResponse;

public interface Validator {
    public boolean isValidEmail(String email);
    public boolean isValidName(String name);
    public boolean isValidPassword(String password);

    public ValidationResponse getRegisterValidity(String email,
                                                  String name, String password,
                                                  DatabaseManager databaseManager);
    public ValidationResponse getLoginValidity(String email, String password,
                                               DatabaseManager databaseManager);
    public ValidationResponse getUpdateValidity(String newName, String newPassword);
};
