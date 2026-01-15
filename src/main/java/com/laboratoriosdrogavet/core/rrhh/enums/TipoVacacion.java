package com.laboratoriosdrogavet.core.rrhh.enums;

public enum TipoVacacion {

    NORMAL("Normal"),
    URGENTE("Urgente"),
    ACUMULADA("Acumulada"),
    ESPECIAL("Especial RRHH");

    private final String descripcion;

    TipoVacacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

