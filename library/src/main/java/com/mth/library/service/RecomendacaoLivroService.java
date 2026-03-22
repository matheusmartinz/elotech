package com.mth.library.service;

import com.mth.library.exceptions.IsNull;
import com.mth.library.model.Emprestimo;
import com.mth.library.model.Livro;
import com.mth.library.repository.EmprestimoRepository;
import com.mth.library.repository.LivroRepository;
import com.mth.library.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecomendacaoLivroService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Livro> findRecomendacao(UUID uuidUsuario) {
        if(usuarioRepository.findByUuid(uuidUsuario) == null) {
            throw new IsNull("Usuário não encontrado.");
        }

        Set<UUID> livrosRecomendacoes = emprestimoRepository.findByUsuarioUuid(uuidUsuario)
                .stream()
                .map(emprestimo -> emprestimo.getLivro().getUuid())
                .collect(Collectors.toSet());

        if (livrosRecomendacoes.isEmpty()) {
            return Collections.emptyList();
        }

        return livroRepository.recomendarLivros(livrosRecomendacoes)
                .stream().toList();
    }

}
