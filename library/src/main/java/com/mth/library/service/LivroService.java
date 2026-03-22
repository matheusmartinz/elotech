package com.mth.library.service;

import com.mth.library.dto.LivroDTO;
import com.mth.library.exceptions.FailedConditional;
import com.mth.library.exceptions.IsNull;
import com.mth.library.model.Livro;
import com.mth.library.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    private void formatedLivroResponse(LivroDTO livroDTO) {
        if (livroDTO.getIsbn() != null && !livroDTO.getIsbn().isEmpty()) {
            livroDTO.setIsbn(
                    livroDTO.getIsbn().replaceAll("[^0-9]", "").trim()
            );
        }

        if (livroDTO.getCategoria() != null && !livroDTO.getCategoria().isEmpty()) {
            livroDTO.setCategoria(
                    livroDTO.getCategoria().toLowerCase().trim()
            );
        }
    }

    public LivroDTO createLivro(LivroDTO livroDTO) {
        formatedLivroResponse(livroDTO);
        if (livroRepository.existsByIsbn(livroDTO.getIsbn())) {
            throw new FailedConditional("ISBN já cadastrado.");
        }
        Livro livro = new Livro(livroDTO);
        return new LivroDTO(livroRepository.save(livro));
    }

    public List<LivroDTO> findAll() {
        List<Livro> livros = livroRepository.findAll();
        return livros.stream().map(LivroDTO::new).toList();
    }

    public LivroDTO updateLivro(LivroDTO livroDTO, UUID uuidLivro) {
        formatedLivroResponse(livroDTO);
        Livro livro = livroRepository.findByUuid(uuidLivro);
        if (livro == null) {
            throw new IsNull("Livro não encontrado.");
        }
        if (livroRepository.existsByIsbn(livroDTO.getIsbn())) {
            throw new FailedConditional("ISBN já cadastrado.");
        }
        livro.updateLivro(livroDTO);
        return new LivroDTO(livroRepository.save(livro));
    }

    public void deleteLivro(UUID uuidLivro) {
        Livro livro = livroRepository.findByUuid(uuidLivro);
        if (livro == null) {
            throw new IsNull("Livro não encontrado.");
        }
        livroRepository.delete(livro);
    }
}
