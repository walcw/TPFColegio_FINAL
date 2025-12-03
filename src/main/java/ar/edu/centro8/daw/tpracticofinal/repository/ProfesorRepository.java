package ar.edu.centro8.daw.tpracticofinal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.centro8.daw.tpracticofinal.model.Profesor;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

     List<Profesor> findByApellidoContainingIgnoreCase(String apellido);

    

}
