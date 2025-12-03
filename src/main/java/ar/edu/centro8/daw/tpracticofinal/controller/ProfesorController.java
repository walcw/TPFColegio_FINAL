package ar.edu.centro8.daw.tpracticofinal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.edu.centro8.daw.tpracticofinal.model.Profesor;
import ar.edu.centro8.daw.tpracticofinal.service.ProfesorService;

@Controller
public class ProfesorController {
    @Autowired
    private ProfesorService profesorService;

    @GetMapping({ "/profesores" })
	public String inicioProfe(@RequestParam(name = "buscarApellido", defaultValue = "", required = false) String buscarApellido,Model model) {
		
		// 🚨 Lógica de búsqueda condicional
        if (buscarApellido.isEmpty()) {
            // Si el campo de búsqueda está vacío, lista todos
            model.addAttribute("profesores", profesorService.listarTodos());
        } else {
            // Si hay un apellido, busca por ese apellido (necesitas implementar este método)
            model.addAttribute("profesores", profesorService.buscarPorApellido(buscarApellido));
        }
		// model.addAttribute("profesores", profesorService.listarTodos());

		model.addAttribute("profesor", new Profesor()); // Para el formulario modal

		return "profesores";
	}

	@PostMapping("/profesores/save")
	public String saveProfesor(@ModelAttribute Profesor profesor, RedirectAttributes ra) {
		boolean nuevo = (profesor.getId() == null);
		profesorService.save(profesor);
		if (nuevo) {
			ra.addFlashAttribute("successMessage", "Se ha ingresado un nuevo profesor");
		} else {
			ra.addFlashAttribute("successMessage", "Datos del profesor modificados correctamente");
		}
		return "redirect:/profesores";
	}

	@GetMapping("/profesores/edit/{id}")
	public String editProfe(@PathVariable Long id, Model model) {
		Profesor profesor = profesorService.findById(id);
		model.addAttribute("profesor", profesor);
		model.addAttribute("profesores", profesorService.listarTodos());
		return "profesores";
	}

	@GetMapping("/profesores/delete/{id}")
	public String deleteProfe(@PathVariable Long id, RedirectAttributes ra) {
		profesorService.delete(id);
		ra.addFlashAttribute("successMessage", "Profesor eliminado del ID: " + id);
		return "redirect:/profesores";
	}



}