package cl.gestionservicios.modelo;

public abstract class Servicio {

    private String nombre;
    private double costoMensual;

    public Servicio(String nombre, double costoMensual) {
        this.nombre = nombre;
        this.costoMensual = costoMensual;
    }

    public String getNombre() {
        return nombre;
    }

    public double getCostoMensual() {
        return costoMensual;
    }

    public abstract double calcularCosto();

    @Override
    public String toString() {
        return nombre + " - $" + calcularCosto();
    }
}