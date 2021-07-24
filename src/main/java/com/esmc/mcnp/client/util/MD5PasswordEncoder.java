package com.esmc.mcnp.client.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MD5PasswordEncoder {

    private final static String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"};

    private final static String MD5 = "MD5";

    private final Object salt;
    private final String algorithm;

    public MD5PasswordEncoder() {
        this(null);
    }

    public MD5PasswordEncoder(Object salt) {
        this(salt, MD5);
    }

    public MD5PasswordEncoder(Object salt, String algorithm) {
        this.salt = salt;
        this.algorithm = algorithm;
    }

    public String encode(CharSequence charSequence) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            // Chaîne cryptée
            result = byteArrayToHexString(md.digest(mergePasswordAndSalt(charSequence.toString()).getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {

        }
        return result;
    }

    public boolean matches(CharSequence charSequence, String s) {
        String pass1 = encode(charSequence.toString());
        String pass2 = s;
        return pass1.equals(pass2);
    }

    private String mergePasswordAndSalt(String password) {
        if (password == null) {
            password = "";
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }

    /**
     * Convertir le tableau d'octets en chaîne hexadécimale
     *
     * @param b Tableau d'octets
     * @return Chaîne hexadécimale
     */
    private String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    /**
     * Convertir les octets en hexadécimal
     *
     * @param b octet
     * @return Hexadécimal
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    public final static void main(String[] args) {
        MD5PasswordEncoder encoder = new MD5PasswordEncoder();
        System.out.println(encoder.encode("mawuli"));
    }

}
