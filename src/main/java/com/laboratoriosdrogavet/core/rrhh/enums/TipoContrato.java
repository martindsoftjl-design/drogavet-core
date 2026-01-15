package com.laboratoriosdrogavet.core.rrhh.enums;

public enum TipoContrato {

    INDETERMINADO("Indeterminado"),
    PLAZO_FIJO("Plazo fijo"),
    PRACTICAS("Prácticas"),
    LOCACION_SERVICIOS("Locación de servicios");

    private final String label;

    TipoContrato(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
