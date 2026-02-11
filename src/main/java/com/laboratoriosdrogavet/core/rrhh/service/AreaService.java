package com.laboratoriosdrogavet.core.rrhh.service;

import java.util.List;

import com.laboratoriosdrogavet.core.rrhh.model.Area;

public interface AreaService {
	
	List<Area> ListarTodas();
	Area Guardar(Area area);
	Area Actualizar(Long id, Area area);
	void Eliminar(Long id);
	Area obtenerPorId(Long id);


}
