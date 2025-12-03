package ar.edu.centro8.daw.tpracticofinal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.centro8.daw.tpracticofinal.model.Curso;
import ar.edu.centro8.daw.tpracticofinal.service.CursoService;

// 1. Marca como RestController para devolver JSON
@RestController
// 2. Define la ruta base para la API
@RequestMapping("/api/v1/cursos")
public class CursoRestController {
    
    @Autowired
    private CursoService cursoService;

    // --- 1. GET (Listar y Buscar por Nombre) ---
    // Endpoint: /api/v1/cursos?buscarNombre=Matematica
    @GetMapping
    public ResponseEntity<List<Curso>> listarOBuscar(@RequestParam(name = "buscarNombre", required = false) String buscarNombre) {
        List<Curso> cursos;
        
        // Usamos el campo 'nombre' para buscar, tal como definiste en CursoService.buscarPorCurso
        if (buscarNombre != null && !buscarNombre.isEmpty()) {
            cursos = cursoService.buscarPorCurso(buscarNombre); 
        } else {
            cursos = cursoService.listarTodos();
        }
        // Retorna la lista con estado 200 OK
        return new ResponseEntity<>(cursos, HttpStatus.OK); 
    }

    // --- 2. GET (Buscar por ID) ---
    // Endpoint: /api/v1/cursos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        Curso curso = cursoService.findById(id);
        if (curso != null) {
            return new ResponseEntity<>(curso, HttpStatus.OK); // 200 OK
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // --- 3. POST (Crear Nuevo Curso) ---
    // Endpoint: /api/v1/cursos
    @PostMapping
    public ResponseEntity<Curso> guardar(@RequestBody Curso curso) {
        // Asegúrate de que el ID es nulo para que JPA lo inserte como nuevo
        curso.setId(null); 
        Curso nuevoCurso = cursoService.save(curso);
        // Retorna el nuevo objeto creado con estado 201 Created
        return new ResponseEntity<>(nuevoCurso, HttpStatus.CREATED); 
    }

    // --- 4. PUT (Actualizar Curso Existente) ---
    // Endpoint: /api/v1/cursos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizar(@PathVariable Long id, @RequestBody Curso cursoDetalles) {
        Curso cursoExistente = cursoService.findById(id);
        
        if (cursoExistente != null) {
            // Establece el ID del cursoDetalles al ID de la URL para asegurar la actualización
            cursoDetalles.setId(id);
            
            // Asumiendo que cursoService.save() actualiza el curso si el ID existe
            Curso cursoActualizado = cursoService.save(cursoDetalles);
            return new ResponseEntity<>(cursoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --- 5. DELETE (Eliminar Curso) ---
    // Endpoint: /api/v1/cursos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // Nota: Asegúrate de manejar correctamente las relaciones ManyToMany (alumnos)
        // si hay restricciones de integridad referencial.
        if (cursoService.findById(id) != null) {
            cursoService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
