package cl.gestionservicios;

import cl.gestionservicios.modelo.Internet;
import cl.gestionservicios.modelo.Servicio;
import cl.gestionservicios.modelo.Telefonia;
import cl.gestionservicios.modelo.Television;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServicioTest {

    @Test
    void internetDebeCobrarExtraSiVelocidadEsMayorA500Mbps() {
        Servicio internet = new Internet("Internet Fibra", 20000, 600);

        double resultado = internet.calcularCosto();

        assertEquals(25000, resultado);
    }

    @Test
    void telefoniaDebeCobrarExtraSiTieneMasDe1000Minutos() {
        Servicio telefonia = new Telefonia("Telefonía Móvil", 12000, 1200);

        double resultado = telefonia.calcularCosto();

        assertEquals(15000, resultado);
    }

    @Test
    void televisionDebeCobrarExtraSiTieneMasDe100Canales() {
        Servicio television = new Television("Televisión HD", 18000, 150);

        double resultado = television.calcularCosto();

        assertEquals(22000, resultado);
    }
}