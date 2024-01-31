package com.authapi.authapi;

import javax.json.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/camaras")
public class CamaraControlador {

    @Autowired
    private CamaraServicio CamaraServicio;

    @GetMapping("/seleccionar")
    public ResponseEntity<?> seleccionarIncidencias() {
        // Aquí deberías validar el token JWT
        JsonArray resultado = CamaraServicio.seleccionarCamaras();
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/insertar")
    public ResponseEntity<?> insertarIncidencia(@RequestBody String jsonCamara) {
        // Aquí deberías validar el token JWT
        CamaraServicio.insertarCamara(jsonCamara);
        return ResponseEntity.ok("Incidencia insertada con éxito");
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarIncidencia(@RequestBody String jsonCamara) {
        // Aquí deberías validar el token JWT
        CamaraServicio.actualizarCamara(jsonCamara);
        return ResponseEntity.ok("Incidencia actualizada con éxito");
    }

    @DeleteMapping("/borrar")
    public ResponseEntity<?> borrarIncidencia(@RequestBody String jsonCamara) {
        // Aquí deberías validar el token JWT
        CamaraServicio.borrarCamara(jsonCamara);
        return ResponseEntity.ok("Incidencia borrada con éxito");
    }

    // Otros endpoints si es necesario
}
