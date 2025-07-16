package br.com.anderson.imageliteapi.application.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;

@Component
public class SecretKeyGenerator {

    private SecretKey key;

    public Key getKey() {
        if (key == null) {
            key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // forma correta e compatível
        }
        return key;
    }
}