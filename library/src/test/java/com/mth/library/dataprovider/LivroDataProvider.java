package com.mth.library.dataprovider;

import com.mth.library.dto.LivroDTO;
import com.mth.library.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class LivroDataProvider {

    @Autowired
    private LivroService livroService;

    public static LivroDTO createLivroDefault() {
        LivroDTO livroDTO = new LivroDTO();
        livroDTO.setUuid(null);
        livroDTO.setTitulo("Crepuscúlo");
        livroDTO.setAutor("Mth");
        livroDTO.setIsbn("0-5179-2676-8");
        livroDTO.setDataPublicacao(LocalDate.now());
        livroDTO.setCategoria("TERROR");
        return livroDTO;
    }

    public static LivroDTO createLivroCustom(UUID uuid, String titulo, String autor, String isbn, LocalDate dataPublicacao, String categoria) {
        LivroDTO livroDTO = new LivroDTO();
        livroDTO.setUuid(uuid);
        livroDTO.setTitulo(titulo);
        livroDTO.setAutor(autor);
        livroDTO.setIsbn(isbn);
        livroDTO.setDataPublicacao(dataPublicacao);
        livroDTO.setCategoria(categoria);
        return livroDTO;
    }

    public LivroDTO createAndSave(UUID uuid, String titulo, String autor, String isbn, LocalDate dataPublicacao, String categoria) {
        LivroDTO livroDTO = new LivroDTO();
        livroDTO.setUuid(uuid);
        livroDTO.setTitulo(titulo);
        livroDTO.setAutor(autor);
        livroDTO.setIsbn(isbn);
        livroDTO.setDataPublicacao(dataPublicacao);
        livroDTO.setCategoria(categoria);

        return livroService.createLivro(livroDTO);
    }

}
