package cl.gestionservicios.servicio;

import cl.gestionservicios.modelo.Cliente;

public interface Notificador {

    void notificar(Cliente cliente, double totalMensual);
}