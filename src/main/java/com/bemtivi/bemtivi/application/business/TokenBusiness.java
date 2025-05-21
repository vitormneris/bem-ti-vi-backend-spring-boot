package com.bemtivi.bemtivi.application.business;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bemtivi.bemtivi.application.domain.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenBusiness {
    private final String secretKey = "AKSJNDSA790KJBASB89HASHF";

    public String tokenGeneration(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withExpiresAt(LocalDateTime.now().plusMinutes(100).toInstant(ZoneOffset.of("-03:00")))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String getSubject(String token) {
        token = token.replace("Bearer ", "");
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build().verify(token).getSubject();
    }

    public String getClaimId(String token) {
        token = token.replace("Bearer ", "");
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);
        String id = String.valueOf(jwt.getClaims().get("id"));
        return id.substring(1, id.length() - 1);
    }
}