package br.com.anderson.imageliteapi.application.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import io.jsonwebtoken.security.Keys;

@Component
public class SecretKeyGenerator {

//    private SecretKey key;
    private Key key;

    public Key getKey() {
        if(key == null) {
//            key = Jwts.SIG.HS256.key().build();
            key = Keys.secretKeyFor(SignatureAlgorithm.ES256);
        }

        return key;
    }
}
