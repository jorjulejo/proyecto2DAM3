package com.authapi.authapi;

import javax.json.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flujos")
public class FlujoControlador {

    @Autowired
    private FlujoServicio FlujoServicio;

    @GetMapping("/seleccionar")
    public ResponseEntity<?> seleccionarIncidencias() {
        // Aquí deberías validar el token JWT
        JsonArray resultado = FlujoServicio.seleccionarFlujos();
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/insertar")
    public ResponseEntity<?> insertarIncidencia(@RequestBody String jsonFlujo) {
        // Aquí deberías validar el token JWT
        FlujoServicio.insertarFlujo(jsonFlujo);
        return ResponseEntity.ok("Incidencia insertada con éxito");
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarIncidencia(@RequestBody String jsonFlujo) {
        // Aquí deberías validar el token JWT
        FlujoServicio.actualizarFlujo(jsonFlujo);
        return ResponseEntity.ok("Incidencia actualizada con éxito");
    }

    @DeleteMapping("/borrar")
    public ResponseEntity<?> borrarIncidencia(@RequestBody String jsonFlujo) {
        // Aquí deberías validar el token JWT
        FlujoServicio.borrarFlujo(jsonFlujo);
        return ResponseEntity.ok("Incidencia borrada con éxito");
    }

    // Otros endpoints si es necesario
}
