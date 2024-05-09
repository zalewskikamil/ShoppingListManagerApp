package pl.kamilzalewski.shoppinglistmanager.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.kamilzalewski.shoppinglistmanager.jwt.exceptions.TokenValidityException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        try {
            return claimsResolver.apply(Jwts.parser().verifyWith(getSignInKey()).build()
                    .parseSignedClaims(token).getPayload());
        } catch (ExpiredJwtException e) {
            throw new TokenValidityException("Token expired. Please refresh token or sing in once again");
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(UserDetails userDetails, long expirationTokenPeriodInMillis) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTokenPeriodInMillis))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            if (isTokenExpired(token)) {
                return false;
            }
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername());
        } catch (MalformedJwtException | IllegalArgumentException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractClaims(token, Claims::getExpiration).before(new Date());
        } catch (TokenValidityException e) {
            return true;
        }
    }
}
