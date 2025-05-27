package app.hakai.backend.auth;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import app.hakai.backend.models.User;
import app.hakai.backend.repositories.UserRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository usersRepository;

    @Override
    protected void doFilterInternal(
        @SuppressWarnings("null") HttpServletRequest request,
        @SuppressWarnings("null") HttpServletResponse response,
        @SuppressWarnings("null") FilterChain filterChain
    ) throws ServletException, IOException, java.io.IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                UUID uuid = jwtUtil.validateTokenAndGetUserId(token);
                User user = usersRepository.findById(uuid)
                    .orElseThrow(
                        () -> new RuntimeException("User not found")
                    );
                
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, 
                    null,
                    List.of()
                );
                
                SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
            } catch (Exception e) {
                response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED, 
                    "Token inválido"
                );
                
                return;
            }
        };

        filterChain.doFilter(request, response);
    };
};