package app.database;

import app.user.User;
import app.user.password.PasswordHasher;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private ArrayList<User> users;
    private Connection connection;

    public DatabaseManager() {
        users = new ArrayList<>();

        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                //JDBC Driver not found
            }

            connection = DriverManager.getConnection(DatabaseDetails.DB_URL,
                    DatabaseDetails.DB_USER,
                    DatabaseDetails.DB_PASSWORD);
        } catch (SQLException e) {
            //Failed to connect to DB
            return;
        }

        fetchUsersFromDatabase();
    }

    public DatabaseManager(Connection connection) {
        this.connection = connection;
        this.users = new ArrayList<>();
        fetchUsersFromDatabase();
    }

    private void fetchUsersFromDatabase() {
        String sql = DatabaseQueries.SELECT_ALL_USERS;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String fullName = firstName + " " + lastName;
                String passwordHash = resultSet.getString("password_hash");

                User user = new User(email, fullName, passwordHash, true);
                users.add(user);
            }
        } catch (SQLException e) {
            //Failed to load users
        }
    }

    public void insertUserToDatabase(User user) {
        reloadUsers();

        String sql = DatabaseQueries.INSERT_USER;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, extractFirstName(user.getName()));
            statement.setString(3, extractLastName(user.getName()));
            statement.setString(4, user.getPassword());

            statement.executeUpdate();
        } catch (SQLException e) {
            //Failed to insert user
        }
    }

    public void updateUserInfo(String email, String newName, String newPassword) {
        reloadUsers();

        if (newName != null && !newName.isBlank()) {
            String sql = DatabaseQueries.UPDATE_USER_NAME;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, extractFirstName(newName));
                statement.setString(2, extractLastName(newName));
                statement.setString(3, email);

                statement.executeUpdate();
            } catch (SQLException e) {
                //Failed to update name;
            }
        }

        if (newPassword != null && !newPassword.isBlank()) {
            String sql = DatabaseQueries.UPDATE_USER_PASSWORD;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, PasswordHasher.hash(newPassword));
                statement.setString(2, email);

                statement.executeUpdate();
            } catch (SQLException e) {
                //Failed to update password;
            }
        }
    }

    String extractFirstName(String str) {
        String[] names = str.trim().split("\\s+");
        return names.length > 0 ? names[0] : "";
    }

    String extractLastName(String str) {
        String[] names = str.trim().split("\\s+");
        return names.length > 1 ? names[names.length - 1] : "";
    }

    public void addUser(User user) {
        if (!userExists(user)) {
            users.add(user);
            insertUserToDatabase(user);
        }
    }

    public boolean checkLogin(String email, String password) {
        reloadUsers();
        User user = getUserByEmail(email);

        if (user == null) {
            return false;
        }

        return user.getPassword().equals(PasswordHasher.hash(password));
    }

    public User getUserByEmail(String email) {
        reloadUsers();
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }

        return null;
    }

    public boolean userExists(User user) {
        return userExists(user.getEmail());
    }

    public boolean userExists(String email) {
        return getUserByEmail(email) != null;
    }

    private void reloadUsers() {
        users.clear();
        fetchUsersFromDatabase();
    }
}
