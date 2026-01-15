package com.laboratoriosdrogavet.core.rrhh.enums;

public enum Cargo {
	
	OPERARIO("Operario"),
	ASISTENTE("Asistente"),
	ANALISTA("Analista"),
	AUXILIAR("Auxiliar"),
	GERENTE("Gerente"),
	DIRECTOR("Director"),
	CONTADOR("Contador"),
	JEFE_DE_OPERACIONES("Jefe de Operaciones");
	
	private final String descripcion;
	
	Cargo(String descripcion){
		this.descripcion=descripcion;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
}
