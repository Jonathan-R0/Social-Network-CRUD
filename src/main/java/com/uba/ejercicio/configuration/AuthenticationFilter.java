package com.uba.ejercicio.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uba.ejercicio.services.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final int BEARER_LENGTH = "Bearer ".length();

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

                String token = authorizationHeader.substring(BEARER_LENGTH);
                String username = tokenManager.getEmailFromToken(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userService.loadUserByUsername(username);
                    if (tokenManager.validateToken(token, userDetails) && userDetails.isEnabled()) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }

            }

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            Map<String, Object> data = new HashMap<>();
            data.put("error message", e.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(data));
            response.getWriter().flush();
        }
    }

}
