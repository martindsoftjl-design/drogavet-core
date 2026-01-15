package com.laboratoriosdrogavet.core.rrhh.enums;

public enum SistemaPension {
	
	AFP("AFP"),
	ONP("ONP");
	
	private final String descripcion;
	
	SistemaPension(String descripcion) {
		
		this.descripcion = descripcion;
		
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	
	
}
