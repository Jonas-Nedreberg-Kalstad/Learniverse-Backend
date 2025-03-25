package no.ntnu.idata2306.util;

import no.ntnu.idata2306.exception.CardTokenHashException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * HashUtils is a utility class that provides methods for generating hashed values for card tokens.
 * This ensures that card tokens are unique even if the same card is added by different users.
 */
public class HashUtils {

    /**
     * Generates a hashed value for the given card token using SHA-512 and a random salt.
     * The hash ensures uniqueness and security for the card token.
     *
     * @param cardToken the card token to be hashed.
     * @return the hashed value of the card token.
     * @throws CardTokenHashException if there is an error generating the hash.
     */
    public static String generateCardTokenHash(String cardToken) throws CardTokenHashException {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            String tokenWithSalt = cardToken + Base64.getEncoder().encodeToString(salt);
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
