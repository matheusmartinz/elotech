package com.mth.library.model;

import com.mth.library.dto.AtualizarEmprestimoDTO;
import com.mth.library.dto.EmprestimoDTO;
import com.mth.library.repository.LivroRepository;
import com.mth.library.repository.UsuarioRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @NotNull(message = "Pessoa não informada.")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotNull(message = "Livro não informado.")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "livro_id")
    private Livro livro;

    @NotNull(message = "Obrigatório informar data de empréstimo.")
    private LocalDate dataEmprestimo;

    @Nullable
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Obrigatório informar status do empréstimo.")
    private Status status;

    public Emprestimo(EmprestimoDTO emprestimoDTO, UsuarioRepository usuarioRepository, LivroRepository livroRepository) {
        this.uuid = UUID.randomUUID();
        this.usuario = usuarioRepository.findByUuid(emprestimoDTO.getUsuarioDTO().getUuid());
        this.livro = livroRepository.findByUuid(emprestimoDTO.getLivroDTO().getUuid());
        this.dataEmprestimo = emprestimoDTO.getDataEmprestimo();
        this.dataDevolucao = emprestimoDTO.getDataDevolucao();
        this.status = emprestimoDTO.getStatus();
    }

    public void updateEmprestimo(AtualizarEmprestimoDTO atualizarEmprestimoDTO) {
        this.dataDevolucao = atualizarEmprestimoDTO.getDataDevolucao();
        this.status = atualizarEmprestimoDTO.getStatus();
    }
}
