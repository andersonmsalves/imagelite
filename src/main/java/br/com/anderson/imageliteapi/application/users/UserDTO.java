package br.com.anderson.imageliteapi.application.users;

import lombok.Data;

@Data
public class UserDTO {

    private String name;
    private String email;
    private String password;
}
