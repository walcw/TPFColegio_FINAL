package ar.edu.centro8.daw.tpracticofinal.service;

import java.util.List;

import ar.edu.centro8.daw.tpracticofinal.model.Curso;

public interface CursoService {

List<Curso> listarTodos();
List<Curso> buscarPorCurso(String cursoname);
Curso save(Curso curso);
Curso findById(Long id);
void delete(Long id);


}
