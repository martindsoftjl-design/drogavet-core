package com.laboratoriosdrogavet.core.rrhh.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laboratoriosdrogavet.core.rrhh.enums.EstadoPeriodo;
import com.laboratoriosdrogavet.core.rrhh.enums.EstadoVacaciones;
import com.laboratoriosdrogavet.core.rrhh.enums.TipoVacacion;
import com.laboratoriosdrogavet.core.rrhh.model.DetalleVacaciones;
import com.laboratoriosdrogavet.core.rrhh.model.Empleado;
import com.laboratoriosdrogavet.core.rrhh.model.PeriodoVacaciones;
import com.laboratoriosdrogavet.core.rrhh.repository.DetalleVacacionesRepository;
import com.laboratoriosdrogavet.core.rrhh.repository.EmpleadoRepository;
import com.laboratoriosdrogavet.core.rrhh.repository.PeriodoVacacionesRepository;
import com.laboratoriosdrogavet.core.rrhh.service.DetalleVacacionesService;
import com.laboratoriosdrogavet.core.rrhh.service.PeriodoVacacionesService;

@Service
public class DetalleVacacionesServiceImpl implements DetalleVacacionesService {

    private final DetalleVacacionesRepository detalleRepo;
    private final PeriodoVacacionesRepository periodoRepo;
    private final EmpleadoRepository empleadoRepo;
    private final PeriodoVacacionesService periodoVacacionesService;

    public DetalleVacacionesServiceImpl(
            DetalleVacacionesRepository detalleRepo,
            PeriodoVacacionesRepository periodoRepo,
            EmpleadoRepository empleadoRepo,
            PeriodoVacacionesService periodoVacacionesService) {

        this.detalleRepo = detalleRepo;
        this.periodoRepo = periodoRepo;
        this.empleadoRepo = empleadoRepo;
        this.periodoVacacionesService = periodoVacacionesService;
    }

    /* =====================================================
     * SOLICITAR VACACIONES
     * ===================================================== */
    @Override
    @Transactional
    public DetalleVacaciones solicitarVacaciones(
            Long empleadoId,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            TipoVacacion tipo,
            String observacion) {

        // 1️⃣ Validación básica de fechas
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha inicio no puede ser mayor a la fecha fin");
        }

        // 2️⃣ Obtener empleado
        Empleado empleado = empleadoRepo.findById(empleadoId)
                .orElseThrow(() -> new IllegalStateException("Empleado no encontrado"));

        // 3️⃣ Asegurar períodos legales
        periodoVacacionesService.asegurarPeriodosHastaHoy(empleado);

        // 4️⃣ Obtener período activo
        Optional<PeriodoVacaciones> periodoOpt =
                periodoRepo.findByEmpleadoIdAndEstado(empleadoId, EstadoPeriodo.ACTIVO);

        PeriodoVacaciones periodo;

        if (periodoOpt.isPresent()) {
            periodo = periodoOpt.get();
        } else {

            // 🚨 Solo URGENTE puede continuar sin período activo
            if (tipo != TipoVacacion.URGENTE) {
                throw new IllegalStateException("No existe período activo para el empleado");
            }

            // 🔥 Crear período adelantado
            periodo = new PeriodoVacaciones();
            periodo.setEmpleado(empleado);

            LocalDate inicio = empleado.getFechaIngreso();
            LocalDate fin = inicio.plusYears(1).minusDays(1);

            periodo.setFechaInicio(inicio);
            periodo.setFechaFin(fin);
            periodo.setDiasGanados(0);
            periodo.setDiasTomados(0);
            periodo.setSalidasUsadas(0);
            periodo.setEstado(EstadoPeriodo.ACTIVO);

            periodoRepo.save(periodo);
        }

        // 5️⃣ Validar cruce REAL (por EMPLEADO)
        if (detalleRepo.existsCruceFechasEmpleado(
                empleado.getId(),
                fechaInicio,
                fechaFin,
                EstadoVacaciones.RECHAZADO)) {

            throw new IllegalStateException("Las fechas solicitadas se cruzan con otras vacaciones");
        }

        // 6️⃣ Calcular días solicitados
        int diasSolicitados = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;

        // 7️⃣ Validar saldo (NO aplica a URGENTE)
        if (tipo != TipoVacacion.URGENTE && periodo.getSaldo() < diasSolicitados) {
            throw new IllegalStateException("Saldo insuficiente de vacaciones");
        }

