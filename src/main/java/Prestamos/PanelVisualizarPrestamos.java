/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Prestamos;

import Clases.DetallePrestamoInsumo;
import Clases.Horario;
import Clases.Prestamo;
import Controles.ControladorDetallePrestamoInsumo;
import Controles.ControladorEquipamiento;
import Controles.ControladorHorario;
import Controles.ControladorInsumo;
import Controles.ControladorPrestamo;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * @author Emmanuel (Modificado)
 */
public class PanelVisualizarPrestamos extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(PanelVisualizarPrestamos.class.getName());
    private final ControladorPrestamo controladorPrestamo;
    private final ControladorEquipamiento controladorEquipamiento;
    private final ControladorInsumo controladorInsumo;
    private final ControladorDetallePrestamoInsumo controladorDetalleInsumo;
    private final ControladorHorario controladorHorario;
    private final int ruAdministrador;
    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;
    private JTextArea areaDetalles;
    private JComboBox<String> comboEstado;
    private JTextField campoRU;

    // Color Palette
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243); // Material Blue
    private static final Color SECONDARY_COLOR = new Color(100, 181, 246); // Light Blue
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Soft Gray
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark Gray
    private static final Color BORDER_COLOR = new Color(200, 200, 200); // Light Gray
    private static final Color ACCENT_COLOR = new Color(187, 222, 251); // Pale Blue
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80); // Green
    private static final Color ERROR_COLOR = new Color(244, 67, 54); // Red
    private static final Color WARNING_COLOR = new Color(255, 193, 7); // Amber

    public PanelVisualizarPrestamos(int ruAdministrador) {
        this.ruAdministrador = ruAdministrador;
        this.controladorPrestamo = new ControladorPrestamo();
        this.controladorEquipamiento = new ControladorEquipamiento();
        this.controladorInsumo = new ControladorInsumo();
        this.controladorDetalleInsumo = new ControladorDetallePrestamoInsumo();
        this.controladorHorario = new ControladorHorario();
        initComponents();
        cargarPrestamos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JLabel lblTitulo = new JLabel("Visualizar Préstamos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Roboto", Font.BOLD, 28));
        lblTitulo.setForeground(PRIMARY_COLOR);
        lblTitulo.setBorder(new EmptyBorder(10, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de filtros
        JPanel panelFiltros = createCardPanel("Filtros");
        panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.X_AXIS));
        panelFiltros.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                "Filtros",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Roboto", Font.BOLD, 16),
                PRIMARY_COLOR
            ),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblRU = new JLabel("RU:");
        lblRU.setFont(new Font("Roboto", Font.PLAIN, 16));
        lblRU.setForeground(TEXT_COLOR);
        campoRU = new JTextField(10);
        campoRU.setFont(new Font("Roboto", Font.PLAIN, 16));
        campoRU.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        campoRU.setPreferredSize(new Dimension(120, 35));

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("Roboto", Font.PLAIN, 16));
        lblEstado.setForeground(TEXT_COLOR);
        comboEstado = new JComboBox<>(new String[]{"Todos", "Pendiente", "Aceptado", "Rechazado", "Terminado"});
        styleComboBox(comboEstado);

        JButton btnFiltrar = createStyledButton("Filtrar", PRIMARY_COLOR);
        btnFiltrar.addActionListener(e -> filtrarPrestamos());

        panelFiltros.add(Box.createHorizontalStrut(10));
        panelFiltros.add(lblRU);
        panelFiltros.add(Box.createHorizontalStrut(5));
        panelFiltros.add(campoRU);
        panelFiltros.add(Box.createHorizontalStrut(15));
        panelFiltros.add(lblEstado);
        panelFiltros.add(Box.createHorizontalStrut(5));
        panelFiltros.add(comboEstado);
        panelFiltros.add(Box.createHorizontalStrut(15));
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(Box.createHorizontalGlue());

        // Tabla de préstamos
        String[] columnas = {"ID Préstamo", "RU Usuario", "Nombre Usuario", "Fecha", "Hora", "Estado", "Horario", "Equipamiento", "Insumos"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPrestamos = createStyledTable(modeloTabla);
        tablaPrestamos.setAutoCreateRowSorter(true);
        JScrollPane scrollTabla = createStyledScrollPane(tablaPrestamos);
        scrollTabla.setPreferredSize(new Dimension(800, 300));

        // Panel de detalles y acciones
        JPanel panelDetallesAcciones = new JPanel(new BorderLayout(10, 10));
        panelDetallesAcciones.setBackground(BACKGROUND_COLOR);

        // Área de detalles
        JPanel panelDetalles = createCardPanel("Detalles del Préstamo");
        panelDetalles.setLayout(new BorderLayout());
        areaDetalles = new JTextArea(8, 30);
        areaDetalles.setFont(new Font("Roboto", Font.PLAIN, 14));
        areaDetalles.setEditable(false);
        areaDetalles.setLineWrap(true);
        areaDetalles.setWrapStyleWord(true);
        areaDetalles.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        areaDetalles.setBackground(CARD_COLOR);
        areaDetalles.setForeground(TEXT_COLOR);
        JScrollPane scrollDetalles = createStyledScrollPane(areaDetalles);
        panelDetalles.add(scrollDetalles, BorderLayout.CENTER);

        // Botones de acciones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        panelBotones.setBackground(BACKGROUND_COLOR);

        JButton btnAceptar = createStyledButton("Aceptar", SUCCESS_COLOR);
        btnAceptar.addActionListener(e -> aceptarPrestamo());

        JButton btnRechazar = createStyledButton("Rechazar", ERROR_COLOR);
        btnRechazar.addActionListener(e -> rechazarPrestamo());

        JButton btnTerminar = createStyledButton("Terminar", SUCCESS_COLOR);
        btnTerminar.addActionListener(e -> terminarPrestamo());

        panelBotones.add(Box.createHorizontalGlue());
        panelBotones.add(btnAceptar);
        panelBotones.add(Box.createHorizontalStrut(10));
        panelBotones.add(btnRechazar);
        panelBotones.add(Box.createHorizontalStrut(10));
        panelBotones.add(btnTerminar);
        panelBotones.add(Box.createHorizontalGlue());

        panelDetallesAcciones.add(panelDetalles, BorderLayout.CENTER);
        panelDetallesAcciones.add(panelBotones, BorderLayout.SOUTH);

        // Añadir componentes al panel principal
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(scrollTabla, BorderLayout.CENTER);
        centerPanel.add(panelDetallesAcciones, BorderLayout.SOUTH);

        add(panelFiltros, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Listener para mostrar detalles al seleccionar una fila
        tablaPrestamos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetallesPrestamo();
            }
        });
    }

    // Styling Helper Methods
    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            title,
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Roboto", Font.BOLD, 16),
            PRIMARY_COLOR
        ));
        // Add subtle shadow
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 0, 0, 30)),
            panel.getBorder()
        ));
        return panel;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setBorder(new LineBorder(baseColor, 1, true));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
        return button;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setRowHeight(35);
        table.setFont(new Font("Roboto", Font.PLAIN, 14));
        table.setSelectionBackground(ACCENT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setBorder(new LineBorder(BORDER_COLOR, 1, true));

        // Custom renderer for cells
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                if (model.getColumnName(column).equals("Estado")) {
                    if ("Pendiente".equals(value)) {
                        c.setForeground(WARNING_COLOR);
                    } else if ("Aceptado".equals(value)) {
                        c.setForeground(SUCCESS_COLOR);
                    } else if ("Rechazado".equals(value)) {
                        c.setForeground(ERROR_COLOR);
                    } else if ("Terminado".equals(value)) {
                        c.setForeground(new Color(33, 150, 243)); // Blue for completed
                    } else {
                        c.setForeground(TEXT_COLOR);
                    }
                } else {
                    c.setForeground(TEXT_COLOR);
                }
                if (isSelected) {
                    c.setBackground(ACCENT_COLOR);
                } else {
                    c.setBackground(CARD_COLOR);
                }
                return c;
            }
        });

        // Adjust column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID Préstamo
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  // RU Usuario
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Nombre Usuario
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Fecha
        table.getColumnModel().getColumn(4).setPreferredWidth(60);  // Hora
        table.getColumnModel().getColumn(5).setPreferredWidth(80);  // Estado
        table.getColumnModel().getColumn(6).setPreferredWidth(150); // Horario
        table.getColumnModel().getColumn(7).setPreferredWidth(200); // Equipamiento
        table.getColumnModel().getColumn(8).setPreferredWidth(200); // Insumos

        return table;
    }

    private JScrollPane createStyledScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(15);
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setUnitIncrement(15);
        return scrollPane;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Roboto", Font.PLAIN, 16));
        comboBox.setBackground(CARD_COLOR);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        comboBox.setPreferredSize(new Dimension(150, 35));
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setFont(new Font("Roboto", Font.PLAIN, 12));
                button.setBackground(CARD_COLOR);
                button.setForeground(PRIMARY_COLOR);
                button.setBorder(new LineBorder(BORDER_COLOR, 1, true));
                button.setFocusPainted(false);
                return button;
            }
        });
    }

    private void cargarPrestamos() {
        try {
            modeloTabla.setRowCount(0);
            List<Prestamo> prestamos = controladorPrestamo.listar();
            for (Prestamo prestamo : prestamos) {
                String nombreUsuario = controladorPrestamo.obtenerNombreUsuario(prestamo.getRuUsuario());
                String horarioInfo = obtenerHorarioPrestamo(prestamo.getIdPrestamo());
                String equipamiento = obtenerEquipamientoPrestamo(prestamo.getIdPrestamo());
                String insumos = obtenerInsumosPrestamo(prestamo.getIdPrestamo());
                modeloTabla.addRow(new Object[]{
                    prestamo.getIdPrestamo(),
                    prestamo.getRuUsuario(),
                    nombreUsuario != null ? nombreUsuario : "Desconocido",
                    prestamo.getFechaPrestamo(),
                    prestamo.getHoraPrestamo(),
                    prestamo.getEstadoPrestamo(),
                    horarioInfo,
                    equipamiento,
                    insumos
                });
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar préstamos", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar préstamos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarPrestamos() {
        String filtroRU = campoRU.getText().trim();
        String filtroEstado = comboEstado.getSelectedItem().toString();
        try {
            modeloTabla.setRowCount(0);
            List<Prestamo> prestamos = controladorPrestamo.listar();
            for (Prestamo prestamo : prestamos) {
                boolean coincideRU = filtroRU.isEmpty() || String.valueOf(prestamo.getRuUsuario()).equals(filtroRU);
                boolean coincideEstado = filtroEstado.equals("Todos") || prestamo.getEstadoPrestamo().equals(filtroEstado);
                if (coincideRU && coincideEstado) {
                    String nombreUsuario = controladorPrestamo.obtenerNombreUsuario(prestamo.getRuUsuario());
                    String horarioInfo = obtenerHorarioPrestamo(prestamo.getIdPrestamo());
                    String equipamiento = obtenerEquipamientoPrestamo(prestamo.getIdPrestamo());
                    String insumos = obtenerInsumosPrestamo(prestamo.getIdPrestamo());
                    modeloTabla.addRow(new Object[]{
                        prestamo.getIdPrestamo(),
                        prestamo.getRuUsuario(),
                        nombreUsuario != null ? nombreUsuario : "Desconocido",
                        prestamo.getFechaPrestamo(),
                        prestamo.getHoraPrestamo(),
                        prestamo.getEstadoPrestamo(),
                        horarioInfo,
                        equipamiento,
                        insumos
                    });
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al filtrar préstamos", ex);
            JOptionPane.showMessageDialog(this, "Error al filtrar préstamos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerHorarioPrestamo(int idPrestamo) throws SQLException {
        Integer idHorario = controladorPrestamo.obtenerHorarioPrestamo(idPrestamo);
        if (idHorario == null) {
            return "Sin horario";
        }
        
        Horario horario = controladorHorario.buscarPorId(idHorario);
        if (horario == null) {
            return "Horario no encontrado";
        }
        
        return "ID: " + horario.getIdHorario() + " - " + horario.getDia() + " " + horario.getHora() + " (" + horario.getEstado() + ")";
    }

    private String obtenerEquipamientoPrestamo(int idPrestamo) throws SQLException {
        List<Integer> equipamientoIds = controladorPrestamo.obtenerEquipamientosPrestamo(idPrestamo);
        if (equipamientoIds.isEmpty()) {
            return "Ninguno";
        }
        StringBuilder sb = new StringBuilder();
        for (Integer id : equipamientoIds) {
            Clases.Equipamiento equipamiento = controladorEquipamiento.buscarPorId(id);
            if (equipamiento != null) {
                if (sb.length() > 0) sb.append(", ");
                sb.append("[").append(id).append("] ").append(equipamiento.getNombreEquipamiento()).append(" (").append(equipamiento.getDisponibilidad()).append(")");
            }
        }
        return sb.length() > 0 ? sb.toString() : "Ninguno";
    }

    private String obtenerInsumosPrestamo(int idPrestamo) throws SQLException {
        List<DetallePrestamoInsumo> detalles = controladorDetalleInsumo.listarPorPrestamo(idPrestamo);
        if (detalles.isEmpty()) {
            return "Ninguno";
        }
        StringBuilder sb = new StringBuilder();
        for (DetallePrestamoInsumo detalle : detalles) {
            int idInsumo = detalle.getIdInsumo();
            int cantidadInicial = detalle.getCantidadInicial();
            Integer cantidadFinal = detalle.getCantidadFinal();
            
            String nombreInsumo = controladorInsumo.obtenerNombreInsumo(idInsumo);
            
            if (sb.length() > 0) sb.append(", ");
            sb.append("ID ").append(idInsumo).append(" ").append(nombreInsumo).append(": ").append(cantidadInicial);
            
            if (cantidadFinal != null) {
                sb.append(" (Devuelto: ").append(cantidadFinal).append(")");
            }
        }
        return sb.toString();
    }

    private void mostrarDetallesPrestamo() {
        int filaSeleccionada = tablaPrestamos.getSelectedRow();
        if (filaSeleccionada != -1) {
            int idPrestamo = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            try {
                Prestamo prestamo = controladorPrestamo.buscarPorId(idPrestamo);
                if (prestamo != null) {
                    StringBuilder detalles = new StringBuilder();
                    detalles.append("ID Préstamo: ").append(prestamo.getIdPrestamo()).append("\n");
                    detalles.append("RU Usuario: ").append(prestamo.getRuUsuario()).append("\n");
                    String nombreUsuario = controladorPrestamo.obtenerNombreUsuario(prestamo.getRuUsuario());
                    detalles.append("Nombre Usuario: ").append(nombreUsuario != null ? nombreUsuario : "Desconocido").append("\n");
                    detalles.append("Fecha: ").append(prestamo.getFechaPrestamo()).append("\n");
                    detalles.append("Hora: ").append(prestamo.getHoraPrestamo()).append("\n");
                    detalles.append("Estado: ").append(prestamo.getEstadoPrestamo()).append("\n");
                    
                    // Detalles del horario
                    Integer idHorario = controladorPrestamo.obtenerHorarioPrestamo(idPrestamo);
                    if (idHorario != null) {
                        Horario horario = controladorHorario.buscarPorId(idHorario);
                        if (horario != null) {
                            detalles.append("\nHorario:\n");
                            detalles.append("  ID: ").append(horario.getIdHorario()).append("\n");
                            detalles.append("  Materia: ").append(horario.getMateria()).append("\n");
                            detalles.append("  Día: ").append(horario.getDia()).append("\n");
                            detalles.append("  Hora: ").append(horario.getHora()).append("\n");
                            detalles.append("  Estado: ").append(horario.getEstado()).append("\n");
                        }
                    }
                    
                    // Detalles de equipamiento
                    List<Integer> equipamientoIds = controladorPrestamo.obtenerEquipamientosPrestamo(idPrestamo);
                    if (!equipamientoIds.isEmpty()) {
                        detalles.append("\nEquipamiento:\n");
                        for (Integer id : equipamientoIds) {
                            Clases.Equipamiento equipamiento = controladorEquipamiento.buscarPorId(id);
                            if (equipamiento != null) {
                                detalles.append("  ID ").append(id).append(": ")
                                       .append(equipamiento.getNombreEquipamiento())
                                       .append(" (").append(equipamiento.getDisponibilidad()).append(")\n");
                            }
                        }
                    }
                    
                    // Detalles de insumos
                    List<DetallePrestamoInsumo> detallesInsumo = controladorDetalleInsumo.listarPorPrestamo(idPrestamo);
                    if (!detallesInsumo.isEmpty()) {
                        detalles.append("\nInsumos:\n");
                        for (DetallePrestamoInsumo detalle : detallesInsumo) {
                            String nombreInsumo = controladorInsumo.obtenerNombreInsumo(detalle.getIdInsumo());
                            detalles.append("  ID ").append(detalle.getIdInsumo()).append(": ")
                                   .append(nombreInsumo)
                                   .append(" - Cantidad Inicial: ").append(detalle.getCantidadInicial());
                            
                            if (detalle.getCantidadFinal() != null) {
                                detalles.append(", Cantidad Final: ").append(detalle.getCantidadFinal());
                            }
                            detalles.append("\n");
                        }
                    }
                    
                    detalles.append("\nObservaciones: ").append(prestamo.getObservaciones() != null ? prestamo.getObservaciones() : "Ninguna");
                    areaDetalles.setText(detalles.toString());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error al mostrar detalles del préstamo ID " + idPrestamo, ex);
                JOptionPane.showMessageDialog(this, "Error al mostrar detalles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            areaDetalles.setText("");
        }
    }

    private void aceptarPrestamo() {
        int filaSeleccionada = tablaPrestamos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un préstamo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPrestamo = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 5);

        if (!estado.equals("Pendiente")) {
            JOptionPane.showMessageDialog(this, "Solo se pueden aceptar préstamos en estado Pendiente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String observaciones = JOptionPane.showInputDialog(this, "Ingrese observaciones (opcional):", "Aceptar Préstamo", JOptionPane.PLAIN_MESSAGE);
        if (observaciones == null) {
            return; // El usuario canceló
        }

        try {
            // Verificar disponibilidad de insumos
            List<DetallePrestamoInsumo> detallesInsumo = controladorDetalleInsumo.listarPorPrestamo(idPrestamo);
            for (DetallePrestamoInsumo detalle : detallesInsumo) {
                boolean disponible = controladorDetalleInsumo.verificarDisponibilidadInsumo(
                    detalle.getIdInsumo(), detalle.getCantidadInicial());
                
                if (!disponible) {
                    JOptionPane.showMessageDialog(this,
                        "No hay suficiente cantidad disponible del insumo ID " + detalle.getIdInsumo(),
                        "Error de disponibilidad", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Cambiar estado del horario a "Préstamo"
            Integer idHorario = controladorPrestamo.obtenerHorarioPrestamo(idPrestamo);
            if (idHorario != null) {
                Horario horario = controladorHorario.buscarPorId(idHorario);
                if (horario != null) {
                    horario.setEstado("Préstamo");
                    controladorHorario.actualizar(horario);
                }
            }
            
            // Cambiar disponibilidad de equipamientos a "Préstamo"
            List<Integer> equipamientoIds = controladorPrestamo.obtenerEquipamientosPrestamo(idPrestamo);
            for (Integer idEquipamiento : equipamientoIds) {
                Clases.Equipamiento equipo = controladorEquipamiento.buscarPorId(idEquipamiento);
                if (equipo != null) {
                    equipo.setDisponibilidad("Préstamo");
                    controladorEquipamiento.actualizar(equipo);
                }
            }
            
            // Procesar el préstamo
            controladorPrestamo.aceptarPrestamo(idPrestamo, null, ruAdministrador, observaciones);
            JOptionPane.showMessageDialog(this, "Préstamo aceptado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarPrestamos();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al aceptar préstamo ID " + idPrestamo, ex);
            JOptionPane.showMessageDialog(this, "Error al aceptar préstamo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rechazarPrestamo() {
        int filaSeleccionada = tablaPrestamos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un préstamo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPrestamo = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 5);

        if (!estado.equals("Pendiente")) {
            JOptionPane.showMessageDialog(this, "Solo se pueden rechazar préstamos en estado Pendiente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea rechazar este préstamo?", "Confirmar Rechazo", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controladorPrestamo.rechazarPrestamo(idPrestamo);
            JOptionPane.showMessageDialog(this, "Préstamo rechazado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarPrestamos();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al rechazar préstamo ID " + idPrestamo, ex);
            JOptionPane.showMessageDialog(this, "Error al rechazar préstamo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void terminarPrestamo() {
        int filaSeleccionada = tablaPrestamos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un préstamo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPrestamo = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 5);

        if (!estado.equals("Aceptado")) {
            JOptionPane.showMessageDialog(this, "Solo se pueden terminar préstamos en estado Aceptado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Cambiar estado del horario a "Asignado"
            Integer idHorario = controladorPrestamo.obtenerHorarioPrestamo(idPrestamo);
            if (idHorario != null) {
                Horario horario = controladorHorario.buscarPorId(idHorario);
                if (horario != null) {
                    horario.setEstado("Asignado");
                    controladorHorario.actualizar(horario);
                }
            }
            
            // Cambiar disponibilidad de equipamientos a "Disponible"
            List<Integer> equipamientoIds = controladorPrestamo.obtenerEquipamientosPrestamo(idPrestamo);
            for (Integer idEquipamiento : equipamientoIds) {
                Clases.Equipamiento equipo = controladorEquipamiento.buscarPorId(idEquipamiento);
                if (equipo != null) {
                    equipo.setDisponibilidad("Disponible");
                    controladorEquipamiento.actualizar(equipo);
                }
            }
            
            // Procesar los insumos
            List<DetallePrestamoInsumo> detallesInsumo = controladorDetalleInsumo.listarPorPrestamo(idPrestamo);
            if (detallesInsumo.isEmpty()) {
                // Si no hay insumos, procedemos a terminar el préstamo directamente
                controladorPrestamo.terminarPrestamo(idPrestamo, new ArrayList<>(), new ArrayList<>());
                JOptionPane.showMessageDialog(this, "Préstamo terminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarPrestamos();
                return;
            }

            List<Integer> insumoIds = new ArrayList<>();
            List<Integer> cantidadesDevueltas = new ArrayList<>();

            for (DetallePrestamoInsumo detalle : detallesInsumo) {
                int idInsumo = detalle.getIdInsumo();
                int cantidadInicial = detalle.getCantidadInicial();
                String nombreInsumo = controladorInsumo.obtenerNombreInsumo(idInsumo);
                
                String input = JOptionPane.showInputDialog(this, 
                    "Ingrese la cantidad devuelta para el insumo " + nombreInsumo + " (ID: " + idInsumo + ")\n" +
                    "Cantidad prestada: " + cantidadInicial, 
                    "Cantidad Devuelta", JOptionPane.PLAIN_MESSAGE);
                
                if (input == null) {
                    return; // El usuario canceló
                }

                int cantidadDevuelta;
                try {
                    cantidadDevuelta = Integer.parseInt(input);
                    if (cantidadDevuelta < 0 || cantidadDevuelta > cantidadInicial) {
                        JOptionPane.showMessageDialog(this, "La cantidad devuelta debe estar entre 0 y " + cantidadInicial + ".", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                insumoIds.add(idInsumo);
                cantidadesDevueltas.add(cantidadDevuelta);
            }

            // Procesar el préstamo
            controladorPrestamo.terminarPrestamo(idPrestamo, insumoIds, cantidadesDevueltas);
            JOptionPane.showMessageDialog(this, "Préstamo terminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarPrestamos();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al terminar préstamo ID " + idPrestamo, ex);
            JOptionPane.showMessageDialog(this, "Error al terminar préstamo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}