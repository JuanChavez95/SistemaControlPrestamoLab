/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PanelesMateriales;

import Clases.Equipamiento;
import Clases.HistorialEquipamiento;
import Clases.HistorialGeneral;
import Controles.ControladorEquipamiento;
import Controles.ControladorHistorialEquipamiento;
import Controles.ControladorHistorialGeneral;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import com.toedter.calendar.JDateChooser;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

// Custom RoundedBorder class for rounded corners
class RoundedBorder implements javax.swing.border.Border {
    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.getForeground());
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
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

public class PanelDetalleHerramientas extends JPanel {
    private JTextField txtId;
    private JTextField txtRUAdministrador;
    private JComboBox<Equipamiento> cbEquipamiento;
    private JComboBox<String> cbDisponibilidad;
    private JDateChooser dateChooser;
    private JComboBox<String> cbCategoria;
    private JTextArea txtDescripcion;
    private JButton btnAgregar, btnModificar, btnLimpiar;
    private JTable tablaHistorial;
    private ControladorEquipamiento controlEquipamiento;
    private ControladorHistorialGeneral controlHistorialGeneral;
    private ControladorHistorialEquipamiento controlHistorialEquipamiento;

    // Color Palette (from PanelSolicitarPrestamo)
    private static final Color PRIMARY_COLOR = new Color(2, 136, 209); // Vibrant Blue
    private static final Color SECONDARY_COLOR = new Color(38, 166, 154); // Vibrant Teal
    private static final Color BACKGROUND_COLOR = Color.WHITE; // Pure White
    private static final Color CARD_COLOR = Color.WHITE; // Pure White
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark Gray
    private static final Color BORDER_COLOR = new Color(189, 189, 189); // Lighter Gray
    private static final Color ACCENT_COLOR = new Color(79, 195, 247); // Vivid Light Blue
    private static final Color AVAILABLE_COLOR = new Color(76, 175, 80); // Vibrant Green
    private static final Color UNAVAILABLE_COLOR = new Color(244, 67, 54); // Vibrant Red

    public PanelDetalleHerramientas() {
        controlEquipamiento = new ControladorEquipamiento();
        controlHistorialGeneral = new ControladorHistorialGeneral();
        controlHistorialEquipamiento = new ControladorHistorialEquipamiento();
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(5, 5, 5, 5)); // Reduced padding
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Título
        JLabel titleLabel = new JLabel("Detalle de Herramientas", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(5, 0, 10, 0)); // Reduced top/bottom padding
        add(titleLabel, BorderLayout.NORTH);

        // Panel principal
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(new EmptyBorder(5, 10, 5, 10)); // Reduced padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3); // Tighter insets
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.8;

        // Sección: Formulario
        JPanel sectionFormulario = createCardPanel("Registro de Historial");
        sectionFormulario.setLayout(new GridBagLayout());
        GridBagConstraints gbcSection = new GridBagConstraints();
        gbcSection.insets = new Insets(3, 3, 3, 3); // Tighter insets
        gbcSection.fill = GridBagConstraints.HORIZONTAL;
        gbcSection.weightx = 1.0;

        // Inicializar txtId (no visible en UI)
        txtId = new JTextField(10);
        txtId.setEditable(false);

        // RU Administrador
        JLabel lblRUAdmin = new JLabel("RU Administrador:");
        lblRUAdmin.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblRUAdmin.setForeground(TEXT_COLOR);
        txtRUAdministrador = new JTextField(10);
        txtRUAdministrador.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtRUAdministrador.setBorder(new RoundedBorder(10));
        gbcSection.gridx = 0; gbcSection.gridy = 0;
        sectionFormulario.add(lblRUAdmin, gbcSection);
        gbcSection.gridx = 1;
        sectionFormulario.add(txtRUAdministrador, gbcSection);

