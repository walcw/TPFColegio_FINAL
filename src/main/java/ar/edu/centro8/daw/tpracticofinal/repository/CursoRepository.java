package ar.edu.centro8.daw.tpracticofinal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ar.edu.centro8.daw.tpracticofinal.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByNombreContainingIgnoreCase(String curso);

}
