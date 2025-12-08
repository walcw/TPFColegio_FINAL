package ar.edu.centro8.daw.tpracticofinal.test;

// Importación de la clase principal
import ar.edu.centro8.daw.tpracticofinal.TpracticofinalApplication;


@SpringBootTest
@ContextConfiguration(classes = TpracticofinalApplication.class) // Clase principal referenciada
public class TrolleyTest {

// Ejemplo de cómo inyectarías un bean de Spring, si fuera necesario:
    // @Autowired
    // private TrolleyRepository trolleyRepository; 

    @Test
    public void testCase1() {
        // Tu lógica de prueba original...
        System.out.println("Ejecutando la prueba TrolleyTest...");
    }
}
