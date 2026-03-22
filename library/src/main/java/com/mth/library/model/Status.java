package com.mth.library.model;

public enum Status {
    ATIVO("ATIVO"),
    INATIVO("INATIVO");

    private String descricao;

    Status(String descricao) {
        this.descricao = descricao;
    }
}
