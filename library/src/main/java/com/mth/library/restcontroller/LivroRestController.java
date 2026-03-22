package com.mth.library.restcontroller;

import com.mth.library.dto.LivroDTO;
import com.mth.library.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("livro")
public class LivroRestController {

    @Autowired
    private LivroService livroService;

    @PostMapping("/cadastro")
    public ResponseEntity<LivroDTO> cadastrarLivro(@Valid @RequestBody LivroDTO livroDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.createLivro(livroDTO));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<LivroDTO>> getAllLivros() {
        return ResponseEntity.ok().body(livroService.findAll());
    }

    @PutMapping("/editar/{uuidLivro}")
    public ResponseEntity<LivroDTO> atualizarLivro(@Valid @RequestBody LivroDTO livroDTO, @PathVariable UUID uuidLivro) {
        return ResponseEntity.ok().body(livroService.updateLivro(livroDTO, uuidLivro));
    }

    @DeleteMapping("/excluir/{uuidLivro}")
    public ResponseEntity<String>  excluirLivro(@PathVariable UUID uuidLivro) {
        livroService.deleteLivro(uuidLivro);
        return ResponseEntity.ok().body("Livro excluido com sucesso.");
    }
}
