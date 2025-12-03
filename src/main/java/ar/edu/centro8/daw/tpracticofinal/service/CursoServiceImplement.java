package ar.edu.centro8.daw.tpracticofinal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.centro8.daw.tpracticofinal.model.Curso;
import ar.edu.centro8.daw.tpracticofinal.repository.CursoRepository;


@Service
public class CursoServiceImplement implements CursoService {
@Autowired
private CursoRepository cursoRepository;
@Override
public List<Curso> listarTodos(){
    return cursoRepository.findAll();
}
@Override
public Curso save(Curso curso){
    return cursoRepository.save(curso);
}
@Override
public Curso findById(Long id){
    return cursoRepository.findById(id).orElse(null);
}
@Override
public void delete(Long id){
    cursoRepository.deleteById(id);
}

public List<Curso> buscarPorCurso(String cursoname) {
        return cursoRepository.findByNombreContainingIgnoreCase(cursoname);
    }


}
