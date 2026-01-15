package com.laboratoriosdrogavet.core.rrhh.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.laboratoriosdrogavet.core.rrhh.model.Empleado;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
	
	Optional<Empleado> findById(Long id);
	
    Optional<Empleado> findByDni(String dni);

    boolean existsByDni(String dni);

    boolean existsByDniAndIdNot(String dni, Long id);

    List<Empleado> findByEmpresaId(Long empresaId);

    List<Empleado> findByAreaId(Long areaId);

    List<Empleado> findByCesadoFalse();

    List<Empleado> findByCesadoTrue();

    List<Empleado> findByEmpresaIdAndCesadoFalse(Long empresaId);

    List<Empleado> findByAreaIdAndCesadoFalse(Long areaId);

    Page<Empleado> findByEmpresaIdAndCesadoFalse(Long empresaId, Pageable pageable);

    Page<Empleado> findByAreaIdAndCesadoFalse(Long areaId, Pageable pageable);

    Page<Empleado> findByCesadoFalse(Pageable pageable);

    @Query("""
        SELECT e
        FROM Empleado e
        WHERE LOWER(e.nombres) LIKE LOWER(CONCAT('%', :texto, '%'))
           OR LOWER(e.apellidos) LIKE LOWER(CONCAT('%', :texto, '%'))
    """)
    List<Empleado> buscarPorNombreOApellido(@Param("texto") String texto);
}

