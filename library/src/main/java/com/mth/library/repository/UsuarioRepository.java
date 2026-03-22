package com.mth.library.repository;

import com.mth.library.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUuid(UUID uuidUsuario);

    boolean existsUsuarioByEmail(String email);
}
