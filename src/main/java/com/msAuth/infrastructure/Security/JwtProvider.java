package com.msAuth.infrastructure.Security;

import org.springframework.stereotype.Component;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtProvider {
   // @Value("${jwt.secret}")
    // private String jwtSecret;
    private final long JWT_EXPIRATION = 43200000L;
    private final String JWT_SECRET = "tu_clave_secreta_super_segura";

    public String createToken(Long idUser, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(Long.toString(idUser))
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }
}