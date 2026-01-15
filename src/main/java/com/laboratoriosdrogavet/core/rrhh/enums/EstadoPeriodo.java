package com.laboratoriosdrogavet.core.rrhh.enums;

public enum EstadoPeriodo {

    ACTIVO("Activo"),
    CERRADO("Cerrado");

    private final String descripcion;

    EstadoPeriodo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

