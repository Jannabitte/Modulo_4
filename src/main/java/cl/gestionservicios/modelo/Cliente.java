package cl.gestionservicios.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private String rut;
    private String nombre;
    private List<Servicio> serviciosContratados;

    public Cliente(String rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
        this.serviciosContratados = new ArrayList<>();
    }

    public String getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Servicio> getServiciosContratados() {
        return serviciosContratados;
    }

    public void agregarServicio(Servicio servicio) {
        serviciosContratados.add(servicio);
    }

    public double calcularTotalMensual() {
        double total = 0;

        for (Servicio servicio : serviciosContratados) {
            total += servicio.calcularCosto();
        }

        return total;
    }

    public String generarResumen() {
        StringBuilder resumen = new StringBuilder();

        resumen.append("Cliente: ").append(nombre).append("\n");
        resumen.append("RUT: ").append(rut).append("\n");
        resumen.append("Servicios contratados:\n");

        if (serviciosContratados.isEmpty()) {
            resumen.append("- Sin servicios contratados\n");
        } else {
            for (Servicio servicio : serviciosContratados) {
                resumen.append("- ")
                        .append(servicio.getNombre())
                        .append(": $")
                        .append(servicio.calcularCosto())
                        .append("\n");
            }
        }

        resumen.append("Total mensual: $").append(calcularTotalMensual());

        return resumen.toString();
    }
}