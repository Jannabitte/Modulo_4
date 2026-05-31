package cl.gestionservicios.consola;

import cl.gestionservicios.modelo.Cliente;
import cl.gestionservicios.modelo.Internet;
import cl.gestionservicios.modelo.Servicio;
import cl.gestionservicios.modelo.Telefonia;
import cl.gestionservicios.modelo.Television;

import java.util.Scanner;

public class MainConsola {

    private static Cliente clienteActual;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    agregarServicio();
                    break;
                case 3:
                    mostrarResumen();
                    break;
                case 4:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }

            System.out.println();

        } while (opcion != 4);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("==================================");
        System.out.println(" SISTEMA DE GESTIÓN DE SERVICIOS");
        System.out.println("==================================");
        System.out.println("1. Registrar cliente");
        System.out.println("2. Agregar servicio a cliente");
        System.out.println("3. Mostrar resumen del cliente");
        System.out.println("4. Salir");
        System.out.println("==================================");
    }

    private static void registrarCliente() {
        System.out.print("Ingrese RUT del cliente: ");
        String rut = scanner.nextLine();

        System.out.print("Ingrese nombre del cliente: ");
        String nombre = scanner.nextLine();

        if (rut.isBlank() || nombre.isBlank()) {
            System.out.println("Error: el RUT y el nombre no pueden estar vacíos.");
            return;
        }

        clienteActual = new Cliente(rut, nombre);
        System.out.println("Cliente registrado correctamente.");
    }

    private static void agregarServicio() {
        if (clienteActual == null) {
            System.out.println("Primero debe registrar un cliente.");
            return;
        }

        System.out.println("Seleccione tipo de servicio:");
        System.out.println("1. Internet");
        System.out.println("2. Telefonía");
        System.out.println("3. Televisión");

        int tipo = leerEntero("Opción: ");
        double costoBase = leerDouble("Ingrese costo base: ");
        int datoAdicional = leerEntero("Ingrese velocidad/minutos/canales: ");

        Servicio servicio;

        switch (tipo) {
            case 1:
                servicio = new Internet("Internet", costoBase, datoAdicional);
                break;
            case 2:
                servicio = new Telefonia("Telefonía", costoBase, datoAdicional);
                break;
            case 3:
                servicio = new Television("Televisión", costoBase, datoAdicional);
                break;
            default:
                System.out.println("Tipo de servicio no válido.");
                return;
        }

        clienteActual.agregarServicio(servicio);
        System.out.println("Servicio agregado correctamente.");
    }

    private static void mostrarResumen() {
        if (clienteActual == null) {
            System.out.println("Primero debe registrar un cliente.");
            return;
        }

        System.out.println(clienteActual.generarResumen());
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                int numero = Integer.parseInt(scanner.nextLine());
                return numero;
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double numero = Double.parseDouble(scanner.nextLine());
                return numero;
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }
}