package com.woorifisa.reservation.util;

import com.woorifisa.reservation.dto.TokenDTO;
import com.woorifisa.reservation.entity.Role;
import com.woorifisa.reservation.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {

    private final Key secretKey;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 3; // 3시간

    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    public JWTUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public TokenDTO generateTokens(User user) {
        return generateTokens(user.getId(), user.getName(), user.getRole());
    }

    public TokenDTO generateTokens(Long userId, String userName, Role userRole) {
        Date now = new Date();
        Date accessTokenExpiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiry = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        return TokenDTO.builder()
                .tokenType("Bearer")
                .role(userRole)
                .accessToken(createAccessToken(userId, userName, userRole, accessTokenExpiry))
                .accessTokenExpiresAt(accessTokenExpiry)
                .refreshToken(createRefreshToken(userId, userName, refreshTokenExpiry))
                .refreshTokenExpiresAt(refreshTokenExpiry)
                .build();
    }

    public String createAccessToken(Long userId, String userName, Role userRole, Date accessTokenExpiry) {
        return Jwts.builder()
                // Header: 토큰의 타입(JWT)과 서명에 사용된 알고리즘(HS512) 정보를 담는다.
                .header()
                .add("typ", "JWT") // 토큰의 타입 지정. 여기서는 JWT를 사용.
                .add("alg", "HS512") // 서명 알고리즘 지정. 여기서는 HMAC SHA-512를 사용.
                .and()

                // Payload: 토큰에 담을 클레임(데이터)을 지정. 클레임에는 사용자의 이름, 역할, ID 등이 포함될 수 있다.
                .issuer("WOORIFISA-STUDY-ROOM-RESERVATION-SERVER") // iss 클레임: 토큰 발급자를 지정
                .subject(userName) // sub 클레임: 토큰 제목을 지정
//                .audience().add("your-audience").and()  // aud 클레임: 토큰 대상자를 지정
                .expiration(accessTokenExpiry) // exp 클레임: 토큰 만료 시간을 지정
//                .notBefore(Date(System.currentTimeMillis())) // nbf 클레임: 토큰 활성 날짜를 지정. 헤딩 시간 이전에는 토큰이 활성화되지 않는다.
                .issuedAt(new Date()) // iat 클레임: 토큰 발급 시간을 지정
//                .id(UUID.randomUUID().toString()) // jti 클레임: JWT 토큰 식별자를 지정
                .claim("role", userRole) // 사용자 정의 클레임: 사용자의 역할
                .claim("client-id", userId) // 사용자 정의 클레임: 사용자의 식별자

                // Signature: header와 payload를 암호화한 결과. 이 부분이 토큰의 무결성을 보장하는 부분
                .signWith(secretKey) // signWith 메소드를 사용해 서명 알고리즘과 키를 지정

                // compact 메소드를 호출해 JWT 토큰 문자열을 생성한다.
                .compact();
    }

    public String createRefreshToken(Long userId, String userName, Date refreshTokenExpiry) {
        return Jwts.builder()
                .subject(userName) // sub 클레임: 토큰 제목을 지정
                .issuer("WOORIFISA-STUDY-ROOM-RESERVATION-SERVER") // iss 클레임: 토큰 발급자를 지정
                .issuedAt(new Date()) // iat 클레임: 토큰 발급 시간을 지정
                .expiration(refreshTokenExpiry) // exp 클레임: 토큰 만료 시간을 지정
                .claim("client-id", userId) // 사용자 정의 클레임: 사용자의 식별자
                .signWith(secretKey) // signWith 메소드를 사용해 서명 알고리즘과 키를 지정
                .compact(); // JWT 토큰 문자열을 생성
    }

    public TokenDTO refreshTokens(String refreshToken) {
        try {
            // 토큰 파싱 및 검증
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();

            // 만료 시간 검증
            if (claims.getExpiration().before(new Date())) {
                throw new ExpiredJwtException(null, claims, "리프레시 토큰이 만료되었습니다.");
            }

            // 클레임에서 사용자 정보 추출
            String name = claims.getSubject();
            Long userId = claims.get("client-id", Long.class);
            Role role = Role.valueOf(claims.get("role", String.class));

            // 새로운 토큰 발급
            return generateTokens(userId, name, role);

        } catch (ExpiredJwtException e) {
            log.warn("만료된 리프레시 토큰으로 재발급을 시도했습니다.");
            throw new JwtException("토큰이 만료되었습니다.");

        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            log.warn("유효하지 않은 리프레시 토큰으로 재발급을 시도했습니다.: {}", e.getMessage());
            throw new JwtException("토큰이 유효하지 않습니다.");
        }
    }

}
