package com.authapi.authapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    

	public UserDetails loadUserByUsername(String username) {
        // Buscar el usuario en la base de datos por su email (username)
        Usuarios usuario = usuarioRepositorio.findByEmail(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        // Crear y devolver un UserDetails con los detalles del usuario
        return new org.springframework.security.core.userdetails.User(
            usuario.getEmail(),
            usuario.getContrasena(),
            new ArrayList<>()
        );
	}

}
