package dev.cacassiano.workout_tracker.services.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtService {

    @Value("${secret.key}")
    private String jwtSecret;

    // 24 hours
    @Value("${jwt.expiration-ms}")
    private Long expirationMs;

    public String generateToken(String email) {
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret.getBytes());
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expirationMs);

        return JWT.create()
            .withSubject(email)
            .withIssuedAt(now)
            .withExpiresAt(expiresAt)
            .sign(algorithm);
    }

    public String getEmailFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret.getBytes());
            return JWT.require(algorithm).build()
                .verify(token)
                .getSubject();
        } catch (Exception ex) {
            return null;
        }
    }
}
