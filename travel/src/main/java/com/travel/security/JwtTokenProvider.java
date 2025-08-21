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
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {

	private final String JWT_SECRET = "mySuperSecretKeyThatIsLongEnoughToBeUsedWithHS512Algorithm123456!";
    private final long JWT_EXPIRATION_MS = 1000*60*1; // 30분

    private Key getSigningKey() {
        byte[] keyBytes = JWT_SECRET.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
 // Refresh Token 생성
    public String generateRefreshToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                .collect(Collectors.toList());

        long refreshExpiration = 1000L * 60 * 60 * 24 * 7; // 7일

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // JWT의 subject를 username으로 사용
    }
    
    // 토큰 리프레쉬
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Access Token과 같은 키 사용 가능
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // Refresh Token도 만료됐으면 재로그인 필요
            System.out.println("Refresh token expired: " + e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid refresh token: " + e.getMessage());
            return false;
        }
    }
    
 // Claims에서 roles 안전하게 추출
    public List<String> getRolesFromClaims(Claims claims) {
        List<String> roles = new ArrayList<>();
        Object claimRoles = claims.get("roles");

        if (claimRoles instanceof List<?>) {
            for (Object role : (List<?>) claimRoles) {
                if (role instanceof String) {
                    roles.add((String) role);
                }
            }
        }

        return roles;
    }
    
 // username + roles 기반으로 Access Token 생성 (Refresh Token 재발급용)
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)   // 로그인 시 토큰과 동일하게 roles 포함
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }


    // 토큰 생성
    public String generateToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        
     // 권한 문자열 리스트 추출
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(authority -> authority.getAuthority())  // "ROLE_USER", "ROLE_ADMIN" 등
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role) // "ROLE_" 제거해서 "USER" 등으로 변환
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", roles)   // roles 클레임 추가
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
        } catch (ExpiredJwtException e) {
            // 토큰 만료됨
            System.out.println("JWT expired: " + e.getMessage());
            throw e; // 또는 커스텀 예외 던져서 Refresh 처리
        }catch (JwtException | IllegalArgumentException e) {
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
        
        List<String> roles  = claims.get("roles", List.class);
        
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        if (roles != null) {
            authorities = roles.stream()
            		.map(role -> new SimpleGrantedAuthority("ROLE_" + role))  // "USER" → "ROLE_USER"
                    .collect(Collectors.toList());
        }
        
//        User principal = new User(email, "", authorities);
//        System.out.println("authorities = " + authorities);
        
        // 권한 정보는 비워도 되고, 필요하면 DB에서 가져올 수 있음
        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }
}
