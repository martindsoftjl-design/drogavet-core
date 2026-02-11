package com.laboratoriosdrogavet.core.rrhh.controller;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laboratoriosdrogavet.core.rrhh.enums.EstadoVacaciones;
import com.laboratoriosdrogavet.core.rrhh.enums.TipoVacacion;
import com.laboratoriosdrogavet.core.rrhh.model.DetalleVacaciones;
import com.laboratoriosdrogavet.core.rrhh.model.Empleado;
import com.laboratoriosdrogavet.core.rrhh.model.PeriodoVacaciones;
import com.laboratoriosdrogavet.core.rrhh.service.*;

@Controller
@RequestMapping("/rrhh/vacaciones/solicitudes")
@RequiredArgsConstructor
public class DetalleVacacionesController {

    private final DetalleVacacionesService detalleService;
    private final PeriodoVacacionesService periodoService;
    private final AreaService areaService;
    private final EmpleadoService empleadoService;

    /* =========================
     * ATRIBUTOS COMUNES
     * ========================= */
    @ModelAttribute
    public void common(Model model) {
        model.addAttribute("module", "rrhh");
        model.addAttribute("page", "vacaciones");
    }

    /* =========================
     * DASHBOARD RRHH
     * ========================= */
    @GetMapping
    public String dashboard(Model model) {

        model.addAttribute("solicitudes", detalleService.listarTodas());
        model.addAttribute("vacacionesHoy", detalleService.contarEnVacacionesHoy());
        model.addAttribute("pendientes", detalleService.contarPendientes());
        model.addAttribute("cruces", detalleService.contarCruces());
        model.addAttribute("totalEmpleados", empleadoService.listarActivos().size());

        return "rrhh/vacaciones/solicitudes/dashboard";
    }

    /* =========================
     * LISTADO RRHH
     * ========================= */
    @GetMapping("/lista")
    public String listar(
            @RequestParam(required = false) Long areaId,
            @RequestParam(required = false) EstadoVacaciones estado,
            Model model) {

        List<DetalleVacaciones> solicitudes;

        if (areaId != null && estado != null) {
            solicitudes = detalleService.listarPorAreaYEstado(areaId, estado);
        } else if (areaId != null) {
            solicitudes = detalleService.listarPorArea(areaId);
        } else if (estado != null) {
            solicitudes = detalleService.listarPorEstado(estado);
        } else {
            solicitudes = detalleService.listarTodas();
        }

        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("areas", areaService.ListarTodas());
        model.addAttribute("estados", EstadoVacaciones.values());

        return "rrhh/vacaciones/solicitudes/lista";
    }

    /* =========================
     * SOLICITUDES POR PERIODO
     * ========================= */
    @GetMapping("/periodo/{periodoId}")
    public String solicitudesPorPeriodo(
            @PathVariable Long periodoId,
            Model model) {

        PeriodoVacaciones periodo = periodoService.obtenerPorId(periodoId)
                .orElseThrow(() -> new IllegalArgumentException("Periodo no encontrado"));

        model.addAttribute("periodo", periodo);
        model.addAttribute("empleado", periodo.getEmpleado());
        model.addAttribute("solicitudes",
                detalleService.listarPorPeriodo(periodoId));

        return "rrhh/vacaciones/periodos/solicitudes";
    }

    /* =========================
     * VER SOLICITUD
     * ========================= */
    @GetMapping("/{id}")
    public String ver(@PathVariable Long id, Model model) {

        DetalleVacaciones detalle = detalleService.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));

        model.addAttribute("detalle", detalle);
        return "rrhh/vacaciones/solicitudes/ver";
    }

    /* =========================
     * FORMULARIO EMPLEADO
     * ========================= */
    @GetMapping("/nueva/{empleadoId}")
    public String formulario(
            @PathVariable Long empleadoId,
            Model model) {

        Empleado empleado = empleadoService.obtenerPorId(empleadoId);

        model.addAttribute("empleado", empleado);

        model.addAttribute("periodoActivo",
                periodoService.obtenerPeriodoActivo(empleado)
                        .orElse(null));

        model.addAttribute("tiposVacacion", TipoVacacion.values());

        return "rrhh/vacaciones/solicitudes/form";
    }




    /* =========================
     * ENVIAR SOLICITUD
     * ========================= */
    @PostMapping
    public String enviar(
            @RequestParam Long empleadoId,
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin,
            @RequestParam TipoVacacion tipoVacacion,
            @RequestParam(required = false) String observacion,
            RedirectAttributes redirect) {

        try {
            detalleService.solicitarVacaciones(
                    empleadoId,
                    fechaInicio,
                    fechaFin,
                    tipoVacacion,
                    observacion);

            redirect.addFlashAttribute("mensaje", "Solicitud enviada correctamente");
            redirect.addFlashAttribute("tipo", "success");

        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("mensaje", e.getMessage());
            redirect.addFlashAttribute("tipo", "danger");
        }

        return "redirect:/rrhh/vacaciones/periodos/empleado/" + empleadoId;
    }

    /* =========================
     * APROBAR / RECHAZAR
     * ========================= */
    @PostMapping("/{id}/aprobar")
    public String aprobar(@PathVariable Long id, RedirectAttributes redirect) {

        detalleService.aprobar(id);

        redirect.addFlashAttribute("mensaje", "Solicitud aprobada correctamente");
        redirect.addFlashAttribute("tipo", "success");

        return "redirect:/rrhh/vacaciones/solicitudes/lista";
    }

    @PostMapping("/{id}/rechazar")
    public String rechazar(
            @PathVariable Long id,
            @RequestParam String motivo,
            RedirectAttributes redirect) {

        detalleService.rechazar(id, motivo);

        redirect.addFlashAttribute("mensaje", "Solicitud rechazada");
        redirect.addFlashAttribute("tipo", "warning");

        return "redirect:/rrhh/vacaciones/solicitudes/lista";
    }
}
