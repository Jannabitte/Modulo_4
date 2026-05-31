package cl.gestionservicios;

import cl.gestionservicios.modelo.Cliente;
import cl.gestionservicios.modelo.Internet;
import cl.gestionservicios.servicio.GestorClientes;
import cl.gestionservicios.servicio.Notificador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class NotificadorTest {

    private Cliente cliente;
    private Notificador notificadorMock;
    private GestorClientes gestorClientes;

    @BeforeEach
    void prepararDatos() {
        cliente = new Cliente("11.111.111-1", "Carlos Soto");
        cliente.agregarServicio(new Internet("Internet Fibra", 20000, 600));

        notificadorMock = Mockito.mock(Notificador.class);
        gestorClientes = new GestorClientes(notificadorMock);
    }

    @Test
    void debeNotificarAlClienteElTotalMensualAPagar() {
        gestorClientes.notificarTotalMensual(cliente);

        verify(notificadorMock, times(1))
                .notificar(cliente, cliente.calcularTotalMensual());
    }
}