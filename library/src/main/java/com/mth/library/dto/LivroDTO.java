package com.mth.library.dto;

import com.mth.library.model.Livro;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class LivroDTO {
    private UUID uuid;

    private String titulo;

    private String autor;

    private String isbn;

    private LocalDate dataPublicacao;

    private String categoria;

    public LivroDTO(Livro livro) {
        this.uuid = livro.getUuid();
        this.titulo = livro.getTitulo();
        this.autor = livro.getAutor();
        this.isbn = livro.getIsbn();
        this.dataPublicacao = livro.getDataPublicacao();
        this.categoria = livro.getCategoria();
    }
}
