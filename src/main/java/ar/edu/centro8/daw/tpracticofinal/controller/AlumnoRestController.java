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

import ar.edu.centro8.daw.tpracticofinal.model.Alumno;
import ar.edu.centro8.daw.tpracticofinal.service.AlumnoService;

// 1. Usa @RestController para retornar JSON
@RestController
// 2. Define una ruta base diferente (ej. /api/v1/alumnos)
@RequestMapping("/api/v1/alumnos")
public class AlumnoRestController {

    @Autowired
    private AlumnoService alumnoService;

    // --- Endpoints para Postman ---

    // GET /api/v1/alumnos (Listar y buscar)
    @GetMapping
    public ResponseEntity<List<Alumno>> listarOBuscar(@RequestParam(required = false) String buscarApellido) {
        List<Alumno> alumnos;
        if (buscarApellido != null && !buscarApellido.isEmpty()) {
            alumnos = alumnoService.buscarPorApellido(buscarApellido);
        } else {
            alumnos = alumnoService.listarTodos();
        }
        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }

    // GET /api/v1/alumnos/{id} (Buscar por ID)
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> buscarPorId(@PathVariable Long id) {
        Alumno alumno = alumnoService.findById(id);
        if (alumno != null) {
            return new ResponseEntity<>(alumno, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // POST /api/v1/alumnos/save (Guardar nuevo alumno)
    @PostMapping
    // Usa @RequestBody para recibir JSON
    public ResponseEntity<Alumno> guardar(@RequestBody Alumno alumno) {
        // Asegúrate de que el ID es nulo para un nuevo registro
        alumno.setId(null);
        Alumno nuevoAlumno = alumnoService.save(alumno);
        return new ResponseEntity<>(nuevoAlumno, HttpStatus.CREATED); // 201 Created
    }

    // PUT /api/v1/alumnos/{id} (Actualizar alumno existente)
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> actualizar(@PathVariable Long id, @RequestBody Alumno alumnoDetalles) {
        // Asume que alumnoService.save() maneja la actualización si el ID existe
        Alumno alumnoExistente = alumnoService.findById(id);

        if (alumnoExistente != null) {
            // Actualiza los campos necesarios (ejemplo simplificado)
            alumnoExistente.setNombre(alumnoDetalles.getNombre());
            alumnoExistente.setApellido(alumnoDetalles.getApellido());
            // ✨ ¡AÑADE ESTA LÍNEA!
            alumnoExistente.setEmail(alumnoDetalles.getEmail());

            // ... otros campos

            Alumno alumnoActualizado = alumnoService.save(alumnoExistente);
            return new ResponseEntity<>(alumnoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE /api/v1/alumnos/{id} (Eliminar)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (alumnoService.findById(id) != null) {
            alumnoService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
