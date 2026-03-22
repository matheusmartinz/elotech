package com.mth.library.restcontroller;

import com.mth.library.dto.UsuarioDTO;
import com.mth.library.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("usuario")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.createUsuario(usuarioDTO));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        return ResponseEntity.ok().body(usuarioService.findAllUsuarios());
    }

    @PutMapping("/editar/{uuidUsuario}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO, @PathVariable UUID uuidUsuario) {
        return ResponseEntity.ok().body(usuarioService.updateUsuario(usuarioDTO, uuidUsuario));
    }

    @DeleteMapping("/excluir/{uuidUsuario}")
    public ResponseEntity<String> deleteUsuario(@PathVariable UUID uuidUsuario) {
        usuarioService.deleteUsuario(uuidUsuario);
        return ResponseEntity.ok().body("Usuario deletado com sucesso");
    }

}
