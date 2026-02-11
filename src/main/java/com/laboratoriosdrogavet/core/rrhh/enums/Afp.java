package com.laboratoriosdrogavet.core.rrhh.enums;

public enum Afp {
    INTEGRA("AFP Integra"),
    PRIMA("AFP Prima"),
    HABITAT("AFP Habitat"),
    PROFUTURO("AFP Profuturo");

    private final String descripcion;

    Afp(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

