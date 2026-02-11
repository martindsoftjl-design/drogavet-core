package com.laboratoriosdrogavet.core.rrhh.enums;

public enum Banco {

    BCP("Banco de Crédito"),
    BBVA("BBVA"),
    SCOTIABANK("Scotiabank"),
    INTERBANK("Interbank");

    private final String descripcion;

    Banco(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

