package com.authapi.authapi;

import javax.json.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaControlador {

    @Autowired
    private IncidenciaServicio incidenciaServicio;

    @GetMapping("/seleccionar")
    public ResponseEntity<?> seleccionarIncidencias() {
        // Aquí deberías validar el token JWT
        JsonArray resultado = incidenciaServicio.seleccionarIncidencias();
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/insertar")
    public ResponseEntity<?> insertarIncidencia(@RequestBody String jsonIncidencia) {
        // Aquí deberías validar el token JWT
        incidenciaServicio.insertarIncidencia(jsonIncidencia);
        return ResponseEntity.ok("Incidencia insertada con éxito");
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarIncidencia(@RequestBody String jsonIncidencia) {
        // Aquí deberías validar el token JWT
        incidenciaServicio.actualizarIncidencia(jsonIncidencia);
        return ResponseEntity.ok("Incidencia actualizada con éxito");
    }

    @DeleteMapping("/borrar")
    public ResponseEntity<?> borrarIncidencia(@RequestBody String jsonIncidencia) {
        // Aquí deberías validar el token JWT
        incidenciaServicio.borrarIncidencia(jsonIncidencia);
        return ResponseEntity.ok("Incidencia borrada con éxito");
    }

    // Otros endpoints si es necesario
}
