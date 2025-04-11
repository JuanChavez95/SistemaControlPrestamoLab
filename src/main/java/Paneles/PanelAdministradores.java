/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Paneles;

import DataBase.ConexionBD;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Panel para mostrar y gestionar administradores del sistema
 * @author DOC
 */
public class PanelAdministradores extends JPanel {
    
    private JTable tablaAdministradores;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollPane;
    
    public PanelAdministradores() {
        // Configuración del panel
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Título del panel
        JLabel titleLabel = new JLabel("LISTA ADMINISTRADORES", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 150));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        
        // Crear la tabla y su modelo
        crearTablaAdministradores();
        
        // Panel de botones para acciones
        JPanel panelBotones = crearPanelBotones();
        
        // Agregar componentes al panel principal
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Cargar datos de administradores
        cargarDatosAdministradores();
    }
    
    private void crearTablaAdministradores() {
        // Definir columnas para la tabla
        String[] columnas = {
            "RU", "Nombre", "Apellido Paterno", "Apellido Materno", 
            "CI", "Email", "Salario", "Fecha Inicio", "Nro. Título"
        };
        
        // Crear modelo de tabla no editable
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que las celdas no sean editables
            }
        };
        
        // Establecer columnas
        modeloTabla.setColumnIdentifiers(columnas);
        
        // Crear tabla con el modelo
        tablaAdministradores = new JTable(modeloTabla);
        
        // Personalizar tabla
        tablaAdministradores.setRowHeight(25);
        tablaAdministradores.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaAdministradores.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaAdministradores.getTableHeader().setBackground(new Color(210, 210, 255));
        tablaAdministradores.setSelectionBackground(new Color(200, 220, 255));
        tablaAdministradores.setGridColor(new Color(180, 180, 180));
        tablaAdministradores.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Crear scroll pane para la tabla
        scrollPane = new JScrollPane(tablaAdministradores);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(new Color(200, 200, 200))
        ));
    }
    
    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        // Botón Agregar
        JButton btnAgregar = new JButton("Agregar");
        estilizarBoton(btnAgregar, new Color(50, 150, 50));
        btnAgregar.addActionListener(e -> abrirFormularioNuevoAdmin());
        
        // Botón Editar
        JButton btnEditar = new JButton("Editar");
        estilizarBoton(btnEditar, new Color(70, 130, 180));
        btnEditar.addActionListener(e -> editarAdminSeleccionado());
        
        // Botón Eliminar
        JButton btnEliminar = new JButton("Eliminar");
        estilizarBoton(btnEliminar, new Color(180, 70, 70));
        btnEliminar.addActionListener(e -> eliminarAdminSeleccionado());
        
        // Botón Actualizar
        JButton btnActualizar = new JButton("Actualizar");
        estilizarBoton(btnActualizar, new Color(100, 100, 100));
        btnActualizar.addActionListener(e -> cargarDatosAdministradores());
        
        // Agregar botones al panel
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        
        return panelBotones;
    }
    
    private void estilizarBoton(JButton boton, Color colorFondo) {
        boton.setPreferredSize(new Dimension(120, 35));
        boton.setBackground(Color.WHITE);  //Para que el botón sea invisible
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setBorderPainted(false);  // Esto elimina el borde
        boton.setFocusPainted(false);   // Esto elimina el borde de foco
    }
    
    /**
     * Carga los datos de administradores desde la base de datos
     */
    public void cargarDatosAdministradores() {
        // Limpiar tabla antes de cargar nuevos datos
        modeloTabla.setRowCount(0);
        
        String sql = "SELECT u.ru, u.nombre, u.apellido_paterno, u.apellido_materno, u.ci, u.email, " + 
                     "a.salario, a.fecha_inicio, a.nro_titulo " +
                     "FROM usuario u " +
                     "JOIN administrador a ON u.ru = a.ru " +
                     "WHERE u.rol = 'administrador' ORDER BY u.ru";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("ru"),
                    rs.getString("nombre"),
                    rs.getString("apellido_paterno"),
                    rs.getString("apellido_materno"),
                    rs.getInt("ci"),
                    rs.getString("email"),
                    rs.getDouble("salario"),
                    rs.getDate("fecha_inicio"),
                    rs.getString("nro_titulo")
                };
                modeloTabla.addRow(fila);
            }
            
            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, 
                                             "No se encontraron administradores en la base de datos", 
                                             "Información", 
                                             JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                                         "Error al cargar los datos de administradores: " + e.getMessage(), 
                                         "Error", 
                                         JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void abrirFormularioNuevoAdmin() {
        // Aquí iría el código para abrir el formulario de nuevo administrador
        // Por ejemplo:
        // new NuevoAdministrador().setVisible(true);
        JOptionPane.showMessageDialog(this, "Función para agregar nuevo administrador", 
                                     "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void editarAdminSeleccionado() {
        int filaSeleccionada = tablaAdministradores.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un administrador para editar", 
                                         "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener el RU del administrador seleccionado
        int ru = (int) tablaAdministradores.getValueAt(filaSeleccionada, 0);
        
        // Aquí iría el código para abrir el formulario de edición con los datos del administrador
        // Por ejemplo:
        // new EditarAdministrador(ru).setVisible(true);
        JOptionPane.showMessageDialog(this, "Función para editar el administrador con RU: " + ru, 
                                     "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void eliminarAdminSeleccionado() {
        int filaSeleccionada = tablaAdministradores.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un administrador para eliminar", 
                                         "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener el RU y nombre del administrador seleccionado
        int ru = (int) tablaAdministradores.getValueAt(filaSeleccionada, 0);
        String nombre = (String) tablaAdministradores.getValueAt(filaSeleccionada, 1) + " " +
                        (String) tablaAdministradores.getValueAt(filaSeleccionada, 2);
        
        // Confirmar eliminación
        int opcion = JOptionPane.showConfirmDialog(this, 
                                                  "¿Está seguro de eliminar al administrador " + nombre + "?", 
                                                  "Confirmar eliminación", 
                                                  JOptionPane.YES_NO_OPTION, 
                                                  JOptionPane.WARNING_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            // Aquí iría el código para eliminar al administrador de la base de datos
            // Por ahora solo mostramos un mensaje
            JOptionPane.showMessageDialog(this, "Función para eliminar al administrador con RU: " + ru, 
                                         "Información", JOptionPane.INFORMATION_MESSAGE);
            
            // Después de eliminar, recargar los datos
            // cargarDatosAdministradores();
        }
    }
}