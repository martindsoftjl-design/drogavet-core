package com.laboratoriosdrogavet.core.rrhh.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.laboratoriosdrogavet.core.rrhh.model.PeriodoVacaciones;
import com.laboratoriosdrogavet.core.rrhh.model.Empleado;
import com.laboratoriosdrogavet.core.rrhh.enums.EstadoPeriodo;

public interface PeriodoVacacionesRepository
				extends JpaRepository<PeriodoVacaciones, Long>{

	/* ===================== */
    /* CONSULTAS BÁSICAS */
    /* ===================== */

    List<PeriodoVacaciones> findByEmpleadoIdOrderByFechaInicioAsc(Long empleadoId);

    List<PeriodoVacaciones> findByEmpleadoId(Long empleadoId);

    Optional<PeriodoVacaciones> findByEmpleadoIdAndEstado(
            Long empleadoId,
            EstadoPeriodo estado
    );

    Optional<PeriodoVacaciones> findFirstByEmpleadoIdOrderByFechaInicioDesc(
            Long empleadoId
    );
    
    boolean existsByEmpleadoAndFechaInicioBetween(
            Empleado empleado,
            LocalDate inicio,
            LocalDate fin
    );


    /* ===================== */
    /* CONSULTAS DE NEGOCIO */
    /* ===================== */

    @Query("""
    	    SELECT COUNT(p) > 0
    	    FROM PeriodoVacaciones p
    	    WHERE p.empleado = :empleado
    	      AND p.estado = com.laboratoriosdrogavet.core.rrhh.enums.EstadoPeriodo.ACTIVO
    	      AND p.fechaInicio <= :fechaFin
    	      AND p.fechaFin >= :fechaInicio
    	""")
    	boolean existsPeriodoActivoSolapado(
    	        @Param("empleado") Empleado empleado,
    	        @Param("fechaInicio") LocalDate fechaInicio,
    	        @Param("fechaFin") LocalDate fechaFin
    	);
	
}
