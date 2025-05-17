/*
 * Panel para gestionar laboratorios.
 * Autor: Equipo Soldados Caídos (mejorado visualmente)
 */
package Paneles;

import Clases.Laboratorio;
import Controles.ControladorLaboratorio;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

// Clase para borde redondeado personalizado
class RoundedBorder implements Border {
    private int radius;
    private Color color;

    public RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}

public class PanelLaboratorio extends JPanel {
    // Componentes de la interfaz
    private JComboBox<String> cajaUbicacion;
    private JTextField cajaDescripcion, cajaCapacidad;
    private JTable tablaLaboratorios;
    private DefaultTableModel modelo;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;
    private int idSeleccionado = -1; // ID del laboratorio seleccionado
    private ControladorLaboratorio controlador; // Controlador para manejar operaciones con base de datos

    public PanelLaboratorio() {
        controlador = new ControladorLaboratorio(); // Inicializa el controlador
        setLayout(new BorderLayout(10, 10)); // Layout con espaciado
        setBackground(Color.WHITE); // Fondo blanco
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Márgenes reducidos en laterales

        // Título
        JLabel lblTitulo = new JLabel("Gestión de Laboratorios", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(45, 62, 80));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel superior (formulario y botones)
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);

        // Panel de formulario dividido en dos columnas
        JPanel panelForm = new JPanel(new GridLayout(1, 2, 5, 0));
        panelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                "Datos del Laboratorio",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(25, 25, 112)));
        panelForm.setBackground(new Color(250, 250, 255));

        // Panel izquierda
        JPanel panelIzquierda = new JPanel(new GridBagLayout());
        panelIzquierda.setBackground(new Color(250, 250, 255));
        GridBagConstraints gbcIzq = new GridBagConstraints();
        gbcIzq.insets = new Insets(5, 5, 5, 5);
        gbcIzq.fill = GridBagConstraints.HORIZONTAL;

        // Panel derecha
        JPanel panelDerecha = new JPanel(new GridBagLayout());
        panelDerecha.setBackground(new Color(250, 250, 255));
        GridBagConstraints gbcDer = new GridBagConstraints();
        gbcDer.insets = new Insets(5, 5, 5, 5);
        gbcDer.fill = GridBagConstraints.HORIZONTAL;

        // Creación de etiquetas
        JLabel lblUbicacion = new JLabel("Ubicación:");
        JLabel lblDescripcion = new JLabel("Descripción:");
        JLabel lblCapacidad = new JLabel("Capacidad:");

        // Estilo para etiquetas
        for (JLabel label : new JLabel[]{lblUbicacion, lblDescripcion, lblCapacidad}) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setForeground(new Color(25, 25, 112));
        }

        // Inicializa campos del formulario
        cajaUbicacion = new JComboBox<>(new String[]{"Bloque A", "Bloque B", "Bloque C"});
        cajaUbicacion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaUbicacion.setBackground(Color.WHITE);
        cajaUbicacion.setPreferredSize(new Dimension(180, 25));

        cajaDescripcion = new JTextField();
        cajaDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaDescripcion.setPreferredSize(new Dimension(180, 25));

        cajaCapacidad = new JTextField();
        cajaCapacidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cajaCapacidad.setPreferredSize(new Dimension(180, 25));

        // Añade componentes al panel izquierdo
        gbcIzq.gridx = 0; gbcIzq.gridy = 0;
        panelIzquierda.add(lblUbicacion, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(cajaUbicacion, gbcIzq);

        gbcIzq.gridx = 0; gbcIzq.gridy = 1;
        panelIzquierda.add(lblDescripcion, gbcIzq);
        gbcIzq.gridx = 1;
        panelIzquierda.add(cajaDescripcion, gbcIzq);

        // Añade componentes al panel derecho
        gbcDer.gridx = 0; gbcDer.gridy = 0;
        panelDerecha.add(lblCapacidad, gbcDer);
        gbcDer.gridx = 1;
        panelDerecha.add(cajaCapacidad, gbcDer);

        // Añade paneles al formulario
        panelForm.add(panelIzquierda);
        panelForm.add(panelDerecha);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelBotones.setBackground(new Color(250, 250, 255));

        btnAgregar = new JButton("Agregar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        // Estilo para botones
        btnAgregar.setBackground(new Color(60, 179, 113)); // Verde
        btnActualizar.setBackground(new Color(100, 149, 237)); // Azul
        btnEliminar.setBackground(new Color(220, 20, 60)); // Rojo
        btnLimpiar.setBackground(new Color(150, 150, 150)); // Gris

        for (JButton btn : new JButton[]{btnAgregar, btnActualizar, btnEliminar, btnLimpiar}) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(100, 30));
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addHoverEffect(btn);
            panelBotones.add(btn);
        }

        // Añade formulario y botones al panel superior
        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // Tabla para mostrar laboratorios
        modelo = new DefaultTableModel(new String[]{"ID", "Ubicación", "Descripción", "Capacidad"}, 0);
        tablaLaboratorios = new JTable(modelo);
        tablaLaboratorios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaLaboratorios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaLaboratorios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaLaboratorios.setRowHeight(25);
        tablaLaboratorios.setOpaque(false); // Hacer la tabla transparente
        tablaLaboratorios.setShowGrid(true);
        tablaLaboratorios.setGridColor(new Color(100, 149, 237));

        // Estilo para la tabla
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(220, 220, 220)); // Blanco y plomo claro
                if (isSelected) {
                    c.setBackground(new Color(135, 206, 250)); // Azul suave para selección
                    c.setForeground(Color.WHITE);
                } else {
                    c.setForeground(Color.BLACK);
                }
                ((JComponent) c).setOpaque(true); // Celdas opacas para colores
                return c;
            }
        };
        for (int i = 0; i < tablaLaboratorios.getColumnCount(); i++) {
            tablaLaboratorios.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Ajustar anchos de columnas para cubrir el 50% del espacio en blanco adicional
        tablaLaboratorios.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tablaLaboratorios.getColumnModel().getColumn(1).setPreferredWidth(120); // Ubicación
        tablaLaboratorios.getColumnModel().getColumn(2).setPreferredWidth(280); // Descripción
        tablaLaboratorios.getColumnModel().getColumn(3).setPreferredWidth(80); // Capacidad

        // ScrollPane con fondo semitransparente y bordes redondeados
        JScrollPane scroll = new JScrollPane(tablaLaboratorios);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBackground(new Color(100, 149, 237, 50)); // Azul semitransparente
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                        "Lista de Laboratorios",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 13),
                        new Color(25, 25, 112)),
                new RoundedBorder(10, new Color(100, 149, 237))));

        // Panel para centrar la tabla
        JPanel tablaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tablaPanel.setBackground(Color.WHITE);
        tablaPanel.add(scroll);

        // Añade paneles al contenedor principal
        add(panelSuperior, BorderLayout.NORTH);
        add(tablaPanel, BorderLayout.CENTER);

        // Asignar eventos a los botones y tabla
        btnAgregar.addActionListener(e -> agregarLaboratorio());
        btnActualizar.addActionListener(e -> actualizarLaboratorio());
        btnEliminar.addActionListener(e -> eliminarLaboratorio());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        tablaLaboratorios.getSelectionModel().addListSelectionListener(this::cargarSeleccion);

        // Cargar los datos existentes en la tabla
        cargarDatos();
    }

    // Agrega efecto hover a los botones
    private void addHoverEffect(JButton button) {
        Color originalColor = button.getBackground();
        Color hoverColor = originalColor.brighter();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    // Carga los laboratorios desde la base de datos a la tabla
    private void cargarDatos() {
        try {
            modelo.setRowCount(0); // Limpia la tabla
            List<Laboratorio> lista = controlador.listar(); // Obtiene lista desde el controlador
            for (Laboratorio lab : lista) {
                modelo.addRow(new Object[]{
                        lab.getIdLaboratorio(),
                        lab.getUbicacion(),
                        lab.getDescripcion(),
                        lab.getCapacidad()
                });
            }
        } catch (SQLException e) {
            mostrarError(e); // Muestra mensaje si ocurre un error
        }
    }

    // Agrega un nuevo laboratorio a la base de datos
    private void agregarLaboratorio() {
        try {
            validarCampos(); // Valida que los campos no estén vacíos o inválidos
            Laboratorio lab = new Laboratorio(
                    (String) cajaUbicacion.getSelectedItem(),
                    cajaDescripcion.getText(),
                    Integer.parseInt(cajaCapacidad.getText())
            );
            controlador.insertar(lab); // Llama al controlador para insertar
            cargarDatos(); // Recarga la tabla
            limpiarFormulario(); // Limpia los campos
        } catch (Exception ex) {
            mostrarError(ex); // Muestra error si ocurre
        }
    }

    // Actualiza un laboratorio existente
    private void actualizarLaboratorio() {
        try {
            if (idSeleccionado == -1)
                throw new IllegalArgumentException("Seleccione un laboratorio para actualizar.");
            validarCampos();
            Laboratorio lab = new Laboratorio(
                    idSeleccionado,
                    (String) cajaUbicacion.getSelectedItem(),
                    cajaDescripcion.getText(),
                    Integer.parseInt(cajaCapacidad.getText())
            );
            controlador.actualizar(lab);
            cargarDatos();
            limpiarFormulario();
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    // Elimina el laboratorio seleccionado
    private void eliminarLaboratorio() {
        try {
            if (idSeleccionado == -1)
                throw new IllegalArgumentException("Seleccione un laboratorio para eliminar.");
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este laboratorio?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                controlador.eliminar(idSeleccionado);
                cargarDatos();
                limpiarFormulario();
            }
        } catch (Exception ex) {
            mostrarError(ex);
        }
    }

    // Carga los datos del laboratorio seleccionado en los campos
    private void cargarSeleccion(ListSelectionEvent e) {
        int fila = tablaLaboratorios.getSelectedRow();
        if (fila >= 0) {
            idSeleccionado = (int) modelo.getValueAt(fila, 0);
            cajaUbicacion.setSelectedItem(modelo.getValueAt(fila, 1).toString());
            cajaDescripcion.setText(modelo.getValueAt(fila, 2).toString());
            cajaCapacidad.setText(modelo.getValueAt(fila, 3).toString());
        }
    }

    // Limpia todos los campos del formulario y resetea selección
    private void limpiarFormulario() {
        idSeleccionado = -1;
        cajaUbicacion.setSelectedIndex(0);
        cajaDescripcion.setText("");
        cajaCapacidad.setText("");
        tablaLaboratorios.clearSelection();
    }

    // Verifica que los campos tengan datos válidos
    private void validarCampos() {
        if (cajaUbicacion.getSelectedItem() == null || cajaDescripcion.getText().isEmpty() || cajaCapacidad.getText().isEmpty()) {
            throw new IllegalArgumentException("Campos obligatorios.");
        }
        try {
            int capacidad = Integer.parseInt(cajaCapacidad.getText());
            if (capacidad <= 0)
                throw new IllegalArgumentException("Capacidad debe ser mayor a 0.");
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Capacidad no válida.");
        }
    }

    // Muestra un mensaje de error al usuario
    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}