        // Equipamiento
        JLabel lblEquipamiento = new JLabel("Equipamiento:");
        lblEquipamiento.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblEquipamiento.setForeground(TEXT_COLOR);
        cbEquipamiento = new JComboBox<>();
        cbEquipamiento.setFont(new Font("Roboto", Font.PLAIN, 14));
        cbEquipamiento.setBorder(new RoundedBorder(10));
        cbEquipamiento.setRenderer(new ListCellRenderer<Equipamiento>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Equipamiento> list, Equipamiento value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel();
                if (value != null) {
                    label.setText(value.getIdEquipamiento() + " - " + value.getNombreEquipamiento());
                }
                label.setOpaque(true);
                label.setBackground(isSelected ? ACCENT_COLOR : CARD_COLOR);
                label.setForeground(isSelected ? Color.WHITE : TEXT_COLOR);
                label.setFont(new Font("Roboto", Font.PLAIN, 14));
                label.setBorder(new EmptyBorder(2, 5, 2, 5));
                return label;
            }
        });
        cargarEquipamientos();
        gbcSection.gridx = 0; gbcSection.gridy = 1;
        sectionFormulario.add(lblEquipamiento, gbcSection);
        gbcSection.gridx = 1;
        sectionFormulario.add(cbEquipamiento, gbcSection);

        // Disponibilidad
        JLabel lblDisponibilidad = new JLabel("Disponibilidad:");
        lblDisponibilidad.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblDisponibilidad.setForeground(TEXT_COLOR);
        cbDisponibilidad = new JComboBox<>(new String[]{"Disponible", "No Disponible", "De Baja"});
        cbDisponibilidad.setFont(new Font("Roboto", Font.PLAIN, 14));
        cbDisponibilidad.setBorder(new RoundedBorder(10));
        gbcSection.gridx = 0; gbcSection.gridy = 2;
        sectionFormulario.add(lblDisponibilidad, gbcSection);
        gbcSection.gridx = 1;
        sectionFormulario.add(cbDisponibilidad, gbcSection);

        // Fecha
        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblFecha.setForeground(TEXT_COLOR);
        dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Roboto", Font.PLAIN, 14));
        dateChooser.setDate(new Date());
        dateChooser.setBorder(new RoundedBorder(10));
        gbcSection.gridx = 0; gbcSection.gridy = 3;
        sectionFormulario.add(lblFecha, gbcSection);
        gbcSection.gridx = 1;
        sectionFormulario.add(dateChooser, gbcSection);

        // Categoría
        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblCategoria.setForeground(TEXT_COLOR);
        cbCategoria = new JComboBox<>(new String[]{"Reparación", "Actualización", "Restaurada"});
        cbCategoria.setFont(new Font("Roboto", Font.PLAIN, 14));
        cbCategoria.setBorder(new RoundedBorder(10));
        gbcSection.gridx = 0; gbcSection.gridy = 4;
        sectionFormulario.add(lblCategoria, gbcSection);
        gbcSection.gridx = 1;
        sectionFormulario.add(cbCategoria, gbcSection);

        // Descripción
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Roboto", Font.PLAIN, 14));
        lblDescripcion.setForeground(TEXT_COLOR);
        txtDescripcion = new JTextArea(3, 20); // Reduced from 5 to 3 rows
        txtDescripcion.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtDescripcion);
        scrollPane.setBorder(new RoundedBorder(10));
        gbcSection.gridx = 0; gbcSection.gridy = 5;
        sectionFormulario.add(lblDescripcion, gbcSection);
        gbcSection.gridx = 1; gbcSection.weighty = 1.0;
        sectionFormulario.add(scrollPane, gbcSection);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 3)); // Tighter padding
        panelBotones.setBackground(CARD_COLOR);
        btnAgregar = createStyledButton("Agregar", PRIMARY_COLOR);
        btnModificar = createStyledButton("Modificar", SECONDARY_COLOR);
        btnLimpiar = createStyledButton("Limpiar", new Color(108, 117, 125));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnLimpiar);
        gbcSection.gridx = 0; gbcSection.gridy = 6; gbcSection.gridwidth = 2; gbcSection.weighty = 0;
        sectionFormulario.add(panelBotones, gbcSection);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weighty = 0.3; // Reduced weighty
        formPanel.add(sectionFormulario, gbc);

        // Sección: Tabla Historial
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "RU Admin", "Fecha", "Categoría", "Descripción", "Equipamiento", "Disponibilidad"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaHistorial = createStyledTable(model);
        JScrollPane scrollHistorial = createStyledScrollPane(tablaHistorial);
        scrollHistorial.setPreferredSize(new Dimension(600, 200));

        JPanel sectionHistorial = createCardPanel("Historial de Herramientas");
        sectionHistorial.setLayout(new BorderLayout(5, 5));
        sectionHistorial.add(scrollHistorial, BorderLayout.CENTER);
        gbc.gridy = 1; gbc.weighty = 0.6;
        formPanel.add(sectionHistorial, gbc);

        // Envolver en JScrollPane
        JScrollPane mainScroll = createStyledScrollPane(formPanel);
        mainScroll.setBorder(null);
        add(mainScroll, BorderLayout.CENTER);

        // Configurar listeners
        btnAgregar.addActionListener(e -> agregarHistorial());
        btnModificar.addActionListener(e -> modificarHistorial());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        cbEquipamiento.addActionListener(e -> actualizarDisponibilidadSeleccionada());
        tablaHistorial.getSelectionModel().addListSelectionListener(e -> seleccionarHistorial());

        // Cargar historial inicial
        cargarHistorial();
    }

    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10),
            new EmptyBorder(3, 3, 3, 3) // Reduced padding
        ));
        panel.setBorder(BorderFactory.createTitledBorder(
            new RoundedBorder(10),
            title,
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Roboto", Font.BOLD, 14),
            PRIMARY_COLOR
        ));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 0, 0, 20)),
            panel.getBorder()
        ));
        return panel;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.PLAIN, 12)); // Reduced font size
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setBorder(new RoundedBorder(10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(5, 10, 5, 10)); // Reduced padding
        return button;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setRowHeight(25);
        table.setFont(new Font("Roboto", Font.PLAIN, 12));
        table.setSelectionBackground(ACCENT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 12));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setOpaque(false);
        table.setBackground(new Color(255, 255, 255, 200));
        table.setBorder(new RoundedBorder(10));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Roboto", Font.PLAIN, 12));
                if (table.getColumnName(column).equals("Disponibilidad")) {
                    if ("Disponible".equals(value)) {
                        c.setForeground(AVAILABLE_COLOR);
                    } else if ("No Disponible".equals(value) || "De Baja".equals(value)) {
                        c.setForeground(UNAVAILABLE_COLOR);
                    }
                } else {
                    c.setForeground(TEXT_COLOR);
                }
                if (isSelected) {
                    c.setBackground(ACCENT_COLOR);
                } else {
                    c.setBackground(new Color(255, 255, 255, 200));
                }
                return c;
            }
        });

        // Configurar ancho de columnas
        TableColumn columna;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            columna = table.getColumnModel().getColumn(i);
            switch (i) {
                case 0: // ID
                    columna.setPreferredWidth(50);
                    break;
                case 1: // RU Admin
                    columna.setPreferredWidth(100);
                    break;
                case 2: // Fecha
                    columna.setPreferredWidth(100);
                    break;
                case 3: // Categoría
                    columna.setPreferredWidth(100);
                    break;
                case 4: // Descripción
                    columna.setPreferredWidth(200);
                    break;
                case 5: // Equipamiento
                    columna.setPreferredWidth(150);
                    break;
                case 6: // Disponibilidad
                    columna.setPreferredWidth(100);
                    break;
            }
        }

        return table;
    }

    private JScrollPane createStyledScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(new RoundedBorder(10));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(15);
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setUnitIncrement(15);
        return scrollPane;
    }

    private void cargarEquipamientos() {
        cbEquipamiento.removeAllItems();
        try {
            for (Equipamiento equipo : controlEquipamiento.listar()) {
                cbEquipamiento.addItem(equipo);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar equipamientos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarDisponibilidadSeleccionada() {
        Equipamiento equipoSeleccionado = (Equipamiento) cbEquipamiento.getSelectedItem();
        if (equipoSeleccionado != null) {
            String disponibilidad = equipoSeleccionado.getDisponibilidad();
            for (int i = 0; i < cbDisponibilidad.getItemCount(); i++) {
                if (cbDisponibilidad.getItemAt(i).equals(disponibilidad)) {
                    cbDisponibilidad.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void cargarHistorial() {
        DefaultTableModel model = (DefaultTableModel) tablaHistorial.getModel();
        model.setRowCount(0);
        try {
            for (Object[] registro : controlHistorialEquipamiento.buscarHistorialCompletoEquipamiento(
                    cbEquipamiento.getSelectedItem() != null ? 
                    ((Equipamiento)cbEquipamiento.getSelectedItem()).getIdEquipamiento() : 0)) {
                int idEquipamiento = (int) registro[5];
                Equipamiento equipo = controlEquipamiento.buscarPorId(idEquipamiento);
                String nombreEquipo = equipo != null ? equipo.getNombreEquipamiento() : "Desconocido";
                model.addRow(new Object[]{
                    registro[0], registro[1], registro[2], registro[3], registro[4], nombreEquipo,
                    equipo != null ? equipo.getDisponibilidad() : "Desconocido"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarHistorial() {
        if (cbEquipamiento.getSelectedItem() == null || dateChooser.getDate() == null || 
            txtDescripcion.getText().trim().isEmpty() || txtRUAdministrador.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int ruAdministrador;
            try {
                ruAdministrador = Integer.parseInt(txtRUAdministrador.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El RU debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.sql.Date fecha = new java.sql.Date(dateChooser.getDate().getTime());
            String categoria = (String) cbCategoria.getSelectedItem();
            String descripcion = txtDescripcion.getText();
            int idEquipamiento = ((Equipamiento) cbEquipamiento.getSelectedItem()).getIdEquipamiento();
            String disponibilidad = (String) cbDisponibilidad.getSelectedItem();

            HistorialGeneral historialGeneral = new HistorialGeneral(ruAdministrador, dateChooser.getDate(), categoria, descripcion);
            int idHistorial = controlHistorialGeneral.insertar(historialGeneral);
            HistorialEquipamiento historialEquipamiento = new HistorialEquipamiento(idHistorial, idEquipamiento);
            controlHistorialEquipamiento.insertar(historialEquipamiento);
            controlEquipamiento.actualizarDisponibilidad(idEquipamiento, disponibilidad);

            JOptionPane.showMessageDialog(this, "Historial agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarHistorial();
            cargarEquipamientos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarHistorial() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un historial para modificar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cbEquipamiento.getSelectedItem() == null || dateChooser.getDate() == null || 
            txtDescripcion.getText().trim().isEmpty() || txtRUAdministrador.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int idHistorial = Integer.parseInt(txtId.getText());
            int idEquipamiento = ((Equipamiento) cbEquipamiento.getSelectedItem()).getIdEquipamiento();
            int ruAdministrador;
            try {
                ruAdministrador = Integer.parseInt(txtRUAdministrador.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El RU debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            java.sql.Date fecha = new java.sql.Date(dateChooser.getDate().getTime());
            String categoria = (String) cbCategoria.getSelectedItem();
            String descripcion = txtDescripcion.getText();
            HistorialGeneral historialGeneral = new HistorialGeneral(idHistorial, ruAdministrador, dateChooser.getDate(), categoria, descripcion);
            controlHistorialGeneral.actualizar(historialGeneral);
            HistorialEquipamiento historialEquipamiento = new HistorialEquipamiento(idHistorial, idEquipamiento);
            controlHistorialEquipamiento.actualizar(historialEquipamiento);
            String disponibilidad = (String) cbDisponibilidad.getSelectedItem();
            controlEquipamiento.actualizarDisponibilidad(idEquipamiento, disponibilidad);

            JOptionPane.showMessageDialog(this, "Historial modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarHistorial();
            cargarEquipamientos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtRUAdministrador.setText("");
        dateChooser.setDate(new Date());
        cbCategoria.setSelectedIndex(0);
        cbDisponibilidad.setSelectedIndex(0);
        txtDescripcion.setText("");
        if (cbEquipamiento.getItemCount() > 0) {
            cbEquipamiento.setSelectedIndex(0);
            actualizarDisponibilidadSeleccionada();
        }
    }

    private void seleccionarHistorial() {
        int fila = tablaHistorial.getSelectedRow();
        if (fila >= 0) {
            try {
                int idHistorial = (Integer) tablaHistorial.getValueAt(fila, 0);
                int ruAdmin = (Integer) tablaHistorial.getValueAt(fila, 1);
                txtRUAdministrador.setText(String.valueOf(ruAdmin));
                txtId.setText(String.valueOf(idHistorial));
                HistorialGeneral historialGeneral = controlHistorialGeneral.buscarPorId(idHistorial);
                HistorialEquipamiento historialEquipamiento = controlHistorialEquipamiento.buscarPorIdHistorial(idHistorial);
                if (historialGeneral != null && historialEquipamiento != null) {
                    dateChooser.setDate(historialGeneral.getFecha());
                    cbCategoria.setSelectedItem(historialGeneral.getCategoria());
                    txtDescripcion.setText(historialGeneral.getDescripcion());
                    for (int i = 0; i < cbEquipamiento.getItemCount(); i++) {
                        if (cbEquipamiento.getItemAt(i).getIdEquipamiento() == historialEquipamiento.getIdEquipamiento()) {
                            cbEquipamiento.setSelectedIndex(i);
                            break;
                        }
                    }
                    actualizarDisponibilidadSeleccionada();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}