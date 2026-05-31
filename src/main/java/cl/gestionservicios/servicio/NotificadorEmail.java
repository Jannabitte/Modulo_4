package cl.gestionservicios.servicio;

import cl.gestionservicios.modelo.Cliente;

public class NotificadorEmail implements Notificador {

    @Override
    public void notificar(Cliente cliente, double totalMensual) {
        System.out.println("Notificación enviada a " + cliente.getNombre()
                + ". Total mensual a pagar: $" + totalMensual);
    }
}