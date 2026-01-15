package com.laboratoriosdrogavet.core.rrhh.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laboratoriosdrogavet.core.rrhh.model.Empleado;
import com.laboratoriosdrogavet.core.rrhh.repository.EmpleadoRepository;
import com.laboratoriosdrogavet.core.rrhh.service.EmpleadoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    // =========================
    // CRUD
    // =========================

    @Override
    public Empleado registrar(Empleado empleado) {

        if (empleadoRepository.existsByDni(empleado.getDni())) {
            throw new IllegalArgumentException(
                "Ya existe un empleado con el DNI " + empleado.getDni()
            );
        }

        empleado.setCesado(false);
        empleado.setFechaCese(null);
        empleado.setMotivoCese(null);

        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado actualizar(Long id, Empleado datos) {

        Empleado existente = obtenerPorId(id);

        // DATOS PERSONALES
        existente.setNombres(datos.getNombres());
        existente.setApellidos(datos.getApellidos());
        existente.setFechaNacimiento(datos.getFechaNacimiento());
        existente.setDireccion(datos.getDireccion());
        existente.setTelefono(datos.getTelefono());
        existente.setEmail(datos.getEmail());

        // DATOS LABORALES
        existente.setEmpresa(datos.getEmpresa());
        existente.setArea(datos.getArea());
        existente.setCargo(datos.getCargo());
        existente.setTipoContrato(datos.getTipoContrato());
        existente.setRegimenLaboral(datos.getRegimenLaboral());
        existente.setSituacion(datos.getSituacion());
        existente.setFechaIngreso(datos.getFechaIngreso());

        // DATOS ADMINISTRATIVOS
        existente.setBanco(datos.getBanco());
        existente.setNumeroCuenta(datos.getNumeroCuenta());
        existente.setSistemaPension(datos.getSistemaPension());
        existente.setAfp(datos.getAfp());
        existente.setTipoComisionAfp(datos.getTipoComisionAfp());
        existente.setCuspp(datos.getCuspp());

        // CONTACTO EMERGENCIA
        existente.setContactoEmergenciaNombre(datos.getContactoEmergenciaNombre());
        existente.setContactoEmergenciaParentesco(datos.getContactoEmergenciaParentesco());
        existente.setContactoEmergenciaTelefono(datos.getContactoEmergenciaTelefono());

        // OBSERVACIONES
        existente.setObservaciones(datos.getObservaciones());

        return empleadoRepository.save(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public Empleado obtenerPorId(Long id) {
        return empleadoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empleado> listarTodos(Pageable pageable) {
        return empleadoRepository.findAll(pageable);
    }

    // =========================
    // BÚSQUEDAS
    // =========================

    @Override
    @Transactional(readOnly = true)
    public Empleado obtenerPorDni(String dni) {
        return empleadoRepository.findByDni(dni)
            .orElseThrow(() ->
                new IllegalArgumentException("Empleado no encontrado con DNI " + dni)
            );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> listarActivos() {
        return empleadoRepository.findByCesadoFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> listarPorEmpresa(Long empresaId) {
        return empleadoRepository.findByEmpresaIdAndCesadoFalse(empresaId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> listarPorArea(Long areaId) {
        return empleadoRepository.findByAreaIdAndCesadoFalse(areaId);
    }

    // =========================
    // RRHH
    // =========================

    @Override
    public void cesarEmpleado(Long id, String motivo) {

        Empleado empleado = obtenerPorId(id);

        if (empleado.isCesado()) {
            throw new IllegalStateException("El empleado ya está cesado");
        }

        empleado.setCesado(true);
        empleado.setFechaCese(LocalDate.now());
        empleado.setMotivoCese(motivo);

        empleadoRepository.save(empleado);
    }

    @Override
    public void reactivarEmpleado(Long id) {

        Empleado empleado = obtenerPorId(id);

        if (!empleado.isCesado()) {
            throw new IllegalStateException("El empleado no está cesado");
        }

        empleado.setCesado(false);
        empleado.setFechaCese(null);
        empleado.setMotivoCese(null);

        empleadoRepository.save(empleado);
    }

	
}

