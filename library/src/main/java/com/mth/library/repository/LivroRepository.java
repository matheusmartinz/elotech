package com.mth.library.repository;

import com.mth.library.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    Livro findByUuid(UUID uuidLivro);

    boolean existsByIsbn(String isbn);

    @Query("""
                SELECT DISTINCT l FROM Livro l
                WHERE l.categoria IN (
                    SELECT l2.categoria FROM Livro l2
                    WHERE l2.uuid IN :livrosUsuario
                )
                AND l.uuid NOT IN :livrosUsuario
            """)
    List<Livro> recomendarLivros(Set<UUID> livrosUsuario);
}
