package com.laboratoriosdrogavet.core.rrhh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rrhh")
public class RRHHController {
	
	 @GetMapping
	    public String index() {
	        return "rrhh/index";
	    }
	
	@GetMapping("/rrhh/empleados")
	public String empleados(Model model) {
	    model.addAttribute("module", "rrhh");
	    model.addAttribute("page", "empleados");
	    return "rrhh/empleados";
	}


}
