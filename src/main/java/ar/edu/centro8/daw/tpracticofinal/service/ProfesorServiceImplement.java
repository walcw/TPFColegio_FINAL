package ar.edu.centro8.daw.tpracticofinal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.centro8.daw.tpracticofinal.model.Profesor;
import ar.edu.centro8.daw.tpracticofinal.repository.ProfesorRepository;

@Service
public class ProfesorServiceImplement implements ProfesorService {
    @Autowired
    public ProfesorRepository profesorRepository;

    public List<Profesor> listarTodos() {
        return profesorRepository.findAll();
    }

    @Override
    public Profesor save(Profesor profesor) {
        return profesorRepository.save(profesor);
    }

    @Override
    public Profesor findById(Long id) {
        return profesorRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        profesorRepository.deleteById(id);
    }

    public List<Profesor> buscarPorApellido(String apellido) {
        return profesorRepository.findByApellidoContainingIgnoreCase(apellido);
    }
}
