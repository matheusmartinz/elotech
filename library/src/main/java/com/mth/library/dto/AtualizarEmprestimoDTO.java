package com.mth.library.dto;

import com.mth.library.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AtualizarEmprestimoDTO {
    private LocalDate dataDevolucao;
    private Status status;
}
