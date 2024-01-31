package com.authapi.authapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UsuarioServicio {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);
	
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    public Usuarios registrar(Usuarios usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        try {
            String jsonUsuario = objectMapper.writeValueAsString(usuario);
            jdbcTemplate.execute("CALL pkg_usuarios.insertar_usuario('" + jsonUsuario.replace("'", "''") + "')");
            return usuario;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario", e);
        }
    }

    public String login(String email, String contrasena) {
        try {
            Usuarios usuario = jdbcTemplate.queryForObject(
                "SELECT email, contrasena FROM usuarios WHERE email = ?",
                new RowMapper<Usuarios>() {
                    @Override
						public Usuarios mapRow(ResultSet rs, int rowNum) throws SQLException {
							Usuarios user = new Usuarios();
							user.setEmail(rs.getString("email"));
							user.setContrasena(rs.getString("contrasena"));
							return user;
						}
                },
                email
            );

            if (usuario != null) {
                // Utiliza el método matches de BCryptPasswordEncoder para verificar la contraseña
                if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                	String token = jwtUtil.generateToken(usuario);

                    usuario.setToken(token);

                    String jsonUsuario = objectMapper.writeValueAsString(usuario);
                    jdbcTemplate.execute("CALL pkg_usuarios.actualizar_usuario('" + jsonUsuario.replace("'", "''") + "')");

                    logger.info("Inicio de sesión exitoso para el usuario: " + usuario.getEmail());
                    return token;
                }
            }

            logger.error("Inicio de sesión fallido para el usuario: " + email);
            throw new RuntimeException("Las credenciales son incorrectas.");
        } catch (Exception e) {
            logger.error("Error al iniciar sesión", e);
            throw new RuntimeException("Error al iniciar sesión", e);
        }
    }
    
 
}