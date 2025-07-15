package br.com.anderson.imageliteapi.application.users;

import br.com.anderson.imageliteapi.application.jwt.JwtService;
import br.com.anderson.imageliteapi.domain.AccessToken;
import br.com.anderson.imageliteapi.domain.entity.User;
import br.com.anderson.imageliteapi.domain.exception.DuplicateTupleException;
import br.com.anderson.imageliteapi.domain.service.UserService;
import br.com.anderson.imageliteapi.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User save(User user) {
        var possibleUser = getByEmail(user.getEmail());
        if(possibleUser != null) {
            throw new DuplicateTupleException("User already exists!");
        }
        encodePassword(user);
        return userRepository.save(user);
    }

    @Override
    public AccessToken autheticate(String email, String password) {
        var user = getByEmail(email);
        if(user == null) {
            return null;
        }
        boolean matches = passwordEncoder.matches(password, user.getPassword());

        if(matches){
            return jwtService.generateToken(user);
        }
        return null;
    }

    private void encodePassword(User user){
        String rawPassword = user.getPassword();
        String encondedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encondedPassword);
    }
}
