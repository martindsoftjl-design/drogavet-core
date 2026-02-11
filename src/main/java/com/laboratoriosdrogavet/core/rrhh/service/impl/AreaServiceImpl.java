package com.laboratoriosdrogavet.core.rrhh.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.laboratoriosdrogavet.core.rrhh.model.Area;
import com.laboratoriosdrogavet.core.rrhh.repository.AreaRepository;
import com.laboratoriosdrogavet.core.rrhh.service.AreaService;

@Service
public class AreaServiceImpl implements AreaService{
	
	private final AreaRepository areaRepository;
	
	public AreaServiceImpl(AreaRepository areaRepository) {
		this.areaRepository=areaRepository;
	}

	@Override
	public List<Area> ListarTodas() {
		// TODO Auto-generated method stub
		return areaRepository.findAll();
	}

	@Override
	public Area Guardar(Area area) {
		// TODO Auto-generated method stub
		return areaRepository.save(area);
	}

	@Override
	public void Eliminar(Long id) {
		
		areaRepository.deleteById(id);;
		
	}

	@Override
	public Area obtenerPorId(Long id) {
	    return areaRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Área no encontrada"));
	}



	@Override
	public Area Actualizar(Long id, Area area) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
