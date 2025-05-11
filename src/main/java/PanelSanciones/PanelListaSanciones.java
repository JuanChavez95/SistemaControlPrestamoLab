/*
 * Panel de visualización y gestión de sanciones
 */
package PanelSanciones;

import Clases.Sancion;
import Clases.SancionEquipamiento;
import Clases.SancionEquipo;
import Clases.SancionInsumo;
import Controles.ControladorSancion;
import Controles.ControladorSancionEquipamiento;
import Controles.ControladorSancionEquipo;
import Controles.ControladorSancionInsumo;
import DataBase.ConexionBD;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Panel que implementa la visualización y gestión de sanciones en el sistema
 * Permite filtrar, buscar y administrar los estados de las sanciones
 */
public class PanelListaSanciones extends JPanel {

    // Constantes de la interfaz
    private static final Color COLOR_FONDO = new Color(245, 245, 250);
    private static final Color COLOR_PRIMARIO = new Color(25, 63, 95);
    private static final Color COLOR_SECUNDARIO = new Color(72, 126, 176);
    private static final Color COLOR_ACENTO = new Color(0, 103, 177);

    // Estados de sanción y sus colores
    private static final Color COLOR_ACTIVA = new Color(25, 118, 210);
    private static final Color COLOR_CUMPLIDA = new Color(56, 142, 60);
    private static final Color COLOR_NO_CUMPLIDA = new Color(211, 47, 47);

    private JTable tablaSanciones;
    private DefaultTableModel modeloTabla;
    private JButton btnActualizar;
    private JButton btnDetalles;
    private JButton btnCambiarEstado;
    private JButton btnFiltrar;
    private JComboBox<String> cmbFiltroEstado;
    private JTextField txtBuscarUsuario;
    private JLabel lblTotalSanciones;
    private JLabel lblActivasSanciones;
    private JLabel lblCumplidasSanciones;
    private Connection conexion;
    
    // Controladores para manejar las sanciones y sus relaciones
    private ControladorSancion controladorSancion;
    private ControladorSancionEquipamiento controladorSancionEquipamiento;
    private ControladorSancionEquipo controladorSancionEquipo;
    private ControladorSancionInsumo controladorSancionInsumo;
    
    /**
     * Constructor que inicializa una nueva conexión a la base de datos
     */
    public PanelListaSanciones() {
        try {
            this.conexion = ConexionBD.conectar();
            inicializarControladores();
            inicializarComponentes();
        } catch (SQLException ex) {
            mostrarError("Error al conectar con la base de datos", ex);
        }
    }
    
    /**
     * Constructor que utiliza una conexión existente a la base de datos
     * @param conexion La conexión a la base de datos
     */
    public PanelListaSanciones(Connection conexion) {
        this.conexion = conexion;
        inicializarControladores();
        inicializarComponentes();
    }
    
    /**
     * Inicializa los controladores necesarios para gestionar sanciones
     */
    private void inicializarControladores() {
        try {
            this.controladorSancion = new ControladorSancion();
            this.controladorSancionEquipamiento = new ControladorSancionEquipamiento();
            this.controladorSancionEquipo = new ControladorSancionEquipo();
            this.controladorSancionInsumo = new ControladorSancionInsumo();
        } catch (Exception ex) {
            mostrarError("Error al inicializar controladores", ex);
        }
    }

    private void inicializarComponentes() {
        // Configuración visual
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        // Panel superior con título y herramientas de búsqueda
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Visualizar Sanciones");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSuperior.add(titleLabel, BorderLayout.NORTH);
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel();
        panelFiltros.setBackground(Color.WHITE);
        
        panelFiltros.add(new JLabel("Filtrar por estado:"));
        cmbFiltroEstado = new JComboBox<>(new String[]{"Todos", "ACTIVA", "CUMPLIDA", "NO CUMPLIDA"});
        panelFiltros.add(cmbFiltroEstado);
        
        panelFiltros.add(new JLabel("Buscar por RU/Nombre:"));
        txtBuscarUsuario = new JTextField(15);
        panelFiltros.add(txtBuscarUsuario);
        
        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> filtrarSanciones());
        panelFiltros.add(btnFiltrar);
        
