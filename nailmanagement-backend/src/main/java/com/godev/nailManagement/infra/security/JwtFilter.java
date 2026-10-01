package com.godev.nailManagement.infra.security;

import com.godev.nailManagement.domain.client.Client;
import com.godev.nailManagement.domain.client.ClientRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ClientRepository clientRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtFilter(JwtUtil jwtUtil, ClientRepository clientRepository) {
        this.jwtUtil = jwtUtil;
        this.clientRepository = clientRepository;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                Client user = clientRepository.findByUsername(username)
                        .orElseThrow(() -> new IllegalStateException("User not found at JwtFilter!"));

                if (jwtUtil.isTokenValid(token, user)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                            );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            sendUnauthorized(response, "Token expired");
        } catch (MalformedJwtException e) {
            sendUnauthorized(response, "Invalid token format");
        } catch (SignatureException e) {
            sendUnauthorized(response, "Invalid token signature");
        } catch (Exception e) {
            sendUnauthorized(response, "Authentication error");
        }
    }

    private void sendUnauthorized(HttpServletResponse response, String message)
            throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        Map<String, Object> body = new HashMap<>();
        body.put("status", 401);
        body.put("error", "Unauthorized");
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now().toString());

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
