package com.laboratoriosdrogavet.core.rrhh.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laboratoriosdrogavet.core.rrhh.enums.EstadoPeriodo;
import com.laboratoriosdrogavet.core.rrhh.enums.TipoVacacion;
import com.laboratoriosdrogavet.core.rrhh.model.Empleado;
import com.laboratoriosdrogavet.core.rrhh.model.PeriodoVacaciones;
import com.laboratoriosdrogavet.core.rrhh.repository.PeriodoVacacionesRepository;
import com.laboratoriosdrogavet.core.rrhh.service.PeriodoVacacionesService;

@Service
public class PeriodoVacacionesServiceImpl
			implements PeriodoVacacionesService{
	
			private static final int DIAS_LEGALES=30;
			private final PeriodoVacacionesRepository periodoRepo;
			
			public PeriodoVacacionesServiceImpl(PeriodoVacacionesRepository periodoRepo) {
				this.periodoRepo=periodoRepo;
			}
			/*
			@Override
			@Transactional
	        public PeriodoVacaciones crearPeriodoAnual(Empleado empleado) {
				LocalDate fechaInicio = empleado.getFechaIngreso();
		        LocalDate fechaFin = fechaInicio.plusYears(1).minusDays(1);

		        // 1️⃣ Validar que no exista periodo activo solapado
		        if (periodoRepo.existsPeriodoActivoSolapado(
		                empleado, fechaInicio, fechaFin)) {
		            throw new RuntimeException(
		                    "Ya existe un periodo activo que cubre estas fechas");
		        }

		        // 2️⃣ Cerrar periodo activo anterior (si existe)
		        periodoRepo.findByEmpleadoIdAndEstado(
		                empleado.getId(), EstadoPeriodo.ACTIVO)
		                .ifPresent(p -> {
		                    p.setEstado(EstadoPeriodo.CERRADO);
		                    periodoRepo.save(p);
		                });

		        // 3️⃣ Crear nuevo periodo
		        PeriodoVacaciones periodo = new PeriodoVacaciones();
		        periodo.setEmpleado(empleado);
		        periodo.setFechaInicio(fechaInicio);   // ✅ fecha de ingreso
		        periodo.setFechaFin(fechaFin);
		        periodo.setDiasGanados(DIAS_LEGALES);
		        periodo.setDiasTomados(0);
		        periodo.setSalidasUsadas(0);
		        periodo.setEstado(EstadoPeriodo.ACTIVO);

		        // 4️⃣ Guardar
		        periodo = periodoRepo.save(periodo);

		        // 5️⃣ Compensar vacaciones adelantadas (URGENTES)
		        compensarSaldosNegativos(empleado.getId());

		        return periodo;
	        }*/

	        @Override
	        public PeriodoVacaciones cerrarPeriodo(Long periodoId) {
	        	PeriodoVacaciones periodo = obtenerPeriodo(periodoId);
	            periodo.setEstado(EstadoPeriodo.CERRADO);
	            return periodoRepo.save(periodo);
	        }

	        @Override
	        public Optional<PeriodoVacaciones> obtenerPeriodoActivo(Empleado empleado) {
	        	return periodoRepo.findByEmpleadoIdAndEstado(
	                    empleado.getId(), EstadoPeriodo.ACTIVO);
	        }

	        @Override
	        public Optional<PeriodoVacaciones> obtenerPorId(Long id) {
		    // TODO Auto-generated method stub
		    return periodoRepo.findById(id);
	        }

	        @Override
	        public List<PeriodoVacaciones> listarPorEmpleado(Empleado empleado) {
	        	return periodoRepo.findByEmpleadoIdOrderByFechaInicioAsc(
	                    empleado.getId());
	        }
	        
	        /* =====================
	         * REGLAS DE NEGOCIO
	         * ===================== */

	        @Override
	        public void acreditarDias(Long periodoId, Integer dias) {
	        	if (dias == null || dias <= 0) return;

	            PeriodoVacaciones periodo = obtenerPeriodo(periodoId);
	            periodo.setDiasGanados(periodo.getDiasGanados() + dias);
	            periodoRepo.save(periodo);
		    
	        }

	        @Override
	        public void descontarDias(Long periodoId, Integer dias) {
	        	if (dias == null || dias <= 0) return;

	            PeriodoVacaciones periodo = obtenerPeriodo(periodoId);

	            if (periodo.getSaldo() < dias) {
	                throw new RuntimeException(
	                        "Saldo insuficiente de vacaciones");
	            }

	            periodo.setDiasTomados(periodo.getDiasTomados() + dias);
	            periodoRepo.save(periodo);
		    
	        }

	        @Override
	        public Integer obtenerSaldoTotal(Empleado empleado) {
	        	 return periodoRepo.findByEmpleadoId(empleado.getId())
	                     .stream()
	                     .mapToInt(PeriodoVacaciones::getSaldo)
	                     .sum();
	        }

	        @Override
	        @Transactional
	        public void asegurarPeriodosHastaHoy(Empleado empleado) {
	        	LocalDate hoy = LocalDate.now();

	            // 1️⃣ Fecha legal de inicio del primer período
	            LocalDate inicioLegal = empleado.getFechaIngreso().plusYears(1);

	            // 🔒 Aún no cumple 1 año
	            if (inicioLegal.isAfter(hoy)) {
	                return;
	            }

	            // 2️⃣ Obtener períodos existentes
	            List<PeriodoVacaciones> periodos =
	                    periodoRepo.findByEmpleadoIdOrderByFechaInicioAsc(
	                            empleado.getId()
	                    );

	            LocalDate siguienteInicio;

	            if (periodos.isEmpty()) {
	                siguienteInicio = empleado.getFechaIngreso(); // ✅ CORRECTO
	            }
	            else {
	                PeriodoVacaciones ultimo = periodos.get(periodos.size() - 1);
	                siguienteInicio = ultimo.getFechaInicio().plusYears(1);
	            }

	            // 3️⃣ Crear períodos faltantes
	            while (!siguienteInicio.isAfter(hoy)) {

	                LocalDate fechaFin =
	                        siguienteInicio.plusYears(1).minusDays(1);

	                PeriodoVacaciones periodo = new PeriodoVacaciones();
	                periodo.setEmpleado(empleado);
	                periodo.setFechaInicio(siguienteInicio);
	                periodo.setFechaFin(fechaFin);
	                periodo.setDiasGanados(DIAS_LEGALES);
	                periodo.setDiasTomados(0);
	                periodo.setSalidasUsadas(0);
	                periodo.setEstado(EstadoPeriodo.CERRADO);

	                periodoRepo.save(periodo);

	                siguienteInicio = siguienteInicio.plusYears(1);
	            }

	            // 4️⃣ Activar SOLO el último período
	            periodoRepo.findFirstByEmpleadoIdOrderByFechaInicioDesc(
	                    empleado.getId()
	            ).ifPresent(p -> {
	                if (p.getEstado() != EstadoPeriodo.ACTIVO) {
	                    p.setEstado(EstadoPeriodo.ACTIVO);
	                    periodoRepo.save(p);
	                }
	            });
		    
	        }

	        @Override
	        @Transactional
	        public void descontarDiasAcumulados(Empleado empleado, Integer dias, TipoVacacion tipo) {
		    
	        	if (dias == null || dias <= 0) {
	                throw new IllegalArgumentException(
	                        "Los días deben ser mayores a 0");
	            }

	            int diasRestantes = dias;

	            List<PeriodoVacaciones> periodos =
	                    periodoRepo.findByEmpleadoIdOrderByFechaInicioAsc(
	                            empleado.getId()
	                    );

	            // 🔥 Definir orden legal
	            periodos.sort((a, b) -> {

	                // URGENTE → activo primero
	                if (tipo == TipoVacacion.URGENTE) {
	                    if (a.getEstado() == EstadoPeriodo.ACTIVO &&
	                        b.getEstado() == EstadoPeriodo.CERRADO) return -1;
	                    if (a.getEstado() == EstadoPeriodo.CERRADO &&
	                        b.getEstado() == EstadoPeriodo.ACTIVO) return 1;
	                }

	                // ACUMULADA → cerrados primero
	                if (tipo == TipoVacacion.ACUMULADA) {
	                    if (a.getEstado() == EstadoPeriodo.CERRADO &&
	                        b.getEstado() == EstadoPeriodo.ACTIVO) return -1;
	                    if (a.getEstado() == EstadoPeriodo.ACTIVO &&
	                        b.getEstado() == EstadoPeriodo.CERRADO) return 1;
	                }

	                return a.getFechaInicio().compareTo(b.getFechaInicio());
	            });

	            // 🔽 Descuento real
	            for (PeriodoVacaciones periodo : periodos) {

	                if (diasRestantes <= 0) break;

	                int saldoDisponible = periodo.getSaldo();

	                // 🔥 URGENTE puede ir negativo SOLO en activo
	                if (tipo == TipoVacacion.URGENTE &&
	                    periodo.getEstado() == EstadoPeriodo.ACTIVO) {

	                    periodo.setDiasTomados(
	                            periodo.getDiasTomados() + diasRestantes
	                    );

	                    periodoRepo.save(periodo);
	                    diasRestantes = 0;
	                    break;
	                }

	                if (saldoDisponible <= 0) continue;

	                int descuento = Math.min(saldoDisponible, diasRestantes);

	                periodo.setDiasTomados(
	                        periodo.getDiasTomados() + descuento
	                );

	                periodoRepo.save(periodo);
	                diasRestantes -= descuento;
	            }

	            if (diasRestantes > 0 && tipo != TipoVacacion.URGENTE) {
	                throw new IllegalStateException(
	                        "No hay saldo suficiente para cubrir la solicitud");
	            }
		    
	        }

	        @Override
	        @Transactional
	        public void compensarSaldosNegativos(Long empleadoId) {
	        	 // 1️⃣ Obtener período activo (el nuevo)
	            PeriodoVacaciones nuevoPeriodo =
	                    periodoRepo.findByEmpleadoIdAndEstado(
	                            empleadoId, EstadoPeriodo.ACTIVO)
	                    .orElseThrow(() ->
	                            new RuntimeException("No hay período activo"));

	            int diasDisponibles = nuevoPeriodo.getSaldo();

	            if (diasDisponibles <= 0) return;

	            // 2️⃣ Buscar períodos con deuda
	            List<PeriodoVacaciones> periodosConDeuda =
	                    periodoRepo.findByEmpleadoIdOrderByFechaInicioAsc(empleadoId)
	                            .stream()
	                            .filter(p -> p.getSaldo() < 0)
	                            .toList();

	            // 3️⃣ Compensar deudas
	            for (PeriodoVacaciones p : periodosConDeuda) {

	                if (diasDisponibles <= 0) break;

	                int deuda = Math.abs(p.getSaldo());
	                int compensar = Math.min(deuda, diasDisponibles);

	                // Reducir deuda
	                p.setDiasTomados(p.getDiasTomados() - compensar);
	                periodoRepo.save(p);

	                // Consumir saldo del nuevo período
	                nuevoPeriodo.setDiasTomados(
	                        nuevoPeriodo.getDiasTomados() + compensar
	                );

	                diasDisponibles -= compensar;
	            }

	            // 4️⃣ Guardar nuevo período
	            periodoRepo.save(nuevoPeriodo);
		    
	        }
	        
	        /* =====================
	         * MÉTODO PRIVADO
	         * ===================== */
	        private PeriodoVacaciones obtenerPeriodo(Long id) {
	            return periodoRepo.findById(id)
	                    .orElseThrow(() ->
	                            new RuntimeException(
	                                    "Periodo de vacaciones no encontrado"));
	        }

	        
	
	

}
