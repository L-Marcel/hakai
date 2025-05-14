package app.hakai.backend.auth;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(UUID uuid) {
        Date currentDate = new Date();
        Date expirationDate = new Date(
            currentDate.getTime() + EXPIRATION_TIME
        );

        return Jwts
            .builder()
            .setSubject(uuid.toString())
            .setIssuedAt(currentDate)
            .setExpiration(expirationDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    public UUID validateTokenAndGetUserId(String token) {
        Claims claims = Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        String userId = claims.getSubject();
        return UUID.fromString(userId);
    }
};
