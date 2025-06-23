package app.database;

public final class DatabaseDetails {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/registration_app";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "password123";

    public static final String TABLE_USERS = "users";

    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PASSWORD_HASH = "password_hash";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
}
