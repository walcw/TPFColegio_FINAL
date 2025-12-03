package ar.edu.centro8.daw.tpracticofinal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.centro8.daw.tpracticofinal.dto.InscripcionDTO;
import ar.edu.centro8.daw.tpracticofinal.model.Alumno;
import ar.edu.centro8.daw.tpracticofinal.model.Curso;
import ar.edu.centro8.daw.tpracticofinal.repository.AlumnoRepository;

@Service
public class AlumnoServiceImplement implements AlumnoService {
    @Autowired
    private AlumnoRepository alumnoRepository;

    @Override
    public List<Alumno> listarTodos() {
        return alumnoRepository.findAll();
    }

    @Override
    public Alumno save(Alumno alumno) {
        return alumnoRepository.save(alumno);
    }

    @Override
    public Alumno findById(Long id) {
        return alumnoRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        alumnoRepository.deleteById(id);
    }

    // 🆕 Nuevo método para buscar por apellido
    public List<Alumno> buscarPorApellido(String apellido) {
        return alumnoRepository.findByApellidoContainingIgnoreCase(apellido);
    }

    public List<InscripcionDTO> findAllInscripciones() {
        // Obtenemos todos los Alumnos (con sus cursos cargados gracias a la relación
        // @ManyToMany)
        List<Alumno> alumnos = alumnoRepository.findAll();
        List<InscripcionDTO> inscripciones = new ArrayList<>();

        for (Alumno alumno : alumnos) {

            if (alumno.getCursos() != null && !alumno.getCursos().isEmpty()) {

                // Si el alumno tiene cursos, creamos un DTO por cada curso.
                for (Curso curso : alumno.getCursos()) {
                    InscripcionDTO dto = new InscripcionDTO();

                    // Obtener los IDs de todos los cursos del alumno:
                    List<Long> cursosIds = alumno.getCursos().stream().map(Curso::getId).collect(Collectors.toList());
                    // ...
                    // DENTRO del bucle de cursos:
                    dto.setCursosIdsAlumno(cursosIds);

                    // Rellenar datos del ALUMNO (Variables: id, nombre, apellido, email)
                    dto.setAlumnoId(alumno.getId());
                    dto.setNombreAlumno(alumno.getNombre());
                    dto.setApellidoAlumno(alumno.getApellido());
                    dto.setEmailAlumno(alumno.getEmail());

                    // Rellenar datos del CURSO (Variables: id, nombre, dsemana, turno)
                    dto.setCursoId(curso.getId());
                    dto.setNombreCurso(curso.getNombre());
                    dto.setDiaSemanaCurso(curso.getDsemana()); // Mapeado correctamente
                    dto.setTurnoCurso(curso.getTurno()); // Mapeado correctamente

                    // Rellenar nombre del PROFESOR (Navega: curso -> profesor -> nombre)
                    if (curso.getProfesor() != null) {
                        dto.setProfesorCurso(curso.getProfesor().getNombre() + " " + curso.getProfesor().getApellido());
                    } else {
                        dto.setProfesorCurso("N/A");
                    }

                    inscripciones.add(dto);
                }
            } else {
                // Caso: Alumno sin cursos (se agrega una fila para que aparezca en la tabla)
                InscripcionDTO dto = new InscripcionDTO();
                dto.setAlumnoId(alumno.getId());
                dto.setNombreAlumno(alumno.getNombre());
                dto.setApellidoAlumno(alumno.getApellido());
                dto.setEmailAlumno(alumno.getEmail());
                dto.setNombreCurso("Sin asignación");
                dto.setDiaSemanaCurso("N/A");
                dto.setTurnoCurso("N/A");
                dto.setProfesorCurso("N/A");
                inscripciones.add(dto);
            }
        }
        return inscripciones;
    }

    // public List<InscripcionDTO> buscarInscripcionesPorApellido(String apellido) {

    // List<Alumno> alumnosFiltrados =
    // alumnoRepository.findByApellidoContainingIgnoreCase(apellido);

    // List<InscripcionDTO> inscripciones = new ArrayList<>();

    // return inscripciones;
    // }

    // ----------------------------------------------------
    // Método auxiliar reutilizable para el mapeo a DTOs
    // ----------------------------------------------------
    private List<InscripcionDTO> mapearAlumnosAInscripciones(List<Alumno> alumnos) {
        List<InscripcionDTO> inscripciones = new ArrayList<>();

        for (Alumno alumno : alumnos) {

            // Pre-calcular la lista de IDs de cursos para el botón de edición
            List<Long> cursosIds = new ArrayList<>();
            if (alumno.getCursos() != null) {
                cursosIds = alumno.getCursos().stream().map(Curso::getId).collect(Collectors.toList());
            }

            if (alumno.getCursos() != null && !alumno.getCursos().isEmpty()) {

                // Mapear cada curso a un DTO (una fila por curso)
                for (Curso curso : alumno.getCursos()) {
                    InscripcionDTO dto = new InscripcionDTO();

                    // Datos del Alumno (repetidos por cada curso)
                    dto.setAlumnoId(alumno.getId());
                    dto.setNombreAlumno(alumno.getNombre());
                    dto.setApellidoAlumno(alumno.getApellido());
                    dto.setEmailAlumno(alumno.getEmail());
                    dto.setCursosIdsAlumno(cursosIds); // Lista COMPLETA de cursos del alumno

                    // Datos del Curso
                    dto.setCursoId(curso.getId());
                    dto.setNombreCurso(curso.getNombre());
                    dto.setDiaSemanaCurso(curso.getDsemana());
                    dto.setTurnoCurso(curso.getTurno());

                    // Datos del Profesor
                    if (curso.getProfesor() != null) {
                        String nombreProfesor = curso.getProfesor().getNombre() + " "
                                + curso.getProfesor().getApellido();
                        dto.setProfesorCurso(nombreProfesor);
                    } else {
                        dto.setProfesorCurso("N/A");
                    }

                    inscripciones.add(dto);
                }
            } else {
                // Caso: Alumno sin cursos (se agrega una fila para que aparezca)
                InscripcionDTO dto = new InscripcionDTO();
                dto.setAlumnoId(alumno.getId());
                dto.setNombreAlumno(alumno.getNombre());
                dto.setApellidoAlumno(alumno.getApellido());
                dto.setEmailAlumno(alumno.getEmail());
                dto.setCursosIdsAlumno(cursosIds); // Lista vacía
                dto.setNombreCurso("Sin asignación");
                dto.setDiaSemanaCurso("N/A");
                dto.setTurnoCurso("N/A");
                dto.setProfesorCurso("N/A");
                inscripciones.add(dto);
            }
        }
        return inscripciones;
    }

    @Override
    public List<InscripcionDTO> buscarInscripcionesPorApellido(String apellido) {
        // 1. Asume que tienes un método como este en AlumnoRepository:
        // List<Alumno> findByApellidoContainingIgnoreCase(String apellido);

        List<Alumno> alumnosFiltrados = alumnoRepository.findByApellidoContainingIgnoreCase(apellido);

        // 2. Llama a la función de mapeo para convertir los alumnos filtrados a DTOs.
        return mapearAlumnosAInscripciones(alumnosFiltrados);
    }
}