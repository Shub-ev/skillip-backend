// package com.skillip.skillip_backend.security;


// import java.util.Date;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.function.Function;

// import org.springframework.stereotype.Component;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.userdetails.UserDetails;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwt;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;



// @Component
// public class JwtTokenProvider{
//     @Value("${jwt.secret}")
//     private String secretKey;

//     @Value("${jwt.expiration}")
//     private Long expiration;

//     public String generateToken(UserDetails userDetails){
//         Map<String, Object> claims = new HashMap<>();
//         return createToken(claims, userDetails.getUsername());
//     }

//     public String createToken(Map<String, Object> claims, String subject){
//         return Jwts.builder()
//                 .setClaims(claims)
//                 .setSubject(subject)
//                 .setIssuedAt(new Date(System.currentTimeMillis()))
//                 .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                 .signWith(SignatureAlgorithm.HS256, secretKey)
//                 .compact();
//     }

//     public Boolean validateToken(String token, UserDetails userDetails){
//         final String username = extractUsername(token);
//         return (username.equals(userDetails.getUsernmame()) && !isTokenExpired(token));
//     }


//     public String extractUsername(String token){
//         return extractClaim(token, Claims::getSubject);
//     }

//     public Date extractExpiration(String token){
//         return extractClaim(token, Claims::getExpiration);
//     }

//     public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
//         final Claims claims = extractAllClaims(token);
//         return claimsResolver.apply(claims);
//     }
// }