package app.user.password;

import java.security.MessageDigest;

public final class PasswordHasher {

    public static String hash(String password) {
        return hash(password, "SHA-256");
    }

    public static String hash(String password, String hashAlgorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(hashAlgorithm);
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hex = new StringBuilder();

            for (byte b: hashBytes) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
