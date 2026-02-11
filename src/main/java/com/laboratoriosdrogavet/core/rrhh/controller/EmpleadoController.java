package com.laboratoriosdrogavet.core.rrhh.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.laboratoriosdrogavet.core.rrhh.enums.*;
import com.laboratoriosdrogavet.core.rrhh.model.Area;
import com.laboratoriosdrogavet.core.rrhh.model.Empleado;
import com.laboratoriosdrogavet.core.rrhh.model.Empresa;
import com.laboratoriosdrogavet.core.rrhh.service.AreaService;
import com.laboratoriosdrogavet.core.rrhh.service.EmpleadoService;
import com.laboratoriosdrogavet.core.rrhh.service.EmpresaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/rrhh/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final EmpresaService empresaService;
    private final AreaService areaService;

    /* =========================
     * DATOS COMUNES A FORMULARIOS
     * ========================= */
    @ModelAttribute
    public void cargarDatosFormulario(Model model) {

        model.addAttribute("module", "rrhh");
        model.addAttribute("page", "empleados");

        model.addAttribute("cargos", Cargo.values());
        model.addAttribute("situaciones", SituacionLaboral.values());
        model.addAttribute("tiposContrato", TipoContrato.values());
        model.addAttribute("regimenes", RegimenLaboral.values());
        model.addAttribute("sistemasPension", SistemaPension.values());
        model.addAttribute("afps", Afp.values());
        model.addAttribute("bancos", Banco.values());
        model.addAttribute("listaEmpresas", empresaService.listarTodas());
        model.addAttribute("listaAreas", areaService.ListarTodas());
        
    }

    /* =========================
     * LISTAR EMPLEADOS
     * URL: /rrhh/empleados
     * ========================= */
    @GetMapping
    public String listar(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        model.addAttribute(
            "empleados",
            empleadoService.listarTodos(PageRequest.of(page, 20)).getContent()
        );

        return "rrhh/empleados/lista";
    }

    /* =========================
     * NUEVO EMPLEADO
     * ========================= */
    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        model.addAttribute("empleado", new Empleado());
        return "rrhh/empleados/form";
    }

    /* =========================
     * GUARDAR EMPLEADO
     * ========================= */
    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute Empleado empleado,
            BindingResult result,
            @RequestParam Long areaId,
            @RequestParam Long empresaId,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("areas", areaService.ListarTodas());
            model.addAttribute("empresas", empresaService.listarTodas());
            model.addAttribute("cargos", Cargo.values());
            model.addAttribute("tiposContrato", TipoContrato.values());
            model.addAttribute("regimenes", RegimenLaboral.values());
            model.addAttribute("sistemasPension", SistemaPension.values());
            model.addAttribute("afps", Afp.values());
            model.addAttribute("bancos", Banco.values());
            return "rrhh/empleados/form";
        }

        Area area = areaService.obtenerPorId(areaId);

        Empresa empresa = empresaService.obtenerPorId(empresaId);
                

        empleado.setArea(area);
        empleado.setEmpresa(empresa);

        empleadoService.registrar(empleado);

        return "redirect:/rrhh/empleados";
    }


    /* =========================
     * EDITAR EMPLEADO
     * ========================= */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        model.addAttribute("empleado", empleadoService.obtenerPorId(id));
        return "rrhh/empleados/form";
    }

    /* =========================
     * ACTUALIZAR EMPLEADO
     * ========================= */
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

    /* =========================
     * CESAR EMPLEADO
     * ========================= */
    @PostMapping("/cesar/{id}")
    public String cesar(
            @PathVariable Long id,
            @RequestParam String motivo) {

        empleadoService.cesarEmpleado(id, motivo);
        return "redirect:/rrhh/empleados";
    }

    /* =========================
     * REACTIVAR EMPLEADO
     * ========================= */
    @PostMapping("/reactivar/{id}")
    public String reactivar(@PathVariable Long id) {

        empleadoService.reactivarEmpleado(id);
        return "redirect:/rrhh/empleados";
    }
}
