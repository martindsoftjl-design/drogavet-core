/*package com.laboratoriosdrogavet.core.rrhh.controller;


import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.laboratoriosdrogavet.core.rrhh.dto.request.EmpleadoRequest;
import com.laboratoriosdrogavet.core.rrhh.dto.response.EmpleadoResponse;
import com.laboratoriosdrogavet.core.rrhh.model.Empleado;
import com.laboratoriosdrogavet.core.rrhh.service.EmpleadoService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoRestController {

    private final EmpleadoService empleadoService;

    // =========================
    // LISTAR
    // =========================

    @GetMapping
    public Page<EmpleadoResponse> listar(Pageable pageable) {

        return empleadoService.listarTodos(pageable)
                .map(this::toResponse);
    }


    @GetMapping("/{id}")
    public EmpleadoResponse obtenerPorId(@PathVariable Long id) {
        return toResponse(empleadoService.obtenerPorId(id));
    }

    @GetMapping("/dni/{dni}")
    public EmpleadoResponse obtenerPorDni(@PathVariable String dni) {
        return toResponse(empleadoService.obtenerPorDni(dni));
    }

    // =========================
    // CREAR
    // =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpleadoResponse registrar(@Valid @RequestBody EmpleadoRequest request) {

        Empleado empleado = Empleado.builder()
                .dni(request.getDni())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .cargo(request.getCargo())
                .build();

        return toResponse(empleadoService.registrar(empleado));
    }

    // =========================
    // ACTUALIZAR
    // =========================

    @PutMapping("/{id}")
    public EmpleadoResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpleadoRequest request) {

        Empleado empleado = Empleado.builder()
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .cargo(request.getCargo())
                .build();

        return toResponse(empleadoService.actualizar(id, empleado));
    }

    // =========================
    // RRHH
    // =========================

    @PutMapping("/{id}/cesar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cesarEmpleado(
            @PathVariable Long id,
            @RequestParam String motivo) {

        empleadoService.cesarEmpleado(id, motivo);
    }

    @PutMapping("/{id}/reactivar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reactivarEmpleado(@PathVariable Long id) {
        empleadoService.reactivarEmpleado(id);
    }

    // =========================
    // MAPPER PRIVADO
    // =========================

    private EmpleadoResponse toResponse(Empleado e) {
        return EmpleadoResponse.builder()
                .id(e.getId())
                .dni(e.getDni())
                .nombres(e.getNombres())
                .apellidos(e.getApellidos())
                .cargo(e.getCargo())
                .cesado(e.isCesado())
                .build();
    }
}
*/