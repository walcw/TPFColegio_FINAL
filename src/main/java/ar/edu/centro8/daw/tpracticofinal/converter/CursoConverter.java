package ar.edu.centro8.daw.tpracticofinal.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ar.edu.centro8.daw.tpracticofinal.model.Curso;
import ar.edu.centro8.daw.tpracticofinal.service.CursoService;

@Component
public class CursoConverter implements Converter<String, Curso> {

    @Autowired
    private CursoService cursoService;

    @Override
    public Curso convert(String source) {
        if (source == null) return null;
        String s = source.trim();
        if (s.length() == 0) return null;
        try {
            Long id = Long.valueOf(s);
            return cursoService.findById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
