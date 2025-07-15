package br.com.anderson.imageliteapi.infra.repository;

import br.com.anderson.imageliteapi.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {


    User findByEmail(String email);
}
