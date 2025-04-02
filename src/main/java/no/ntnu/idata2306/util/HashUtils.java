package no.ntnu.idata2306.util;

import no.ntnu.idata2306.exception.CardTokenHashException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * HashUtils is a utility class that provides hashing methods.
 */
public class HashUtils {

    /**
     * Generates a hashed value for the given card number using SHA-512 and a random salt.
     * The hash ensures uniqueness and security for the card number.
     *
     * @param cardNumber the card number to be hashed into a cardToken.
     * @return the hashed value of the card token.
     * @throws CardTokenHashException if there is an error generating the hash.
     */
    public static String generateCardTokenHash(String cardNumber) throws CardTokenHashException {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            String tokenWithSalt = cardNumber + Base64.getEncoder().encodeToString(salt);
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(tokenWithSalt.getBytes());

            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new CardTokenHashException("Error generating card token hash", e);
        }
    }

    private HashUtils(){

    }
}
