package com.authapi.authapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public Usuarios loadUserByUsername(String username) {
        // Usamos la instancia inyectada de UsuarioRepositorio para buscar el usuario
        Usuarios usuario = usuarioRepositorio.findByEmail(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        return usuario;
    }
}
