package com.authapi.authapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UsuarioServicio {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    public Usuarios registrar(Usuarios usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        try {
            String jsonUsuario = objectMapper.writeValueAsString(usuario);
            // Asegúrate de que la sintaxis del llamado al procedimiento almacenado sea correcta para Oracle
            jdbcTemplate.execute(
                "CALL pkg_usuarios.insertar_usuario('" + jsonUsuario.replace("'", "''") + "')"
            );
            return usuario;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario", e);
        }
    }
}
