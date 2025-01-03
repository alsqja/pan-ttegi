package com.example.panttegi.util;

import com.example.panttegi.user.dto.TokenDto;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("${jwt.expiry-millis}")
    private long expiryMillis;

    @Getter
    @Value("${jwt.refresh-expiry-millis}")
    private long refreshExpiryMillis;

    private final UserRepository userRepository;

    public TokenDto generateToken(Authentication authentication) throws EntityNotFoundException {

        String email = authentication.getName();

        String accessToken = this.generateTokenBy(email);
        String refreshToken = this.generateRefreshToken(email);

        return new TokenDto(accessToken, refreshToken);
    }

    public TokenDto generateToken(String email) throws EntityNotFoundException {

        String accessToken = this.generateTokenBy(email);
        String refreshToken = this.generateRefreshToken(email);

        return new TokenDto(accessToken, refreshToken);
    }

    public String getUsername(String token) {
        Claims claims = this.getClaims(token);
        return claims.getSubject();
    }

    public boolean validToken(String token) throws JwtException {
        try {
            return !this.tokenExpired(token);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        }

        return false;
    }

    private String generateTokenBy(String email) throws EntityNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 email에 맞는 값이 존재하지 않습니다."));
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + this.expiryMillis);

        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claim("role", user.getRole())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    private Claims getClaims(String token) {
        if (!StringUtils.hasText(token)) {
            throw new MalformedJwtException("토큰이 비어 있습니다.");
        }

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean tokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return this.resolveClaims(token, Claims::getExpiration);
    }

    private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getClaims(token);
        return claimsResolver.apply(claims);
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(String email) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + this.refreshExpiryMillis);

        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();
    }

    // 리프레시 토큰 검증
    public boolean validRefreshToken(String token) {
        try {
            Claims claims = this.getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            log.error("Invalid refresh token: {}", e.getMessage());
            return false;
        }
    }

}