        panelSuperior.add(panelFiltros, BorderLayout.CENTER);
        
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla y modelo
        String[] columnas = {"ID Sanción", "RU Usuario", "Tipo", "Fecha Sanción", "Estado", "Días Suspensión", "Fecha Inicio", "Fecha Fin", "Elementos"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaSanciones = new JTable(modeloTabla);
        tablaSanciones.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaSanciones.getColumnModel().getColumn(0).setPreferredWidth(70);
        tablaSanciones.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaSanciones.getColumnModel().getColumn(8).setPreferredWidth(250);
        
        // Agregar sorter para ordenar la tabla
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabla);
        tablaSanciones.setRowSorter(sorter);
        
        JScrollPane scrollPane = new JScrollPane(tablaSanciones);
        add(scrollPane, BorderLayout.CENTER);

        // Botones - Eliminando los iconos que causan el error
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);

        btnActualizar = new JButton("Actualizar Lista");
        // Quitamos la referencia al icono que causa el error
        btnActualizar.addActionListener(e -> cargarSanciones());
        panelBotones.add(btnActualizar);

        btnDetalles = new JButton("Ver Detalles");
        // Quitamos la referencia al icono que causa el error
        btnDetalles.addActionListener(e -> verDetallesSancion());
        panelBotones.add(btnDetalles);

        btnCambiarEstado = new JButton("Cambiar Estado");
        // Quitamos la referencia al icono que causa el error
        btnCambiarEstado.addActionListener(e -> cambiarEstadoSancion());
        panelBotones.add(btnCambiarEstado);

        add(panelBotones, BorderLayout.SOUTH);

        // Cargar datos al iniciar
        cargarSanciones();
    }
    
    private void filtrarSanciones() {
        String estadoFiltro = cmbFiltroEstado.getSelectedItem().toString();
        String busquedaUsuario = txtBuscarUsuario.getText().trim();
        
        cargarSanciones(estadoFiltro, busquedaUsuario);
    }

    private void cargarSanciones() {
        cargarSanciones("Todos", "");
    }
    
    private void cargarSanciones(String filtroEstado, String filtroUsuario) {
        modeloTabla.setRowCount(0);
        try {
            StringBuilder sqlBuilder = new StringBuilder(
                "SELECT s.id_sancion, s.ru_usuario, u.nombre, u.apellido_paterno, " +
                "s.tipo_sancion, s.fecha_sancion, s.estado_sancion, s.dias_suspension, " +
                "s.fecha_inicio, s.fecha_fin, s.id_prestamo " +
                "FROM sancion s " +
                "JOIN usuario u ON s.ru_usuario = u.ru WHERE 1=1"
            );
            
            if (!filtroEstado.equals("Todos")) {
                sqlBuilder.append(" AND s.estado_sancion = '").append(filtroEstado).append("'");
            }
            
            if (!filtroUsuario.isEmpty()) {
                sqlBuilder.append(" AND (CAST(u.ru AS CHAR) LIKE '%").append(filtroUsuario).append("%'")
                         .append(" OR u.nombre LIKE '%").append(filtroUsuario).append("%'")
                         .append(" OR u.apellido_paterno LIKE '%").append(filtroUsuario).append("%')");
            }
            
            sqlBuilder.append(" ORDER BY s.fecha_sancion DESC");

            try (PreparedStatement stmt = conexion.prepareStatement(sqlBuilder.toString());
                 ResultSet rs = stmt.executeQuery()) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                while (rs.next()) {
                    int idSancion = rs.getInt("id_sancion");
                    Object[] fila = new Object[9];
                    fila[0] = idSancion;
                    fila[1] = rs.getInt("ru_usuario") + " - " + rs.getString("nombre") + " " + rs.getString("apellido_paterno");
                    fila[2] = rs.getString("tipo_sancion");

                    java.sql.Date fechaSancion = rs.getDate("fecha_sancion");
                    fila[3] = fechaSancion != null ? sdf.format(fechaSancion) : "N/A";

                    fila[4] = rs.getString("estado_sancion");
                    fila[5] = rs.getInt("dias_suspension");

                    java.sql.Date fechaInicio = rs.getDate("fecha_inicio");
                    fila[6] = fechaInicio != null ? sdf.format(fechaInicio) : "N/A";

                    java.sql.Date fechaFin = rs.getDate("fecha_fin");
                    fila[7] = fechaFin != null ? sdf.format(fechaFin) : "N/A";
                    
                    // Obtener resumen de elementos afectados
                    fila[8] = obtenerResumenElementosAfectados(idSancion);

                    modeloTabla.addRow(fila);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar las sanciones: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String obtenerResumenElementosAfectados(int idSancion) {
        StringBuilder resumen = new StringBuilder();
        try {
            // Contar equipos
            String sqlEquiposCount = "SELECT COUNT(*) FROM sancion_equipo WHERE id_sancion = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(sqlEquiposCount)) {
                stmt.setInt(1, idSancion);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    resumen.append(rs.getInt(1)).append(" Equipo(s), ");
                }
            }
            
            // Contar equipamientos
            String sqlEquipamientoCount = "SELECT COUNT(*) FROM sancion_equipamiento WHERE id_sancion = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(sqlEquipamientoCount)) {
                stmt.setInt(1, idSancion);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    resumen.append(rs.getInt(1)).append(" Equipamiento(s), ");
                }
            }
            
            // Contar y sumar insumos
            String sqlInsumosCount = "SELECT COUNT(*), SUM(cantidad_afectada) FROM sancion_insumo WHERE id_sancion = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(sqlInsumosCount)) {
                stmt.setInt(1, idSancion);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    resumen.append(rs.getInt(1)).append(" Insumo(s): ").append(rs.getInt(2)).append(" unidades");
                }
            }
            
            // Si no hay ningún elemento asociado
            if (resumen.length() == 0) {
                resumen.append("Sin elementos asociados");
            } else if (resumen.toString().endsWith(", ")) {
                // Eliminar la última coma si existe
                resumen.delete(resumen.length() - 2, resumen.length());
            }
            
        } catch (SQLException ex) {
            resumen.append("Error al obtener elementos");
        }
        return resumen.toString();
    }

    private void cambiarEstadoSancion() {
        int filaSeleccionada = tablaSanciones.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una sanción para cambiar su estado", 
                    "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtenemos el índice real si está filtrada la tabla
        int modelRow = tablaSanciones.convertRowIndexToModel(filaSeleccionada);
        int idSancion = (int) modeloTabla.getValueAt(modelRow, 0);
        String estadoActual = (String) modeloTabla.getValueAt(modelRow, 4);
        
        // Opciones para el estado
        String[] opciones = {"ACTIVA", "CUMPLIDA", "NO CUMPLIDA"};
        String estadoNuevo = (String) JOptionPane.showInputDialog(this, 
                "Seleccione el nuevo estado para la sanción #" + idSancion, 
                "Cambiar Estado de Sanción", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                opciones, 
                estadoActual);
        
        // Si el usuario cancela o selecciona el mismo estado
        if (estadoNuevo == null || estadoNuevo.equals(estadoActual)) {
            return;
        }
        
        // Confirmación
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cambiar el estado de la sanción #" + idSancion + 
                " de '" + estadoActual + "' a '" + estadoNuevo + "'?",
                "Confirmar cambio de estado", JOptionPane.YES_NO_OPTION);
        
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                // Buscar la sanción por ID para actualizar su estado
                Sancion sancion = controladorSancion.buscarPorId(idSancion);
                if (sancion != null) {
                    sancion.setEstadoSancion(estadoNuevo);
                    controladorSancion.actualizar(sancion);
                    JOptionPane.showMessageDialog(this, "El estado de la sanción #" + idSancion + 
                            " ha sido actualizado a '" + estadoNuevo + "'", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    // Recargar la lista
                    cargarSanciones();
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró la sanción con ID " + idSancion, 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar el estado de la sanción: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void verDetallesSancion() {
        int filaSeleccionada = tablaSanciones.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una sanción para ver sus detalles", 
                    "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtenemos el índice real si está filtrada la tabla
        int modelRow = tablaSanciones.convertRowIndexToModel(filaSeleccionada);
        int idSancion = (int) modeloTabla.getValueAt(modelRow, 0);

        try {
            // Crear un JDialog para mostrar los detalles
            JDialog detallesDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Detalles de Sanción #" + idSancion, true);
            detallesDialog.setLayout(new BorderLayout());
            detallesDialog.setSize(700, 500);
            detallesDialog.setLocationRelativeTo(this);
            
            // Panel con información general de la sanción
            JPanel panelInfo = crearPanelInfoSancion(idSancion);
            detallesDialog.add(panelInfo, BorderLayout.NORTH);
            
            // Panel con pestañas para diferentes tipos de elementos afectados
            JTabbedPane tabPane = new JTabbedPane();
            
            // Pestaña para equipos - eliminando los iconos que causan errores
            JPanel panelEquipos = crearPanelElementosEquipo(idSancion);
            tabPane.addTab("Equipos", panelEquipos);
            
            // Pestaña para equipamientos - eliminando los iconos que causan errores
            JPanel panelEquipamientos = crearPanelElementosEquipamiento(idSancion);
            tabPane.addTab("Equipamientos", panelEquipamientos);
            
            // Pestaña para insumos - eliminando los iconos que causan errores
            JPanel panelInsumos = crearPanelElementosInsumos(idSancion);
            tabPane.addTab("Insumos", panelInsumos);
            
            detallesDialog.add(tabPane, BorderLayout.CENTER);
            
            // Botón de cerrar
            JButton btnCerrar = new JButton("Cerrar");
            btnCerrar.addActionListener(e -> detallesDialog.dispose());
            
            JPanel panelBotones = new JPanel();
            panelBotones.add(btnCerrar);
            detallesDialog.add(panelBotones, BorderLayout.SOUTH);
            
            detallesDialog.setVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener detalles de la sanción: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel crearPanelInfoSancion(int idSancion) throws SQLException {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Información General"));
        
        String sqlSancion = "SELECT s.*, u.nombre, u.apellido_paterno FROM sancion s " +
                            "JOIN usuario u ON s.ru_usuario = u.ru " +
                            "WHERE s.id_sancion = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sqlSancion)) {
            stmt.setInt(1, idSancion);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                
                // Datos del usuario
                panel.add(new JLabel("Usuario:", SwingConstants.RIGHT));
                panel.add(new JLabel(rs.getString("nombre") + " " + rs.getString("apellido_paterno") + 
                        " (RU: " + rs.getInt("ru_usuario") + ")"));
                
                // Datos del préstamo
                int idPrestamo = rs.getInt("id_prestamo");
                panel.add(new JLabel("ID Préstamo:", SwingConstants.RIGHT));
                panel.add(new JLabel(rs.wasNull() ? "No asociado" : String.valueOf(idPrestamo)));
                
                // Tipo de sanción
                panel.add(new JLabel("Tipo de sanción:", SwingConstants.RIGHT));
                panel.add(new JLabel(rs.getString("tipo_sancion")));
                
                // Estado de sanción
                panel.add(new JLabel("Estado:", SwingConstants.RIGHT));
                JLabel lblEstado = new JLabel(rs.getString("estado_sancion"));
                switch(rs.getString("estado_sancion")) {
                    case "ACTIVA":
                        lblEstado.setForeground(Color.BLUE);
                        break;
                    case "CUMPLIDA":
                        lblEstado.setForeground(new Color(0, 128, 0)); // Verde oscuro
                        break;
                    case "NO CUMPLIDA":
                        lblEstado.setForeground(Color.RED);
                        break;
                }
                panel.add(lblEstado);
                
                // Fechas
                java.sql.Date fechaSancion = rs.getDate("fecha_sancion");
                panel.add(new JLabel("Fecha de sanción:", SwingConstants.RIGHT));
                panel.add(new JLabel(fechaSancion != null ? sdf.format(fechaSancion) : "N/A"));
                
                java.sql.Date fechaInicio = rs.getDate("fecha_inicio");
                panel.add(new JLabel("Fecha inicio:", SwingConstants.RIGHT));
                panel.add(new JLabel(fechaInicio != null ? sdf.format(fechaInicio) : "N/A"));
                
                java.sql.Date fechaFin = rs.getDate("fecha_fin");
                panel.add(new JLabel("Fecha fin:", SwingConstants.RIGHT));
                panel.add(new JLabel(fechaFin != null ? sdf.format(fechaFin) : "N/A"));
                
                // Días de suspensión
                panel.add(new JLabel("Días de suspensión:", SwingConstants.RIGHT));
                panel.add(new JLabel(String.valueOf(rs.getInt("dias_suspension"))));
                
                // Descripción
                panel.add(new JLabel("Descripción:", SwingConstants.RIGHT));
                JTextArea txtDescripcion = new JTextArea(rs.getString("descripcion"));
                txtDescripcion.setLineWrap(true);
                txtDescripcion.setWrapStyleWord(true);
                txtDescripcion.setEditable(false);
                JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
                scrollDesc.setPreferredSize(new Dimension(300, 60));
                panel.add(scrollDesc);
            }
        }
        
        return panel;
    }
    
    private JPanel crearPanelElementosEquipo(int idSancion) throws SQLException {
        JPanel panel = new JPanel(new BorderLayout());
        
        DefaultTableModel modeloEquipos = new DefaultTableModel(
                new String[]{"ID Equipo", "Dispositivo", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable tablaEquipos = new JTable(modeloEquipos);
        JScrollPane scrollEquipos = new JScrollPane(tablaEquipos);
        panel.add(scrollEquipos, BorderLayout.CENTER);
        
        // Obtener los equipos relacionados con la sanción
        String sqlEquipos = "SELECT e.* FROM sancion_equipo se JOIN equipos e ON se.id_equipos = e.id_equipos WHERE se.id_sancion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sqlEquipos)) {
            stmt.setInt(1, idSancion);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("id_equipos");
                fila[1] = rs.getString("dispositivo");
                fila[2] = rs.getString("estado");
                modeloEquipos.addRow(fila);
            }
        }
        
        JLabel lblInfo = new JLabel("Equipos afectados: " + modeloEquipos.getRowCount());
        lblInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(lblInfo, BorderLayout.NORTH);
        
        if (modeloEquipos.getRowCount() == 0) {
            JLabel lblNoData = new JLabel("No hay equipos asociados a esta sanción", SwingConstants.CENTER);
            panel.add(lblNoData, BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    private JPanel crearPanelElementosEquipamiento(int idSancion) throws SQLException {
        JPanel panel = new JPanel(new BorderLayout());
        
        DefaultTableModel modeloEquipamiento = new DefaultTableModel(
                new String[]{"ID Equipamiento", "Nombre", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable tablaEquipamiento = new JTable(modeloEquipamiento);
        JScrollPane scrollEquipamiento = new JScrollPane(tablaEquipamiento);
        panel.add(scrollEquipamiento, BorderLayout.CENTER);
        
        // Obtener los equipamientos relacionados con la sanción
        String sqlEquipamiento = "SELECT e.* FROM sancion_equipamiento se JOIN equipamiento e ON se.id_equipamiento = e.id_equipamiento WHERE se.id_sancion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sqlEquipamiento)) {
            stmt.setInt(1, idSancion);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getInt("id_equipamiento");
                fila[1] = rs.getString("nombre_equipamiento");
                fila[2] = rs.getString("disponibilidad");
                modeloEquipamiento.addRow(fila);
            }
        }
        
        JLabel lblInfo = new JLabel("Equipamientos afectados: " + modeloEquipamiento.getRowCount());
        lblInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(lblInfo, BorderLayout.NORTH);
        
        if (modeloEquipamiento.getRowCount() == 0) {
            JLabel lblNoData = new JLabel("No hay equipamientos asociados a esta sanción", SwingConstants.CENTER);
            panel.add(lblNoData, BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    private JPanel crearPanelElementosInsumos(int idSancion) throws SQLException {
        JPanel panel = new JPanel(new BorderLayout());
        
        DefaultTableModel modeloInsumos = new DefaultTableModel(
                new String[]{"ID Insumo", "Nombre", "Cantidad Afectada", "Disponibilidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable tablaInsumos = new JTable(modeloInsumos);
        JScrollPane scrollInsumos = new JScrollPane(tablaInsumos);
        panel.add(scrollInsumos, BorderLayout.CENTER);
        
        // Obtener los insumos relacionados con la sanción
        String sqlInsumos = "SELECT i.*, si.cantidad_afectada FROM sancion_insumo si JOIN insumos i ON si.id_insumo = i.id_insumo WHERE si.id_sancion = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sqlInsumos)) {
            stmt.setInt(1, idSancion);
            ResultSet rs = stmt.executeQuery();
            
            int totalAfectado = 0;
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id_insumo");
                fila[1] = rs.getString("nombre_insumo");
                int cantidadAfectada = rs.getInt("cantidad_afectada");
                fila[2] = cantidadAfectada;
                fila[3] = rs.getString("disponibilidad");
                modeloInsumos.addRow(fila);
                
                totalAfectado += cantidadAfectada;
            }
            
            JLabel lblInfo = new JLabel("Insumos afectados: " + modeloInsumos.getRowCount() + 
                    " (Total: " + totalAfectado + " unidades)");
            lblInfo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panel.add(lblInfo, BorderLayout.NORTH);
        }
        
        if (modeloInsumos.getRowCount() == 0) {
            JLabel lblNoData = new JLabel("No hay insumos asociados a esta sanción", SwingConstants.CENTER);
            panel.add(lblNoData, BorderLayout.CENTER);
        }
        
        return panel;
    }
}