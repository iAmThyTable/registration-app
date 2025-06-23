package app.user;

import app.user.password.PasswordHasher;

public class User {
    private final String email;
    private String name;
    private String password;

    public User(String email, String name, String password) {
        this(email, name, password, false);
    }

    public User(String email, String name, String password, boolean isHashed) {
        this.email = email;
        changeName(name);
        changePassword(password, isHashed);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void changeName(String newName) {
        this.name = newName;
    }

    public void changePassword(String newPassword, boolean isHashed) {
        this.password = isHashed ? newPassword : PasswordHasher.hash(newPassword);
    }
}
