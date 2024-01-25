package com.authapi.authapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception {
        try {
            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Si la autenticación es exitosa, se obtiene el UserDetails
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            // Generar el token JWT
            final String jwt = jwtUtil.generateToken(userDetails);

            // Devolver el token como respuesta
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (Exception e) {
            // Manejo de errores en caso de autenticación fallida
            throw new Exception("Error en la autenticación: " + e.getMessage());
        }
    }
}
