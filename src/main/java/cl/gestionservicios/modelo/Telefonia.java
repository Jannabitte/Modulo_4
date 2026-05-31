package cl.gestionservicios.modelo;

public class Telefonia extends Servicio {

    private int minutosIncluidos;

    public Telefonia(String nombre, double costoMensual, int minutosIncluidos) {
        super(nombre, costoMensual);
        this.minutosIncluidos = minutosIncluidos;
    }

    public int getMinutosIncluidos() {
        return minutosIncluidos;
    }

    @Override
    public double calcularCosto() {
        if (minutosIncluidos > 1000) {
            return getCostoMensual() + 3000;
        }
        return getCostoMensual();
    }
}