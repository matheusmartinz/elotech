package com.mth.library.usuario;

import com.mth.library.dataprovider.UsuarioDataProvider;
import com.mth.library.dto.UsuarioDTO;
import com.mth.library.exceptions.FailedConditional;
import com.mth.library.model.Usuario;
import com.mth.library.repository.UsuarioRepository;
import com.mth.library.service.UsuarioService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioServiceIT {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void createUsuario() {
        UsuarioDTO usuarioDTO = UsuarioDataProvider.createUsuarioDefault("create@gmail.com");
        long before = usuarioRepository.count();
        UsuarioDTO toResponse = usuarioService.createUsuario(usuarioDTO);
        long after = usuarioRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(toResponse.getUuid()).isNotNull();
            s.assertThat(toResponse.getEmail()).contains("@");
            s.assertThat(toResponse.getDataCadastro()).isEqualTo(LocalDate.now());
            s.assertThat(toResponse.getTelefone()).isEqualTo("44990009871");
            s.assertThat(after).isEqualTo(before + 1);
        });
    }

    @Test
    public void dateIsAfterNow() {
        UsuarioDTO usuarioDTO = UsuarioDataProvider.createUsuarioCustom(null, LocalDate.now().plusDays(1),"Pedro Silva" ,"teste@gmail.com", "44990008888");

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> usuarioService.createUsuario(usuarioDTO))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("A data de cadastro não pode ser maior que o dia atual.");
        });
    }

    @Test
    public void getAllUsuarios() {
        long before = usuarioRepository.count();
        UsuarioDTO usuarioDTO1 = UsuarioDataProvider.createUsuarioDefault("getAll@gmail.com");
        usuarioService.createUsuario(usuarioDTO1);
        UsuarioDTO usuarioDTO2 = UsuarioDataProvider.createUsuarioCustom(null, LocalDate.now(), "Roberto","teste@gmail.com", "(44)990669876");
        usuarioService.createUsuario(usuarioDTO2);
        long after = usuarioRepository.count();

        List<UsuarioDTO> usuarios = usuarioService.findAllUsuarios();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 2);
            s.assertThat(usuarioDTO2).isNotEqualTo(usuarioDTO1);
            s.assertThat(usuarios.size()).isGreaterThan(1);
        });
    }

    @Test
    public void updateUsuario() {
        long before = usuarioRepository.count();
        UsuarioDTO usuarioDTO = UsuarioDataProvider.createUsuarioDefault("update@gmail.com");
        UsuarioDTO usuarioSaved = usuarioService.createUsuario(usuarioDTO);
        long afterSaved = usuarioRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(afterSaved).isEqualTo(before + 1);
            s.assertThat(usuarioDTO.getEmail()).contains("@");
            s.assertThat(usuarioDTO.getDataCadastro()).isEqualTo(LocalDate.now());
            s.assertThat(usuarioDTO.getTelefone()).isNotNull();
        });

        UsuarioDTO usuarioUpdate = UsuarioDataProvider.createUsuarioCustom(usuarioSaved.getUuid(), LocalDate.now(), "Cleiton","afterUpdate@gmail.com", "44990997654");
        UsuarioDTO responseUpdate = usuarioService.updateUsuario(usuarioUpdate, usuarioUpdate.getUuid());
        long afterUpdate = usuarioRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(responseUpdate.getUuid()).isEqualTo(usuarioSaved.getUuid());
            s.assertThat(afterUpdate).isEqualTo(afterSaved);
            s.assertThat(responseUpdate.getEmail()).isNotEqualTo(usuarioSaved.getEmail());
            s.assertThat(responseUpdate.getTelefone()).isNotEqualTo(usuarioSaved.getTelefone());
        });
    }

    @Test
    public void deleteUsuario() {
        UsuarioDTO usuarioDTO = UsuarioDataProvider.createUsuarioDefault("delete@gmail.com");

        UsuarioDTO usuarioSaved = usuarioService.createUsuario(usuarioDTO);


        SoftAssertions.assertSoftly(s -> {
            s.assertThat(usuarioSaved.getUuid()).isNotNull();
        });

        usuarioService.deleteUsuario(usuarioSaved.getUuid());

        Usuario usuarioNotExist = usuarioRepository.findByUuid(usuarioSaved.getUuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(usuarioNotExist).isNull();
        });
    }
}
