/*
 * Panel para visualizar y gestionar préstamos de equipamiento e insumos.
 * Permite filtrar préstamos, mostrar detalles y realizar acciones como aceptar, rechazar o terminar un préstamo.
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
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Utilidades.ServicioCorreo;

/**
 * Clase que representa un panel para visualizar y gestionar préstamos.
 * @author 
 */
public class PanelVisualizarPrestamos extends JPanel {

    // Logger para registrar errores y eventos
    private static final Logger LOGGER = Logger.getLogger(PanelVisualizarPrestamos.class.getName());
    
    // Controladores para interactuar con la lógica de negocio
    private final ControladorPrestamo controladorPrestamo;
    private final ControladorEquipamiento controladorEquipamiento;
    private final ControladorInsumo controladorInsumo;
    private final ControladorDetallePrestamoInsumo controladorDetalleInsumo;
    private final ControladorHorario controladorHorario;
    
    // Identificador del administrador
    private final int ruAdministrador;
    
    // Componentes de la interfaz gráfica
    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;
    private JTextArea areaDetalles;
    private JComboBox<String> comboEstado;
    private JTextField campoRU;

    // Paleta de colores para la interfaz
    private static final Color PRIMARY_COLOR = new Color(21, 101, 192); // Azul oscuro
    private static final Color SECONDARY_COLOR = new Color(66, 133, 244); // Azul claro
    private static final Color BACKGROUND_COLOR = new Color(238, 238, 238); // Gris claro
    private static final Color CARD_COLOR = new Color(250, 250, 250); // Blanco suave
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Gris oscuro
    private static final Color BORDER_COLOR = new Color(189, 189, 189); // Gris claro
    private static final Color ACCENT_COLOR = new Color(187, 222, 251); // Azul pálido
    private static final Color SUCCESS_COLOR = new Color(46, 125, 50); // Verde
    private static final Color ERROR_COLOR = new Color(211, 47, 47); // Rojo
    private static final Color WARNING_COLOR = new Color(239, 108, 0); // Ámbar

    /**
     * Constructor del panel.
     * @param ruAdministrador Identificador del administrador que usa el panel.
     */
    public PanelVisualizarPrestamos(int ruAdministrador) {
        this.ruAdministrador = ruAdministrador;
        this.controladorPrestamo = new ControladorPrestamo();
        this.controladorEquipamiento = new ControladorEquipamiento();
        this.controladorInsumo = new ControladorInsumo();
        this.controladorDetalleInsumo = new ControladorDetallePrestamoInsumo();
        this.controladorHorario = new ControladorHorario();
        initComponents();
        cargarPrestamos();
        verificarConfiguracionCorreo();
    }