        // 8️⃣ Validar número de salidas (solo NORMAL)
        if (tipo == TipoVacacion.NORMAL
                && periodo.getSalidasUsadas() >= periodo.getSalidasMax()) {

            throw new IllegalStateException("Ya se alcanzó el máximo de salidas permitidas");
        }

        // 9️⃣ Crear detalle (sin tocar saldo aún)
        DetalleVacaciones detalle = new DetalleVacaciones();
        detalle.setPeriodo(periodo);
        detalle.setFechaInicio(fechaInicio);
        detalle.setFechaFin(fechaFin);
        detalle.setDiasTomados(diasSolicitados);
        detalle.setTipo(tipo);
        detalle.setEstado(EstadoVacaciones.PENDIENTE);
        detalle.setObservacion(observacion);

        return detalleRepo.save(detalle);
    }

    /* =====================================================
     * APROBAR VACACIONES
     * ===================================================== */
    @Override
    @Transactional
    public DetalleVacaciones aprobar(Long detalleId) {

        DetalleVacaciones detalle = obtenerDetalle(detalleId);
        PeriodoVacaciones periodo = detalle.getPeriodo();

        if (detalle.getEstado() != EstadoVacaciones.PENDIENTE) {
            throw new IllegalStateException("La solicitud no está pendiente");
        }

        if (detalle.getTipo() != TipoVacacion.URGENTE
                && periodo.getSaldo() < detalle.getDiasTomados()) {

            throw new IllegalStateException("El periodo no tiene saldo suficiente");
        }

        if (detalle.getTipo() == TipoVacacion.NORMAL
                && periodo.getSalidasUsadas() >= periodo.getSalidasMax()) {

            throw new IllegalStateException("Se alcanzó el máximo de salidas permitidas");
        }

        // Lógica centralizada
        periodoVacacionesService.descontarDiasAcumulados(
                periodo.getEmpleado(),
                detalle.getDiasTomados(),
                detalle.getTipo()
        );

        detalle.setEstado(EstadoVacaciones.APROBADO);
        return detalleRepo.save(detalle);
    }

    /* =====================================================
     * RECHAZAR VACACIONES
     * ===================================================== */
    @Override
    @Transactional
    public DetalleVacaciones rechazar(Long detalleId, String motivo) {

        DetalleVacaciones detalle = obtenerDetalle(detalleId);

        if (detalle.getEstado() != EstadoVacaciones.PENDIENTE) {
            throw new IllegalStateException("La solicitud no está pendiente");
        }

        detalle.setEstado(EstadoVacaciones.RECHAZADO);
        detalle.setObservacion(motivo);

        return detalleRepo.save(detalle);
    }

    /* =====================
     * CONSULTAS
     * ===================== */

    @Override
    public Optional<DetalleVacaciones> obtenerPorId(Long id) {
        return detalleRepo.findById(id);
    }

    @Override
    public List<DetalleVacaciones> listarPorPeriodo(Long periodoId) {
        return detalleRepo.findByPeriodoId(periodoId);
    }

    @Override
    public List<DetalleVacaciones> listarPendientes() {
        return detalleRepo.findByEstado(EstadoVacaciones.PENDIENTE);
    }

    @Override
    public List<DetalleVacaciones> listarTodas() {
        return detalleRepo.findAll();
    }

    @Override
    public List<DetalleVacaciones> listarPorArea(Long areaId) {
        return detalleRepo.findByPeriodo_Empleado_Area_Id(areaId);
    }

    @Override
    public List<DetalleVacaciones> listarPorEstado(EstadoVacaciones estado) {
        return detalleRepo.findByEstado(estado);
    }

    @Override
    public List<DetalleVacaciones> listarPorAreaYEstado(Long areaId, EstadoVacaciones estado) {
        return detalleRepo.findByPeriodo_Empleado_Area_IdAndEstado(areaId, estado);
    }

    @Override
    public Long contarPendientes() {
        return detalleRepo.countByEstado(EstadoVacaciones.PENDIENTE);
    }

    @Override
    public Long contarEnVacacionesHoy() {
        return detalleRepo.countVacacionesActivasHoy(
                LocalDate.now(),
                EstadoVacaciones.APROBADO
        );
    }

    @Override
    public Long contarCruces() {
        return detalleRepo.countSolicitudesConCruce(
                EstadoVacaciones.PENDIENTE,
                EstadoVacaciones.APROBADO
        );
    }

    /* =====================
     * MÉTODO PRIVADO
     * ===================== */

    private DetalleVacaciones obtenerDetalle(Long id) {
        return detalleRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Detalle de vacaciones no encontrado"));
    }
}
