package com.laboratoriosdrogavet.core.rrhh.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.laboratoriosdrogavet.core.rrhh.enums.Cargo;
import com.laboratoriosdrogavet.core.rrhh.enums.RegimenLaboral;
import com.laboratoriosdrogavet.core.rrhh.enums.SistemaPension;
import com.laboratoriosdrogavet.core.rrhh.enums.SituacionLaboral;
import com.laboratoriosdrogavet.core.rrhh.enums.TipoContrato;
import com.laboratoriosdrogavet.core.rrhh.model.Empleado;
import com.laboratoriosdrogavet.core.rrhh.service.AreaService;
import com.laboratoriosdrogavet.core.rrhh.service.EmpleadoService;
import com.laboratoriosdrogavet.core.rrhh.service.EmpresaService;

@Controller
@RequestMapping("/rrhh/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final EmpresaService empresaService;
    private final AreaService areaService;
    
    @ModelAttribute
    public void cargarDatosFormulario(Model model) {

        model.addAttribute("cargos", Cargo.values());
        model.addAttribute("situaciones", SituacionLaboral.values());
        model.addAttribute("tiposContrato", TipoContrato.values());
        model.addAttribute("regimenes", RegimenLaboral.values());
        model.addAttribute("sistemasPension", SistemaPension.values());
        model.addAttribute("listaEmpresas", empresaService.ListarTodas());
        model.addAttribute("listaAreas", areaService.ListarTodas());

        
    }


    // =========================
    // LISTAR EMPLEADOS
    // =========================
    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
            "empleados",
            empleadoService.listarTodos(PageRequest.of(0, 20)).getContent()
        );

        return "rrhh/empleados/lista";
    }

    // =========================
    // FORM NUEVO EMPLEADO
    // =========================
    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        model.addAttribute("empleado", new Empleado());
        return "rrhh/empleados/form";
    }

    // =========================
    // GUARDAR EMPLEADO
    // =========================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Empleado empleado, Model model) {

        try {
            empleadoService.registrar(empleado);
            return "redirect:/rrhh/empleados";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("empleado", empleado);
            return "rrhh/empleados/form";
        }
    }

    // =========================
    // EDITAR EMPLEADO
    // =========================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        model.addAttribute("empleado", empleadoService.obtenerPorId(id));
        return "rrhh/empleados/form";
    }

    // =========================
    // ACTUALIZAR EMPLEADO
    // =========================
    @PostMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable Long id,
            @ModelAttribute Empleado empleado,
            Model model) {

        try {
            empleadoService.actualizar(id, empleado);
            return "redirect:/rrhh/empleados";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("empleado", empleado);
            return "rrhh/empleados/form";
        }
    }

    // =========================
    // CESAR EMPLEADO
    // =========================
    @PostMapping("/cesar/{id}")
    public String cesar(
            @PathVariable Long id,
            @RequestParam String motivo) {

        empleadoService.cesarEmpleado(id, motivo);
        return "redirect:/rrhh/empleados";
    }

    // =========================
    // REACTIVAR EMPLEADO
    // =========================
    @PostMapping("/reactivar/{id}")
    public String reactivar(@PathVariable Long id) {

        empleadoService.reactivarEmpleado(id);
        return "redirect:/rrhh/empleados";
    }
}
