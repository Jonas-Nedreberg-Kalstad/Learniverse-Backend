package no.ntnu.idata2306.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for handling JWT tokens.
 */
@Component
public class JwtUtil {

    @Value("${JWTSECRET}")
    private String SECRET_KEY;
    private static final String JWT_AUTH_KEY = "roles";

    /**
     * Validates a JWT token for a given user.
     *
     * @param token       the JWT token to validate
     * @param userDetails the user details to validate against
     * @return true if the token is valid for the user and not expired, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userEmail = extractUser(token);
        return userDetails != null && userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Generates a JWT token for a given user.
     * <b>Token is valid for 12 hours after it is generated.</b>
     *
     * @param userDetails the user details for which the token is generated
     * @return the generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        final long TIME_NOW = System.currentTimeMillis();
        final long MILLISECONDS_IN_HOUR = 60 * 60 * 1000;
        final long HOURS_WHICH_TOKEN_LASTS = 12;
        final long TIME_WHEN_USER_GETS_KICKED = TIME_NOW + (MILLISECONDS_IN_HOUR * HOURS_WHICH_TOKEN_LASTS);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim(JWT_AUTH_KEY, userDetails.getAuthorities())
                .issuedAt(new Date(TIME_NOW))
                .expiration(new Date(TIME_WHEN_USER_GETS_KICKED))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Retrieves the signing key used for JWT token generation and validation.
     *
     * @return the signing key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String extractUser(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token the JWT token
     * @return the expiration date extracted from the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from a JWT token.
     *
     * @param token the JWT token
     * @param claimsResolver the function to apply to the claims to retrieve the desired claim
     * @param <T> the type of the claim to extract
     * @return the extracted claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param token the JWT token
     * @return the claims extracted from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    /**
     * Checks if a JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * Sets the secret key for JWT token operations. This is primarily used for testing purposes.
     *
     * @param secretKey the secret key to set
     */
    public void setSecretKey(String secretKey) {
        this.SECRET_KEY = secretKey;
    }
}
