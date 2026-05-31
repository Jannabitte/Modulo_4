package cl.gestionservicios;

import cl.gestionservicios.modelo.Cliente;
import cl.gestionservicios.modelo.Internet;
import cl.gestionservicios.modelo.Telefonia;
import cl.gestionservicios.modelo.Television;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void prepararDatos() {
        cliente = new Cliente("12.345.678-9", "Ana Pérez");
    }

    @Test
    void clienteDebeCalcularTotalMensualDeServiciosContratados() {
        cliente.agregarServicio(new Internet("Internet Fibra", 20000, 600));
        cliente.agregarServicio(new Telefonia("Telefonía Móvil", 12000, 1200));
        cliente.agregarServicio(new Television("Televisión HD", 18000, 150));

        double total = cliente.calcularTotalMensual();

        assertEquals(62000, total);
    }

    @Test
    void clienteDebeIniciarSinServiciosContratados() {
        assertEquals(0, cliente.getServiciosContratados().size());
    }

    @Test
    void clienteDebePermitirAgregarUnServicio() {
        cliente.agregarServicio(new Internet("Internet Básico", 18000, 300));

        assertEquals(1, cliente.getServiciosContratados().size());
    }
}