package com.laboratoriosdrogavet.core.rrhh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laboratoriosdrogavet.core.rrhh.model.Area;

public interface AreaRepository extends JpaRepository<Area, Long>{
	
	Optional<Area> findById(Long id);

}
