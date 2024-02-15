package com.authapi.authapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Properties;

import javax.json.JsonArray;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
	
	@PostMapping("/seleccionarUsuario")
    public ResponseEntity<?> seleccionarUsuario(@RequestBody String jsonIncidencia) {
        // Aquí deberías validar el token JWT
        JsonArray resultado = usuarioServicio.seleccionarUsuario(jsonIncidencia);
        return ResponseEntity.ok(resultado);
    }

	@PostMapping("/seleccionarAdmin")
    public ResponseEntity<?> seleccionarAdmin(@RequestBody String jsonIncidencia) {
        JsonArray resultado = usuarioServicio.seleccionarAdmin(jsonIncidencia);
        return ResponseEntity.ok(resultado);
    }
	
	@PostMapping("/enviar-correo")
	public ResponseEntity<?> enviarCorreo(@RequestBody CorreoRequest correoRequest) {
		try {
			// Configuración del servidor de correo saliente (SMTP)
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com"); // Reemplaza con tu servidor SMTP
			props.put("mail.smtp.port", "587"); // Reemplaza con el puerto SMTP adecuado

			// Credenciales del remitente
			String remitenteCorreo = "tuappfav@gmail.com"; // Reemplaza con tu dirección de correo
			String contrasenaCorreo = "hfdj owna hsfc wazt"; // Reemplaza con tu contraseña de correo

			// Autenticación
			Session session = Session.getInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(remitenteCorreo, contrasenaCorreo);
				}
			});

			// Crear mensaje de correo
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(remitenteCorreo));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoRequest.getDestinatario()));
			message.setSubject(correoRequest.getAsunto());
			message.setText(correoRequest.getCuerpo());

			// Enviar correo
			Transport.send(message);

			return ResponseEntity.ok("Correo enviado exitosamente.");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error al enviar el correo."+e);
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

	// Clase interna para representar una solicitud de correo
	public static class CorreoRequest {
		private String destinatario;
		private String asunto;
		private String cuerpo;

		public String getDestinatario() {
			return destinatario;
		}

		public void setDestinatario(String destinatario) {
			this.destinatario = destinatario;
		}

		public String getAsunto() {
			return asunto;
		}

		public void setAsunto(String asunto) {
			this.asunto = asunto;
		}

		public String getCuerpo() {
			return cuerpo;
		}

		public void setCuerpo(String cuerpo) {
			this.cuerpo = cuerpo;
		}
	}
}
