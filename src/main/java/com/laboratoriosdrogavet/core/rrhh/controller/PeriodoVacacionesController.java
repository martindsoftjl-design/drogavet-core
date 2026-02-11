package com.laboratoriosdrogavet.core.rrhh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laboratoriosdrogavet.core.rrhh.model.Empleado;
import com.laboratoriosdrogavet.core.rrhh.model.PeriodoVacaciones;
import com.laboratoriosdrogavet.core.rrhh.service.EmpleadoService;
import com.laboratoriosdrogavet.core.rrhh.service.PeriodoVacacionesService;

@Controller
@RequestMapping("/rrhh/vacaciones/periodos")
public class PeriodoVacacionesController {

    private final PeriodoVacacionesService periodoService;
    private final EmpleadoService empleadoService;

    public PeriodoVacacionesController(
            PeriodoVacacionesService periodoService,
            EmpleadoService empleadoService) {
        this.periodoService = periodoService;
        this.empleadoService = empleadoService;
    }

    /* =====================================================
     * LISTAR PERIODOS POR EMPLEADO
     * ===================================================== */
    @GetMapping("/empleado/{empleadoId}")
    public String listarPorEmpleado(
            @PathVariable Long empleadoId,
            Model model) {

        Empleado empleado = empleadoService.obtenerPorId(empleadoId);
        
        periodoService.asegurarPeriodosHastaHoy(empleado);

        model.addAttribute("page", "periodos");
        model.addAttribute("empleado", empleado);
        model.addAttribute("periodos",
                periodoService.listarPorEmpleado(empleado));
        model.addAttribute("saldoTotal",
                periodoService.obtenerSaldoTotal(empleado));

        return "rrhh/vacaciones/periodos/lista";
    }

    /* =====================================================
     * GENERAR / REGULARIZAR PERIODOS
     * ===================================================== */
    @PostMapping("/empleado/{empleadoId}/generar-periodos")
    public String generarPeriodos(
            @PathVariable Long empleadoId,
            RedirectAttributes redirect) {

        periodoService.asegurarPeriodosHastaHoy(
                empleadoService.obtenerPorId(empleadoId));

        redirect.addFlashAttribute("mensaje",
                "Períodos de vacaciones generados correctamente");
        redirect.addFlashAttribute("tipo", "success");

        return "redirect:/rrhh/vacaciones/periodos/empleado/" + empleadoId;
    }

    /* =====================================================
     * VER DETALLE DEL PERIODO
     * ===================================================== */
    @GetMapping("/{periodoId}")
    public String verDetalle(
            @PathVariable Long periodoId,
            Model model) {

        PeriodoVacaciones periodo = periodoService.obtenerPorId(periodoId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Periodo no encontrado"));

        model.addAttribute("page", "periodos");
        model.addAttribute("periodo", periodo);

        return "rrhh/vacaciones/periodos/detalle";
    }

    /* =====================================================
     * CERRAR PERIODO
     * ===================================================== */
    @PostMapping("/{periodoId}/cerrar")
    public String cerrarPeriodo(
            @PathVariable Long periodoId,
            RedirectAttributes redirect) {

        PeriodoVacaciones periodo = periodoService.obtenerPorId(periodoId)
                .orElseThrow(() ->
                        new RuntimeException("Periodo no encontrado"));

        periodoService.cerrarPeriodo(periodoId);

        redirect.addFlashAttribute("mensaje",
                "Periodo cerrado correctamente");
        redirect.addFlashAttribute("tipo", "success");

        return "redirect:/rrhh/vacaciones/periodos/empleado/" +
                periodo.getEmpleado().getId();
    }
}

