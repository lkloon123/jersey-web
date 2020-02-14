package com.example.services;

import com.example.database.JdbiInstance;
import com.example.model.User;
import com.example.utils.EnvHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class JwtService {
    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(EnvHelper.get("JWT_KEY").getBytes(StandardCharsets.UTF_8));

        //expired after 1 hour
        Calendar expiryDate = Calendar.getInstance();
        expiryDate.add(Calendar.SECOND, 10);

        return Jwts.builder()
                .setId("" + user.getId())
                .setExpiration(expiryDate.getTime())
                .setNotBefore(new Date())
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();
    }

    public User parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(EnvHelper.get("JWT_KEY").getBytes(StandardCharsets.UTF_8));

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return attemptToGetUserFromClaims(claims);
        } catch (JwtException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String extractTokenIdFromClaims(Claims claims) {
        return (String) claims.get(Claims.ID);
    }

    private User attemptToGetUserFromClaims(Claims claims) {
        Optional<User> user = JdbiInstance.getInstance().withHandle(handle -> {
            handle.registerRowMapper(ConstructorMapper.factory(User.class));
            return handle.select("SELECT * FROM user WHERE id = ?")
                    .bind(0, extractTokenIdFromClaims(claims))
                    .mapTo(User.class)
                    .findFirst();
        });

        if (user.isPresent()) {
            return user.get();
        }

        return null;
    }
}
