package com.laboratoriosdrogavet.core.rrhh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laboratoriosdrogavet.core.rrhh.model.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>{
	
	
}
