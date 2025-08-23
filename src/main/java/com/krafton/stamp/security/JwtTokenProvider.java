package com.krafton.stamp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyPlain;

    @Value("${jwt.validity-ms:3600000}") // 기본 1시간
    private long accessTokenValidity;

    private Key secretKey;

    @PostConstruct
    public void init() {
        // secret이 Base64일 수도, 평문일 수도 있어서 안전하게 처리
        byte[] keyBytes;
        try {
            // Base64로 해석이 되면 이 경로
            keyBytes = Decoders.BASE64.decode(secretKeyPlain);
        } catch (IllegalArgumentException e) {
            // Base64가 아니면 UTF-8 바이트로 사용
            keyBytes = secretKeyPlain.getBytes(StandardCharsets.UTF_8);
        }
        if (keyBytes.length < 32) {
            // HS256 최소 256bit 권장
            throw new IllegalArgumentException("jwt.secret must be at least 32 bytes");
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /** ✅ 이메일 클레임 포함하여 토큰 생성 (userId는 sub에) */
    public String createToken(String userId, String email) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessTokenValidity);

        return Jwts.builder()
                .setSubject(userId)                  // sub = DB user.id (문자열)
                .claim("email", email)               // 이메일 클레임
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /** (호환용) Long id + email 버전 오버로드 */
    public String createToken(Long userId, String email) {
        return createToken(String.valueOf(userId), email);
    }

    /** ✅ 토큰 유효성 검증 (만료/서명 등) */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** ✅ 토큰에서 유저 ID(sub) 추출 */
    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    /** ✅ 토큰에서 이메일 추출 (없으면 null) */
    public @Nullable String getEmail(String token) {
        Claims claims = parseClaims(token);
        String email = claims.get("email", String.class);
        if (email == null || email.isBlank()) {
            email = claims.get("preferred_username", String.class);
        }
        return (email != null && !email.isBlank()) ? email : null;
    }

    /** 내부: 서명 검증 포함 파싱 */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
