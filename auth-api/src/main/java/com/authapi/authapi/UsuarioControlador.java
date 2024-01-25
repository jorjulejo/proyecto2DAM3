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
}

