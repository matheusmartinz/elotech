package com.mth.library.emprestimo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mth.library.dataprovider.EmprestimoDataProvider;
import com.mth.library.dataprovider.LivroDataProvider;
import com.mth.library.dataprovider.UsuarioDataProvider;
import com.mth.library.dto.EmprestimoDTO;
import com.mth.library.dto.LivroDTO;
import com.mth.library.dto.UsuarioDTO;
import com.mth.library.repository.EmprestimoRepository;
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
public class EmprestimoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private LivroDataProvider livroDataProvider;

    @Test
    public void statusNull() throws Exception {
        emprestimoRepository.deleteAll();
        UsuarioDTO usuarioCreated = usuarioDataProvider.createAndSave(null, LocalDate.now(), "Lucas", "statusNull@gmail.com", "44991330998");
        LivroDTO livroCreated = livroDataProvider.createAndSave(null, "Guerra Estelar", "Mth", "9-7207-1596-2", LocalDate.now(), "Ficção");

        EmprestimoDTO emprestimoDTO = EmprestimoDataProvider.createEmprestimoCustom(null, usuarioCreated, livroCreated,
                LocalDate.now(), null, null);

        String response = objectMapper.writeValueAsString(emprestimoDTO);

        mockMvc.perform(post("/emprestimo/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório informar status do empréstimo."));
    }

}
