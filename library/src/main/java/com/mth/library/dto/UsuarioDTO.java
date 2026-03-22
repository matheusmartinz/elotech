package com.mth.library.dto;

import com.mth.library.model.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {

    private UUID uuid;

    private String nome;

    private String email;

    private LocalDate dataCadastro;

    private String telefone;

    public UsuarioDTO(Usuario usuario) {
        this.uuid = usuario.getUuid();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.dataCadastro = usuario.getDataCadastro();
        this.telefone = usuario.getTelefone();
    }
}
