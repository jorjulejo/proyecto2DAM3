package com.authapi.authapi;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    @Id
    private String email;
    private String contrasena;
    private String token;
    private String snAdmin;
    
    
    
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSnAdmin() {
		return snAdmin;
	}
	public void setSnAdmin(String snAdmin) {
		this.snAdmin = snAdmin;
	}
    
    
}
