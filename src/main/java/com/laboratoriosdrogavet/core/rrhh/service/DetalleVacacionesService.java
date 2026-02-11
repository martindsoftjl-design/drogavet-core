package com.laboratoriosdrogavet.core.rrhh.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import com.laboratoriosdrogavet.core.rrhh.enums.EstadoVacaciones;
import com.laboratoriosdrogavet.core.rrhh.enums.TipoVacacion;
import com.laboratoriosdrogavet.core.rrhh.model.DetalleVacaciones;


public interface DetalleVacacionesService {
	
	 /* ===================== */
    /* FLUJO DE VACACIONES */
    /* ===================== */

    DetalleVacaciones solicitarVacaciones(
            Long empleadoId,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            TipoVacacion tipo,
            String observacion
    );

    DetalleVacaciones aprobar(Long detalleId);

    DetalleVacaciones rechazar(Long detalleId, String motivo);

    /* ===================== */
    /* CONSULTAS */
    /* ===================== */

    Optional<DetalleVacaciones> obtenerPorId(Long id);

    List<DetalleVacaciones> listarPorPeriodo(Long periodoId);

    List<DetalleVacaciones> listarPendientes();
    
    List<DetalleVacaciones> listarTodas();
    List<DetalleVacaciones> listarPorArea(Long areaId);
    List<DetalleVacaciones> listarPorEstado(EstadoVacaciones estado);
    List<DetalleVacaciones> listarPorAreaYEstado(Long areaId, EstadoVacaciones estado);
    
    /* ===================== */
    /* MÉTRICAS DASHBOARD */
    /* ===================== */

    Long contarEnVacacionesHoy();

    Long contarPendientes();

    Long contarCruces();

}
