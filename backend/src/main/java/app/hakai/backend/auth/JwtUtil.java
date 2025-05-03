package app.hakai.backend.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 1 dia em milissegundos
    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(UUID uuid) {
        Date currentDate = new Date();
        Date expirationDate = new Date(
            currentDate.getTime() + EXPIRATION_TIME
        );

        return Jwts
            .builder()
            .setSubject(uuid.toString()) // Quem é o dono do token
            .setIssuedAt(currentDate) // Quando ele foi criado
            .setExpiration(expirationDate) // Quando ele expira
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    };
};
