package ar.edu.centro8.daw.tpracticofinal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

@GetMapping("/")
	public String getIndex() {

		return "index";
	}
}
