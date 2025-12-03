package ar.edu.centro8.daw.tpracticofinal.model;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name="profesores")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Profesor {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del profesor es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del profesor debe tener entre 2 y 100 caracteres")
    private String nombre;

    private String apellido;
    // public enum especialidad {
    //     MATEMATICAS, FISICA, QUIMICA, LENGUAJE, HISTORIA, GEOGRAFIA, BIOLOGIA, PROGRAMACION
    // };

    // @NotNull(message = "La especialidad es obligatoria")
    // private especialidad especialidad;
    
    private String especialidad;


    @OneToMany(mappedBy = "profesor")
    @JsonIgnore // <-- ¡Añade esta anotación aquí!
    private List<Curso> cursos;

}
