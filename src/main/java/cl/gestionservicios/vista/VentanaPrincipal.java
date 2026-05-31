package cl.gestionservicios.vista;

import cl.gestionservicios.modelo.Cliente;
import cl.gestionservicios.modelo.Internet;
import cl.gestionservicios.modelo.Servicio;
import cl.gestionservicios.modelo.Telefonia;
import cl.gestionservicios.modelo.Television;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private JTextField txtRut;
    private JTextField txtNombre;
    private JComboBox<String> comboServicio;
    private JTextField txtCostoBase;
    private JTextField txtDatoAdicional;
    private JTextArea areaResumen;

    private Cliente clienteActual;

    public VentanaPrincipal() {
        setTitle("Gestión de Servicios Contratados");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        crearPanelSuperior();
        crearPanelCentral();
        crearPanelInferior();
    }

    private void crearPanelSuperior() {
        JPanel panelCliente = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCliente.setBorder(BorderFactory.createTitledBorder("Datos del cliente"));

        JLabel lblRut = new JLabel("RUT:");
        txtRut = new JTextField();

        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField();

        panelCliente.add(lblRut);
        panelCliente.add(txtRut);
        panelCliente.add(lblNombre);
        panelCliente.add(txtNombre);

        add(panelCliente, BorderLayout.NORTH);
    }

    private void crearPanelCentral() {
        JPanel panelServicios = new JPanel(new GridLayout(5, 2, 10, 10));
        panelServicios.setBorder(BorderFactory.createTitledBorder("Agregar servicio"));

        JLabel lblServicio = new JLabel("Tipo de servicio:");
        comboServicio = new JComboBox<>(new String[]{"Internet", "Telefonía", "Televisión"});

        JLabel lblCostoBase = new JLabel("Costo base:");
        txtCostoBase = new JTextField();

        JLabel lblDatoAdicional = new JLabel("Velocidad / Minutos / Canales:");
        txtDatoAdicional = new JTextField();

        JButton btnCrearCliente = new JButton("Crear cliente");
        JButton btnAgregarServicio = new JButton("Agregar servicio");
        JButton btnMostrarResumen = new JButton("Mostrar resumen");
        JButton btnLimpiar = new JButton("Limpiar");

        btnCrearCliente.addActionListener(e -> crearCliente());
        btnAgregarServicio.addActionListener(e -> agregarServicio());
        btnMostrarResumen.addActionListener(e -> mostrarResumen());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelServicios.add(lblServicio);
        panelServicios.add(comboServicio);
        panelServicios.add(lblCostoBase);
        panelServicios.add(txtCostoBase);
        panelServicios.add(lblDatoAdicional);
        panelServicios.add(txtDatoAdicional);
        panelServicios.add(btnCrearCliente);
        panelServicios.add(btnAgregarServicio);
        panelServicios.add(btnMostrarResumen);
        panelServicios.add(btnLimpiar);

        add(panelServicios, BorderLayout.CENTER);
    }

    private void crearPanelInferior() {
        areaResumen = new JTextArea();
        areaResumen.setEditable(false);
        areaResumen.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(areaResumen);
        scroll.setBorder(BorderFactory.createTitledBorder("Resumen del cliente"));

        add(scroll, BorderLayout.SOUTH);
    }

    private void crearCliente() {
        String rut = txtRut.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (rut.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes ingresar RUT y nombre del cliente.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        clienteActual = new Cliente(rut, nombre);

        JOptionPane.showMessageDialog(this,
                "Cliente creado correctamente.",
                "Cliente registrado",
                JOptionPane.INFORMATION_MESSAGE);

        areaResumen.setText("Cliente creado:\n" +
                "Nombre: " + clienteActual.getNombre() + "\n" +
                "RUT: " + clienteActual.getRut() + "\n");
    }

    private void agregarServicio() {
        if (clienteActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Primero debes crear un cliente.",
                    "Cliente no registrado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String tipoServicio = comboServicio.getSelectedItem().toString();
            double costoBase = Double.parseDouble(txtCostoBase.getText().trim());
            int datoAdicional = Integer.parseInt(txtDatoAdicional.getText().trim());

            Servicio servicio;

            switch (tipoServicio) {
                case "Internet":
                    servicio = new Internet("Internet", costoBase, datoAdicional);
                    break;
                case "Telefonía":
                    servicio = new Telefonia("Telefonía", costoBase, datoAdicional);
                    break;
                case "Televisión":
                    servicio = new Television("Televisión", costoBase, datoAdicional);
                    break;
                default:
                    JOptionPane.showMessageDialog(this,
                            "Tipo de servicio no válido.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
            }

            clienteActual.agregarServicio(servicio);

            JOptionPane.showMessageDialog(this,
                    "Servicio agregado correctamente.",
                    "Servicio registrado",
                    JOptionPane.INFORMATION_MESSAGE);

            mostrarResumen();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El costo base y el dato adicional deben ser números válidos.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarResumen() {
        if (clienteActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Primero debes crear un cliente.",
                    "Cliente no registrado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        areaResumen.setText(clienteActual.generarResumen());
    }

    private void limpiarFormulario() {
        txtRut.setText("");
        txtNombre.setText("");
        txtCostoBase.setText("");
        txtDatoAdicional.setText("");
        areaResumen.setText("");
        clienteActual = null;
    }
}