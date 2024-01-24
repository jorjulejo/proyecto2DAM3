package com.example.Authapi;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        // Permite solicitudes al endpoint de registro sin autenticación
        if ("/api/auth/register".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Para otros caminos, continúa con el resto de los filtros
        filterChain.doFilter(request, response);
    }
}
