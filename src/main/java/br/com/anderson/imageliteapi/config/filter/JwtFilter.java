package br.com.anderson.imageliteapi.config.filter;

import br.com.anderson.imageliteapi.application.jwt.InvalidTokenException;
import br.com.anderson.imageliteapi.application.jwt.JwtService;
import br.com.anderson.imageliteapi.domain.entity.User;
import br.com.anderson.imageliteapi.domain.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = getToken(request);

        if(token != null) {
            try{
                String email = jwtService.getEmailFromToken(token);
                User user = userService.getByEmail(email);
                setUserAsAuthenticated(user);
            }catch (JwtException e) {
                //throw new InvalidTokenException(e.getMessage());
                log.error("InvalidTokenException: {}", e.getMessage());
            }catch(Exception e) {
                log.error("Exception: {}", e.getMessage());
            }

        }

        filterChain.doFilter(request, response); // Não impedir esta execução
    }

    private void setUserAsAuthenticated(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER") // USER é um Role padrão do SpringSecurity
                .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    private String getToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if(authHeader != null) {
            String[] authHeadersParts = authHeader.split(" ");

            if(authHeadersParts.length == 2) {
                return authHeadersParts[1];
            }
        }
        return null;
    }
}
