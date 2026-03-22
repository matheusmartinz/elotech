package com.mth.library.dataprovider;

import com.mth.library.dto.UsuarioDTO;
import com.mth.library.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class UsuarioDataProvider {

    @Autowired
    private UsuarioService usuarioService;

    public static UsuarioDTO createUsuarioDefault(String email) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUuid(null);
        usuarioDTO.setDataCadastro(LocalDate.now());
        usuarioDTO.setNome("Jorge");
        usuarioDTO.setEmail(email);
        usuarioDTO.setTelefone("44990009871");
        return usuarioDTO;
    }

    public static UsuarioDTO createUsuarioCustom(UUID uuid, LocalDate dataCadastro, String nome, String email, String telefone) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUuid(uuid);
        usuarioDTO.setDataCadastro(dataCadastro);
        usuarioDTO.setNome(nome);
        usuarioDTO.setEmail(email);
        usuarioDTO.setTelefone(telefone);
        return usuarioDTO;
    }

    public UsuarioDTO createAndSave(UUID uuid, LocalDate dataCadastro, String nome, String email, String telefone) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setUuid(uuid);
        usuarioDTO.setDataCadastro(dataCadastro);
        usuarioDTO.setNome(nome);
        usuarioDTO.setEmail(email);
        usuarioDTO.setTelefone(telefone);

        return usuarioService.createUsuario(usuarioDTO);
    }

}
