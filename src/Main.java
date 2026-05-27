import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().showUI());
    }

    static class App {

        // -------- java.time --------
        private static final DateTimeFormatter TS_FMT =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // -------- java.io --------
        private static final String FILE_NAME = "registro.txt";

        // -------- Paleta de colores --------
        private final Color BG = new Color(18, 18, 22);
        private final Color PANEL = new Color(26, 26, 32);
        private final Color BORDER = new Color(45, 45, 55);

        private final Color TEXT = new Color(245, 245, 245);
        private final Color SUBTEXT = new Color(180, 180, 190);
        private final Color MUTED = new Color(120, 120, 135);

        private final Color OK = new Color(160, 220, 170);
        private final Color WARN = new Color(255, 170, 170);

        private final Color BTN_SAVE = new Color(76, 175, 80);
        private final Color BTN_CLEAR = new Color(90, 90, 105);
        private final Color BTN_EXPORT = new Color(33, 150, 243);

        // -------- java.util --------
        private final List<Registro> registros = new ArrayList<>();

        // -------- javax.swing --------
        private JFrame frame;
        private JTextField txtNombre;
        private JLabel lblEstado;
        private JTable table;
        private DefaultTableModel model;

        void showUI() {
            setLookAndFeel();
            buildUI();
            loadFromFile();
            frame.setVisible(true);
        }

        private void setLookAndFeel() {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
                // Si falla el LookAndFeel, la aplicación continúa con el estilo por defecto.
            }
        }

        private void buildUI() {
            frame = new JFrame("Registro Pro — Demo Java");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(920, 560);
            frame.setLocationRelativeTo(null);

            JPanel root = new JPanel(new BorderLayout(12, 12));
            root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
            root.setBackground(BG);

            // Header
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);

            JLabel title = new JLabel("Registro Pro");
            title.setForeground(TEXT);
            title.setFont(new Font("SansSerif", Font.BOLD, 28));

            JLabel subtitle = new JLabel(
                    "Interfaz, validación, guardado y carga automática " +
                            "(java.util • java.time • java.io • javax.swing)"
            );
            subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
            subtitle.setForeground(SUBTEXT);

            JPanel titleBox = new JPanel();
            titleBox.setOpaque(false);
            titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
            titleBox.add(title);
            titleBox.add(Box.createVerticalStrut(4));
            titleBox.add(subtitle);

            header.add(titleBox, BorderLayout.WEST);

            // Formulario
            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(PANEL);
            form.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));

            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(6, 6, 6, 6);
            gc.fill = GridBagConstraints.HORIZONTAL;

            JLabel lblNombre = new JLabel("Nombre");
            lblNombre.setForeground(new Color(220, 220, 230));
            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 13));

            txtNombre = new JTextField();
            txtNombre.setFont(new Font("SansSerif", Font.PLAIN, 14));
            styleField(txtNombre);

            JButton btnGuardar = makeButton("Guardar", BTN_SAVE);
            JButton btnLimpiar = makeButton("Limpiar", BTN_CLEAR);
            JButton btnExportar = makeButton("Exportar .txt", BTN_EXPORT);

            btnGuardar.addActionListener(e -> onGuardar());
            btnLimpiar.addActionListener(e -> onLimpiar());
            btnExportar.addActionListener(e -> onExportar());

            txtNombre.addActionListener(e -> onGuardar());

            gc.gridx = 0;
            gc.gridy = 0;
            gc.weightx = 0.2;
            form.add(lblNombre, gc);

            gc.gridx = 1;
            gc.gridy = 0;
            gc.weightx = 0.8;
            form.add(txtNombre, gc);

            gc.gridx = 2;
            gc.gridy = 0;
            gc.weightx = 0;
            form.add(btnGuardar, gc);

            gc.gridx = 3;
            gc.gridy = 0;
            form.add(btnLimpiar, gc);

            gc.gridx = 4;
            gc.gridy = 0;
            form.add(btnExportar, gc);

            // Tabla
            model = new DefaultTableModel(new Object[]{"Fecha/Hora", "Nombre"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            table = new JTable(model);
            table.setRowHeight(26);
            table.setFont(new Font("SansSerif", Font.PLAIN, 13));
            table.setForeground(new Color(235, 235, 245));
            table.setBackground(PANEL);
            table.setGridColor(BORDER);
            table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
            table.getTableHeader().setBackground(new Color(30, 30, 38));
            table.getTableHeader().setForeground(new Color(220, 220, 230));

            JScrollPane sp = new JScrollPane(table);
            sp.getViewport().setBackground(PANEL);
            sp.setBorder(BorderFactory.createLineBorder(BORDER));

            // Footer
            JPanel footer = new JPanel(new BorderLayout());
            footer.setOpaque(false);

            lblEstado = new JLabel("Listo.");
            lblEstado.setForeground(SUBTEXT);
            lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 12));

            JLabel hint = new JLabel("Tip: Enter para guardar • Archivo: " + FILE_NAME);
            hint.setForeground(MUTED);
            hint.setFont(new Font("SansSerif", Font.PLAIN, 12));

            footer.add(lblEstado, BorderLayout.WEST);
            footer.add(hint, BorderLayout.EAST);

            // Layout general
            root.add(header, BorderLayout.NORTH);

            JPanel center = new JPanel(new BorderLayout(12, 12));
            center.setOpaque(false);
            center.add(form, BorderLayout.NORTH);
            center.add(sp, BorderLayout.CENTER);

            root.add(center, BorderLayout.CENTER);
            root.add(footer, BorderLayout.SOUTH);

            frame.setContentPane(root);
        }

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
            JButton button = new JButton(text);
            button.setFont(new Font("SansSerif", Font.BOLD, 12));
            button.setForeground(Color.WHITE);
            button.setBackground(bg);
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setContentAreaFilled(true);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return button;
        }

        private void onGuardar() {
            String nombre = txtNombre.getText().trim();

            if (nombre.isEmpty()) {
                warn("El nombre no puede estar vacío.");
                return;
            }

            if (nombre.length() < 2) {
                warn("Nombre demasiado corto.");
                return;
            }

            if (!nombre.matches("[\\p{L} .'-]+")) {
                warn("Nombre inválido. Usa letras y espacios.");
                return;
            }

            String timestamp = LocalDateTime.now().format(TS_FMT);
            Registro registro = new Registro(timestamp, nombre);

            registros.add(registro);
            model.addRow(new Object[]{registro.getTimestamp(), registro.getNombre()});

            appendToFile(registro);

            txtNombre.setText("");
            ok("Guardado: " + nombre);
        }

        private void onLimpiar() {
            int option = JOptionPane.showConfirmDialog(
                    frame,
                    "Esto limpia la tabla en pantalla, pero no borra el archivo. ¿Continuar?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (option != JOptionPane.YES_OPTION) {
                return;
            }

            registros.clear();
            model.setRowCount(0);
            ok("Tabla limpia. El archivo se mantiene intacto.");
        }

        private void onExportar() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Exportar registro");
            fileChooser.setSelectedFile(new File("registro_exportado.txt"));

            int option = fileChooser.showSaveDialog(frame);

            if (option != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File outputFile = fileChooser.getSelectedFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                for (Registro registro : registros) {
                    writer.write(registro.getTimestamp() + " | " + registro.getNombre());
                    writer.newLine();
                }

                ok("Exportado: " + outputFile.getName());

            } catch (IOException exception) {
                warn("No se pudo exportar: " + exception.getMessage());
            }
        }

        private void loadFromFile() {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                ok("No hay archivo previo. Se creará al guardar.");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int count = 0;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\s\\|\\s", 2);

                    if (parts.length == 2) {
                        Registro registro = new Registro(parts[0], parts[1]);
                        registros.add(registro);
                        model.addRow(new Object[]{registro.getTimestamp(), registro.getNombre()});
                        count++;
                    }
                }

                ok("Cargados " + count + " registros desde " + FILE_NAME);

            } catch (IOException exception) {
                warn("No se pudo leer " + FILE_NAME + ": " + exception.getMessage());
            }
        }

        private void appendToFile(Registro registro) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                writer.write(registro.getTimestamp() + " | " + registro.getNombre());
                writer.newLine();

            } catch (IOException exception) {
                warn("Error guardando archivo: " + exception.getMessage());
            }
        }

        private void ok(String message) {
            lblEstado.setForeground(OK);
            lblEstado.setText(message);
        }

        private void warn(String message) {
            lblEstado.setForeground(WARN);
            lblEstado.setText(message);
            Toolkit.getDefaultToolkit().beep();
        }
    }
}

class Registro {

    private final String timestamp;
    private final String nombre;

    public Registro(String timestamp, String nombre) {
        this.timestamp = timestamp;
        this.nombre = nombre;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNombre() {
        return nombre;
    }
}