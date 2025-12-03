package ar.edu.centro8.daw.tpracticofinal.service;

import java.util.List;

import ar.edu.centro8.daw.tpracticofinal.model.Profesor;

public interface ProfesorService {
    
List<Profesor> listarTodos();
List<Profesor> buscarPorApellido(String apellido);
Profesor save(Profesor profesor);
Profesor findById(Long id);
void delete(Long id);


}