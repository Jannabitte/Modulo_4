package cl.gestionservicios.servicio;

import cl.gestionservicios.modelo.Cliente;

public class GestorClientes {

    private Notificador notificador;

    public GestorClientes(Notificador notificador) {
        this.notificador = notificador;
    }

    public void notificarTotalMensual(Cliente cliente) {
        double totalMensual = cliente.calcularTotalMensual();
        notificador.notificar(cliente, totalMensual);
    }
}