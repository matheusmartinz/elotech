package com.mth.library.service;

import com.mth.library.dto.UsuarioDTO;
import com.mth.library.exceptions.FailedConditional;
import com.mth.library.exceptions.IsNull;
import com.mth.library.model.Usuario;
import com.mth.library.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getDataCadastro().isAfter(LocalDate.now())) {
            throw new FailedConditional("A data de cadastro não pode ser maior que o dia atual.");
        }
        if(usuarioRepository.existsUsuarioByEmail(usuarioDTO.getEmail())){
            throw new FailedConditional("Email em uso.");
        }
        Usuario usuario = new Usuario(usuarioDTO);
        return new UsuarioDTO(usuarioRepository.save(usuario));
    }

    public List<UsuarioDTO> findAllUsuarios() {
        return usuarioRepository.findAll().stream().map(UsuarioDTO::new).toList();
    }

    public UsuarioDTO updateUsuario(UsuarioDTO usuarioDTO, UUID uuidUsuario) {
        Usuario usuario = usuarioRepository.findByUuid(uuidUsuario);
        if (usuario == null) {
            throw new IsNull("Usuário não encontrado.");
        }
        usuario.updateUsuario(usuarioDTO);
        return new UsuarioDTO(usuarioRepository.save(usuario));
    }

    public void deleteUsuario(UUID uuidUsuario) {
        Usuario usuario = usuarioRepository.findByUuid(uuidUsuario);
        if (usuario == null) {
            throw new IsNull("Usuário não encontrado.");
        }
        usuarioRepository.delete(usuario);
    }
}
