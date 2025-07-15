package br.com.anderson.imageliteapi.domain.service;

import br.com.anderson.imageliteapi.domain.AccessToken;
import br.com.anderson.imageliteapi.domain.entity.User;

public interface UserService {

    User getByEmail(String email);
    User save(User user);
    AccessToken autheticate(String email, String password);
}
