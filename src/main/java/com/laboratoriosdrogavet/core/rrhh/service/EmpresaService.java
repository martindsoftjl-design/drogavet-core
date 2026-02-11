package com.laboratoriosdrogavet.core.rrhh.service;

import java.util.List;


import com.laboratoriosdrogavet.core.rrhh.model.Empresa;

public interface EmpresaService {

    List<Empresa> listarTodas();

    Empresa guardar(Empresa empresa);

    Empresa actualizar(Long id, Empresa empresa);

    Empresa obtenerPorId(Long id);

    void eliminar(Long id);

    boolean empresaExiste(String ruc);
}