    /**
     * Inicializa los componentes gráficos del panel.
     */
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Panel principal con desplazamiento
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);
        JScrollPane mainScrollPane = createStyledScrollPane(contentPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(mainScrollPane, BorderLayout.CENTER);

        // Título del panel
        JLabel lblTitulo = new JLabel("Visualizar Préstamos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Roboto", Font.BOLD, 30));
        lblTitulo.setForeground(PRIMARY_COLOR);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 25, 0));
        contentPanel.add(lblTitulo, BorderLayout.NORTH);

        // Panel de filtros
        JPanel panelFiltros = createCardPanel("Filtros");
        panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.X_AXIS));
        panelFiltros.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                createRoundedBorder(BORDER_COLOR, 15),
                "Filtros",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Roboto", Font.BOLD, 18),
                PRIMARY_COLOR
            ),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Campo para filtrar por RU
        JLabel lblRU = new JLabel("RU:");
        lblRU.setFont(new Font("Roboto", Font.PLAIN, 16));
        lblRU.setForeground(TEXT_COLOR);
        campoRU = new JTextField(6);
        campoRU.setFont(new Font("Roboto", Font.PLAIN, 16));
        campoRU.setBorder(createRoundedBorder(BORDER_COLOR, 10));
        campoRU.setPreferredSize(new Dimension(100, 40));

        // Combo para filtrar por estado
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("Roboto", Font.PLAIN, 16));
        lblEstado.setForeground(TEXT_COLOR);
        comboEstado = new JComboBox<>(new String[]{"Todos", "Pendiente", "Aceptado", "Rechazado", "Terminado"});
        styleComboBox(comboEstado);

        // Botón para aplicar filtros
        JButton btnFiltrar = createStyledButton("Filtrar", PRIMARY_COLOR);
        btnFiltrar.addActionListener(e -> filtrarPrestamos());

        // Añadir componentes al panel de filtros
        panelFiltros.add(Box.createHorizontalStrut(15));
        panelFiltros.add(lblRU);
        panelFiltros.add(Box.createHorizontalStrut(10));
        panelFiltros.add(campoRU);
        panelFiltros.add(Box.createHorizontalStrut(20));
        panelFiltros.add(lblEstado);
        panelFiltros.add(Box.createHorizontalStrut(10));
        panelFiltros.add(comboEstado);
        panelFiltros.add(Box.createHorizontalStrut(20));
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(Box.createHorizontalGlue());

        // Configuración de la tabla de préstamos
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
        scrollTabla.setPreferredSize(new Dimension(900, 400));

        // Panel de detalles y acciones
        JPanel panelDetallesAcciones = new JPanel(new BorderLayout(15, 15));
        panelDetallesAcciones.setBackground(BACKGROUND_COLOR);

        // Área para mostrar detalles del préstamo seleccionado
        JPanel panelDetalles = createCardPanel("Detalles del Préstamo");
        panelDetalles.setLayout(new BorderLayout());
        areaDetalles = new JTextArea(10, 30);
        areaDetalles.setFont(new Font("Roboto", Font.PLAIN, 14));
        areaDetalles.setEditable(false);
        areaDetalles.setLineWrap(true);
        areaDetalles.setWrapStyleWord(true);
        areaDetalles.setBorder(createRoundedBorder(BORDER_COLOR, 10));
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
        panelBotones.add(Box.createHorizontalStrut(15));
        panelBotones.add(btnRechazar);
        panelBotones.add(Box.createHorizontalStrut(15));
        panelBotones.add(btnTerminar);
        panelBotones.add(Box.createHorizontalGlue());

        panelDetallesAcciones.add(panelDetalles, BorderLayout.CENTER);
        panelDetallesAcciones.add(panelBotones, BorderLayout.SOUTH);

        // Añadir componentes al panel principal
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(scrollTabla, BorderLayout.CENTER);
        centerPanel.add(panelDetallesAcciones, BorderLayout.SOUTH);

        contentPanel.add(panelFiltros, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // Listener para mostrar detalles al seleccionar una fila
        tablaPrestamos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetallesPrestamo();
            }
        });
    }

    /**
     * Crea un panel con estilo de tarjeta para contener componentes.
     * @param title Título del panel.
     * @return JPanel con estilo aplicado.
     */
    private JPanel createCardPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            createRoundedBorder(BORDER_COLOR, 15),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setBorder(BorderFactory.createTitledBorder(
            panel.getBorder(),
            title,
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Roboto", Font.BOLD, 18),
            PRIMARY_COLOR
        ));
        // Añadir sombra sutil
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 0, 0, 50)),
            panel.getBorder()
        ));
        return panel;
    }

    /**
     * Crea un botón estilizado con bordes redondeados y efecto hover.
     * @param text Texto del botón.
     * @param baseColor Color base del botón.
     * @return JButton estilizado.
     */
    private JButton createStyledButton(String text, Color baseColor) {
        RoundedButton button = new RoundedButton(text, baseColor);
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
        button.setMaximumSize(new Dimension(120, 40));
        button.setMinimumSize(new Dimension(120, 40));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        // Efecto hover
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

    /**
     * Crea una tabla estilizada para mostrar los préstamos.
     * @param model Modelo de datos de la tabla.
     * @return JTable estilizada.
     */
    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // Estilo de la tabla
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(230, 234, 240));
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(187, 222, 251));
        table.setSelectionForeground(Color.BLACK);
        table.setIntercellSpacing(new Dimension(10, 10));

        // Estilo del encabezado
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(25, 118, 210));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Renderizador personalizado para celdas
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                          boolean isSelected, boolean hasFocus,
                                                          int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                setFont(new Font("Segoe UI", Font.PLAIN, 14));

                // Fondo alternado para filas
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(250, 251, 255) : Color.WHITE);
                } else {
                    c.setBackground(new Color(187, 222, 251));
                }

                // Estilo para la columna "Estado"
                if (column == 5 && value != null) {
                    c.setForeground(new Color(33, 33, 33));
                    switch (value.toString().toUpperCase()) {
                        case "PENDIENTE":
                            c.setBackground(new Color(255, 245, 157));
                            break;
                        case "ACEPTADO":
                            c.setBackground(new Color(165, 214, 167));
                            break;
                        case "RECHAZADO":
                            c.setBackground(new Color(239, 154, 154));
                            break;
                        case "TERMINADO":
                            c.setBackground(new Color(189, 195, 199));
                            break;
                        default:
                            c.setBackground(row % 2 == 0 ? new Color(250, 251, 255) : Color.WHITE);
                            break;
                    }
                } else {
                    c.setForeground(new Color(33, 33, 33));
                }

                // Bordes suaves para celdas
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        });

        // Ajustar ancho de columnas
        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setPreferredWidth(70);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setPreferredWidth(170);
        table.getColumnModel().getColumn(7).setPreferredWidth(220);
        table.getColumnModel().getColumn(8).setPreferredWidth(220);

        return table;
    }

    /**
     * Crea un JScrollPane estilizado.
     * @param view Componente a incluir en el JScrollPane.
     * @return JScrollPane estilizado.
     */
    private JScrollPane createStyledScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(createRoundedBorder(BORDER_COLOR, 15));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(20);
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setUnitIncrement(20);
        return scrollPane;
    }

    /**
     * Aplica estilo al JComboBox.
     * @param comboBox JComboBox a estilizar.
     */
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Roboto", Font.PLAIN, 16));
        comboBox.setBackground(CARD_COLOR);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBorder(createRoundedBorder(BORDER_COLOR, 10));
        comboBox.setPreferredSize(new Dimension(160, 40));
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setFont(new Font("Roboto", Font.PLAIN, 12));
                button.setBackground(CARD_COLOR);
                button.setForeground(PRIMARY_COLOR);
                button.setBorder(createRoundedBorder(BORDER_COLOR, 10));
                button.setFocusPainted(false);
                return button;
            }
        });
    }

    /**
     * Crea un borde redondeado personalizado.
     * @param color Color del borde.
     * @param radius Radio de los bordes redondeados.
     * @return Border personalizado.
     */
    private Border createRoundedBorder(Color color, int radius) {
        return new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(4, 8, 4, 8);
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        };
    }

    /**
     * Clase interna para botones redondeados personalizados.
     */
    private class RoundedButton extends JButton {
        private Color backgroundColor;
        private int radius;

        public RoundedButton(String text, Color backgroundColor) {
            super(text);
            this.backgroundColor = backgroundColor;
            this.radius = 30;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    /**
     * Carga todos los préstamos en la tabla.
     */
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

    /**
     * Filtra los préstamos según RU y estado seleccionados.
     */
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

    /**
     * Obtiene información del horario asociado a un préstamo.
     * @param idPrestamo ID del préstamo.
     * @return Cadena con información del horario.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
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

    /**
     * Obtiene los equipamientos asociados a un préstamo.
     * @param idPrestamo ID del préstamo.
     * @return Cadena con información de los equipamientos.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
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

    /**
     * Obtiene los insumos asociados a un préstamo.
     * @param idPrestamo ID del préstamo.
     * @return Cadena con información de los insumos.
     * @throws SQLException Si ocurre un error en la base de datos.
     */
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

    /**
     * Muestra los detalles del préstamo seleccionado en la tabla.
     */
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

    /**
     * Acepta un préstamo seleccionado, verificando disponibilidad y actualizando estados.
     */
    private void aceptarPrestamo() {
    int filaSeleccionada = tablaPrestamos.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione un préstamo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int idPrestamo = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
    String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 5);
    int ruUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 1);

    if (!estado.equals("Pendiente")) {
        JOptionPane.showMessageDialog(this, "Solo se pueden aceptar préstamos en estado Pendiente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String observaciones = JOptionPane.showInputDialog(this, "Ingrese observaciones (opcional):", "Aceptar Préstamo", JOptionPane.PLAIN_MESSAGE);
    if (observaciones == null) {
        return;
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
        
        // Actualizar estado del horario
        Integer idHorario = controladorPrestamo.obtenerHorarioPrestamo(idPrestamo);
        if (idHorario != null) {
            Horario horario = controladorHorario.buscarPorId(idHorario);
            if (horario != null) {
                horario.setEstado("Préstamo");
                controladorHorario.actualizar(horario);
            }
        }
        
        // Actualizar disponibilidad de equipamientos
        List<Integer> equipamientoIds = controladorPrestamo.obtenerEquipamientosPrestamo(idPrestamo);
        for (Integer idEquipamiento : equipamientoIds) {
            Clases.Equipamiento equipo = controladorEquipamiento.buscarPorId(idEquipamiento);
            if (equipo != null) {
                equipo.setDisponibilidad("Préstamo");
                controladorEquipamiento.actualizar(equipo);
            }
        }
        
        // Procesar aceptación del préstamo
        controladorPrestamo.aceptarPrestamo(idPrestamo, null, ruAdministrador, observaciones);
        
        // NUEVO: Enviar correo de notificación
        try {
            String emailUsuario = controladorPrestamo.obtenerEmailUsuario(ruUsuario);
            if (emailUsuario != null && !emailUsuario.isEmpty()) {
                boolean correoEnviado = ServicioCorreo.enviarNotificacionPrestamo(emailUsuario, ruUsuario, true);
                if (correoEnviado) {
                    LOGGER.info("Correo de aceptación enviado exitosamente al usuario RU: " + ruUsuario);
                } else {
                    LOGGER.warning("No se pudo enviar el correo de aceptación al usuario RU: " + ruUsuario);
                }
            } else {
                LOGGER.warning("No se encontró email para el usuario RU: " + ruUsuario);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error al enviar correo de aceptación para usuario RU: " + ruUsuario, e);
        }
        
        JOptionPane.showMessageDialog(this, "Préstamo aceptado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        cargarPrestamos();
    } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Error al aceptar préstamo ID " + idPrestamo, ex);
        JOptionPane.showMessageDialog(this, "Error al aceptar préstamo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    /**
     * Rechaza un préstamo seleccionado.
     */
    private void rechazarPrestamo() {
    int filaSeleccionada = tablaPrestamos.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione un préstamo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int idPrestamo = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
    String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 5);
    int ruUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 1);

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
        
        // NUEVO: Enviar correo de notificación
        try {
            String emailUsuario = controladorPrestamo.obtenerEmailUsuario(ruUsuario);
            if (emailUsuario != null && !emailUsuario.isEmpty()) {
                boolean correoEnviado = ServicioCorreo.enviarNotificacionPrestamo(emailUsuario, ruUsuario, false);
                if (correoEnviado) {
                    LOGGER.info("Correo de rechazo enviado exitosamente al usuario RU: " + ruUsuario);
                } else {
                    LOGGER.warning("No se pudo enviar el correo de rechazo al usuario RU: " + ruUsuario);
                }
            } else {
                LOGGER.warning("No se encontró email para el usuario RU: " + ruUsuario);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error al enviar correo de rechazo para usuario RU: " + ruUsuario, e);
        }
        
        JOptionPane.showMessageDialog(this, "Préstamo rechazado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        cargarPrestamos();
    } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Error al rechazar préstamo ID " + idPrestamo, ex);
        JOptionPane.showMessageDialog(this, "Error al rechazar préstamo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    /**
 * Verifica si la configuración de correo está establecida correctamente.
 */
private void verificarConfiguracionCorreo() {
    if (!ServicioCorreo.verificarConfiguracion()) {
        JOptionPane.showMessageDialog(this, 
            "Advertencia: La configuración de correo no está establecida.\n" +
            "Las notificaciones por correo no funcionarán hasta que se configure correctamente.", 
            "Configuración de Correo", 
            JOptionPane.WARNING_MESSAGE);
    }
}

    /**
     * Termina un préstamo seleccionado, solicitando cantidades devueltas de insumos.
     */
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
            // Actualizar estado del horario
            Integer idHorario = controladorPrestamo.obtenerHorarioPrestamo(idPrestamo);
            if (idHorario != null) {
                Horario horario = controladorHorario.buscarPorId(idHorario);
                if (horario != null) {
                    horario.setEstado("Asignado");
                    controladorHorario.actualizar(horario);
                }
            }
            
            // Actualizar disponibilidad de equipamientos
            List<Integer> equipamientoIds = controladorPrestamo.obtenerEquipamientosPrestamo(idPrestamo);
            for (Integer idEquipamiento : equipamientoIds) {
                Clases.Equipamiento equipo = controladorEquipamiento.buscarPorId(idEquipamiento);
                if (equipo != null) {
                    equipo.setDisponibilidad("Disponible");
                    controladorEquipamiento.actualizar(equipo);
                }
            }
            
            // Procesar insumos
            List<DetallePrestamoInsumo> detallesInsumo = controladorDetalleInsumo.listarPorPrestamo(idPrestamo);
            if (detallesInsumo.isEmpty()) {
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
                    return;
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

            // Procesar terminación del préstamo
            controladorPrestamo.terminarPrestamo(idPrestamo, insumoIds, cantidadesDevueltas);
            JOptionPane.showMessageDialog(this, "Préstamo terminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarPrestamos();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al terminar préstamo ID " + idPrestamo, ex);
            JOptionPane.showMessageDialog(this, "Error al terminar préstamo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}