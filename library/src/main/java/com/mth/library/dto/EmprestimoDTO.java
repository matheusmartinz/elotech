package com.mth.library.dto;

import com.mth.library.model.Emprestimo;
import com.mth.library.model.Livro;
import com.mth.library.model.Status;
import com.mth.library.model.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EmprestimoDTO {
    private UUID uuid;

    private UsuarioDTO usuarioDTO;

    private LivroDTO livroDTO;

    private LocalDate dataEmprestimo;

    private LocalDate dataDevolucao;

    private Status status;

    public EmprestimoDTO(Emprestimo emprestimo) {
        this.uuid = emprestimo.getUuid();
        this.usuarioDTO = new UsuarioDTO(emprestimo.getUsuario());
        this.livroDTO = new LivroDTO(emprestimo.getLivro());
        this.dataEmprestimo = emprestimo.getDataEmprestimo();
        this.dataDevolucao = emprestimo.getDataDevolucao();
        this.status = emprestimo.getStatus();
    }
}
