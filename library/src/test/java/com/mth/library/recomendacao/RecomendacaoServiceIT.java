package com.mth.library.recomendacao;

import com.mth.library.dataprovider.EmprestimoDataProvider;
import com.mth.library.dataprovider.LivroDataProvider;
import com.mth.library.dataprovider.UsuarioDataProvider;
import com.mth.library.dto.EmprestimoDTO;
import com.mth.library.dto.LivroDTO;
import com.mth.library.dto.UsuarioDTO;
import com.mth.library.model.Livro;
import com.mth.library.model.Status;
import com.mth.library.repository.LivroRepository;
import com.mth.library.service.EmprestimoService;
import com.mth.library.service.RecomendacaoLivroService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class RecomendacaoServiceIT {

    @Autowired
    private RecomendacaoLivroService recomendacaoLivroService;

    @Autowired
    private LivroDataProvider livroDataProvider;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private LivroRepository livroRepository;

    @Test
    public void callRecomendacaoService() {
        livroRepository.deleteAll();
        UsuarioDTO usuarioCreated = usuarioDataProvider.createAndSave(null, LocalDate.now(), "Gisele", "recomendacao@gmail.com", "44991330998");
        LivroDTO livro1 = livroDataProvider.createAndSave(null, "Alienígenas", "Mth", "0-7207-9996-2", LocalDate.now(), "Ficção");
        livroDataProvider.createAndSave(null, "Guerra nas estrelas", "Mth", "0-7207-1096-2", LocalDate.now(), "Ficção");
        livroDataProvider.createAndSave(null, "Persy Jackson", "Mth", "0-7207-4596-2", LocalDate.now(), "Ficção");
        livroDataProvider.createAndSave(null, "Harry Potter", "Mth", "0-7207-2896-2", LocalDate.now(), "Ficção");

        EmprestimoDTO emprestimoDTO = EmprestimoDataProvider.createEmprestimoCustom(null, usuarioCreated, livro1,
                LocalDate.now().minusDays(1), null, Status.ATIVO);

        emprestimoService.createEmprestimo(emprestimoDTO);

        List<Livro> recomendacaoLivros = recomendacaoLivroService.findRecomendacao(usuarioCreated.getUuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(recomendacaoLivros.size()).isEqualTo(3);
            s.assertThat(recomendacaoLivros.get(0).getTitulo()).isEqualTo("Guerra nas estrelas");
            s.assertThat(recomendacaoLivros.get(1).getTitulo()).isEqualTo("Persy Jackson");
            s.assertThat(recomendacaoLivros.get(2).getTitulo()).isEqualTo("Harry Potter");
        });
    }
}
