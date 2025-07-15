package br.com.anderson.imageliteapi.application.jwt;

import br.com.anderson.imageliteapi.domain.AccessToken;
import br.com.anderson.imageliteapi.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public AccessToken generateToken(User user){
        return new AccessToken("");
    }
}
