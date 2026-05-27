package clase7a10;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Clase10ListaCompras {

    // Paleta (morado + palo rosa + blanco)
    static final Color MORADO_OSCURO = new Color(55, 0, 90);
    static final Color MORADO_MEDIO  = new Color(90, 0, 130);
    static final Color PALO_ROSA     = new Color(248, 187, 208);
    static final Color BLANCO        = Color.WHITE;
    static final Color GRIS_SUAVE    = new Color(245, 245, 245);

    // Categorías (orden fijo)
    static final String[] CATEGORIAS = {
            "Desayuno y once",
            "Almuerzos",
            "Picoteo",
            "Bebidas",
            "Aseo",
            "Otros"
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Clase10ListaCompras::crearVentana);
    }

    private static void crearVentana() {
        JFrame frame = new JFrame("Lista de Compras");
        frame.setSize(720, 520);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Datos reales
        ArrayList<Item> items = new ArrayList<>();

        // Modelo visual
        DefaultListModel<String> modelo = new DefaultListModel<>();
        JList<String> lista = new JList<>(modelo);
        lista.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lista.setBackground(GRIS_SUAVE);
        lista.setSelectionBackground(PALO_ROSA);
        lista.setSelectionForeground(MORADO_OSCURO);

        // Mapa para eliminar: misma longitud que el modelo. Null = encabezado, Item = producto real.
        ArrayList<Item> indexMap = new ArrayList<>();

        // ======= Layout principal =======
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(MORADO_MEDIO);

        JLabel titulo = new JLabel("LISTA DE COMPRAS (por secciones)", SwingConstants.CENTER);
        titulo.setForeground(BLANCO);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(16, 10, 10, 10));
        root.add(titulo, BorderLayout.NORTH);

        // ======= Panel de entrada =======
        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(MORADO_MEDIO);
        top.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCat = new JLabel("Sección:");
        lblCat.setForeground(BLANCO);
        lblCat.setFont(new Font("SansSerif", Font.BOLD, 14));

        JComboBox<String> cbCategoria = new JComboBox<>(CATEGORIAS);
        cbCategoria.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cbCategoria.setBackground(BLANCO);

        JLabel lblProd = new JLabel("Producto:");
        lblProd.setForeground(BLANCO);
        lblProd.setFont(new Font("SansSerif", Font.BOLD, 14));

        JTextField txtProducto = new JTextField(16);
        txtProducto.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtProducto.setBackground(BLANCO);

        JLabel lblCant = new JLabel("Cantidad:");
        lblCant.setForeground(BLANCO);
        lblCant.setFont(new Font("SansSerif", Font.BOLD, 14));

        JSpinner spCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        spCantidad.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton btnAgregar = crearBoton("Agregar");

        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        top.add(lblCat, c);
        c.gridx = 1; c.gridy = 0; c.weightx = 0.6;
        top.add(cbCategoria, c);

        c.gridx = 2; c.gridy = 0; c.weightx = 0;
        top.add(lblProd, c);
        c.gridx = 3; c.gridy = 0; c.weightx = 1.0;
        top.add(txtProducto, c);

        c.gridx = 4; c.gridy = 0; c.weightx = 0;
        top.add(lblCant, c);
        c.gridx = 5; c.gridy = 0; c.weightx = 0.3;
        top.add(spCantidad, c);

        c.gridx = 6; c.gridy = 0; c.weightx = 0;
        top.add(btnAgregar, c);

        // ======= Centro (lista) =======
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // ======= Panel inferior =======
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(MORADO_MEDIO);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JPanel acciones = new JPanel(new GridLayout(1, 6, 10, 10));
        acciones.setBackground(MORADO_MEDIO);

        JButton btnEliminar = crearBoton("Eliminar");
        JButton btnLimpiar = crearBoton("Limpiar");
        JButton btnOrdenar = crearBoton("Ordenar A-Z");
        JButton btnBoleta  = crearBoton("Boleta");
        JButton btnGuardar = crearBoton("Guardar .txt");
        JButton btnCargar  = crearBoton("Cargar .txt");

        acciones.add(btnEliminar);
        acciones.add(btnLimpiar);
        acciones.add(btnOrdenar);
        acciones.add(btnBoleta);
        acciones.add(btnGuardar);
        acciones.add(btnCargar);

        JLabel lblResumen = new JLabel("Total productos: 0 | Total unidades: 0");
        lblResumen.setForeground(BLANCO);
        lblResumen.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblResumen.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        bottom.add(acciones, BorderLayout.CENTER);
        bottom.add(lblResumen, BorderLayout.SOUTH);

        // Armado central
        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(MORADO_MEDIO);
        centro.add(top, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        centro.add(bottom, BorderLayout.SOUTH);

        root.add(centro, BorderLayout.CENTER);

        // ======= Helpers (actualizar vista / resumen) =======
        Runnable actualizarTodo = () -> {
            ordenarItems(items);
            reconstruirModeloPorSecciones(items, modelo, indexMap);
            actualizarResumen(items, lblResumen);
        };

        // ======= ACCIONES =======

        btnAgregar.addActionListener(e -> {
            String categoria = (String) cbCategoria.getSelectedItem();
            String producto = txtProducto.getText().trim();
            int cantidad = (Integer) spCantidad.getValue();

            if (producto.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Ingrese un producto (no vacío).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Si ya existe el mismo producto en la misma sección, sumamos cantidad (más pro que duplicar)
            Item existente = buscarItem(items, categoria, producto);
            if (existente != null) {
                existente.cantidad += cantidad;
            } else {
                items.add(new Item(categoria, producto, cantidad));
            }

            txtProducto.setText("");
            spCantidad.setValue(1);
            txtProducto.requestFocus();
            actualizarTodo.run();
        });

        // Enter agrega
        txtProducto.addActionListener(e -> btnAgregar.doClick());

        btnEliminar.addActionListener(e -> {
            int idx = lista.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(frame, "Seleccione un producto (no un título de sección).", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Item seleccionado = indexMap.get(idx);
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(frame, "Eso es un título de sección. Seleccione un producto.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int resp = JOptionPane.showConfirmDialog(frame,
                    "¿Eliminar: " + seleccionado.nombre + " x" + seleccionado.cantidad + " ?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (resp == JOptionPane.YES_OPTION) {
                items.remove(seleccionado);
                actualizarTodo.run();
            }
        });

        btnLimpiar.addActionListener(e -> {
            if (items.isEmpty()) return;

            int resp = JOptionPane.showConfirmDialog(frame,
                    "¿Seguro que desea borrar TODA la lista?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (resp == JOptionPane.YES_OPTION) {
                items.clear();
                actualizarTodo.run();
                txtProducto.requestFocus();
            }
        });

        btnOrdenar.addActionListener(e -> {
            ordenarItems(items);
            actualizarTodo.run();
        });

        btnBoleta.addActionListener(e -> {
            String boleta = generarBoleta(items);
            mostrarBoleta(frame, boleta);

            // Opción extra: exportar boleta
            int resp = JOptionPane.showConfirmDialog(frame,
                    "¿Desea guardar la boleta en un .txt?",
                    "Guardar boleta",
                    JOptionPane.YES_NO_OPTION);

            if (resp == JOptionPane.YES_OPTION) {
                guardarTextoConChooser(frame, boleta, "boleta_compras.txt");
            }
        });

        btnGuardar.addActionListener(e -> {
            String contenido = serializarParaArchivo(items);
            guardarTextoConChooser(frame, contenido, "lista_compras.txt");
        });

        btnCargar.addActionListener(e -> {
            String contenido = cargarTextoConChooser(frame);
            if (contenido == null) return;

            ArrayList<Item> cargados = parsearDesdeArchivo(contenido);
            items.clear();
            items.addAll(cargados);
            actualizarTodo.run();
        });

        // Inicial
        actualizarTodo.run();

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    // ====== UI helpers ======
    private static JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(PALO_ROSA);
        boton.setForeground(MORADO_OSCURO);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        return boton;
    }

    // ====== Lógica de items ======
    static class Item {
        String categoria;
        String nombre;
        int cantidad;

        Item(String categoria, String nombre, int cantidad) {
            this.categoria = categoria;
            this.nombre = nombre;
            this.cantidad = cantidad;
        }
    }

    private static Item buscarItem(List<Item> items, String categoria, String nombre) {
        for (Item it : items) {
            if (it.categoria.equalsIgnoreCase(categoria) && it.nombre.equalsIgnoreCase(nombre)) {
                return it;
            }
        }
        return null;
    }

    private static void ordenarItems(List<Item> items) {
        Collections.sort(items, (a, b) -> {
            // Primero por orden de categoría (según CATEGORIAS), luego por nombre A-Z
            int ca = indiceCategoria(a.categoria);
            int cb = indiceCategoria(b.categoria);
            if (ca != cb) return Integer.compare(ca, cb);
            return a.nombre.compareToIgnoreCase(b.nombre);
        });
    }

    private static int indiceCategoria(String cat) {
        for (int i = 0; i < CATEGORIAS.length; i++) {
            if (CATEGORIAS[i].equalsIgnoreCase(cat)) return i;
        }
        return CATEGORIAS.length; // si no existe, al final
    }

    private static void reconstruirModeloPorSecciones(List<Item> items, DefaultListModel<String> modelo, ArrayList<Item> indexMap) {
        modelo.clear();
        indexMap.clear();

        // Agrupar manteniendo orden de secciones fijo
        for (String cat : CATEGORIAS) {
            ArrayList<Item> delGrupo = new ArrayList<>();
            for (Item it : items) {
                if (it.categoria.equalsIgnoreCase(cat)) delGrupo.add(it);
            }
            if (delGrupo.isEmpty()) continue;

            // Header
            modelo.addElement("[" + cat.toUpperCase() + "]");
            indexMap.add(null);

            // Items
            for (Item it : delGrupo) {
                modelo.addElement("  • " + it.nombre + "   x" + it.cantidad);
                indexMap.add(it);
            }

            // Separador visual
            modelo.addElement(" ");
            indexMap.add(null);
        }

        if (modelo.isEmpty()) {
            modelo.addElement("Tu lista está vacía. Agrega productos arriba 👆");
            indexMap.add(null);
        }
    }

    private static void actualizarResumen(List<Item> items, JLabel lblResumen) {
        int totalProductos = items.size();
        int totalUnidades = 0;
        for (Item it : items) totalUnidades += it.cantidad;

        lblResumen.setText("Total productos: " + totalProductos + " | Total unidades: " + totalUnidades);
    }

    // ====== Boleta ======
    private static String generarBoleta(List<Item> items) {
        if (items.isEmpty()) return "BOLETA DE COMPRAS\n\n(Lista vacía)";

        StringBuilder sb = new StringBuilder();
        sb.append("BOLETA DE COMPRAS\n");
        sb.append("====================================\n");

        int totalUnidades = 0;
        int totalProductos = items.size();

        for (String cat : CATEGORIAS) {
            ArrayList<Item> delGrupo = new ArrayList<>();
            for (Item it : items) if (it.categoria.equalsIgnoreCase(cat)) delGrupo.add(it);

            if (delGrupo.isEmpty()) continue;

            sb.append("\n").append(cat.toUpperCase()).append("\n");
            sb.append("------------------------------------\n");

            for (Item it : delGrupo) {
                sb.append("- ").append(it.nombre).append("  x").append(it.cantidad).append("\n");
                totalUnidades += it.cantidad;
            }
        }

        sb.append("\n====================================\n");
        sb.append("TOTAL PRODUCTOS: ").append(totalProductos).append("\n");
        sb.append("TOTAL UNIDADES : ").append(totalUnidades).append("\n");
        sb.append("====================================\n");

        return sb.toString();
    }

    private static void mostrarBoleta(JFrame frame, String boleta) {
        JTextArea area = new JTextArea(boleta);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setBackground(GRIS_SUAVE);
        area.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(520, 380));

        JOptionPane.showMessageDialog(frame, sp, "Boleta", JOptionPane.INFORMATION_MESSAGE);
    }

    // ====== Guardar / Cargar TXT ======
    private static String serializarParaArchivo(List<Item> items) {
        // Formato: CATEGORIA|PRODUCTO|CANTIDAD
        StringBuilder sb = new StringBuilder();
        sb.append("# lista_compras\n");
        sb.append("# formato: CATEGORIA|PRODUCTO|CANTIDAD\n");

        for (Item it : items) {
            sb.append(it.categoria).append("|")
                    .append(it.nombre).append("|")
                    .append(it.cantidad).append("\n");
        }
        return sb.toString();
    }

    private static ArrayList<Item> parsearDesdeArchivo(String contenido) {
        ArrayList<Item> cargados = new ArrayList<>();
        String[] lineas = contenido.split("\n");

        for (String linea : lineas) {
            linea = linea.trim();
            if (linea.isEmpty() || linea.startsWith("#")) continue;

            String[] parts = linea.split("\\|");
            if (parts.length != 3) continue;

            String cat = parts[0].trim();
            String prod = parts[1].trim();
            String cantStr = parts[2].trim();

            int cant;
            try {
                cant = Integer.parseInt(cantStr);
            } catch (NumberFormatException e) {
                continue;
            }

            if (prod.isEmpty() || cant < 1) continue;

            // Ajustar categoría si no calza con las disponibles
            if (indiceCategoria(cat) == CATEGORIAS.length) cat = "Otros";

            // Si viene repetido, sumamos
            Item existente = buscarItem(cargados, cat, prod);
            if (existente != null) existente.cantidad += cant;
            else cargados.add(new Item(cat, prod, cant));
        }

        ordenarItems(cargados);
        return cargados;
    }

    private static void guardarTextoConChooser(JFrame frame, String contenido, String nombreSugerido) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(nombreSugerido));

        int resp = chooser.showSaveDialog(frame);
        if (resp != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        // Asegurar .txt
        if (!file.getName().toLowerCase().endsWith(".txt")) {
            file = new File(file.getParentFile(), file.getName() + ".txt");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(contenido);
            JOptionPane.showMessageDialog(frame, "Guardado en:\n" + file.getAbsolutePath(), "OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String cargarTextoConChooser(JFrame frame) {
        JFileChooser chooser = new JFileChooser();
        int resp = chooser.showOpenDialog(frame);
        if (resp != JFileChooser.APPROVE_OPTION) return null;

        File file = chooser.getSelectedFile();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
            JOptionPane.showMessageDialog(frame, "Cargado desde:\n" + file.getAbsolutePath(), "OK", JOptionPane.INFORMATION_MESSAGE);
            return sb.toString();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error al cargar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}