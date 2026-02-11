package com.laboratoriosdrogavet.core.rrhh.service;

import java.util.List;
import java.util.Optional;

import com.laboratoriosdrogavet.core.rrhh.enums.TipoVacacion;
import com.laboratoriosdrogavet.core.rrhh.model.Empleado;
import com.laboratoriosdrogavet.core.rrhh.model.PeriodoVacaciones;

public interface PeriodoVacacionesService {

    /* ===================== */
    /* GESTIÓN DE PERÍODOS */
    /* ===================== */

  //  PeriodoVacaciones crearPeriodoAnual(Empleado empleado);

    PeriodoVacaciones cerrarPeriodo(Long periodoId);

    Optional<PeriodoVacaciones> obtenerPeriodoActivo(Empleado empleado);

    Optional<PeriodoVacaciones> obtenerPorId(Long id);

    List<PeriodoVacaciones> listarPorEmpleado(Empleado empleado);

    /* ===================== */
    /* REGLAS DE NEGOCIO */
    /* ===================== */

    void acreditarDias(Long periodoId, Integer dias);

    void descontarDias(Long periodoId, Integer dias);

    Integer obtenerSaldoTotal(Empleado empleado);

    /**
     * Garantiza que el empleado tenga todos sus períodos de vacaciones
     * creados desde su fecha de ingreso hasta hoy.
     */
    void asegurarPeriodosHastaHoy(Empleado empleado);

    void descontarDiasAcumulados(
            Empleado empleado,
            Integer dias,
            TipoVacacion tipo
    );

    void compensarSaldosNegativos(Long empleadoId);
}
