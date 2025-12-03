package ar.edu.centro8.daw.tpracticofinal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.centro8.daw.tpracticofinal.model.Alumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    // Spring Data JPA crea esta consulta automáticamente
    List<Alumno> findByApellidoContainingIgnoreCase(String apellido);
    


}
