package com.authapi.authapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioRepositorio usuarioRepositorio;
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    
    
    List<GrantedAuthority> authorities = Collections.singletonList(
    	    new SimpleGrantedAuthority("ROLE_USER") // Reemplaza "ROLE_USER" con la autoridad que desees asignar
    	);
    // Constructor para la inyección de dependencias
    public JwtRequestFilter(JwtUtil jwtUtil, UsuarioRepositorio usuarioRepositorio) {
        this.jwtUtil = jwtUtil;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", authorizationHeader);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
            logger.debug("JWT: {}", jwt);
            logger.debug("Username extracted from JWT: {}", username);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Usuarios usuario = usuarioRepositorio.findByEmail(username); // Uso correcto del método findByEmail

            if (usuario != null && jwtUtil.validateToken(jwt, usuario)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        usuario, null, authorities); // Asegúrate de que usuario tenga un método getAuthorities()
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.debug("Authentication set in security context for '{}'", username);
            } else {
                logger.debug("Token JWT no válido o usuario no encontrado");
            }
        } else {
            logger.debug("Username es nulo o ya existe autenticación en el contexto");
        }

        chain.doFilter(request, response);
    }
}
