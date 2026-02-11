package com.laboratoriosdrogavet.core.rrhh.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laboratoriosdrogavet.core.rrhh.model.Empresa;
import com.laboratoriosdrogavet.core.rrhh.repository.EmpresaRepository;
import com.laboratoriosdrogavet.core.rrhh.service.EmpresaService;

@Service
@Transactional
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @Override
    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    @Override
    public Empresa guardar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    @Override
    public Empresa actualizar(Long id, Empresa empresa) {

        Empresa empresaExistente = empresaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Empresa no encontrada con id: " + id)
                );

        empresaExistente.setRuc(empresa.getRuc());
        empresaExistente.setRazonSocial(empresa.getRazonSocial());
        empresaExistente.setNombreComercial(empresa.getNombreComercial());
        empresaExistente.setDireccion(empresa.getDireccion());
        empresaExistente.setActivo(empresa.isActivo());

        return empresaRepository.save(empresaExistente);
    }

    @Override
    public Empresa obtenerPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Empresa no encontrada con id: " + id)
                );
    }

    @Override
    public void eliminar(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new RuntimeException("Empresa no encontrada con id: " + id);
        }
        empresaRepository.deleteById(id);
    }

    @Override
    public boolean empresaExiste(String ruc) {
        return empresaRepository.existsByRuc(ruc);
    }
}
