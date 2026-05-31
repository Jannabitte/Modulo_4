package cl.gestionservicios;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ServicioTest.class,
        ClienteTest.class,
        NotificadorTest.class
})
public class SuitePruebasTest {
}