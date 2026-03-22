package com.mth.library.restcontroller;

import com.mth.library.model.Livro;
import com.mth.library.service.RecomendacaoLivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("recomendacao")
public class RecomendacaoRestController {

    @Autowired
    private RecomendacaoLivroService recomendacaoLivroService;

    @GetMapping("/{uuidUsuario}")
    public ResponseEntity<List<Livro>> findByIsbn(@PathVariable UUID uuidUsuario) {
        return ResponseEntity.ok().body(recomendacaoLivroService.findRecomendacao(uuidUsuario));
    }

}
