package com.example.config;
import com.example.controllers.AuthController;
import com.example.exceptions.NotFoundException;
import com.example.models.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public String extractUserName(String token) {
        logger.info("Extracting username from token.");
        String username = extractClaim(token, Claims::getSubject);
        logger.info("Username extracted: {}", username);
        return username;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        logger.debug("Extracting claim using claims resolver.");
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            logger.debug("Parsing token to extract claims.");
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("Failed to extract claims from token: {}", e.getMessage());
            throw e;
        }
    }

    private String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        logger.info("Generating token for user: {}", userDetails.getUsername());
        String token = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        logger.info("Token generated successfully.");
        return token;
    }

    public String generateTokenWithClaims(
            UserDetails user, Map<String, Object> claims
    ) throws NotFoundException {
        logger.info("Generating token with additional claims for user: {}", user.getUsername());
        return generateToken(claims, user);
    }

    private Key getSigningKey() {
        logger.debug("Retrieving signing key.");
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        logger.info("Validating token for user: {}", userDetails.getUsername());
        final String userName = extractUserName(token);
        boolean isValid = (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
        if (isValid) {
            logger.info("Token is valid.");
        } else {
            logger.warn("Token is invalid or expired.");
        }
        return isValid;
    }

    private boolean isTokenExpired(String token) {
        boolean expired = extractExpiration(token).before(new Date());
        if (expired) {
            logger.warn("Token has expired.");
        } else {
            logger.debug("Token is not expired.");
        }
        return expired;
    }

    private Date extractExpiration(String token) {
        logger.debug("Extracting token expiration date.");
        return extractClaim(token, Claims::getExpiration);
    }
}

