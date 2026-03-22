package com.mth.library.usuario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mth.library.dataprovider.UsuarioDataProvider;
import com.mth.library.dto.UsuarioDTO;
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
public class UsuarioControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void nomeIsNull() throws Exception {
        UsuarioDTO usuarioDTO = UsuarioDataProvider.createUsuarioCustom(null, LocalDate.now(),null,"teste2@gmail.com", "44990008888");

        String response = objectMapper.writeValueAsString(usuarioDTO);

        mockMvc.perform(post("/usuario/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório inserir nome."));
    }

    @Test
    public void emailInvalid() throws Exception {
        UsuarioDTO usuarioDTO = UsuarioDataProvider.createUsuarioCustom(null, LocalDate.now(),"Kaiky","teste.com", "44990008888");

        String response = objectMapper.writeValueAsString(usuarioDTO);

        mockMvc.perform(post("/usuario/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Formato de email incorreto."));
    }

    @Test
    public void emailIsNull() throws Exception {
        UsuarioDTO usuarioDTO = UsuarioDataProvider.createUsuarioCustom(null, LocalDate.now(),"Felipe Ribeiro" ,null, "44990008888");

        String response = objectMapper.writeValueAsString(usuarioDTO);

        mockMvc.perform(post("/usuario/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório inserir email."));
    }

    @Test
    public void emailIsEmpty() throws Exception {
        UsuarioDTO usuarioDTO = UsuarioDataProvider.createUsuarioCustom(null, LocalDate.now(), "Silva","", "44990008888");

        String response = objectMapper.writeValueAsString(usuarioDTO);

        mockMvc.perform(post("/usuario/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório inserir email."));
    }

    @Test
    public void telefoneIsNull() throws Exception {
        UsuarioDTO usuarioDTO = UsuarioDataProvider.createUsuarioCustom(null, LocalDate.now(), "Jorge","teste3@gmail.com", null);

        String response = objectMapper.writeValueAsString(usuarioDTO);

        mockMvc.perform(post("/usuario/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório inserir telefone."));
    }

    @Test
    public void telefoneInvalid() throws Exception {
        UsuarioDTO usuarioDTO = UsuarioDataProvider.createUsuarioCustom(null, LocalDate.now(), "Jorge","teste4@gmail.com", "123");

        String response = objectMapper.writeValueAsString(usuarioDTO);

        mockMvc.perform(post("/usuario/cadastro").contentType(MediaType.APPLICATION_JSON).content(response).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Telefone inválido."));
    }

}
