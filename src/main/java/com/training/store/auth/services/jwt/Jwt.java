package com.training.store.auth.services.jwt;

import com.training.store.common.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;


@AllArgsConstructor
public class Jwt {

    private final Claims claims;
    private final SecretKey secretKey;

    public boolean isExpired() {
        try {
            return this.claims.getExpiration().before(new Date());
        } catch (JwtException exception) {
            return false;
        }
    }

    public Long getUserId() {
        return Long.valueOf(this.claims.getSubject());
    }

    public Role getRole() {
        return Role.valueOf(this.claims.get("role", String.class));
    }

    public String toString() {
        return Jwts.builder().claims(this.claims).signWith(secretKey).compact();
    }
}
