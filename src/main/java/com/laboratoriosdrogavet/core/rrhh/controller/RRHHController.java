package com.laboratoriosdrogavet.core.rrhh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rrhh")
public class RRHHController {

    /* =========================
     * DASHBOARD RRHH
     * ========================= */
    @GetMapping
    public String index(Model model) {
        model.addAttribute("page", "dashboard");
        return "rrhh/index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("page", "dashboard");
        return "rrhh/dashboard";
    }

    /* =========================
     * VACACIONES (MENÚ)
     * ========================= */
    @GetMapping("/vacaciones")
    public String vacacionesIndex(Model model) {
        model.addAttribute("page", "vacaciones");
        return "rrhh/vacaciones/index";
    }
}

