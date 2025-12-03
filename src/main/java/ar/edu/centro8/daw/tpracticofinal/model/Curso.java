package ar.edu.centro8.daw.tpracticofinal.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cursos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del curso debe tener entre 2 y 100 caracteres")
    private String nombre;

    // @NotBlank(message = "La descripción del curso es obligatoria")
    // @Size(min = 10, max = 500, message = "La descripción del curso debe tener
    // entre 10 y 500 caracteres")
    // private String descripcion;

    // @NotNull(message = "La duración del curso es obligatoria")
    // private Integer duracionHoras;

    // private String modalidad;
    // private enum diasSemana {
    // LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO
    // };
    private String dsemana;

    @NotBlank(message = "El turno es obligatorio")
    @Size(min = 2, max = 50, message = "El turno debe tener entre 2 y 50 caracteres")
    private String turno;


    @NotNull(message = "El profesor es obligatorio")
    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    @ManyToMany(mappedBy = "cursos")
    @JsonIgnore // <-- ¡Añade esta anotación aquí!
    private List<Alumno> alumnos;
}
