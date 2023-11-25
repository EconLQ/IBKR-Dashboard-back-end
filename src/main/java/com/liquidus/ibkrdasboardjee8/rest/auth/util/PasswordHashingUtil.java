package com.liquidus.ibkrdasboardjee8.rest.auth.util;

import com.liquidus.ibkrdasboardjee8.rest.auth.enitity.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Class designed to properly encode and decode user's password and check whether the password from
 * request ({@link com.liquidus.ibkrdasboardjee8.rest.auth.resources.LoginResource#login(User)})
 * is the same to the one stored in the database
 */
public class PasswordHashingUtil {

    private static String DB_URL;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static Logger logger = Logger.getLogger(PasswordHashingUtil.class.getName());

    public PasswordHashingUtil() {
    }

    /**
     * Encode the password using <a href="https://ru.wikipedia.org/wiki/PBKDF2">PBKDF2 standard</a>
     *
     * @param password password value from {@link com.liquidus.ibkrdasboardjee8.rest.auth.resources.LoginResource#login(User)}
     * @return encoded password along with salt and iterations number
     */
    public static String[] hashPassword(String password) {
        byte[] salt = generateSalt();
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            String hashedPassword = Base64.getEncoder().encodeToString(hash);

            return new String[]{hashedPassword, Base64.getEncoder().encodeToString(salt), "10000"};
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.warning("Error encoding key: " + e.getMessage());
        }
        return new String[]{};
    }

    /**
     * Connects to the database and gets user's password
     *
     * @param username get from request
     * @return string array with the password, salt and iterations number
     */
    public static String[] getUserPassword(String username) {
        setDbProperties();  // set DB properties to avoid class instantiation
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "select password, salt, iterations from Users where username=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            String[] pwd = new String[3];
            while (rs.next()) {
                // remove the padding characters from the Base64-encoded password
                String password = rs.getString("password").replace("=", "");
                pwd[0] = new String(Base64.getDecoder().decode(password));

                pwd[1] = rs.getString("salt");
                pwd[2] = String.valueOf(rs.getInt("iterations"));
            }

            return pwd;
        } catch (SQLException e) {
            logger.warning("Failed to establish connection to database: " + e.getMessage());
        }
        return null;
    }

    /**
     * Generate and return a random salt 16-byte (128 bits) array
     *
     * @return a sequence of bits, known as a cryptographic salt
     */
    private static byte[] generateSalt() {
        byte[] salt = new byte[16]; // 128 bits
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     * Checks whether the password from the request is equal to the one stored in the database
     *
     * @param encodedPassword password from the database
     * @param requestPassword password from the login request
     * @param salt            sequence of bits (stored in the database)
     * @param iterations      number of iterations
     * @return true if request password and password in the database are equal
     */
    public static boolean isMatched(String encodedPassword, String requestPassword, byte[] salt, int iterations) {
        KeySpec spec = new PBEKeySpec(requestPassword.toCharArray(), salt, iterations, 256);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            String hashedRequestPassword = Base64.getEncoder().encodeToString(hash);
            return hashedRequestPassword.equals(encodedPassword);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.warning("Exception encoding key: " + e.getMessage());
        }
        return false;
    }

    /**
     * Read and set database url, username and password values from the properties file
     */
    private static void setDbProperties() {
        Properties properties = new Properties();
        try {
            properties.load(PasswordHashingUtil.class.getClassLoader().getResourceAsStream("DBCredentials.properties"));

            // assign values from the file
            DB_URL = properties.getProperty("db.url");
            DB_USERNAME = properties.getProperty("db.username");
            DB_PASSWORD = properties.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
