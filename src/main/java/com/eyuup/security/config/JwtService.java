package com.eyuup.security.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;



@Service
public class JwtService {

    private final static String SECRET_KEY="wUEIsKbbjn+3Wgu6TzDOk5MZ3BD4T0GMmYWfn5O1hrfon1al+xF9E2Yq6H6cMtXb";

    public String extractUserName(String jwt){
        return extractClaim(jwt, Claims::getSubject);
    }

    // Function<inputType,outputType>
    public <T> T extractClaim(String token,Function<Claims,T> claimsReslover ){
        final Claims claims=extractAllClaims(token);
        return claimsReslover.apply(claims);
    }


      public String generateToken(UserDetails userDetails)
       {
            return generateToken(new HashMap<>(), userDetails);
       }

    public String generateToken(Map<String,Object> extraClaims,UserDetails userDetails){
        return Jwts
        .builder()
        .setClaims(extraClaims != null ? extraClaims : new HashMap<>())
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24) )
        .signWith(getSignkey(),SignatureAlgorithm.HS256)
        .compact(); 
        
    }


    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username=extractUserName(token);
        return (username.equals(userDetails.getUsername()));
    }

    public boolean isExpiredToken(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts
        .parserBuilder()
        .setSigningKey(getSignkey())
        .build()
        .parseClaimsJws(token)
        .getBody();
        
        
    }


    private Key getSignkey() {
       byte[] KeyBtes= Decoders.BASE64.decode(SECRET_KEY);
       return  Keys.hmacShaKeyFor(KeyBtes);
    }

}
