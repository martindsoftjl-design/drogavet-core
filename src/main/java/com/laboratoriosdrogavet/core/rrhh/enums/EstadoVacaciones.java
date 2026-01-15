package com.laboratoriosdrogavet.core.rrhh.enums;

public enum EstadoVacaciones {

    PENDIENTE("Pendiente"),
    APROBADO("Aprobado"),
    RECHAZADO("Rechazado");

    private final String descripcion;

    EstadoVacaciones(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

