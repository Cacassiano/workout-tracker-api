package dev.cacassiano.workout_tracker.use_cases.services.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

    @Value("${secret.key}")
    private String jwtSecret;

    // 24 hours
    @Value("${jwt.expiration-ms}")
    private Long expirationMs;

    public String generateToken(String email, String id) {
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret.getBytes());
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expirationMs);

        return JWT.create()
            .withSubject(email)
            .withClaim("user_id", id)
            .withIssuedAt(now)
            .withExpiresAt(expiresAt)
            .sign(algorithm);
    }

    private DecodedJWT decodeJwt(String token) {
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret.getBytes());
        return JWT.require(algorithm).build()
            .verify(token);
    }

    public String getEmailFromToken(String token){
        DecodedJWT decodedJWT = decodeJwt(token);
        return decodedJWT.getSubject();
    }
    public String getIdFromToken(String token){
        DecodedJWT decodedJWT = decodeJwt(token);
        return decodedJWT.getClaim("user_id").asString();
    }
}
