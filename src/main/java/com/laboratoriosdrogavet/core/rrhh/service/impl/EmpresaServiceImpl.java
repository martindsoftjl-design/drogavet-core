package com.laboratoriosdrogavet.core.rrhh.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.laboratoriosdrogavet.core.rrhh.model.Empresa;
import com.laboratoriosdrogavet.core.rrhh.repository.EmpresaRepository;
import com.laboratoriosdrogavet.core.rrhh.service.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService{
	
	private final EmpresaRepository empresaRepository;
	
	public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
		this.empresaRepository=empresaRepository;
	}

	@Override
	public List<Empresa> ListarTodas() {
		
		return empresaRepository.findAll();
	}

	@Override
	public Empresa Guardar(Empresa empresa) {
		
		return empresaRepository.save(empresa);
	}

	@Override
	public Empresa Actualizar(Long id, Empresa empresa) {
		
		return null;
	}

	@Override
	public Empresa ObtenerPorId(Long id) {
		
		return empresaRepository.findById(id)
				.orElseThrow(()-> new RuntimeException("Empresa no encontrada"));
	}

	@Override
	public void Eliminar(Long id) {
		empresaRepository.deleteById(id);
		
	}

	@Override
	public Boolean EmpresaExiste(String ruc) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
