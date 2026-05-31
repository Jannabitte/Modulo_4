package cl.gestionservicios.modelo;

public class Internet extends Servicio {

    private int velocidadMbps;

    public Internet(String nombre, double costoMensual, int velocidadMbps) {
        super(nombre, costoMensual);
        this.velocidadMbps = velocidadMbps;
    }

    public int getVelocidadMbps() {
        return velocidadMbps;
    }

    @Override
    public double calcularCosto() {
        if (velocidadMbps > 500) {
            return getCostoMensual() + 5000;
        }
        return getCostoMensual();
    }
}