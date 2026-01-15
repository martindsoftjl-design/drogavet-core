package com.laboratoriosdrogavet.core.rrhh.service;

import java.util.List;

import com.laboratoriosdrogavet.core.rrhh.model.Empresa;

public interface EmpresaService{
	
	List<Empresa> ListarTodas();
	Empresa Guardar(Empresa empresa);
	Empresa Actualizar(Long id, Empresa empresa);
	Empresa ObtenerPorId(Long id);
	void Eliminar(Long id);
	Boolean EmpresaExiste(String ruc);	

}
