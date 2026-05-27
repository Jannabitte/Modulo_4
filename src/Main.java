import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Main {

    // Paleta base rosado/morado
    private static final Color BG_DARK_PURPLE = new Color(255, 190, 220);
    private static final Color PANEL_PURPLE   = new Color(255, 190, 220);
    private static final Color PINK_ACCENT    = new Color(190, 90, 220);
    private static final Color SOFT_PINK      = new Color(30, 30, 30);
    private static final Color TEXT_LIGHT     = new Color(30, 30, 35);

    // Modelo de datos
    private static final Map<String, Integer> votos = new HashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::buildUI);
    }

    private static void buildUI() {
        JFrame frame = new JFrame("Encuesta Personalizada y/o Simulada - Utilización de Colecciones + String + Math");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(980, 680);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(BG_DARK_PURPLE);
        root.setBorder(new EmptyBorder(14, 14, 14, 14));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_DARK_PURPLE);

        JLabel title = new JLabel("Procesador de Encuestas Editable Y/O Simulada");
        title.setForeground(SOFT_PINK);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel subtitle = new JLabel("Ingresa 5 nombres y votos para personalizar tus resultados.Ej:videojuego/votos, equipo de la clínica/asistencias y más.");
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);

        // Layout central: izquierda (inputs + resultados) / derecha (gráfico)
        JPanel center = new JPanel(new GridLayout(1, 2, 12, 12));
        center.setBackground(BG_DARK_PURPLE);

        // IZQUIERDA: tabla editable + salida + botones
        JPanel left = new JPanel(new BorderLayout(10, 10));
        left.setBackground(BG_DARK_PURPLE);

        // Tabla editable (5 filas)
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Nombre", "Votos"}, 5) {
            @Override public boolean isCellEditable(int row, int col) { return true; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT_LIGHT);
        table.setBackground(PANEL_PURPLE);
        table.setGridColor(PINK_ACCENT);

        // Header de la tabla
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(PANEL_PURPLE);
        table.getTableHeader().setForeground(SOFT_PINK);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PINK_ACCENT, 2),
                "1) Ingresa 5 Nombres + N° ",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 12),
                SOFT_PINK
        ));
        tableScroll.getViewport().setBackground(PANEL_PURPLE);

        // Área de resultados
        JTextArea output = new JTextArea();
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        output.setFont(new Font("Consolas", Font.PLAIN, 14));
        output.setForeground(TEXT_LIGHT);
        output.setBackground(PANEL_PURPLE);
        output.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane outScroll = new JScrollPane(output);
        outScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PINK_ACCENT, 2),
                "2) Resultados y Análisis",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 12),
                SOFT_PINK
        ));
        outScroll.getViewport().setBackground(PANEL_PURPLE);

        // Botones
        JPanel controls = new JPanel(new GridLayout(1, 4, 10, 10));
        controls.setBackground(BG_DARK_PURPLE);

        JButton btnEjemplo = createButton("Simular ejemplo");
        JButton btnProcesar = createButton("Ver resultados");
        JButton btnLimpiar = createButton("Limpiar");
        JButton btnSalir = createButton("Salir");

        controls.add(btnEjemplo);
        controls.add(btnProcesar);
        controls.add(btnLimpiar);
        controls.add(btnSalir);

        JSplitPane splitLeft = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                tableScroll,
                outScroll
        );

        splitLeft.setResizeWeight(0.6); // 60% tabla, 40% resultados
        splitLeft.setDividerSize(6);

        left.add(splitLeft, BorderLayout.CENTER);
        left.add(controls, BorderLayout.SOUTH);

        // DERECHA: gráfico
        BarChartPanel chartPanel = new BarChartPanel();
        chartPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PINK_ACCENT, 2),
                "3) Gráfico simple (barras)",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 12),
                SOFT_PINK
        ));

        center.add(left);
        center.add(chartPanel);

        // Acciones
        btnEjemplo.addActionListener(e -> {
            model.setValueAt("God of War", 0, 0);
            model.setValueAt("42",        0, 1);

            model.setValueAt("Call of Duty", 1, 0);
            model.setValueAt("27",          1, 1);

            model.setValueAt("League of Legends", 2, 0);
            model.setValueAt("31",               2, 1);

            model.setValueAt("Minecraft", 3, 0);
            model.setValueAt("50",        3, 1);

            model.setValueAt("Age of Empires", 4, 0);
            model.setValueAt("18",             4, 1);

            output.append("✅ Simulación cargada. Edita lo que quieras y presiona \"Ver resultados\".\n\n");
        });

        btnProcesar.addActionListener(e -> {
            // arma el Map desde la tabla (editable en tiempo real)
            votos.clear();

            for (int i = 0; i < model.getRowCount(); i++) {
                Object nombreObj = model.getValueAt(i, 0);
                Object votosObj  = model.getValueAt(i, 1);

                String nombre = (nombreObj == null) ? "" : nombreObj.toString().trim();
                String votosStr = (votosObj == null) ? "" : votosObj.toString().trim();

                if (nombre.isEmpty()) continue; // permite dejar filas vacías

                int v;
                try {
                    v = Integer.parseInt(votosStr);
                    if (v < 0) throw new NumberFormatException("negativo");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Votos inválidos en fila " + (i + 1) + ". Debe ser un entero >= 0.\n" +
                                    "Nombre: \"" + nombre + "\"  | Votos: \"" + votosStr + "\"",
                            "Error de validación",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Si repiten el mismo nombre, sumamos (útil si el usuario se equivoca)
                votos.put(nombre, votos.getOrDefault(nombre, 0) + v);
            }

            if (votos.isEmpty()) {
                output.append("⚠️ No hay datos válidos. Escribe al menos 1 nombre con votos.\n\n");
                chartPanel.setData(Collections.emptyMap());
                return;
            }

            // 2) Orden alfabético: keySet -> list -> sort
            List<String> listaOrdenada = new ArrayList<>(votos.keySet());
            Collections.sort(listaOrdenada, String.CASE_INSENSITIVE_ORDER);

            output.append("📌 Listado en orden Alfabetico (+aviso si contiene 'of')\n");
            output.append("--------------------------------------------------------\n");
            for (String item : listaOrdenada) {
                String mayus = item.toUpperCase();
                boolean contieneOf = item.toLowerCase().contains("of");
                output.append("• " + mayus + (contieneOf ? "  <-- contiene \"OF\"\n" : "\n"));
            }
            output.append("\n");

            // 3) Math: total, porcentajes, top con Math.max (recorrido manual)
            int totalVotos = 0;
            for (int v : votos.values()) totalVotos += v;

            // max votos con Math.max
            int maxVotos = Integer.MIN_VALUE;
            for (int v : votos.values()) {
                maxVotos = Math.max(maxVotos, v);
            }

            // encontrar nombre del top
            String top = null;
            for (String k : votos.keySet()) {
                if (votos.get(k) == maxVotos) {
                    top = k;
                    break;
                }
            }

            output.append("📊 ANÁLISIS DE DATOS (Math)\n");
            output.append("---------------------------\n");
            output.append("Total de votos emitidos: " + totalVotos + "\n\n");
            output.append("Porcentaje por ítem (Math.round):\n");

            for (String item : listaOrdenada) {
                int v = votos.get(item);
                double porcentaje = (totalVotos == 0) ? 0.0 : (v * 100.0) / totalVotos;
                long porcentajeRedondeado = Math.round(porcentaje);
                output.append(String.format("• %-22s -> %3d votos -> %3d%%\n", item, v, porcentajeRedondeado));
            }

            output.append("\n🏆 Más votado: " + top + " (" + maxVotos + " votos)\n\n");

            // actualizar gráfico
            chartPanel.setData(votos);
        });

        btnLimpiar.addActionListener(e -> {
            output.setText("");
            votos.clear();
            chartPanel.setData(Collections.emptyMap());

            // limpia tabla
            for (int r = 0; r < model.getRowCount(); r++) {
                model.setValueAt("", r, 0);
                model.setValueAt("", r, 1);
            }
        });

        btnSalir.addActionListener(e -> frame.dispose());

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    private static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(PINK_ACCENT);
        btn.setForeground(new Color(35, 10, 60));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SOFT_PINK, 2),
                new EmptyBorder(10, 12, 10, 12)
        ));
        return btn;
    }

    // Panel de gráfico de barras (Java.Swing)
    private static class BarChartPanel extends JPanel {
        private Map<String, Integer> data = Collections.emptyMap();

        BarChartPanel() {
            setBackground(BG_DARK_PURPLE);
        }

        void setData(Map<String, Integer> newData) {
            // copiamos para no depender de referencias externas
            this.data = (newData == null) ? Collections.emptyMap() : new HashMap<>(newData);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fondo interior
            g2.setColor(PANEL_PURPLE);
            int pad = 18;
            int w = getWidth() - pad * 2;
            int h = getHeight() - pad * 2;
            g2.fillRoundRect(pad, pad, w, h, 18, 18);

            if (data == null || data.isEmpty()) {
                g2.setColor(TEXT_LIGHT);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                g2.drawString("Sin datos. Ingresa desde 1 nombre y voto " + "luego 'Ver resultados'", pad + 14, pad + 30);
                g2.dispose();
                return;
            }

            // Orden alfabético para que coincida con salida
            List<String> keys = new ArrayList<>(data.keySet());
            keys.sort(String.CASE_INSENSITIVE_ORDER);

            int max = 0;
            for (int v : data.values()) max = Math.max(max, v);
            if (max == 0) max = 1;

            // Área de barras
            int chartX = pad + 20;
            int chartY = pad + 50;
            int chartW = w - 40;
            int chartH = h - 80;

            // Ejes simples
            g2.setColor(SOFT_PINK);
            g2.drawLine(chartX, chartY + chartH, chartX + chartW, chartY + chartH);

            int n = keys.size();
            int barGap = 10;
            int barW = Math.max(12, (chartW - (barGap * (n + 1))) / n);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            for (int i = 0; i < n; i++) {
                String k = keys.get(i);
                int v = data.get(k);

                double ratio = v / (double) max;
                int barH = (int) Math.round(ratio * (chartH - 10));

                int x = chartX + barGap + i * (barW + barGap);
                int y = chartY + chartH - barH;

                // barra (rosado)
                g2.setColor(PINK_ACCENT);
                g2.fillRoundRect(x, y, barW, barH, 10, 10);

                // valor arriba
                g2.setColor(TEXT_LIGHT);
                g2.drawString(String.valueOf(v), x + 3, y - 4);

                // etiqueta abajo (recortada)
                String label = k.length() > 10 ? k.substring(0, 10) + "…" : k;
                g2.drawString(label, x, chartY + chartH + 16);
            }

            g2.dispose();
        }
    }
}