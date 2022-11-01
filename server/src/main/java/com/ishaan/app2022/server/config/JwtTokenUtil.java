package com.ishaan.app2022.server.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final String JWT_ISSUER = "ENERGY_APP_2022";

    private static final long EXPIRES = 60 * 60 * 1000;


    // THis method will generate a JWT with the email address
    public String generateJWT(UserDetails details) throws JWTCreationException {
        Algorithm jwtAlgorithm = Algorithm.HMAC512(jwtSecret);
        return JWT.create().withIssuer(JWT_ISSUER).withSubject(details.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRES)).sign(jwtAlgorithm);
    }

    // This method will validate the JWT and ensure it is not expired
    public boolean validateJWT(String jwtToken, UserDetails details) {
        try {
            Algorithm jwtAlgorithm = Algorithm.HMAC512(jwtSecret);
            final JWTVerifier jwtVerifier = JWT.require(jwtAlgorithm)
                    .withIssuer(JWT_ISSUER).build();
            DecodedJWT jwt = jwtVerifier.verify(jwtToken);
            return jwt.getSubject().equals(details.getUsername()) && jwt.getExpiresAt().after(new Date());
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    // This method will return the email from the JWT
    public String getEmailFromJWT(String jwtToken) {
        try {
            Algorithm jwtAlgorithm = Algorithm.HMAC512(jwtSecret);
            final JWTVerifier jwtVerifier = JWT.require(jwtAlgorithm)
                    .withIssuer(JWT_ISSUER).build();
           DecodedJWT jwt = jwtVerifier.verify(jwtToken);
           return jwt.getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
