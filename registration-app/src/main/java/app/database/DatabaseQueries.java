package app.database;

public final class DatabaseQueries {

    public static final String SELECT_ALL_USERS = "SELECT " +
            DatabaseDetails.EMAIL + ", " +
            DatabaseDetails.FIRST_NAME + ", " +
            DatabaseDetails.LAST_NAME + ", " +
            DatabaseDetails.PASSWORD_HASH +
            " FROM " + DatabaseDetails.TABLE_USERS;


    public static final String INSERT_USER = "INSERT INTO " + DatabaseDetails.TABLE_USERS + " (" +
            DatabaseDetails.EMAIL + ", " +
            DatabaseDetails.FIRST_NAME + ", " +
            DatabaseDetails.LAST_NAME + ", " +
            DatabaseDetails.PASSWORD_HASH + ") VALUES (?, ?, ?, ?)";


    public static final String UPDATE_USER_NAME = "UPDATE " + DatabaseDetails.TABLE_USERS + " SET " +
            DatabaseDetails.FIRST_NAME + " = ?, " +
            DatabaseDetails.LAST_NAME + " = ? WHERE " +
            DatabaseDetails.EMAIL + " = ?";

    public static final String UPDATE_USER_PASSWORD = "UPDATE " +
            DatabaseDetails.TABLE_USERS + " SET " +
            DatabaseDetails.PASSWORD_HASH + " = ? WHERE " +
            DatabaseDetails.EMAIL + " = ?";
}
