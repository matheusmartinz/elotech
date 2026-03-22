package com.mth.library.livro;

import com.mth.library.dataprovider.LivroDataProvider;
import com.mth.library.dto.LivroDTO;
import com.mth.library.exceptions.FailedConditional;
import com.mth.library.repository.LivroRepository;
import com.mth.library.service.LivroService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class LivroServiceIT {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LivroService livroService;

    @Test
    public void createLivro() {
        long before = livroRepository.count();
        LivroDTO livroDTO = LivroDataProvider.createLivroDefault();
        LivroDTO response = livroService.createLivro(livroDTO);
        long after = livroRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 1);
            s.assertThat(response.getUuid()).isNotNull();
            s.assertThat(response.getDataPublicacao()).isNotNull();
            s.assertThat(response.getIsbn()).matches("^(\\d{9}[\\dX]|\\d{13}|\\d{1,5}-\\d{1,7}-\\d{1,7}-[\\dX])$");
            s.assertThat(response.getTitulo()).isNotBlank();
            s.assertThat(response.getAutor()).isEqualTo("Mth");
            s.assertThat(response.getCategoria()).isEqualTo(livroDTO.getCategoria().toLowerCase());
        });
    }

    @Test
    public void createLivroSameIsbn() {
        LivroDTO livroDTO1 = LivroDataProvider.createLivroCustom(null, "Nemo", "Mth", "0-5179-2676-9", LocalDate.now(), "Romance");
        livroService.createLivro(livroDTO1);

        LivroDTO responseSameIsbn = LivroDataProvider.createLivroCustom(null, "Ana Belle", "Mth", "0-5179-2676-9", LocalDate.now(), "Terror");

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> livroService.createLivro(responseSameIsbn))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("ISBN já cadastrado.");
        });
    }

    @Test
    public void getAllLivros() {
        livroRepository.deleteAll();
        long before = livroRepository.count();
        LivroDTO livroDTO = LivroDataProvider.createLivroDefault();
        livroService.createLivro(livroDTO);

        LivroDTO livroDTO2 = LivroDataProvider.createLivroCustom(null, "Mundo de Bob", "Mth", "0-9999-2676-9", LocalDate.now(), "Infantil");
        livroService.createLivro(livroDTO2);
        long after = livroRepository.count();

        List<LivroDTO> listagemLivros = livroService.findAll();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 2);
            s.assertThat(listagemLivros.size()).isEqualTo(2);
        });
    }

    @Test
    public void updateLivro() {
        livroRepository.deleteAll();
        long before = livroRepository.count();
        LivroDTO livroDTO1 = LivroDataProvider.createLivroCustom(null, "Nemo", "Mth", "0-5179-2676-9", LocalDate.now(), "Romance");
        LivroDTO livroSaved = livroService.createLivro(livroDTO1);
        long afterSaved = livroRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(afterSaved).isEqualTo(before + 1);
            s.assertThat(livroSaved.getUuid()).isNotNull();
        });

        LivroDTO livroDTO2 = LivroDataProvider.createLivroCustom(livroSaved.getUuid(), "Batman", "Bth", "0-5179-4076-9", LocalDate.now().plusDays(2), "Suspense");
        LivroDTO livroResponse2 = livroService.updateLivro(livroDTO2, livroSaved.getUuid());
        long afterUpdated = livroRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(afterUpdated).isEqualTo(afterSaved);
            s.assertThat(livroResponse2.getUuid()).isEqualTo(livroSaved.getUuid());
            s.assertThat(livroResponse2.getTitulo()).isNotEqualTo(livroSaved.getTitulo());
            s.assertThat(livroResponse2.getAutor()).isNotEqualTo(livroSaved.getAutor());
            s.assertThat(livroSaved.getDataPublicacao()).isEqualTo(livroResponse2.getDataPublicacao().minusDays(2));
        });
    }

    @Test
    public void deleteLivro() {
        livroRepository.deleteAll();
        long before = livroRepository.count();
        LivroDTO livroDTO = LivroDataProvider.createLivroDefault();
        LivroDTO livroSaved = livroService.createLivro(livroDTO);
        long after = livroRepository.count();

        livroService.deleteLivro(livroSaved.getUuid());
        long afterDeleted = livroRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 1);
            s.assertThat(afterDeleted).isEqualTo(0);
        });
    }
}
