package clase7a10;

import javax.swing.*;
import java.awt.*;

public class Clase9TablaMultiplicar {

    // Paleta (morado + palo rosa + blanco)
    static final Color MORADO_OSCURO = new Color(55, 0, 90);
    static final Color MORADO_MEDIO  = new Color(90, 0, 130);
    static final Color PALO_ROSA     = new Color(248, 187, 208);
    static final Color BLANCO        = Color.WHITE;
    static final Color GRIS_SUAVE    = new Color(245, 245, 245);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Clase9TablaMultiplicar::crearVentana);
    }

    private static void crearVentana() {
        JFrame frame = new JFrame("Tabla de Multiplicar");
        frame.setSize(520, 420);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(MORADO_MEDIO);

        // Título
        JLabel titulo = new JLabel("TABLA DE MULTIPLICAR", SwingConstants.CENTER);
        titulo.setForeground(BLANCO);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(18, 10, 10, 10));

        // Zona superior: input + botones
        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(MORADO_MEDIO);
        top.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbl = new JLabel("Número:");
        lbl.setForeground(BLANCO);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));

        JTextField txtNumero = new JTextField(10);
        txtNumero.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtNumero.setBackground(BLANCO);
        txtNumero.setForeground(Color.DARK_GRAY);

        JButton btnGenerar = crearBoton("Generar");
        JButton btnLimpiar = crearBoton("Limpiar");

        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        top.add(lbl, c);

        c.gridx = 1; c.gridy = 0; c.weightx = 1;
        top.add(txtNumero, c);

        c.gridx = 2; c.gridy = 0; c.weightx = 0;
        top.add(btnGenerar, c);

        c.gridx = 3; c.gridy = 0;
        top.add(btnLimpiar, c);

        // Área de resultado
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 16));
        area.setBackground(GRIS_SUAVE);
        area.setForeground(Color.DARK_GRAY);
        area.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Acciones
        btnGenerar.addActionListener(e -> {
            String input = txtNumero.getText().trim();

            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int numero;
            try {
                numero = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Tabla del ").append(numero).append(":\n\n");

            for (int i = 1; i <= 10; i++) {
                int r = numero * i;
                sb.append(numero).append(" x ").append(i).append(" = ").append(r).append("\n");
            }

            area.setText(sb.toString());
        });

        btnLimpiar.addActionListener(e -> {
            txtNumero.setText("");
            area.setText("");
            txtNumero.requestFocus();
        });

        // Enter genera
        txtNumero.addActionListener(e -> btnGenerar.doClick());

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(top, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private static JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(PALO_ROSA);
        boton.setForeground(MORADO_OSCURO);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        return boton;
    }
}