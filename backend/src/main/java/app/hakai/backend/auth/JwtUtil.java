package app.hakai.backend.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final long EXPIRATION_TIME = 86400000; // 1 dia em milissegundos

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // Quem é o dono do token
                .setIssuedAt(new java.util.Date()) // Data que o token foi criado
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Quando ele expira
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }
}
