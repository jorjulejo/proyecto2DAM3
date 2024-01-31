package com.authapi.authapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

	@Autowired
	private UsuarioServicio usuarioServicio;

	@PostMapping("/registro")
	public ResponseEntity<Usuarios> registrarUsuario(@RequestBody Usuarios usuario) {
		Usuarios usuarioRegistrado = usuarioServicio.registrar(usuario);
		return ResponseEntity.ok(usuarioRegistrado);
	}

	@PostMapping("/login")
	public ResponseEntity<?> iniciarSesion(@RequestBody CredencialesLogin credenciales) {
        try {
            String token = usuarioServicio.login(credenciales.getEmail(), credenciales.getContrasena());
            return ResponseEntity.ok(token); // Devolver el token
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Las credenciales son incorrectas.");
        }
    }
	
    

	// Clase interna para representar las credenciales de inicio de sesión
	public static class CredencialesLogin {
		private String email;
		private String contrasena;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getContrasena() {
			return contrasena;
		}

		public void setContrasena(String contrasena) {
			this.contrasena = contrasena;
		}
	}
}
