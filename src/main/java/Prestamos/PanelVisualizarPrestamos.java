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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
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
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Título
        JLabel lblTitulo = new JLabel("Visualizar Préstamos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(128, 0, 128));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel de filtros
        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.X_AXIS));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        JLabel lblRU = new JLabel("RU:");
        campoRU = new JTextField(10);
        JLabel lblEstado = new JLabel("Estado:");
        comboEstado = new JComboBox<>(new String[]{"Todos", "Pendiente", "Aceptado", "Rechazado", "Terminado"});
        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> filtrarPrestamos());

        panelFiltros.add(Box.createHorizontalStrut(10));
        panelFiltros.add(lblRU);
        panelFiltros.add(Box.createHorizontalStrut(5));
        panelFiltros.add(campoRU);
        panelFiltros.add(Box.createHorizontalStrut(10));
        panelFiltros.add(lblEstado);
        panelFiltros.add(Box.createHorizontalStrut(5));
        panelFiltros.add(comboEstado);
        panelFiltros.add(Box.createHorizontalStrut(10));
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
        tablaPrestamos = new JTable(modeloTabla);
        tablaPrestamos.setRowHeight(25);
        tablaPrestamos.getTableHeader().setReorderingAllowed(false);
        tablaPrestamos.setAutoCreateRowSorter(true);

        // Centrar contenido de las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tablaPrestamos.getColumnCount(); i++) {
            tablaPrestamos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollTabla = new JScrollPane(tablaPrestamos);
        scrollTabla.setPreferredSize(new Dimension(800, 200));

        // Panel de detalles y acciones
        JPanel panelDetallesAcciones = new JPanel(new BorderLayout(10, 10));

        // Área de detalles
        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalles del Préstamo"));
        areaDetalles = new JTextArea(5, 30);
        areaDetalles.setEditable(false);
        areaDetalles.setLineWrap(true);
        areaDetalles.setWrapStyleWord(true);
        JScrollPane scrollDetalles = new JScrollPane(areaDetalles);
        panelDetalles.add(scrollDetalles, BorderLayout.CENTER);

        // Botones de acciones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBackground(new Color(0, 128, 0));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.addActionListener(e -> aceptarPrestamo());

        JButton btnRechazar = new JButton("Rechazar");
        btnRechazar.setBackground(new Color(255, 0, 0));
        btnRechazar.setForeground(Color.WHITE);
        btnRechazar.addActionListener(e -> rechazarPrestamo());

        JButton btnTerminar = new JButton("Terminar");
        btnTerminar.setBackground(new Color(0, 128, 0));
        btnTerminar.setForeground(Color.WHITE);
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

        // Panel superior (título y filtros)
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.add(lblTitulo);
        panelSuperior.add(Box.createVerticalStrut(10));
        panelSuperior.add(panelFiltros);

        // Añadir componentes al panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelDetallesAcciones, BorderLayout.SOUTH);

        // Listener para mostrar detalles al seleccionar una fila
        tablaPrestamos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetallesPrestamo();
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
        // Asumimos que hay un método para obtener el ID del horario asociado al préstamo
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

        List<Integer> insumoIds = new ArrayList<>();       // Cambiado de detalleInsumoIds a insumoIds
        List<Integer> cantidadesDevueltas = new ArrayList<>();

        for (DetallePrestamoInsumo detalle : detallesInsumo) {
            int idInsumo = detalle.getIdInsumo();        // Ahora usamos idInsumo directamente
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

            insumoIds.add(idInsumo);                     // Agregamos ID de insumo en vez de ID de detalle
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
