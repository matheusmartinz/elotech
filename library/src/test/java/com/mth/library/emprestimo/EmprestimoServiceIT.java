package com.mth.library.emprestimo;

import com.mth.library.dataprovider.EmprestimoDataProvider;
import com.mth.library.dataprovider.LivroDataProvider;
import com.mth.library.dataprovider.UsuarioDataProvider;
import com.mth.library.dto.AtualizarEmprestimoDTO;
import com.mth.library.dto.EmprestimoDTO;
import com.mth.library.dto.LivroDTO;
import com.mth.library.dto.UsuarioDTO;
import com.mth.library.exceptions.FailedConditional;
import com.mth.library.model.Status;
import com.mth.library.repository.EmprestimoRepository;
import com.mth.library.service.EmprestimoService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class EmprestimoServiceIT {

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private LivroDataProvider livroDataProvider;

    @Test
    public void createEmprestimo() {
        LivroDTO livroCreated = livroDataProvider.createAndSave(null, "Guerra Estelar", "Mth", "0-7207-1596-2", LocalDate.now(), "Ficção");
        UsuarioDTO usuarioCreated = usuarioDataProvider.createAndSave(null, LocalDate.now(), "Lucas", "emprestimoCreate@gmail.com", "44991330998");

        EmprestimoDTO emprestimoDTO = EmprestimoDataProvider.createEmprestimoCustom(null, usuarioCreated, livroCreated,
                LocalDate.now().minusDays(1), null, Status.ATIVO);

        long before = emprestimoRepository.count();
        EmprestimoDTO emprestimoSaved = emprestimoService.createEmprestimo(emprestimoDTO);
        long after = emprestimoRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 1);
            s.assertThat(emprestimoSaved.getStatus()).isEqualTo(Status.ATIVO);
            s.assertThat(emprestimoSaved.getUsuarioDTO().getUuid()).isEqualTo(usuarioCreated.getUuid());
            s.assertThat(emprestimoSaved.getLivroDTO().getUuid()).isEqualTo(livroCreated.getUuid());
            s.assertThat(emprestimoSaved.getDataEmprestimo()).isNotEqualTo(LocalDate.now());
        });
    }

    @Test
    public void createAfterDateNow() {
        UsuarioDTO usuarioCreated = usuarioDataProvider.createAndSave(null, LocalDate.now(), "Lucas", "createDateFailed@gmail.com", "44991330998");
        LivroDTO livroCreated = livroDataProvider.createAndSave(null, "Guerra Estelar", "Mth", "0-7207-8896-2", LocalDate.now(), "Ficção");

        EmprestimoDTO emprestimoDTO = EmprestimoDataProvider.createEmprestimoCustom(null, usuarioCreated, livroCreated,
                LocalDate.now().plusDays(1), null, Status.ATIVO);

       SoftAssertions.assertSoftly(s -> {
           s.assertThatThrownBy(() -> emprestimoService.createEmprestimo(emprestimoDTO))
                   .isInstanceOf(FailedConditional.class)
                   .hasMessage("Data de empréstimo não pode ser maior que o dia atual.");
       });
    }

    @Test
    public void emprestimoMesmoLivro() {
        UsuarioDTO usuario1 = usuarioDataProvider.createAndSave(null, LocalDate.now(), "Lucas", "lucas@gmail.com", "44991330998");
        UsuarioDTO usuario2 = usuarioDataProvider.createAndSave(null, LocalDate.now(), "Carla", "carla@gmail.com", "44991330977");

        LivroDTO livroCreated = livroDataProvider.createAndSave(null, "Guerra Estelar", "Mth", "0-7207-5696-2", LocalDate.now(), "Ficção");

        EmprestimoDTO emprestimoDTO = EmprestimoDataProvider.createEmprestimoCustom(null, usuario1, livroCreated,
                LocalDate.now().minusDays(1), null, Status.ATIVO);
        emprestimoService.createEmprestimo(emprestimoDTO);

        EmprestimoDTO emprestimoMesmoLivro = EmprestimoDataProvider.createEmprestimoCustom(null, usuario2, livroCreated,
                LocalDate.now(), null, Status.ATIVO);

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> emprestimoService.createEmprestimo(emprestimoMesmoLivro))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("Livro emprestado.");
        });
    }

    @Test
    public void getAllEmprestimos() {
        emprestimoRepository.deleteAll();
        UsuarioDTO usuarioCreated = usuarioDataProvider.createAndSave(null, LocalDate.now(), "Lucas", "emprestimogetall@gmail.com", "44991330998");
        LivroDTO livroCreated1 = livroDataProvider.createAndSave(null, "Guerra Estelar", "Mth", "0-0097-1596-2", LocalDate.now(), "Ficção");
        LivroDTO livroCreated2 = livroDataProvider.createAndSave(null, "A Culpa é das estrelas", "Mth", "0-8807-1596-9", LocalDate.now().minusYears(5), "Drama");

        EmprestimoDTO emprestimoDTO1 = EmprestimoDataProvider.createEmprestimoCustom(null, usuarioCreated, livroCreated1,
                LocalDate.now().minusDays(10), null, Status.ATIVO);

        EmprestimoDTO emprestimoDTO2 = EmprestimoDataProvider.createEmprestimoCustom(null, usuarioCreated, livroCreated2,
                LocalDate.now(), null, Status.ATIVO);

        EmprestimoDTO emprestimo1 = emprestimoService.createEmprestimo(emprestimoDTO1);
        EmprestimoDTO emprestimo2 = emprestimoService.createEmprestimo(emprestimoDTO2);

        List<EmprestimoDTO> emprestimos = emprestimoService.getAllEmprestimos();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(emprestimos.size()).isEqualTo(2);
            s.assertThat(emprestimo1.getUsuarioDTO().getUuid()).isEqualTo(usuarioCreated.getUuid());
            s.assertThat(emprestimo2.getUsuarioDTO().getUuid()).isEqualTo(usuarioCreated.getUuid());
        });
    }

    @Test
    public void updateEmprestimo() {
        emprestimoRepository.deleteAll();
        UsuarioDTO usuarioCreated = usuarioDataProvider.createAndSave(null, LocalDate.now(), "Lucas", "emprestimoUpdate@gmail.com", "44991330998");
        LivroDTO livroCreated = livroDataProvider.createAndSave(null, "A Culpa é das estrelas", "Mth", "0-7207-1596-9", LocalDate.now().minusYears(5), "DRAMA");

        EmprestimoDTO emprestimoDTO = EmprestimoDataProvider.createEmprestimoCustom(null, usuarioCreated, livroCreated,
                LocalDate.now().minusDays(10), null, Status.ATIVO);

        long before = emprestimoRepository.count();
        EmprestimoDTO emprestimo = emprestimoService.createEmprestimo(emprestimoDTO);
        long afterCreated = emprestimoRepository.count();

        AtualizarEmprestimoDTO atualizarEmprestimoDTO = new AtualizarEmprestimoDTO();
        atualizarEmprestimoDTO.setStatus(Status.INATIVO);
        atualizarEmprestimoDTO.setDataDevolucao(LocalDate.now());

        EmprestimoDTO emprestimoUpdate = emprestimoService.updateEmprestimo(atualizarEmprestimoDTO, emprestimo.getUuid());
        long afterUpdate = emprestimoRepository.count();

        SoftAssertions.assertSoftly(s -> {
           s.assertThat(emprestimoUpdate.getStatus()).isNotEqualTo(emprestimo.getStatus());
           s.assertThat(emprestimoUpdate.getStatus()).isEqualTo(Status.INATIVO);
           s.assertThat(emprestimoUpdate.getUsuarioDTO().getUuid()).isEqualTo(usuarioCreated.getUuid());
           s.assertThat(emprestimoUpdate.getLivroDTO().getUuid()).isEqualTo(livroCreated.getUuid());
           s.assertThat(afterCreated).isEqualTo(before + 1);
            s.assertThat(afterUpdate).isEqualTo(afterCreated);
        });
    }

    @Test
    public void deleteEmprestimo() {
        emprestimoRepository.deleteAll();
        UsuarioDTO usuarioCreated = usuarioDataProvider.createAndSave(null, LocalDate.now(), "Lucas", "emprestimo.delete@gmail.com", "44991330998");
        LivroDTO livroCreated1 = livroDataProvider.createAndSave(null, "Guerra Estelar", "Mth", "0-0007-1596-2", LocalDate.now(), "Ficção");

        EmprestimoDTO emprestimoDTO = EmprestimoDataProvider.createEmprestimoCustom(null, usuarioCreated, livroCreated1,
                LocalDate.now().minusDays(10), null, Status.ATIVO);
        EmprestimoDTO emprestimo = emprestimoService.createEmprestimo(emprestimoDTO);
        long beforeDeleted = emprestimoRepository.count();

        emprestimoService.deleteEmprestimo(emprestimo.getUuid());
        long afterDeleted = emprestimoRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(afterDeleted).isNotEqualTo(beforeDeleted);
        });
    }

}
