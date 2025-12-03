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

import ar.edu.centro8.daw.tpracticofinal.model.Profesor;
import ar.edu.centro8.daw.tpracticofinal.service.ProfesorService;

// 1. Define como RestController para asegurar la respuesta JSON
@RestController
// 2. Define el path base de la API, distinto a la vista Thymeleaf
@RequestMapping("/api/v1/profesores")
public class ProfesorRestController {

    @Autowired
    private ProfesorService profesorService;

    // --- 1. GET (Listar y Buscar por Apellido) ---
    @GetMapping
    public ResponseEntity<List<Profesor>> listarOBuscar(@RequestParam(required = false) String buscarApellido) {
        List<Profesor> profesores;

        // La lógica de búsqueda debe estar implementada en ProfesorService
        if (buscarApellido != null && !buscarApellido.isEmpty()) {
            profesores = profesorService.buscarPorApellido(buscarApellido); // Necesitas este método
        } else {
            profesores = profesorService.listarTodos();
        }
        // Retorna la lista con estado 200 OK
        return new ResponseEntity<>(profesores, HttpStatus.OK);
    }

    // --- 2. GET (Buscar por ID) ---
    // Endpoint: /api/v1/profesores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Profesor> buscarPorId(@PathVariable Long id) {
        Profesor profesor = profesorService.findById(id);
        if (profesor != null) {
            return new ResponseEntity<>(profesor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }

    // --- 3. POST (Crear Nuevo Profesor) ---
    // Endpoint: /api/v1/profesores
    @PostMapping
    public ResponseEntity<Profesor> guardar(@RequestBody Profesor profesor) {
        // Asegúrate de que el ID es nulo para que JPA lo inserte como nuevo
        profesor.setId(null);
        Profesor nuevoProfesor = profesorService.save(profesor);
        // Retorna el nuevo objeto creado con estado 201 Created
        return new ResponseEntity<>(nuevoProfesor, HttpStatus.CREATED);
    }

    // --- 4. PUT (Actualizar Profesor Existente) ---
    // Endpoint: /api/v1/profesores/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Profesor> actualizar(@PathVariable Long id, @RequestBody Profesor profesorDetalles) {
        Profesor profesorExistente = profesorService.findById(id);

        if (profesorExistente != null) {
            // Asignar el ID de la ruta para asegurar la actualización
            profesorDetalles.setId(id);
            Profesor profesorActualizado = profesorService.save(profesorDetalles);
            return new ResponseEntity<>(profesorActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --- 5. DELETE (Eliminar Profesor) ---
    // Endpoint: /api/v1/profesores/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (profesorService.findById(id) != null) {
            profesorService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}