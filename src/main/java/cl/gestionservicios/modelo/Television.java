package cl.gestionservicios.modelo;

public class Television extends Servicio {

    private int cantidadCanales;

    public Television(String nombre, double costoMensual, int cantidadCanales) {
        super(nombre, costoMensual);
        this.cantidadCanales = cantidadCanales;
    }

    public int getCantidadCanales() {
        return cantidadCanales;
    }

    @Override
    public double calcularCosto() {
        if (cantidadCanales > 100) {
            return getCostoMensual() + 4000;
        }
        return getCostoMensual();
    }
}
