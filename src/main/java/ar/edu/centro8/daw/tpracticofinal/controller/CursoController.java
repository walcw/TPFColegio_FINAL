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

import ar.edu.centro8.daw.tpracticofinal.model.Curso;
import ar.edu.centro8.daw.tpracticofinal.service.CursoService;
import ar.edu.centro8.daw.tpracticofinal.service.ProfesorService;

@Controller
public class CursoController {
	@Autowired
	private CursoService cursoService;
	@Autowired
	private ProfesorService profesorService;

	// @GetMapping({ "/cursos", "/cursos/" })
	// @GetMapping({ "/cursos"})
	// public String inicioCurso(Model modelo, @ModelAttribute("successMessage")
	// String successMessage) {

	// modelo.addAttribute("cursos", cursoService.listarTodos());
	// modelo.addAttribute("curso", new Curso());
	// modelo.addAttribute("profesores", profesorService.listarTodos());
	// return "cursos";
	// }
	// @GetMapping({ "/cursos" })
	// public String inicio(
	// 		@RequestParam(name = "buscarCurso", defaultValue = "", required = false) String buscarCurso,
	// 		Model model, @ModelAttribute("successMessage") String successMessage) {

		
	// 	if (buscarCurso.isEmpty()) {
			
	// 		model.addAttribute("cursos", cursoService.listarTodos());
	// 	} else {
						
	// 		model.addAttribute("cursos", cursoService.buscarPorCurso(buscarCurso));
	// 	}

	// 	model.addAttribute("curso", new Curso()); 
		
	// 	model.addAttribute("buscarCurso", buscarCurso); 
	// 	model.addAttribute("profesores", profesorService.listarTodos());

	// 	return "/cursos";
	// }


// 	@GetMapping({ "/cursos" })
// public String inicio(
//         @RequestParam(name = "buscarCurso", defaultValue = "", required = false) String buscarCurso,
//         Model model, @ModelAttribute("successMessage") String successMessage) {

   
//     if (buscarCurso.isEmpty()) {
//         model.addAttribute("cursos", cursoService.listarTodos());
//     } else {
//         model.addAttribute("cursos", cursoService.buscarPorCurso(buscarCurso));
//     }

//     model.addAttribute("curso", new Curso()); 
  
//     model.addAttribute("buscarCurso", buscarCurso); 
//     model.addAttribute("profesores", profesorService.listarTodos());

//     return "cursos"; 
// }

@GetMapping({ "/cursos" })
public String inicio(
        // @RequestParam(name = "buscarCurso", defaultValue = "", required = false) String buscarCurso,
        // Model model, @ModelAttribute("successMessage") String successMessage) {
	@RequestParam(name = "buscarCurso", defaultValue = "", required = false) String buscarCurso,
        Model model,RedirectAttributes ra,@ModelAttribute Curso curso ) {
     boolean nuevo = (curso.getId() == null);
    // 1. Manejar la búsqueda y la lista de cursos
    if (buscarCurso.isEmpty()) {
        model.addAttribute("cursos", cursoService.listarTodos());
    } else {
        // Ejecuta la búsqueda
        List<Curso> resultados = cursoService.buscarPorCurso(buscarCurso);
        model.addAttribute("cursos", resultados);
        
        
    }
	// 2. 💡 Agregar el mensaje de feedback
        if (nuevo) {
            // Si encuentra resultados
            ra.addFlashAttribute("successMessage", "Mostrando resultados para: " + buscarCurso);
        } else {
            // Si no encuentra resultados
            ra.addFlashAttribute("successMessage", "No se encontraron resultados para: " + buscarCurso);
        }

    // 3. Pasar el successMessage (para CRUD, no para búsqueda)
    // if (successMessage != null && !successMessage.isEmpty()) {
    //     model.addAttribute("successMessage", successMessage);
    // }

    model.addAttribute("curso", new Curso());
    model.addAttribute("buscarCurso", buscarCurso);
    model.addAttribute("profesores", profesorService.listarTodos());

    return "cursos";
}

	@PostMapping("/cursos/save")
	public String saveCurso(@ModelAttribute Curso curso, RedirectAttributes ra) {
		boolean nuevo = (curso.getId() == null);
		cursoService.save(curso);
		if (nuevo) {
			ra.addFlashAttribute("successMessage", "Se ha ingresado un nuevo curso");
		} else {
			ra.addFlashAttribute("successMessage", "Datos del curso modificados correctamente");
		}
		return "redirect:/cursos";
	}

	@GetMapping("/cursos/edit/{id}")
	public String editCurso(@PathVariable Long id, Model model) {
		Curso curso = cursoService.findById(id);
		model.addAttribute("curso", curso);
		model.addAttribute("cursos", cursoService.listarTodos());
		model.addAttribute("profesores", profesorService.listarTodos());

		return "cursos";
	}

	@GetMapping("/cursos/delete/{id}")
	public String deleteCurso(@PathVariable Long id, RedirectAttributes ra) {
		cursoService.delete(id);
		ra.addFlashAttribute("successMessage", "Curso eliminado del ID: " + id);
		return "redirect:/cursos";
	}

}
