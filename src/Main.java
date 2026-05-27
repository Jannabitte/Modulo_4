import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static class Movimiento {
        String fechaHora;
        String tipo;
        double monto;
        double saldoLuego;

        Movimiento(String fechaHora, String tipo, double monto, double saldoLuego) {
            this.fechaHora = fechaHora;
            this.tipo = tipo;
            this.monto = monto;
            this.saldoLuego = saldoLuego;
        }
    }

    public static void main(String[] args) {
        final String PIN_CORRECTO = "1312";   // cámbialo si quieres
        final int MAX_INTENTOS = 3;

        Scanner sc = new Scanner(System.in);

        System.out.println("=== CAJERO AUTOMÁTICO ===");

        boolean acceso = false;

        for (int intento = 1; intento <= MAX_INTENTOS; intento++) {
            System.out.print("Ingresa tu clave de 4 dígitos: ");
            String pin = sc.nextLine().trim();

            // Validación: exactamente 4 dígitos
            if (!pin.matches("\\d{4}")) {
                System.out.println("Clave inválida: deben ser 4 números (0-9). Intento " + intento + "/" + MAX_INTENTOS);
                continue;
            }

            // Comparación
            if (pin.equals(PIN_CORRECTO)) {
                acceso = true;
                break;
            } else {
                System.out.println("Clave incorrecta. Intento " + intento + "/" + MAX_INTENTOS);
            }
        }

        if (!acceso) {
            System.out.println("Tarjeta bloqueada. Vuelve a intentarlo más tarde.");
            return;
        }

        System.out.println("Acceso concedido ✅");

        double saldo = 100000;
        List<Movimiento> historial = new ArrayList<>();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");// saldo inicial

        int opcion;

        do {
            System.out.println("\n=== MENÚ CAJERO ===");
            System.out.println("1. Consultar saldo");
            System.out.println("2. Depositar dinero");
            System.out.println("3. Retirar dinero");
            System.out.println("4. Salir");
            System.out.println("5. Ver movimientos");
            System.out.print("Seleccione una opción: ");

            while (!sc.hasNextInt()) {
                System.out.println("Debe ingresar un número válido.");
                sc.next();
            }

            opcion = sc.nextInt();

            switch (opcion) {

                case 1:
                    System.out.println("Su saldo actual es: $" + saldo);
                    break;

                case 2:
                    System.out.print("Ingrese monto a depositar: ");
                    double deposito = sc.nextDouble();

                    if (deposito > 0) {
                        saldo += deposito;
                        String fecha = LocalDateTime.now().format(formato);
                        historial.add(new Movimiento(fecha, "Depósito", deposito, saldo));
                        System.out.println("Depósito exitoso. Nuevo saldo: $" + saldo);
                    } else {
                        System.out.println("Monto inválido.");
                    }
                    break;

                case 3:
                    System.out.print("Ingrese monto a retirar: ");
                    double retiro = sc.nextDouble();

                    if (retiro > 0 && retiro <= saldo) {
                        saldo -= retiro;
                        String fecha = LocalDateTime.now().format(formato);
                        historial.add(new Movimiento(fecha, "Retiro", retiro, saldo));
                        System.out.println("Retiro exitoso. Nuevo saldo: $" + saldo);
                    } else {
                        System.out.println("Fondos insuficientes o monto inválido.");
                    }
                    break;

                case 4:
                    System.out.println("Gracias por usar el cajero.");
                    break;
                case 5:
                    if (historial.isEmpty()) {
                        System.out.println("No hay movimientos registrados.");
                    } else {
                        System.out.println("\n=== HISTORIAL DE MOVIMIENTOS ===");
                        for (Movimiento m : historial) {
                            System.out.println(
                                    m.fechaHora + " | " +
                                            m.tipo + " | $" +
                                            m.monto + " | Saldo: $" +
                                            m.saldoLuego
                            );
                        }
                    }
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 4);
        // En el siguiente paso: menú (saldo, depositar, retirar, salir)
    }
}