package com.laboratoriosdrogavet.core.rrhh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laboratoriosdrogavet.core.rrhh.model.Area;
import com.laboratoriosdrogavet.core.rrhh.service.AreaService;

@Controller
@RequestMapping("/rrhh/areas")
@RequiredArgsConstructor
public class AreaController {

    private final AreaService areaService;

    /* =========================
     * ATRIBUTOS COMUNES
     * ========================= */
    @ModelAttribute
    public void common(Model model) {
        model.addAttribute("module", "rrhh");
        model.addAttribute("page", "areas");
    }

    /* =========================
     * LISTAR
     * ========================= */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("areas", areaService.ListarTodas());
        model.addAttribute("page","areas");
        return "rrhh/areas/lista";
    }

    /* =========================
     * NUEVA
     * ========================= */
    @GetMapping("/nuevo")
    public String nueva(Model model) {
        model.addAttribute("area", new Area());
        return "rrhh/areas/form";
    }

    /* =========================
     * GUARDAR
     * ========================= */
    @PostMapping("/guardar")
    public String guardar(
            Area area,
            RedirectAttributes redirect) {

        areaService.Guardar(area);

        redirect.addFlashAttribute("mensaje", "Área guardada correctamente");
        redirect.addFlashAttribute("tipo", "success");

        return "redirect:/rrhh/areas";
    }

    /* =========================
     * EDITAR
     * ========================= */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Area area = areaService.obtenerPorId(id);

        model.addAttribute("area", area);
        return "rrhh/areas/form";
    }

    /* =========================
     * ELIMINAR
     * ========================= */
    @GetMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Long id,
            RedirectAttributes redirect) {

        areaService.Eliminar(id);

        redirect.addFlashAttribute("mensaje", "Área eliminada correctamente");
        redirect.addFlashAttribute("tipo", "success");

        return "redirect:/rrhh/areas";
    }
}
