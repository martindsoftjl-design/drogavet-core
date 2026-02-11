package com.laboratoriosdrogavet.core.rrhh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laboratoriosdrogavet.core.rrhh.model.Empresa;
import com.laboratoriosdrogavet.core.rrhh.service.EmpresaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/rrhh/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    // ✅ LISTAR EMPRESAS
    @GetMapping
    public String listarEmpresas(Model model) {
        model.addAttribute("empresas", empresaService.listarTodas());
        model.addAttribute("page","empresas");
        return "/rrhh/empresas/lista";
    }

    // ✅ FORMULARIO NUEVO
    @GetMapping("/nuevo")
    public String nuevaEmpresa(Model model) {
        model.addAttribute("empresa", new Empresa());
        return "/rrhh/empresas/form";
    }

    // ✅ GUARDAR / EDITAR CON VALIDACIÓN REAL
    @PostMapping("/guardar")
    public String guardarEmpresa(
            @Valid @ModelAttribute("empresa") Empresa empresa,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs) {

        // ✅ VALIDACIONES HTML + BACKEND
        if (result.hasErrors()) {
            return "/rrhh/empresas/form";
        }

        boolean esEdicion = (empresa.getId() != null);

        // ✅ VALIDAR RUC DUPLICADO SOLO AL REGISTRAR
        if (!esEdicion && empresaService.empresaExiste(empresa.getRuc())) {
            result.rejectValue(
                "ruc",
                "error.empresa",
                "Este RUC ya está registrado."
            );
            return "/rrhh/empresas/form";
        }

        empresaService.guardar(empresa);

        if (esEdicion) {
            redirectAttrs.addFlashAttribute("mensaje", "Empresa actualizada correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("mensaje", "Empresa registrada correctamente.");
        }

        redirectAttrs.addFlashAttribute("tipo", "success");

        return "redirect:/rrhh/empresas";
    }


    // ✅ FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editarEmpresa(@PathVariable Long id,
                                Model model,
                                RedirectAttributes redirectAttrs) {

        try {
            Empresa empresa = empresaService.obtenerPorId(id);
            model.addAttribute("empresa", empresa);
            return "/rrhh/empresas/form";
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("mensaje", "Empresa no encontrada.");
            redirectAttrs.addFlashAttribute("tipo", "danger");
            return "redirect:/rrhh/empresas";
        }
    }


    // ✅ ELIMINAR CON MENSAJE
    @GetMapping("/eliminar/{id}")
    public String eliminarEmpresa(@PathVariable Long id,
                                  RedirectAttributes redirectAttrs) {

        try {
            empresaService.eliminar(id);
            redirectAttrs.addFlashAttribute("mensaje", "Empresa eliminada correctamente.");
            redirectAttrs.addFlashAttribute("tipo", "success");
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("mensaje", "La empresa no existe.");
            redirectAttrs.addFlashAttribute("tipo", "danger");
        }

        return "redirect:/rrhh/empresas";
    }

}
