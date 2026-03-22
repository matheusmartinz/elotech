package com.mth.library.restcontroller;

import com.mth.library.dto.AtualizarEmprestimoDTO;
import com.mth.library.dto.EmprestimoDTO;
import com.mth.library.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("emprestimo")
public class EmprestimoRestController {

    @Autowired
    private EmprestimoService emprestimoService;

    @PostMapping("/cadastro")
    public ResponseEntity<EmprestimoDTO> createEmprestimo(@Valid @RequestBody EmprestimoDTO emprestimoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.createEmprestimo(emprestimoDTO));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<EmprestimoDTO>> findAllEmprestimo() {
        return ResponseEntity.ok().body(emprestimoService.getAllEmprestimos());
    }

    @PutMapping("/editar/{uuidEmprestimo}")
    public ResponseEntity<EmprestimoDTO> updateEmprestimo(@Valid @RequestBody AtualizarEmprestimoDTO atualizarEmprestimoDTO, @PathVariable UUID uuidEmprestimo) {
        return ResponseEntity.ok().body(emprestimoService.updateEmprestimo(atualizarEmprestimoDTO, uuidEmprestimo));
    }

    @DeleteMapping("/excluir/{uuidEmprestimo}")
    public ResponseEntity<String> deleteEmprestimo(@PathVariable UUID uuidEmprestimo) {
        emprestimoService.deleteEmprestimo(uuidEmprestimo);
        return ResponseEntity.ok().body("Empréstimo excluído com sucesso");
    }
}
