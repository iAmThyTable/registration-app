package app.database;

import app.user.User;
import app.user.password.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DatabaseManagerTest {

    @Mock
    Connection mockConnection;
    @Mock
    PreparedStatement mockStatement;
    @Mock
    ResultSet mockResultSet;

    DatabaseManager db;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(any())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);

        db = new DatabaseManager(mockConnection);
    }

    @Test
    void fetchUsersFromDatabase_sqlException() throws SQLException {
        when(mockConnection.prepareStatement(any())).thenThrow(SQLException.class);
        db = new DatabaseManager(mockConnection);
    }

    @Test
    void insertUserToDatabaseSuccess() throws SQLException {
        User user = new User("user@gmail.com", "Name",
                "Password",false);
        db.insertUserToDatabase(user);
        verify(mockStatement).executeUpdate();
    }

    @Test
    void insertUserToDatabaseFail() throws SQLException {
        when(mockConnection.prepareStatement(any())).thenThrow(SQLException.class);
        db.insertUserToDatabase(new User("user@gmail.com", "Name",
                "Password", false));
    }

    @Test
    void updateUserInfo() throws SQLException {
        db.updateUserInfo("user@gmail.com", "New Name","");
        verify(mockStatement, atLeastOnce()).executeUpdate();
    }

    @Test
    void updateUserName() throws SQLException {
        db.updateUserInfo("user@gmail.com", "New Name","");
        verify(mockStatement, atLeastOnce()).executeUpdate();
    }

    @Test
    void updateUserPassword() throws SQLException {
        db.updateUserInfo("user@gmail.com", "", "NewPassword");
        verify(mockStatement, atLeastOnce()).executeUpdate();
    }

    @Test
    void updateUserInfoFail() throws SQLException {
        when(mockConnection.prepareStatement(any())).thenThrow(SQLException.class);
        db.updateUserInfo("user@gmail.com", "Name", "pass");
    }

    @Test
    void addExistingUser() {
        User user = new User("user@gmail.com", "Name",
                "Password", false);
        db.addUser(user);
        assertDoesNotThrow(() -> db.addUser(user));
    }

    @Test
    void checkLoginFailWrongPassword() {
        User user = new User("user@gmail.com", "Name",
                PasswordHasher.hash("Password"), false);
        db.addUser(user);
        assertFalse(db.checkLogin("user@gmail.com", "FalsePassword"));
    }

    @Test
    void checkLoginNoUser() {
        assertFalse(db.checkLogin("nonexistent@gmail.com", "Password"));
    }

    @Test
    void getUserByEmailDoesntExist() {
        assertNull(db.getUserByEmail("nonexistent@gmail.com"));
    }

    @Test
    void extractNamesVariousFormats() {
        assertEquals("Darth", db.extractFirstName("Darth      Vader"));
        assertEquals("Vader", db.extractLastName("Darth    Vader"));

        assertEquals("Solo", db.extractFirstName("Solo   "));
        assertEquals("", db.extractLastName("   Solo"));

        assertEquals("", db.extractFirstName("   "));
        assertEquals("", db.extractLastName("       "));
    }
}
