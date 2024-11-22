package server.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;  

    @Value("${jwt.expirationMs}")
    private long expirationMs;  
    final private SecretKey key;
    final private JwtParser parser;
    @Autowired
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expirationMs}") long expirationMs) {
        try {
            this.key = Keys.hmacShaKeyFor(secret.getBytes());
            this.parser = Jwts.parserBuilder().setSigningKey(this.key).build();
        } catch (Exception e) {
             
            e.printStackTrace();
            throw new RuntimeException("Error creating JwtUtil", e);
        }
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs*10);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(UserDetails user, String token) {
        try {
            Claims claims = parser.parseClaimsJws(token).getBody();
            boolean unexpired = claims.getExpiration().after(Date.from(Instant.now()));

            if (!unexpired) {
               
                throw new ExpiredJwtException(null, null, "Token has expired", null);
            }

            return user.getUsername().equals(claims.getSubject());
        } catch (ExpiredJwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }



}
