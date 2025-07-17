package br.com.anderson.imageliteapi.application.jwt;

import br.com.anderson.imageliteapi.domain.AccessToken;
import br.com.anderson.imageliteapi.domain.entity.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecretKeyGenerator secretKeyGenerator;

    public AccessToken generateToken(User user){

        Key key = secretKeyGenerator.getKey();
        Date expirateDate = generateExpirationDate();
        Map<String, Object> claims = generateTokenClaims(user);

        String token = Jwts.builder()
                .signWith(key)
                .setSubject(user.getEmail())
                .setExpiration(expirateDate)
                .addClaims(claims)
                .compact();
        return new AccessToken(token);
    }

    private Date generateExpirationDate() {
        var expirationMinutes = 60;
        LocalDateTime now = LocalDateTime.now().plusMinutes(expirationMinutes);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Map<String, Object> generateTokenClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        return claims;
    }

    public String getEmailFromToken(String tokenJwt) {

        try{
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(secretKeyGenerator.getKey()) // ajuste aqui: use `setSigningKey` e não `verifyWith`
                    .build();

            Jws<Claims> claimsJws = parser.parseClaimsJws(tokenJwt); // método correto para tokens assinados comuns (JWS)
            Claims claims = claimsJws.getBody();

            return claims.getSubject(); // geralmente o e-mail é armazenado no subject
        }catch(JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        }

    }

//    public String getEmailFromToken(String tokenJwt) {
//        JwtParser build = Jwts.parserBuilder().verifyWith(secretKeyGenerator.getKey())
//                .build();
//
//        Jws< Claims > claims = build.parseSignedClaims(tokenJwt);
//
//        Claims cls = claims.getPayload();
//
//        return cls.getSubject();
//    }
}
