package com.authapi.authapi;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuarios {
    @Id
    private String email;
    private String contrasena;
    private String token;
    private String sn_admin;
    
    
    
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
		return sn_admin;
	}
	public void setSnAdmin(String snAdmin) {
		this.sn_admin = snAdmin;
	}
    
    
}
