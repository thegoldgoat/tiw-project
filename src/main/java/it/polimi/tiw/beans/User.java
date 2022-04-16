package it.polimi.tiw.beans;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.Getter;

public class User {
    @Getter
    final private int userPk;

    @Getter
    final private String username;

    @Getter
    final private String password;

    public User(int userPk, String username, String password) {
        this.userPk = userPk;
        this.username = username;
        this.password = password;
    }

    public static String hashPassword(String plaintextPassword) {
        return getSHAString(plaintextPassword);
    }

    private static byte[] getSHABytes(String input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Should never get here, SHA algorithm is staticly defined.");
        }

        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    private static String getSHAString(String input) {
        byte[] hash = getSHABytes(input);

        BigInteger number = new BigInteger(1, hash);

        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

}
