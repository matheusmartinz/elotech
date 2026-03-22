package com.mth.library.repository;

import com.mth.library.model.Emprestimo;
import com.mth.library.model.Livro;
import com.mth.library.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    Emprestimo findByUuid(UUID uuidEmprestimo);

    Optional<Emprestimo> findByLivroAndStatus(Livro livro, Status status);

    List<Emprestimo> findByUsuarioUuid(UUID uuidUsuario);
}
