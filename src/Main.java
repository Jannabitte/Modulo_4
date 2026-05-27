import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.LinkedHashSet;

public class Main {

    // ======== MODELO EN MEMORIA (List/Set/Map) ========
    private static final List<String> inscritos = new ArrayList<>();
    private static final Map<String, Paciente> pacientes = new LinkedHashMap<>();
    private static final Map<String, List<Sesion>> sesionesPorPaciente = new LinkedHashMap<>();

    // ======== UI ========
    private JFrame frame;
    private JComboBox<String> cbPacientes;
    private DefaultTableModel modelPacientes;
    private DefaultTableModel modelSesiones;

    // NUEVO: barra de progreso animada
    private ProgressPanel progressPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().start());
    }

    private void start() {
        frame = new JFrame("TAA Vet - App Demo (List/Set/Map) + Animación");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1020, 680);
        frame.setLocationRelativeTo(null);

        frame.setContentPane(buildUI());
        seedDemoData();
        refreshAll();

        frame.setVisible(true);
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        root.setBackground(new Color(248, 248, 255));

        AnimatedHeader header = new AnimatedHeader();
        root.add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("1) Registro Paciente", buildPanelRegistroPaciente());
        tabs.addTab("2) Registrar Sesión", buildPanelRegistrarSesion());
        tabs.addTab("3) Tableros", buildPanelTableros());

        root.add(tabs, BorderLayout.CENTER);
        return root;
    }

    // ===================== TAB 1: REGISTRO PACIENTE =====================
    private JPanel buildPanelRegistroPaciente() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtNombre = new JTextField(20);
        JComboBox<String> cbDiscapacidad = new JComboBox<>(new String[]{
                "TEA (Autismo)", "Síndrome de Down", "Discapacidad motora", "Discapacidad visual",
                "Discapacidad auditiva", "Adulto mayor", "Otra"
        });
        JComboBox<String> cbTerapia = new JComboBox<>(new String[]{
                "Canoterapia", "Equinoterapia", "Terapia con conejo", "Terapia con gato", "Otra"
        });
        JComboBox<String> cbAnimal = new JComboBox<>(new String[]{
                "Perro", "Caballo", "Conejo", "Gato"
        });

        PulseBadge badge = new PulseBadge("LISTO", new Color(35, 130, 70));

        JButton btnGuardar = new JButton("Registrar Paciente");
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                msg("Falta el nombre del paciente.");
                badge.setMode("FALTA NOMBRE", new Color(165, 60, 60));
                return;
            }

            if (pacientes.containsKey(nombre)) {
                int r = JOptionPane.showConfirmDialog(frame,
                        "Ya existe un paciente con ese nombre. ¿Sobrescribir perfil?",
                        "Confirmar", JOptionPane.YES_NO_OPTION);
                if (r != JOptionPane.YES_OPTION) return;
            }

            Paciente pac = new Paciente(
                    nombre,
                    (String) cbDiscapacidad.getSelectedItem(),
                    (String) cbTerapia.getSelectedItem(),
                    (String) cbAnimal.getSelectedItem()
            );

            pacientes.put(nombre, pac);
            sesionesPorPaciente.putIfAbsent(nombre, new ArrayList<>());
            inscritos.add(nombre);

            txtNombre.setText("");
            refreshAll();
            badge.setMode("GUARDADO", new Color(35, 130, 70));
            msg("Paciente registrado: " + nombre);
        });

        int row = 0;
        c.gridx = 0; c.gridy = row; form.add(new JLabel("Nombre paciente:"), c);
        c.gridx = 1; c.gridy = row++; form.add(txtNombre, c);

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Tipo de discapacidad:"), c);
        c.gridx = 1; c.gridy = row++; form.add(cbDiscapacidad, c);

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Tipo de terapia:"), c);
        c.gridx = 1; c.gridy = row++; form.add(cbTerapia, c);

        c.gridx = 0; c.gridy = row; form.add(new JLabel("Animal asistente:"), c);
        c.gridx = 1; c.gridy = row++; form.add(cbAnimal, c);

        c.gridx = 1; c.gridy = row; form.add(btnGuardar, c);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.add(form, BorderLayout.CENTER);
        top.add(badge, BorderLayout.EAST);

        p.add(top, BorderLayout.NORTH);

        JTextArea info = new JTextArea(
                "Innovación sugerida: barra de progreso por paciente según evolución.\n" +
                        "La verás en la pestaña 'Tableros' como panel animado.\n"
        );
        info.setEditable(false);
        info.setBackground(new Color(245, 245, 250));
        info.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.add(info, BorderLayout.CENTER);

        return p;
    }

    // ===================== TAB 2: REGISTRAR SESION =====================
    private JPanel buildPanelRegistrarSesion() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.setBackground(Color.WHITE);

        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        cbPacientes = new JComboBox<>(new String[]{});
        cbPacientes.addActionListener(e -> refreshAll()); // para actualizar barra al cambiar

        JTextField txtFecha = new JTextField("2026-03-03", 10);

        JSpinner spSocial = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner spEmocional = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        JSpinner spMotricidad = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

        PulseBadge badge = new PulseBadge("OK", new Color(40, 120, 160));

        JButton btnGuardarSesion = new JButton("Guardar Sesión");
        btnGuardarSesion.addActionListener(e -> {
            String nombre = (String) cbPacientes.getSelectedItem();
            if (nombre == null || nombre.isEmpty()) {
                badge.setMode("SIN PACIENTE", new Color(165, 60, 60));
                msg("No hay pacientes registrados.");
                return;
            }

            int social = (Integer) spSocial.getValue();
            int emocional = (Integer) spEmocional.getValue();
            int motricidad = (Integer) spMotricidad.getValue();

            int[] indicadores = {social, emocional, motricidad};
            double suma = 0;
            for (int v : indicadores) suma += v;
            double promedio = suma / 3.0;

            Sesion s = new Sesion(txtFecha.getText().trim(), social, emocional, motricidad, promedio);
            sesionesPorPaciente.putIfAbsent(nombre, new ArrayList<>());
            sesionesPorPaciente.get(nombre).add(s);

            refreshAll();

            if (promedio < 5) {
                badge.setMode("ALERTA", new Color(165, 60, 60));
                msg("Sesión guardada. Promedio = " + String.format(Locale.US, "%.2f", promedio) + "  ⚠️ Revaluar plan.");
            } else {
                badge.setMode("OK", new Color(40, 120, 160));
                msg("Sesión guardada. Promedio = " + String.format(Locale.US, "%.2f", promedio) + " ✅");
            }
        });

        int row = 0;
        c.gridx = 0; c.gridy = row; top.add(new JLabel("Paciente:"), c);
        c.gridx = 1; c.gridy = row++; top.add(cbPacientes, c);

        c.gridx = 0; c.gridy = row; top.add(new JLabel("Fecha (texto):"), c);
        c.gridx = 1; c.gridy = row++; top.add(txtFecha, c);

        c.gridx = 0; c.gridy = row; top.add(new JLabel("Interacción social (0-10):"), c);
        c.gridx = 1; c.gridy = row++; top.add(spSocial, c);

        c.gridx = 0; c.gridy = row; top.add(new JLabel("Regulación emocional (0-10):"), c);
        c.gridx = 1; c.gridy = row++; top.add(spEmocional, c);

        c.gridx = 0; c.gridy = row; top.add(new JLabel("Motricidad fina (0-10):"), c);
        c.gridx = 1; c.gridy = row++; top.add(spMotricidad, c);

        c.gridx = 1; c.gridy = row; top.add(btnGuardarSesion, c);

        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(Color.WHITE);
        north.add(top, BorderLayout.CENTER);
        north.add(badge, BorderLayout.EAST);

        p.add(north, BorderLayout.NORTH);

        JTextArea info = new JTextArea(
                "La barra de progreso (Tableros) se anima al guardar sesión.\n" +
                        "Se calcula desde el promedio de la ÚLTIMA sesión del paciente seleccionado.\n"
        );
        info.setEditable(false);
        info.setBackground(new Color(245, 245, 250));
        info.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.add(info, BorderLayout.CENTER);

        return p;
    }

    // ===================== TAB 3: TABLEROS =====================
    private JPanel buildPanelTableros() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.setBackground(Color.WHITE);

        modelPacientes = new DefaultTableModel(
                new Object[]{"Paciente", "Discapacidad", "Terapia", "Animal", "Sesiones", "Promedio Última"}, 0
        ) { @Override public boolean isCellEditable(int row, int column) { return false; } };

        JTable tPac = new JTable(modelPacientes);
        tPac.setRowHeight(24);

        modelSesiones = new DefaultTableModel(
                new Object[]{"Paciente", "Fecha", "Social", "Emocional", "Motricidad", "Promedio"}, 0
        ) { @Override public boolean isCellEditable(int row, int column) { return false; } };

        JTable tSes = new JTable(modelSesiones);
        tSes.setRowHeight(24);

        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> refreshAll());

        JButton btnVerUnicos = new JButton("Ver Únicos (Set)");
        btnVerUnicos.addActionListener(e -> {
            Set<String> unicos = new LinkedHashSet<>(inscritos);
            StringBuilder sb = new StringBuilder();
            sb.append("Inscritos (List): ").append(inscritos.size()).append(" (permite duplicados)\n");
            sb.append("Únicos (Set): ").append(unicos.size()).append(" (sin duplicados)\n\n");
            int i = 1;
            for (String n : unicos) sb.append(i++).append(") ").append(n).append("\n");
            JOptionPane.showMessageDialog(frame, sb.toString(), "Set de únicos", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setBackground(Color.WHITE);
        actions.add(btnRefrescar);
        actions.add(btnVerUnicos);

        // NUEVO: barra animada abajo
        progressPanel = new ProgressPanel();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tPac),
                new JScrollPane(tSes));
        split.setResizeWeight(0.55);

        p.add(actions, BorderLayout.NORTH);
        p.add(split, BorderLayout.CENTER);
        p.add(progressPanel, BorderLayout.SOUTH);

        return p;
    }

    // ===================== REFRESH =====================
    private void refreshAll() {
        refreshComboPacientes();
        refreshTablaPacientes();
        refreshTablaSesiones();
        refreshProgreso();
    }

    private void refreshComboPacientes() {
        if (cbPacientes == null) return;
        Object selected = cbPacientes.getSelectedItem();

        cbPacientes.removeAllItems();
        for (String nombre : pacientes.keySet()) cbPacientes.addItem(nombre);

        if (selected != null) cbPacientes.setSelectedItem(selected);
    }

    private void refreshTablaPacientes() {
        if (modelPacientes == null) return;
        modelPacientes.setRowCount(0);

        for (Paciente p : pacientes.values()) {
            List<Sesion> ses = sesionesPorPaciente.getOrDefault(p.nombre, new ArrayList<>());
            int n = ses.size();
            String lastAvg = (n == 0) ? "-" : String.format(Locale.US, "%.2f", ses.get(n - 1).promedio);

            modelPacientes.addRow(new Object[]{
                    p.nombre, p.discapacidad, p.terapia, p.animal, n, lastAvg
            });
        }
    }

    private void refreshTablaSesiones() {
        if (modelSesiones == null || cbPacientes == null) return;
        modelSesiones.setRowCount(0);

        String nombre = (String) cbPacientes.getSelectedItem();
        if (nombre == null) return;

        List<Sesion> ses = sesionesPorPaciente.getOrDefault(nombre, new ArrayList<>());
        for (Sesion s : ses) {
            modelSesiones.addRow(new Object[]{
                    nombre, s.fecha, s.social, s.emocional, s.motricidad, String.format(Locale.US, "%.2f", s.promedio)
            });
        }
    }

    // NUEVO: actualiza barra con el promedio de la ÚLTIMA sesión del paciente seleccionado
    private void refreshProgreso() {
        if (progressPanel == null || cbPacientes == null) return;

        String nombre = (String) cbPacientes.getSelectedItem();
        if (nombre == null) {
            progressPanel.setTarget("Sin paciente", 0);
            return;
        }

        List<Sesion> ses = sesionesPorPaciente.getOrDefault(nombre, new ArrayList<>());
        if (ses.isEmpty()) {
            progressPanel.setTarget(nombre + " (sin sesiones)", 0);
            return;
        }

        double lastAvg = ses.get(ses.size() - 1).promedio; // 0..10
        int percent = (int) Math.round((lastAvg / 10.0) * 100.0);
        progressPanel.setTarget(nombre + " - Progreso última sesión", percent);
    }

    // ===================== DEMO DATA =====================
    private void seedDemoData() {
        addPaciente(new Paciente("Matías", "TEA (Autismo)", "Canoterapia", "Perro"));
        addPaciente(new Paciente("Camila", "Discapacidad motora", "Equinoterapia", "Caballo"));
        addPaciente(new Paciente("Tomás", "Síndrome de Down", "Terapia con conejo", "Conejo"));

        inscritos.add("Matías");
        inscritos.add("Camila");
        inscritos.add("Matías");
        inscritos.add("Tomás");

        sesionesPorPaciente.get("Matías").add(new Sesion("2026-03-01", 7, 6, 5, (7 + 6 + 5) / 3.0));
        sesionesPorPaciente.get("Camila").add(new Sesion("2026-03-02", 9, 8, 7, (9 + 8 + 7) / 3.0));
        sesionesPorPaciente.get("Tomás").add(new Sesion("2026-03-02", 4, 4, 5, (4 + 4 + 5) / 3.0));
    }

    private void addPaciente(Paciente p) {
        pacientes.put(p.nombre, p);
        sesionesPorPaciente.putIfAbsent(p.nombre, new ArrayList<>());
    }

    private void msg(String m) {
        JOptionPane.showMessageDialog(frame, m);
    }

    // ===================== MODELOS =====================
    private static class Paciente {
        String nombre, discapacidad, terapia, animal;
        Paciente(String nombre, String discapacidad, String terapia, String animal) {
            this.nombre = nombre; this.discapacidad = discapacidad; this.terapia = terapia; this.animal = animal;
        }
    }

    private static class Sesion {
        String fecha; int social, emocional, motricidad; double promedio;
        Sesion(String fecha, int social, int emocional, int motricidad, double promedio) {
            this.fecha = fecha; this.social = social; this.emocional = emocional; this.motricidad = motricidad; this.promedio = promedio;
        }
    }

    // ===================== ANIMACIÓN 1: HEADER =====================
    private static class AnimatedHeader extends JPanel {
        private float t = 0f;
        private final java.util.List<Paw> paws = new ArrayList<>();
        private final Timer timer;

        AnimatedHeader() {
            setPreferredSize(new Dimension(1000, 90));
            setOpaque(false);

            Random r = new Random();
            for (int i = 0; i < 10; i++) {
                paws.add(new Paw(-r.nextInt(600), 25 + r.nextInt(40), 1.5f + r.nextFloat() * 2.5f, 10 + r.nextInt(16)));
            }

            timer = new Timer(16, e -> {
                t += 0.012f;
                for (Paw p : paws) {
                    p.x += p.v;
                    if (p.x > getWidth() + 40) p.x = -60;
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            float shift = (float) (Math.sin(t) * 0.15 + 0.5);
            Color c1 = blend(new Color(255, 236, 245), new Color(235, 245, 255), shift);
            Color c2 = blend(new Color(230, 230, 255), new Color(255, 230, 240), 1 - shift);

            GradientPaint gp = new GradientPaint(0, 0, c1, w, h, c2);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w, h, 22, 22);

            g2.setColor(new Color(35, 35, 55));
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            g2.drawString("TAA Vet - Terapias Asistidas con Animales", 18, 34);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.setColor(new Color(60, 60, 80));
            g2.drawString("Barra de progreso animada por paciente (última sesión) + List/Set/Map", 18, 56);

            for (Paw p : paws) drawPaw(g2, (int) p.x, p.y, p.size);

            g2.dispose();
        }

        private void drawPaw(Graphics2D g2, int x, int y, int size) {
            g2.setColor(new Color(90, 90, 120, 80));
            int pad = size / 2;

            g2.fillOval(x, y, size + 6, size + 4);

            g2.fillOval(x - pad, y - pad, size / 2 + 3, size / 2 + 3);
            g2.fillOval(x + size / 2, y - pad - 1, size / 2 + 3, size / 2 + 3);
            g2.fillOval(x + size + 1, y, size / 2 + 3, size / 2 + 3);
            g2.fillOval(x + size / 2 + 1, y + size / 2 + 1, size / 2 + 3, size / 2 + 3);
        }

        private Color blend(Color a, Color b, float t) {
            t = Math.max(0, Math.min(1, t));
            int r = (int) (a.getRed() + (b.getRed() - a.getRed()) * t);
            int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
            int bl = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t);
            return new Color(r, g, bl);
        }

        private static class Paw {
            float x, v;
            int y, size;
            Paw(float x, int y, float v, int size) { this.x = x; this.y = y; this.v = v; this.size = size; }
        }
    }

    // ===================== ANIMACIÓN 2: BADGE PULSANTE =====================
    private static class PulseBadge extends JPanel {
        private String text;
        private Color base;
        private float phase = 0f;
        private final Timer timer;

        PulseBadge(String text, Color base) {
            this.text = text;
            this.base = base;
            setPreferredSize(new Dimension(150, 64));
            setOpaque(false);

            timer = new Timer(16, e -> {
                phase += 0.06f;
                repaint();
            });
            timer.start();
        }

        void setMode(String text, Color base) {
            this.text = text;
            this.base = base;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            float pulse = (float) (0.5 + 0.5 * Math.sin(phase)); // 0..1
            int glow = (int) (10 + 10 * pulse);

            g2.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), 50));
            g2.fillRoundRect(10 - glow / 2, 10 - glow / 2, w - 20 + glow, h - 20 + glow, 22, 22);

            g2.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), 220));
            g2.fillRoundRect(10, 10, w - 20, h - 20, 18, 18);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(text)) / 2;
            int ty = (h + fm.getAscent()) / 2 - 3;
            g2.drawString(text, tx, ty);

            g2.dispose();
        }
    }

    // ===================== ANIMACIÓN 3: BARRA DE PROGRESO =====================
    private static class ProgressPanel extends JPanel {
        private int current = 0; // 0..100 (animado)
        private int target = 0;  // 0..100
        private String label = "Sin datos";
        private final Timer timer;

        ProgressPanel() {
            setPreferredSize(new Dimension(1000, 74));
            setOpaque(false);
            setBorder(new EmptyBorder(8, 8, 8, 8));

            timer = new Timer(16, e -> {
                if (current == target) return;
                int delta = target - current;
                int step = Math.max(1, Math.abs(delta) / 12); // easing simple
                current += (delta > 0) ? step : -step;

                // Clamp
                if ((delta > 0 && current > target) || (delta < 0 && current < target)) current = target;
                repaint();
            });
            timer.start();
        }

        void setTarget(String label, int percent) {
            this.label = label;
            this.target = Math.max(0, Math.min(100, percent));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            // Contenedor
            g2.setColor(new Color(245, 245, 250));
            g2.fillRoundRect(0, 0, w, h, 18, 18);

            // Texto superior
            g2.setColor(new Color(35, 35, 55));
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString(label, 12, 20);

            String pctText = current + "%";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(pctText, w - fm.stringWidth(pctText) - 12, 20);

            // Barra
            int barX = 12, barY = 30, barW = w - 24, barH = 22;
            g2.setColor(new Color(225, 225, 235));
            g2.fillRoundRect(barX, barY, barW, barH, 14, 14);

            int fillW = (int) Math.round(barW * (current / 100.0));

            // Color según tramo (sin hardcode loco)
            Color fill;
            if (current >= 80) fill = new Color(35, 130, 70);
            else if (current >= 50) fill = new Color(40, 120, 160);
            else fill = new Color(165, 60, 60);

            g2.setColor(new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), 220));
            g2.fillRoundRect(barX, barY, Math.max(14, fillW), barH, 14, 14);

            // Indicador circular que se mueve con la barra
            int dotX = barX + fillW;
            dotX = Math.max(barX + 8, Math.min(barX + barW - 8, dotX));
            int dotY = barY + barH / 2;

            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillOval(dotX - 9, dotY - 9, 18, 18);

            g2.setColor(Color.WHITE);
            g2.fillOval(dotX - 7, dotY - 7, 14, 14);

            g2.setColor(fill);
            g2.fillOval(dotX - 5, dotY - 5, 10, 10);

            g2.dispose();
        }
    }
}