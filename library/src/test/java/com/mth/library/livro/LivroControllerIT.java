package com.mth.library.livro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mth.library.dataprovider.LivroDataProvider;
import com.mth.library.dto.LivroDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LivroControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void tituloIsNull() throws Exception {
        LivroDTO livroResponse = LivroDataProvider.createLivroCustom(null, null, "mth", "0-1519-7138-2", LocalDate.now(), "Aventura");

        String response = objectMapper.writeValueAsString(livroResponse);

        mockMvc.perform(post("/livro/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório inserir título."));
    }

    @Test
    public void autorIsNull() throws Exception {
        LivroDTO livroResponse = LivroDataProvider.createLivroCustom(null, "Roben Hood", null, "0-1519-7138-2", LocalDate.now(), "Aventura");

        String response = objectMapper.writeValueAsString(livroResponse);

        mockMvc.perform(post("/livro/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório inserir autor."));
    }

    @Test
    public void IsbnIsNull() throws Exception {
        LivroDTO livroResponse = LivroDataProvider.createLivroCustom(null, "Roben Hood", "Mth", null, LocalDate.now(), "Aventura");

        String response = objectMapper.writeValueAsString(livroResponse);

        mockMvc.perform(post("/livro/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ISBN é obrigatório."));
    }

    @Test
    public void IsbnIsInvalid() throws Exception {
        LivroDTO livroResponse = LivroDataProvider.createLivroCustom(null, "Roben Hood", "Mth", "0-1519-7138-299", LocalDate.now(), "Aventura");

        String response = objectMapper.writeValueAsString(livroResponse);

        mockMvc.perform(post("/livro/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("ISBN inválido."));
    }

    @Test
    public void DateIsNull() throws Exception {
        LivroDTO livroResponse = LivroDataProvider.createLivroCustom(null, "Roben Hood", "Mth", "0-1519-7138-2", null, "Aventura");

        String response = objectMapper.writeValueAsString(livroResponse);

        mockMvc.perform(post("/livro/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório inserir data de publicação."));
    }

    @Test
    public void categoriaIsNull() throws Exception {
        LivroDTO livroResponse = LivroDataProvider.createLivroCustom(null, "Roben Hood", "Mth", "0-1519-7138-2", LocalDate.now(), null);

        String response = objectMapper.writeValueAsString(livroResponse);

        mockMvc.perform(post("/livro/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório inserir categoria."));
    }
}
