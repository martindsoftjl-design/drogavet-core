package com.laboratoriosdrogavet.core.rrhh.service;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.laboratoriosdrogavet.core.rrhh.model.Empleado;



public interface EmpleadoService {

    // CRUD
    Empleado registrar(Empleado empleado);

    Empleado actualizar(Long id, Empleado empleado);

    Empleado obtenerPorId(Long id);

    Empleado obtenerPorDni(String dni);

    Page<Empleado> listarTodos(Pageable pageable);

    // Búsquedas
    List<Empleado> listarActivos();

    List<Empleado> listarPorEmpresa(Long empresaId);

    List<Empleado> listarPorArea(Long areaId);

    // RRHH
    void cesarEmpleado(Long id, String motivo);

    void reactivarEmpleado(Long id);
}

