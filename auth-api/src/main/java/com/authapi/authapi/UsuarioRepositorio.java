package com.authapi.authapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuarios, String> {
    // Método personalizado para buscar un usuario por su dirección de correo electrónico
    Usuarios findByEmail(String email);
}
