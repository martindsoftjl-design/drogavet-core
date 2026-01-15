package com.laboratoriosdrogavet.core.rrhh.enums;

public enum SituacionLaboral {
	
	NINGUNA("Ninguna"),
    CONFIANZA("Confianza"),
    ADMINISTRATIVO("Administrativo"),
    OPERATIVO("Operativo"),
    PRACTICANTE("Practicante"),
    TEMPORADA("Temporada");
	
	private final String descripcion;
	
	SituacionLaboral(String descripcion){
		this.descripcion=descripcion;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
}
