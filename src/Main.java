import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMApp().showUI());
    }

    static class ATMApp {

        // ===================== CONFIGURACIÓN DEL BANCO =====================
        private final String PIN_CORRECTO = "2026";
        private final int MAX_INTENTOS = 3;

        // ===================== ESTADO DEL CAJERO =====================
        private int intentos = 0;
        private double saldo = 100000;
        private final List<Movimiento> historial = new ArrayList<>();
        private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // ===================== COMPONENTES UI =====================
        private JFrame frame;

        // Paleta de colores
        private final Color BG = new Color(245, 247, 250);
        private final Color PANEL = Color.WHITE;
        private final Color BORDER = new Color(255, 122, 0);

        private final Color TEXT = new Color(15, 23, 42);
        private final Color SUBTEXT = new Color(71, 85, 105);

        private final Color OK = new Color(22, 163, 74);
        private final Color WARN = new Color(220, 38, 38);

        private final Color PRIMARY = new Color(0, 84, 164);
        private final Color ACCENT = new Color(255, 122, 0);

        public void showUI() {
            setLookAndFeel();

            frame = new JFrame("Banco Jannabitte DEMO");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 560);
            frame.setLocationRelativeTo(null);

            showLoginScreen();

            frame.setVisible(true);
        }

        private void setLookAndFeel() {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }

        // ===================== PANTALLA DE LOGIN =====================
        private void showLoginScreen() {
            JPanel root = new JPanel(new BorderLayout(12, 12));
            root.setBorder(new EmptyBorder(18, 18, 18, 18));
            root.setBackground(BG);

            JLabel title = new JLabel("Cajero Automático");
            title.setForeground(TEXT);
            title.setFont(new Font("SansSerif", Font.BOLD, 26));

            JLabel subtitle = new JLabel("Ingresa tu PIN de 4 dígitos. Máximo " + MAX_INTENTOS + " intentos.");
            subtitle.setForeground(SUBTEXT);
            subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));

            JPanel header = new JPanel();
            header.setOpaque(false);
            header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
            header.add(title);
            header.add(Box.createVerticalStrut(4));
            header.add(subtitle);

            root.add(header, BorderLayout.NORTH);

            JPanel card = new JPanel(new GridBagLayout());
            card.setBackground(PANEL);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(16, 16, 16, 16)
            ));

            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(6, 6, 6, 6);
            gc.fill = GridBagConstraints.HORIZONTAL;

            JLabel lblPin = new JLabel("PIN:");
            lblPin.setForeground(TEXT);
            lblPin.setFont(new Font("SansSerif", Font.BOLD, 13));

            JPasswordField txtPin = new JPasswordField();
            txtPin.setFont(new Font("SansSerif", Font.PLAIN, 14));
            styleField(txtPin);

            JLabel lblEstado = new JLabel(" ");
            lblEstado.setForeground(SUBTEXT);
            lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 12));

            JButton btnIngresar = makeButton("Ingresar", PRIMARY);
            JButton btnSalir = makeSecondaryButton("Salir");

            btnIngresar.addActionListener(e -> {
                String pin = new String(txtPin.getPassword()).trim();

                if (!pin.matches("\\d{4}")) {
                    lblEstado.setForeground(WARN);
                    lblEstado.setText("PIN inválido: deben ser 4 números.");
                    Toolkit.getDefaultToolkit().beep();
                    txtPin.setText("");
                    txtPin.requestFocusInWindow();
                    return;
                }

                if (pin.equals(PIN_CORRECTO)) {
                    intentos = 0;
                    showMainScreen();
                } else {
                    intentos++;
                    int quedan = MAX_INTENTOS - intentos;

                    lblEstado.setForeground(WARN);
                    lblEstado.setText("PIN incorrecto. Intentos restantes: " + quedan);
                    Toolkit.getDefaultToolkit().beep();

                    txtPin.setText("");
                    txtPin.requestFocusInWindow();

                    if (intentos >= MAX_INTENTOS) {
                        JOptionPane.showMessageDialog(
                                frame,
                                "Tarjeta bloqueada. Vuelve más tarde.",
                                "Bloqueo",
                                JOptionPane.ERROR_MESSAGE
                        );
                        frame.dispose();
                    }
                }
            });

            txtPin.addActionListener(e -> btnIngresar.doClick());

            btnSalir.addActionListener(e -> frame.dispose());

            gc.gridx = 0;
            gc.gridy = 0;
            gc.weightx = 0.2;
            card.add(lblPin, gc);

            gc.gridx = 1;
            gc.gridy = 0;
            gc.weightx = 0.8;
            card.add(txtPin, gc);

            gc.gridx = 0;
            gc.gridy = 1;
            gc.gridwidth = 2;
            card.add(lblEstado, gc);

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            actions.setOpaque(false);
            actions.add(btnSalir);
            actions.add(btnIngresar);

            gc.gridx = 0;
            gc.gridy = 2;
            gc.gridwidth = 2;
            card.add(actions, gc);

            root.add(card, BorderLayout.CENTER);

            frame.setContentPane(root);
            frame.revalidate();
            frame.repaint();

            txtPin.requestFocusInWindow();
        }

        // ===================== PANTALLA PRINCIPAL =====================
        private void showMainScreen() {
            JPanel root = new JPanel(new BorderLayout(12, 12));
            root.setBorder(new EmptyBorder(14, 14, 14, 14));
            root.setBackground(BG);

            JLabel title = new JLabel("Panel del Cajero");
            title.setForeground(TEXT);
            title.setFont(new Font("SansSerif", Font.BOLD, 22));

            JLabel subtitle = new JLabel("Demo educativa de cajero automático en Java Swing");
            subtitle.setForeground(SUBTEXT);
            subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));

            JPanel header = new JPanel();
            header.setOpaque(false);
            header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
            header.add(title);
            header.add(Box.createVerticalStrut(4));
            header.add(subtitle);

            root.add(header, BorderLayout.NORTH);

            JTextArea area = new JTextArea();
            area.setEditable(false);
            area.setFont(new Font("Consolas", Font.PLAIN, 13));
            area.setBackground(PANEL);
            area.setForeground(TEXT);
            area.setCaretColor(TEXT);
            area.setBorder(new EmptyBorder(12, 12, 12, 12));
            area.setText("Bienvenida/o al cajero automático.\nSelecciona una opción del menú lateral.\n");

            JScrollPane sp = new JScrollPane(area);
            sp.setBorder(BorderFactory.createLineBorder(BORDER));
            sp.getViewport().setBackground(PANEL);

            JPanel side = new JPanel();
            side.setBackground(PANEL);
            side.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(12, 12, 12, 12)
            ));
            side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

            JLabel lblSaldo = new JLabel("Saldo: $" + money(saldo));
            lblSaldo.setForeground(OK);
            lblSaldo.setFont(new Font("SansSerif", Font.BOLD, 14));

            JLabel lblEstado = new JLabel("Listo.");
            lblEstado.setForeground(SUBTEXT);
            lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 12));

            JButton btnSaldo = makeButton("Consultar saldo", ACCENT);
            JButton btnDepositar = makeButton("Depositar", new Color(33, 150, 243));
            JButton btnRetirar = makeButton("Retirar", new Color(244, 67, 54));
            JButton btnMovs = makeButton("Ver movimientos", new Color(156, 39, 176));
            JButton btnSalir = makeButton("Salir", ACCENT);

            btnSaldo.addActionListener(e -> {
                JOptionPane.showMessageDialog(
                        frame,
                        "Saldo actual: $" + money(saldo),
                        "Saldo",
                        JOptionPane.INFORMATION_MESSAGE
                );

                lblEstado.setForeground(OK);
                lblEstado.setText("Consulta de saldo realizada.");
            });

            btnDepositar.addActionListener(e -> {
                Double monto = pedirMonto("Ingrese monto a depositar:");

                if (monto == null) {
                    return;
                }

                if (monto <= 0) {
                    lblEstado.setForeground(WARN);
                    lblEstado.setText("Monto inválido.");
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }

                saldo += monto;
                registrar("Depósito", monto, saldo);

                lblSaldo.setText("Saldo: $" + money(saldo));
                lblEstado.setForeground(OK);
                lblEstado.setText("Depósito exitoso: $" + money(monto));
            });

            btnRetirar.addActionListener(e -> {
                Double monto = pedirMonto("Ingrese monto a retirar:");

                if (monto == null) {
                    return;
                }

                if (monto <= 0) {
                    lblEstado.setForeground(WARN);
                    lblEstado.setText("Monto inválido.");
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }

                if (monto > saldo) {
                    lblEstado.setForeground(WARN);
                    lblEstado.setText("Fondos insuficientes.");
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }

                saldo -= monto;
                registrar("Retiro", monto, saldo);

                lblSaldo.setText("Saldo: $" + money(saldo));
                lblEstado.setForeground(OK);
                lblEstado.setText("Retiro exitoso: $" + money(monto));
            });

            btnMovs.addActionListener(e -> {
                if (historial.isEmpty()) {
                    area.setText("No hay movimientos registrados.\n");
                    lblEstado.setForeground(SUBTEXT);
                    lblEstado.setText("Historial vacío.");
                    return;
                }

                StringBuilder sb = new StringBuilder();
                sb.append("=== HISTORIAL DE MOVIMIENTOS ===\n\n");

                for (Movimiento m : historial) {
                    sb.append(m.getFechaHora())
                            .append(" | ")
                            .append(padRight(m.getTipo(), 10))
                            .append(" | $")
                            .append(money(m.getMonto()))
                            .append(" | Saldo posterior: $")
                            .append(money(m.getSaldoLuego()))
                            .append("\n");
                }

                area.setText(sb.toString());
                area.setCaretPosition(0);

                lblEstado.setForeground(OK);
                lblEstado.setText("Historial actualizado: " + historial.size() + " movimiento(s).");
            });

            btnSalir.addActionListener(e -> {
                int opt = JOptionPane.showConfirmDialog(
                        frame,
                        "¿Deseas salir del cajero?",
                        "Salir",
                        JOptionPane.YES_NO_OPTION
                );

                if (opt == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            });

            side.add(lblSaldo);
            side.add(Box.createVerticalStrut(10));
            side.add(btnSaldo);
            side.add(Box.createVerticalStrut(10));
            side.add(btnDepositar);
            side.add(Box.createVerticalStrut(10));
            side.add(btnRetirar);
            side.add(Box.createVerticalStrut(10));
            side.add(btnMovs);
            side.add(Box.createVerticalStrut(18));
            side.add(btnSalir);
            side.add(Box.createVerticalStrut(14));
            side.add(lblEstado);

            root.add(sp, BorderLayout.CENTER);
            root.add(side, BorderLayout.EAST);

            frame.setContentPane(root);
            frame.revalidate();
            frame.repaint();
        }

        // ===================== MÉTODOS AUXILIARES =====================
        private void styleField(JTextField field) {
            field.setBackground(BG);
            field.setForeground(TEXT);
            field.setCaretColor(TEXT);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 90)),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        }

        private JButton makeButton(String text, Color bg) {
            JButton b = new JButton(text);
            b.setFont(new Font("SansSerif", Font.BOLD, 12));
            b.setForeground(Color.WHITE);
            b.setBackground(bg);
            b.setOpaque(true);
            b.setBorderPainted(false);
            b.setContentAreaFilled(true);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return b;
        }

        private JButton makeSecondaryButton(String text) {
            JButton b = new JButton(text);
            b.setFont(new Font("SansSerif", Font.BOLD, 12));
            b.setForeground(TEXT);
            b.setBackground(PANEL);
            b.setOpaque(true);
            b.setContentAreaFilled(true);
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
            ));
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return b;
        }

        private void registrar(String tipo, double monto, double saldoLuego) {
            String fecha = LocalDateTime.now().format(fmt);
            historial.add(new Movimiento(fecha, tipo, monto, saldoLuego));
        }

        private Double pedirMonto(String mensaje) {
            String input = JOptionPane.showInputDialog(frame, mensaje);

            if (input == null) {
                return null;
            }

            input = input.trim().replace(",", ".");

            if (input.isEmpty()) {
                return null;
            }

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Entrada inválida. Ejemplo válido: 1000 o 2500.50",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return null;
            }
        }

        private String money(double v) {
            if (v == (long) v) {
                return String.valueOf((long) v);
            }

            return String.format("%.2f", v);
        }

        private String padRight(String s, int n) {
            if (s.length() >= n) {
                return s;
            }

            return s + " ".repeat(n - s.length());
        }

        // ===================== MODELO =====================
        static class Movimiento {
            private final String fechaHora;
            private final String tipo;
            private final double monto;
            private final double saldoLuego;

            public Movimiento(String fechaHora, String tipo, double monto, double saldoLuego) {
                this.fechaHora = fechaHora;
                this.tipo = tipo;
                this.monto = monto;
                this.saldoLuego = saldoLuego;
            }

            public String getFechaHora() {
                return fechaHora;
            }

            public String getTipo() {
                return tipo;
            }

            public double getMonto() {
                return monto;
            }

            public double getSaldoLuego() {
                return saldoLuego;
            }
        }
    }
}