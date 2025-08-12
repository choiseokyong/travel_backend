package com.travel.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {

	private final String JWT_SECRET = "mySuperSecretKeyThatIsLongEnoughToBeUsedWithHS512Algorithm123456!";
    private final long JWT_EXPIRATION_MS = 86400000; // 1일

    private Key getSigningKey() {
        byte[] keyBytes = JWT_SECRET.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String generateToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰에서 사용자 이메일 추출
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
 // 토큰에서 Claims 추출 메서드 추가
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    // 요청 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    // 토큰 → Authentication 변환 (권한 숫자 → 문자열 권한 변환 포함)
    public Authentication getAuthentication(String token) {
    	Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }

        String email = getEmailFromToken(token);
        
        List<Integer> roleNumbers = claims.get("roles", List.class);
        
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        if (roleNumbers != null) {
            authorities = roleNumbers.stream()
                    .map(role -> {
                        switch (role) {
                            case 1:
                                return new SimpleGrantedAuthority("ADMIN");
                            case 5:
                                return new SimpleGrantedAuthority("USER");
                            default:
                                return new SimpleGrantedAuthority("GUEST");
                        }
                    })
                    .collect(Collectors.toList());
        }
        
        User principal = new User(email, "", authorities);
        
        // 권한 정보는 비워도 되고, 필요하면 DB에서 가져올 수 있음
        return new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
    }
}
