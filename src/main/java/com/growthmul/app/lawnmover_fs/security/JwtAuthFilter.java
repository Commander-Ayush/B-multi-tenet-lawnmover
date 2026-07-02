package com.growthmul.app.lawnmover_fs.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Runs on every request. If a valid "Authorization: Bearer <jwt>" header is
 * present, populates the security context with an AdminPrincipal carrying
 * companyId — that's the ONLY way companyId enters an /admin/** request.
 * No token, or an invalid one, just means the request continues
 * unauthenticated — SecurityConfig's authorizeHttpRequests rules are what
 * actually reject it with 401 for protected routes.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            Claims claims = jwtService.parseAndValidate(token);
            if (claims != null) {
                Long companyId = jwtService.extractCompanyId(claims);
                String email = jwtService.extractEmail(claims);
                AdminPrincipal principal = new AdminPrincipal(companyId, email);

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
                var authToken = new UsernamePasswordAuthenticationToken(principal, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            // an invalid/expired token just means we don't authenticate —
            // no need to short-circuit here, the route guard handles it.
        }

        filterChain.doFilter(request, response);
    }
}
