package com.example.Authapi;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    @Id
    private String email;
    private String contrasena;
    private String token;
    private String snAdmin;

 
    public Usuario() {
    }

    public Usuario(String email, String contrasena, String snAdmin) {
        this.email = email;
        this.contrasena = contrasena;
        this.snAdmin = snAdmin;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getToken() {
        return token;
    }

    public String getSnAdmin() {
        return snAdmin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSnAdmin(String snAdmin) {
        this.snAdmin = snAdmin;
    }
}

