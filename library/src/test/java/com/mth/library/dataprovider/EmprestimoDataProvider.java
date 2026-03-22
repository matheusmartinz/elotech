package com.mth.library.dataprovider;

import com.mth.library.dto.EmprestimoDTO;
import com.mth.library.dto.LivroDTO;
import com.mth.library.dto.UsuarioDTO;
import com.mth.library.model.Status;

import java.time.LocalDate;
import java.util.UUID;

public class EmprestimoDataProvider {


    public static EmprestimoDTO createEmprestimoCustom(UUID uuid, UsuarioDTO usuarioDTO, LivroDTO livroDTO, LocalDate dataEmprestimo, LocalDate dataDevolucao, Status status) {
        EmprestimoDTO emprestimoDTO = new EmprestimoDTO();
        emprestimoDTO.setUuid(uuid);
        emprestimoDTO.setUsuarioDTO(usuarioDTO);
        emprestimoDTO.setLivroDTO(livroDTO);
        emprestimoDTO.setDataEmprestimo(dataEmprestimo);
        emprestimoDTO.setDataDevolucao(dataDevolucao);
        emprestimoDTO.setStatus(status);
        return emprestimoDTO;
    }
}
