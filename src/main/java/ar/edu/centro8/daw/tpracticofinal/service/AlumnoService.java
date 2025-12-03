package ar.edu.centro8.daw.tpracticofinal.service;

import java.util.List;

import ar.edu.centro8.daw.tpracticofinal.dto.InscripcionDTO;
import ar.edu.centro8.daw.tpracticofinal.model.Alumno;

public interface AlumnoService {
    List<Alumno> listarTodos();

    List<Alumno> buscarPorApellido(String apellido);

    Alumno save(Alumno alumno);

    Alumno findById(Long id);

    void delete(Long id);

    // Nuevo método para listar todas las inscripciones (para la tabla principal)
    List<InscripcionDTO> findAllInscripciones();

    // Nuevo método para buscar inscripciones filtradas por apellido (para la
    // búsqueda)
    List<InscripcionDTO> buscarInscripcionesPorApellido(String apellido);

}
