package com.mth.library.model;

import com.mth.library.dto.UsuarioDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @NotBlank(message = "Obrigatório inserir nome.")
    private String nome;

    @NotBlank(message = "Obrigatório inserir email.")
    @Email(message = "Formato de email incorreto.")
    private String email;

    @NotNull(message = "Obrigatório inserir data de cadastro.")
    private LocalDate dataCadastro;

    @NotBlank(message = "Obrigatório inserir telefone.")
    @Pattern(
            regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$",
            message = "Telefone inválido."
    )
    private String telefone;

    public Usuario(UsuarioDTO usuarioDTO) {
        this.uuid = UUID.randomUUID();
        this.nome = usuarioDTO.getNome();
        this.email = usuarioDTO.getEmail();
        this.dataCadastro = usuarioDTO.getDataCadastro();
        this.telefone = usuarioDTO.getTelefone();
    }
    public void updateUsuario(UsuarioDTO usuarioDTO) {
        this.nome = usuarioDTO.getNome();
        this.email = usuarioDTO.getEmail();
        this.dataCadastro = usuarioDTO.getDataCadastro();
        this.telefone = usuarioDTO.getTelefone();
    }
}
