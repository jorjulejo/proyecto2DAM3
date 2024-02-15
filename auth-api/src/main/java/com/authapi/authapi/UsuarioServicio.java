package com.authapi.authapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class UsuarioServicio {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);
	
	@Autowired
	private EntityManager entityManager;

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
        if(usuario.getEmail().toLowerCase().contains("@plaiaundi.net")|| usuario.getEmail().toLowerCase().contains("@plaiaundi.com")) {
            usuario.setSnAdmin("S");
        } else {
            usuario.setSnAdmin("N");
        }
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
    
	public JsonArray seleccionarUsuario(String email) {
		Query query = entityManager.createNativeQuery("SELECT pkg_usuarios.seleccionar_usuario(:email) FROM DUAL");
		query.setParameter("email", email);
		Clob clob = (Clob) query.getSingleResult();
		String jsonString = convertClobToString(clob);
		JsonArray jsonArray = null;

		try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
			jsonArray = jsonReader.readArray(); // Cambiado de readObject() a readArray()
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonArray;
	}


	public String seleccionarAdmin(String email) {
	    Query query = entityManager.createNativeQuery("SELECT pkg_usuarios.seleccionar_admin(:email) FROM DUAL");
	    // Prepara el JSON de entrada con el email
	    String inputJson = "{\"email\":\"" + email + "\"}";
	    query.setParameter("email", inputJson);
	    Clob clob = (Clob) query.getSingleResult();
	    String jsonString = convertClobToString(clob);

	    String snAdmin = null;

	    try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
	        JsonObject jsonObject = jsonReader.readObject(); // Lee el objeto JSON
	        snAdmin = jsonObject.getString("snAdmin"); // Obtiene el valor de "snAdmin"
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return snAdmin; // Retorna "S", "N", o null si hubo un error
	}


	private String convertClobToString(Clob clob) {
	    if (clob == null) {
	        // Manejar el caso nulo, por ejemplo, devolver una cadena vacía o null
	        return ""; // o puedes devolver "" si prefieres una cadena vacía
	    }
		StringBuilder sb = new StringBuilder();
		try {
			java.io.Reader reader = clob.getCharacterStream();
			java.io.BufferedReader br = new java.io.BufferedReader(reader);

			String line;
			while (null != (line = br.readLine())) {
				sb.append(line);
			}
			br.close();
		} catch (SQLException | java.io.IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
    
 
}