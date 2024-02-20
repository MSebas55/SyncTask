package com.dam.pruebaspring.repositories;

import com.dam.pruebaspring.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByNombre(String nombre);


    Usuario findByEmail(String email);
}
