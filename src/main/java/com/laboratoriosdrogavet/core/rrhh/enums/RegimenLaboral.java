package com.laboratoriosdrogavet.core.rrhh.enums;

public enum RegimenLaboral {

    GENERAL("Régimen General"),
    MYPE("MYPE"),
    CAS("CAS");

    private final String label;

    RegimenLaboral(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
