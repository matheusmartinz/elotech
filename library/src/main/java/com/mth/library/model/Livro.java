package com.mth.library.model;

import com.mth.library.dto.LivroDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @NotBlank(message = "Obrigatório inserir título.")
    private String titulo;

    @NotBlank(message = "Obrigatório inserir autor.")
    private String autor;

    @NotBlank(message = "ISBN é obrigatório.")
    @Pattern(
            regexp = "^(\\d{9}[\\dX]|\\d{13}|\\d{1,5}-\\d{1,7}-\\d{1,7}-[\\dX])$",
            message = "ISBN inválido."
    )
    private String isbn;

    @NotNull(message = "Obrigatório inserir data de publicação.")
    private LocalDate dataPublicacao;

    @NotBlank(message = "Obrigatório inserir categoria.")
    private String categoria;

    public Livro(LivroDTO livroDTO) {
        this.uuid = UUID.randomUUID();
        this.titulo = livroDTO.getTitulo();
        this.autor = livroDTO.getAutor();
        this.isbn = livroDTO.getIsbn();
        this.dataPublicacao = livroDTO.getDataPublicacao();
        this.categoria = livroDTO.getCategoria();
    }

    public void updateLivro(LivroDTO livroDTO) {
        this.titulo = livroDTO.getTitulo();
        this.autor = livroDTO.getAutor();
        this.isbn = livroDTO.getIsbn();
        this.dataPublicacao = livroDTO.getDataPublicacao();
        this.categoria = livroDTO.getCategoria();
    }
}
