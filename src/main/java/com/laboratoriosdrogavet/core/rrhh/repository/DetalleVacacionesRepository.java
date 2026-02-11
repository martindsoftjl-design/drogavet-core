package com.laboratoriosdrogavet.core.rrhh.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.laboratoriosdrogavet.core.rrhh.enums.EstadoVacaciones;
import com.laboratoriosdrogavet.core.rrhh.model.DetalleVacaciones;

public interface DetalleVacacionesRepository
        extends JpaRepository<DetalleVacaciones, Long> {

    /* ===================== */
    /* CONSULTAS BÁSICAS */
    /* ===================== */

    List<DetalleVacaciones> findByPeriodoId(Long periodoId);

    List<DetalleVacaciones> findByPeriodoIdAndEstado(
            Long periodoId,
            EstadoVacaciones estado
    );

    List<DetalleVacaciones> findByEstado(EstadoVacaciones estado);

    List<DetalleVacaciones> findByPeriodo_Empleado_Area_Id(Long areaId);

    List<DetalleVacaciones> findByPeriodo_Empleado_Area_IdAndEstado(
            Long areaId,
            EstadoVacaciones estado
    );

    /* ===================== */
    /* VALIDACIONES DE NEGOCIO */
    /* ===================== */

    long countByPeriodoIdAndEstado(
            Long periodoId,
            EstadoVacaciones estado
    );

    // ✅ Cruce REAL por EMPLEADO
    @Query("""
        SELECT COUNT(d) > 0
        FROM DetalleVacaciones d
        WHERE d.periodo.empleado.id = :empleadoId
          AND d.estado <> :rechazado
          AND d.fechaInicio <= :fechaFin
          AND d.fechaFin >= :fechaInicio
    """)
    boolean existsCruceFechasEmpleado(
            @Param("empleadoId") Long empleadoId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("rechazado") EstadoVacaciones rechazado
    );

    /* ===================== */
    /* MÉTRICAS DASHBOARD */
    /* ===================== */

    Long countByEstado(EstadoVacaciones estado);

    @Query("""
        SELECT COUNT(d)
        FROM DetalleVacaciones d
        WHERE d.estado = :estado
          AND d.fechaInicio <= :hoy
          AND d.fechaFin >= :hoy
    """)
    Long countVacacionesActivasHoy(
            @Param("hoy") LocalDate hoy,
            @Param("estado") EstadoVacaciones estado
    );

    @Query("""
        SELECT COUNT(d)
        FROM DetalleVacaciones d
        WHERE d.estado = :pendiente
          AND EXISTS (
              SELECT 1
              FROM DetalleVacaciones d2
              WHERE d2.id <> d.id
                AND d2.periodo.empleado.area = d.periodo.empleado.area
                AND d2.estado = :aprobado
                AND d.fechaInicio <= d2.fechaFin
                AND d.fechaFin >= d2.fechaInicio
          )
    """)
    Long countSolicitudesConCruce(
            @Param("pendiente") EstadoVacaciones pendiente,
            @Param("aprobado") EstadoVacaciones aprobado
    );
}
