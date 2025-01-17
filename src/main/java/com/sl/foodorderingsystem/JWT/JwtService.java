package com.sl.foodorderingsystem.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final static String SECRET_KEY="9b05357a733c412aa360fc6020a767fcbdd312d334814ccad568e0d01f39453b";

    public String generateToken(Map<String, Object > extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .addClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(getSignInKey() , SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<String, Object>() , userDetails);
    }


    public Boolean isTokenValid(String token, UserDetails userDetails){
         String username=extractUsername(token);
        return username.equals(userDetails.getUsername()) && isTokenExpired(token);
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token){
       return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public  <T> T  extractClaim(String token, Function<Claims , T> claimsResolver){
        final Claims claims =  extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Key getSignInKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername( String token) {
        return extractClaim(token , Claims::getSubject);
    }

    public Date extractExpiration( String token){
        return extractClaim(token, Claims::getExpiration);
    }




}
