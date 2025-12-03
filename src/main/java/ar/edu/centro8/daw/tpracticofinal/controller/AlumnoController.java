package ar.edu.centro8.daw.tpracticofinal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.edu.centro8.daw.tpracticofinal.dto.InscripcionDTO;
import ar.edu.centro8.daw.tpracticofinal.model.Alumno;
import ar.edu.centro8.daw.tpracticofinal.service.AlumnoService;
import ar.edu.centro8.daw.tpracticofinal.service.CursoService;

@Controller
public class AlumnoController {
	@Autowired
	private AlumnoService alumnoService;

	@Autowired
	private CursoService cursoService;

	// @GetMapping({ "/alumnos" })
	// public String inicio(Model model, @ModelAttribute("successMessage") String
	// successMessage) {
	// model.addAttribute("alumnos", alumnoService.listarTodos());

	// if (successMessage != null && !successMessage.isEmpty()) {
	// model.addAttribute("successMessage", successMessage);
	// }

	// model.addAttribute("alumno", new Alumno()); // Para el formulario modal

	// return "alumnos";
	// }
	@GetMapping({ "/alumnos" })
	public String inicio(
			@RequestParam(name = "buscarApellido", defaultValue = "", required = false) String buscarApellido,
			Model model) {

		// 1. Obtener la lista de inscripciones (para la tabla)
		// List<InscripcionDTO> inscripciones = alumnoService.findAllInscripciones();
		// model.addAttribute("inscripciones", inscripciones);

		List<InscripcionDTO> inscripciones;

		// 🚨 Lógica de búsqueda condicional
		if (buscarApellido.isEmpty()) {
			// Si no hay búsqueda, lista TODAS las inscripciones
			inscripciones = alumnoService.findAllInscripciones();
			// Si el campo de búsqueda está vacío, lista todos
			model.addAttribute("alumnos", alumnoService.listarTodos());
		} else {

			// Si hay búsqueda, lista las inscripciones filtradas por apellido
			inscripciones = alumnoService.buscarInscripcionesPorApellido(buscarApellido);
			// Si hay un apellido, busca por ese apellido (necesitas implementar este
			// método)
			model.addAttribute("alumnos", alumnoService.buscarPorApellido(buscarApellido));
		}

		// El modelo de la tabla SIEMPRE debe ser 'inscripciones'
		model.addAttribute("inscripciones", inscripciones);
		
		model.addAttribute("alumno", new Alumno()); // Para el formulario modal
		model.addAttribute("cursos", cursoService.listarTodos());
		model.addAttribute("buscarApellido", buscarApellido); // Asegúrate de pasar el valor de búsqueda

		// model.addAttribute("alumnos", alumnoService.listarTodos());
		// model.addAttribute("alumno", new Alumno()); // Para el formulario modal
		// model.addAttribute("cursos", cursoService.listarTodos());
		// model.addAttribute(buscarApellido);

		return "alumnos";
	}

	@PostMapping("/alumnos/save")
	public String save(@ModelAttribute Alumno alumno, RedirectAttributes ra) {
		boolean nuevo = (alumno.getId() == null);
		alumnoService.save(alumno);
		if (nuevo) {
			ra.addFlashAttribute("successMessage", "Se ha ingresado un nuevo alumno");
		} else {
			ra.addFlashAttribute("successMessage", "Datos del alumno modificados correctamente");
		}
		return "redirect:/alumnos";
	}

	@GetMapping("/alumnos/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		Alumno alumno = alumnoService.findById(id);
		model.addAttribute("alumno", alumno);
		model.addAttribute("alumnos", alumnoService.listarTodos());
		model.addAttribute("cursos", cursoService.listarTodos());
		return "alumnos"; // Puedes usar una vista específica si prefieres: "edit"
	}

	@GetMapping("/alumnos/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes ra) {
		alumnoService.delete(id);
		ra.addFlashAttribute("successMessage", "Alumno eliminado del ID: " + id);
		return "redirect:/alumnos";
	}

}
