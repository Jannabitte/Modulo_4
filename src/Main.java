import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Main {

    // ====== COLORES
    private static final Color BG_DARK_PURPLE = new Color(200, 220, 250);
    private static final Color PANEL_PURPLE   = new Color(200, 200, 250);
    private static final Color PINK_ACCENT    = new Color(200, 190, 220);
    private static final Color SOFT_PINK      = new Color(205, 190, 220);
    private static final Color TEXT_LIGHT     = new Color(30, 20, 25);

    // ====== SEGURIDAD ======
    private static final String ADMIN_PIN = "2026"; // cambiar PIN aquí

    // ====== DATA ======
    private final Map<String, Student> studentsById = new HashMap<>();

    // ====== UI ======
    private JTextArea output;
    private DefaultTableModel tableModel;

    // Inputs agregar
    private JTextField tfId;
    private JTextField tfName;
    private JTextField tfNotes; // "5.0, 4.2, 6.1"

    // Inputs buscar
    private JTextField tfSearchId;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().buildUI());
    }

    private void buildUI() {
        JFrame frame = new JFrame("Sistema de Notas Estudiantes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(BG_DARK_PURPLE);
        root.setBorder(new EmptyBorder(14, 14, 14, 14));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_DARK_PURPLE);

        JLabel title = new JLabel("TrabajoJannabitte: Map + Set + List + String + Math - Swing(visual) ");
        title.setForeground(SOFT_PINK);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel subtitle = new JLabel("Escala 1.0 a 7.0 | Edición protegida por PIN (4 dígitos)");
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);

        // Centro: izquierda (panel acciones) / derecha (tabla + salida)
        JPanel center = new JPanel(new GridLayout(1, 2, 12, 12));
        center.setBackground(BG_DARK_PURPLE);

        JPanel left = new JPanel();
        left.setBackground(BG_DARK_PURPLE);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JPanel right = new JPanel(new BorderLayout(10, 10));
        right.setBackground(BG_DARK_PURPLE);

        // ====== PANEL: Agregar estudiante ======
        JPanel addPanel = cardPanel("1) Agregar estudiante");
        addPanel.setLayout(new GridLayout(4, 2, 8, 8));

        tfId = field();
        tfName = field();
        tfNotes = field();

        addPanel.add(label("ID / Rut :"));
        addPanel.add(tfId);

        addPanel.add(label("Nombre:"));
        addPanel.add(tfName);

        addPanel.add(label("Notas (ej: 5.5,4.0,6.1):"));
        addPanel.add(tfNotes);

        JButton btnAdd = button("Agregar Alumno ");
        JButton btnSim = button("Simular");
        addPanel.add(btnSim);
        addPanel.add(btnAdd);

        // ====== PANEL: Buscar / Editar ======
        JPanel searchPanel = cardPanel("4) Buscar estudiante (sin PIN)");
        searchPanel.setLayout(new GridLayout(3, 2, 8, 8));

        tfSearchId = field();
        JButton btnSearch = button("Buscar");
        JButton btnEdit = button("Editar (PIN)");
        JButton btnDelete = button("Eliminar (PIN)");

        searchPanel.add(label("Consultar ID "));
        searchPanel.add(tfSearchId);

        searchPanel.add(btnSearch);
        searchPanel.add(btnEdit);

        searchPanel.add(btnDelete);
        searchPanel.add(new JLabel("Solo Personal Autorizado cuenta con PIN de Edición"));

        // ====== PANEL: Acciones rápidas ======
        JPanel actionsPanel = cardPanel("Acciones Rapidas ");
        actionsPanel.setLayout(new GridLayout(4, 2, 8, 8));

        JButton btnShow = button(" Mostrar estudiantes");
        JButton btnAvg = button(" Área promedios");
        JButton btnUnique = button(" Mostrar únicos");
        JButton btnMap = button(" Mostrar mapa");
        JButton btnClear = button("Limpiar salida");
        JButton btnExit = button(" Salir");

        actionsPanel.add(btnShow);
        actionsPanel.add(btnAvg);
        actionsPanel.add(btnUnique);
        actionsPanel.add(btnMap);
        actionsPanel.add(btnClear);
        actionsPanel.add(btnExit);

        left.add(addPanel);
        left.add(Box.createVerticalStrut(10));
        left.add(searchPanel);
        left.add(Box.createVerticalStrut(10));
        left.add(actionsPanel);

        // ====== TABLA (derecha arriba) ======
        tableModel = new DefaultTableModel(new Object[]{"ID", "NOMBRE", "NOTAS", "PROM", "ESTADO"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        styleTable(table);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(titled("Estudiantes (vista)"));
        tableScroll.getViewport().setBackground(PANEL_PURPLE);

        // ====== SALIDA (derecha abajo) ======
        output = new JTextArea();
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        output.setFont(new Font("Consolas", Font.PLAIN, 15));
        output.setForeground(TEXT_LIGHT);
        output.setBackground(PANEL_PURPLE);
        output.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane outScroll = new JScrollPane(output);
        outScroll.setBorder(titled("Análisis"));

        // agrandar análisis
        JSplitPane splitRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, outScroll);
        splitRight.setResizeWeight(0.55);
        splitRight.setDividerSize(6);

        right.add(splitRight, BorderLayout.CENTER);

        center.add(left);
        center.add(right);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);

        frame.setContentPane(root);
        frame.setVisible(true);

        // ====== EVENTOS ======
        btnSim.addActionListener(e -> simulateData());
        btnAdd.addActionListener(e -> addStudentFromInputs());

        btnShow.addActionListener(e -> showStudents());
        btnAvg.addActionListener(e -> averagesArea());
        btnUnique.addActionListener(e -> showUniques());
        btnMap.addActionListener(e -> showMap());

        btnSearch.addActionListener(e -> searchStudent(false));
        btnEdit.addActionListener(e -> editStudentWithPin(frame));
        btnDelete.addActionListener(e -> deleteStudentWithPin(frame));

        btnClear.addActionListener(e -> output.setText(""));
        btnExit.addActionListener(e -> frame.dispose());

        refreshTable();
    }

    // =================== LOGICA ===================

    private void simulateData() {
        studentsById.clear();
        studentsById.put("A1101", new Student("A1101", "Perez Alejandra", Arrays.asList(5.5, 4.8, 6.1)));
        studentsById.put("A1102", new Student("A1102", "Diaz Angel", Arrays.asList(3.2, 3.9, 4.0)));
        studentsById.put("A1103", new Student("A1103", "Pino Jannabitte", Arrays.asList(6.0, 6.5, 6.2, 5.9)));
        studentsById.put("A1104", new Student("A1104", "Soto Matias", Arrays.asList(4.0, 4.1)));
        studentsById.put("A1105", new Student("A1105", "Ruiz Natalia", Arrays.asList(2.0, 5.0, 3.5)));

        output.append("✅ Simulación cargada.\n\n");
        refreshTable();
    }

    private void addStudentFromInputs() {
        String id = normalizeId(tfId.getText());
        String name = normalizeName(tfName.getText());
        String notesRaw = tfNotes.getText().trim();

        if (id.isEmpty()) { msg("⚠️ ID vacío."); return; }
        if (name.isEmpty()) { msg("⚠️ Nombre vacío."); return; }

        List<Double> notes;
        try {
            notes = parseNotes(notesRaw);
        } catch (IllegalArgumentException ex) {
            msg("⚠️ Notas inválidas: " + ex.getMessage());
            return;
        }

        if (studentsById.containsKey(id)) {
            msg("⚠️ Ya existe un estudiante con ID " + id + ". Usa 'Buscar' y 'Editar (PIN)'.");
            return;
        }

        studentsById.put(id, new Student(id, name, notes));
        msg("✅ Estudiante agregado " + id + " - " + name);
        tfId.setText("");
        tfName.setText("");
        tfNotes.setText("");
        refreshTable();
    }

    private void showStudents() {
        if (studentsById.isEmpty()) { msg("⚠️ No hay estudiantes."); return; }

        // List para ordenar por nombre (String + Collections)
        List<Student> list = new ArrayList<>(studentsById.values());
        list.sort(Comparator.comparing(s -> s.name.toUpperCase()));

        output.append("📋 2) MOSTRAR ESTUDIANTES (ordenados por nombre)\n");
        output.append("------------------------------------------------\n");
        for (Student s : list) {
            String flagOf = s.name.toLowerCase().contains("of") ? " (contiene \"of\")" : "";
            output.append(String.format("• %s | %s%s | PROM: %.1f | %s\n",
                    s.id, s.name.toUpperCase(), flagOf, s.avgRounded1(), s.status()));
        }
        output.append("\n");
    }

    private void averagesArea() {
        if (studentsById.isEmpty()) { msg("⚠️ No hay estudiantes."); return; }

        List<Student> list = new ArrayList<>(studentsById.values());

        // promedio general (Math)
        double sum = 0;
        int count = 0;
        int rojos = 0;
        int ok = 0;

        double best = -1;
        double worst = 999;
        Student bestS = null;
        Student worstS = null;

        for (Student s : list) {
            double a = s.avg();
            sum += a;
            count++;

            if (a < 4.0) rojos++; else ok++;

            // Math.max / Math.min manual
            if (Math.max(best, a) == a) { best = a; bestS = s; }
            if (Math.min(worst, a) == a) { worst = a; worstS = s; }
        }

        double general = (count == 0) ? 0 : sum / count;
        double generalR1 = round1(general);

        long pctRojos = Math.round(rojos * 100.0 / count);
        long pctOk = Math.round(ok * 100.0 / count);

        output.append("📊 3) ÁREA DE PROMEDIOS\n");
        output.append("-----------------------\n");
        output.append("Promedio general del curso: " + generalR1 + "\n");
        output.append("Aprobados (>=4.0): " + ok + " (" + pctOk + "%)\n");
        output.append("Rojos (<4.0): " + rojos + " (" + pctRojos + "%)\n");

        if (bestS != null) output.append("Promedio Más Alto: " + bestS.name + " (" + round1(bestS.avg()) + ")\n");
        if (worstS != null) output.append("Promedio Más Bajo: " + worstS.name + " (" + round1(worstS.avg()) + ")\n");

        output.append("\n");
    }

    private void showUniques() {
        if (studentsById.isEmpty()) { msg("⚠️ No hay estudiantes."); return; }

        // Únicos por ID (Set desde el Map)
        Set<String> ids = new HashSet<>(studentsById.keySet());
        List<String> idsSorted = new ArrayList<>(ids);
        idsSorted.sort(String.CASE_INSENSITIVE_ORDER);

        // Únicos por nombre normalizado
        Set<String> namesUnique = new HashSet<>();
        for (Student s : studentsById.values()) namesUnique.add(normalizeName(s.name));
        List<String> namesSorted = new ArrayList<>(namesUnique);
        namesSorted.sort(String.CASE_INSENSITIVE_ORDER);

        output.append("🧩 5) MOSTRAR ÚNICOS\n");
        output.append("--------------------\n");
        output.append("Únicos por ID:\n");
        for (String id : idsSorted) output.append("• " + id + "\n");

        output.append("\nÚnicos por NOMBRE (normalizado):\n");
        for (String n : namesSorted) output.append("• " + n + "\n");
        output.append("\n");
    }

    private void showMap() {
        if (studentsById.isEmpty()) { msg("⚠️ No hay estudiantes."); return; }

        // Mapa ordenado (TreeMap)
        Map<String, Student> sorted = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        sorted.putAll(studentsById);

        output.append("🗺️ 6) MOSTRAR MAPA DE ESTUDIANTES (ID → datos)\n");
        output.append("--------------------------------------------\n");
        for (Map.Entry<String, Student> e : sorted.entrySet()) {
            Student s = e.getValue();
            output.append(String.format("%s -> %s | PROM %.1f | %s\n",
                    e.getKey(), s.name, s.avgRounded1(), s.status()));
        }
        output.append("\n");
    }

    private void searchStudent(boolean logHeader) {
        String id = normalizeId(tfSearchId.getText());
        if (id.isEmpty()) { msg("⚠️ Ingresa un ID para buscar."); return; }

        Student s = studentsById.get(id);
        if (s == null) {
            msg("❌ No existe estudiante con ID: " + id);
            return;
        }

        if (logHeader) {
            output.append("🔎 4) BUSCAR ESTUDIANTE\n");
            output.append("----------------------\n");
        }

        output.append("Encontrado:\n");
        output.append("ID: " + s.id + "\n");
        output.append("Nombre: " + s.name.toUpperCase() + (s.name.toLowerCase().contains("of") ? " (contiene \"of\")" : "") + "\n");
        output.append("Notas: " + s.notes + "\n");
        output.append("Promedio: " + s.avgRounded1() + "\n");
        output.append("Estado: " + s.status() + "\n\n");
    }

    private void editStudentWithPin(Component parent) {
        String id = normalizeId(tfSearchId.getText());
        if (id.isEmpty()) { msg("⚠️ Ingresa un ID para editar."); return; }
        Student s = studentsById.get(id);
        if (s == null) { msg("❌ No existe estudiante con ID: " + id); return; }

        if (!pinGate(parent)) {
            msg("❌ PIN incorrecto. Edición cancelada.");
            return;
        }

        // Diálogo simple de edición
        String newName = JOptionPane.showInputDialog(parent, "Nuevo nombre (deja vacío para no cambiar):", s.name);
        if (newName != null) {
            newName = normalizeName(newName);
            if (!newName.isEmpty()) s.name = newName;
        }

        String newNotes = JOptionPane.showInputDialog(parent, "Nuevas notas (ej: 5.0,4.2,6.1)\nDeja vacío para no cambiar:", joinNotes(s.notes));
        if (newNotes != null) {
            newNotes = newNotes.trim();
            if (!newNotes.isEmpty()) {
                try {
                    s.notes = parseNotes(newNotes);
                } catch (IllegalArgumentException ex) {
                    msg("⚠️ No se actualizaron notas: " + ex.getMessage());
                }
            }
        }

        msg("✅ Estudiante actualizado: " + s.id);
        refreshTable();
        searchStudent(true);
    }

    private void deleteStudentWithPin(Component parent) {
        String id = normalizeId(tfSearchId.getText());
        if (id.isEmpty()) { msg("⚠️ Ingresa un ID para eliminar."); return; }
        Student s = studentsById.get(id);
        if (s == null) { msg("❌ No existe estudiante con ID: " + id); return; }

        if (!pinGate(parent)) {
            msg("❌ PIN incorrecto. Eliminación cancelada.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(parent,
                "¿Eliminar a " + s.name + " (" + s.id + ")?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            studentsById.remove(id);
            msg("🗑️ Estudiante eliminado: " + id);
            refreshTable();
        } else {
            msg("✅ Eliminación cancelada.");
        }
    }

    // =================== UTILIDADES ===================

    private void refreshTable() {
        tableModel.setRowCount(0);

        List<Student> list = new ArrayList<>(studentsById.values());
        list.sort(Comparator.comparing(st -> st.id.toUpperCase()));

        for (Student s : list) {
            tableModel.addRow(new Object[]{
                    s.id,
                    s.name,
                    joinNotes(s.notes),
                    String.format("%.1f", s.avgRounded1()),
                    s.status()
            });
        }
    }

    private boolean pinGate(Component parent) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            String pin = JOptionPane.showInputDialog(parent, "Ingrese PIN de 4 dígitos (intento " + attempt + " de 3):");
            if (pin == null) return false; // cancelado

            pin = pin.trim();
            if (!pin.matches("\\d{4}")) {
                JOptionPane.showMessageDialog(parent, "El PIN debe tener exactamente 4 dígitos.", "PIN inválido", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (pin.equals(ADMIN_PIN)) return true;
        }
        return false;
    }

    private List<Double> parseNotes(String raw) {
        if (raw == null || raw.trim().isEmpty()) throw new IllegalArgumentException("debes ingresar al menos 1 nota.");

        String[] parts = raw.split(",");
        List<Double> notes = new ArrayList<>();

        for (String p : parts) {
            String s = p.trim().replace(",", "."); // por si pegan con coma rara
            if (s.isEmpty()) continue;

            double v;
            try {
                v = Double.parseDouble(s);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("nota no numérica: \"" + p.trim() + "\"");
            }

            if (v < 1.0 || v > 7.0) {
                throw new IllegalArgumentException("nota fuera de rango (1.0 a 7.0): " + v);
            }

            // redondeo a 1 decimal opcional para uniformar
            v = round1(v);
            notes.add(v);
        }

        if (notes.isEmpty()) throw new IllegalArgumentException("debes ingresar al menos 1 nota válida.");
        return notes;
    }

    private static double round1(double x) {
        return Math.round(x * 10.0) / 10.0;
    }

    private static String normalizeId(String id) {
        if (id == null) return "";
        return id.trim().toUpperCase();
    }

    private static String normalizeName(String name) {
        if (name == null) return "";
        // trim + colapsar espacios múltiples + mayúsculas
        String t = name.trim().replaceAll("\\s+", " ");
        return t.isEmpty() ? "" : t.toUpperCase();
    }

    private static String joinNotes(List<Double> notes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < notes.size(); i++) {
            sb.append(String.format("%.1f", notes.get(i)));
            if (i < notes.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    private void msg(String s) {
        output.append(s + "\n\n");
    }

    // =================== UI HELPERS ===================

    private JPanel cardPanel(String title) {
        JPanel p = new JPanel();
        p.setBackground(BG_DARK_PURPLE);
        p.setBorder(titled(title));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        return p;
    }

    private TitledBorder titled(String title) {
        TitledBorder b = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PINK_ACCENT, 2),
                title
        );
        b.setTitleColor(TEXT_LIGHT);
        b.setTitleFont(new Font("Segoe UI", Font.BOLD, 12));
        return b;
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(TEXT_LIGHT);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setForeground(TEXT_LIGHT);
        f.setBackground(PANEL_PURPLE);
        f.setCaretColor(SOFT_PINK);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PINK_ACCENT, 1),
                new EmptyBorder(6, 8, 6, 8)
        ));
        return f;
    }

    private JButton button(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(PINK_ACCENT);
        btn.setForeground(Color.BLACK);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SOFT_PINK, 2),
                new EmptyBorder(10, 12, 10, 12)
        ));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(PINK_ACCENT);

        table.setBackground(PANEL_PURPLE);
        table.setForeground(TEXT_LIGHT);

        table.setSelectionBackground(new Color(120, 60, 170));
        table.setSelectionForeground(TEXT_LIGHT);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(BG_DARK_PURPLE);
        table.getTableHeader().setForeground(TEXT_LIGHT);

        // Renderer para alternar filas y mejorar contraste
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                c.setOpaque(true);

                if (isSelected) {
                    c.setBackground(new Color(120, 60, 170));
                    c.setForeground(TEXT_LIGHT);
                } else {
                    c.setBackground(row % 2 == 0 ? PANEL_PURPLE : new Color(205, 195, 220));
                    c.setForeground(TEXT_LIGHT);

                    // Colorear estado ROJO en la columna "ESTADO"
                    if (column == 4 && value != null && value.toString().equalsIgnoreCase("ROJO")) {
                        c.setForeground(SOFT_PINK);
                    }
                }
                return c;
            }
        });
    }

    // =================== MODELO ===================

    private static class Student {
        String id;
        String name;
        List<Double> notes;

        Student(String id, String name, List<Double> notes) {
            this.id = normalizeId(id);
            this.name = normalizeName(name);
            this.notes = new ArrayList<>(notes);
        }

        double avg() {
            if (notes == null || notes.isEmpty()) return 0;
            double sum = 0;
            for (double n : notes) sum += n;
            return sum / notes.size();
        }

        double avgRounded1() {
            return round1(avg());
        }

        String status() {
            return avg() < 4.0 ? "No Aprobado ❌" : "Aprobado ✅";
        }
    }
}