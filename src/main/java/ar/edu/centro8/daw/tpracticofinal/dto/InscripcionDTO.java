package ar.edu.centro8.daw.tpracticofinal.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InscripcionDTO {

    // --- Datos del Alumno (De Alumno.java) ---
    private Long alumnoId;
    private String nombreAlumno;
    private String apellidoAlumno;
    private String emailAlumno;

    // --- Datos del Curso (De Curso.java) ---
    private Long cursoId;
    private String nombreCurso; // Mapea a Curso.nombre
    private String diaSemanaCurso; // Mapea a Curso.dsemana
    private String turnoCurso; // Mapea a Curso.turno

    // --- Datos del Profesor (De Curso.profesor.nombre) ---
    private String profesorCurso; // Mapea a Curso.profesor.nombre

    // Agrega esto para el uso en el botón de edición
    private List<Long> cursosIdsAlumno;

    
}